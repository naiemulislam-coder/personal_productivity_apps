package com.naiemulislam.productivity.util.scheduler;

import com.naiemulislam.productivity.data.entity.FixedActivity;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * SchedulerEngine (improved)
 * - Respects fixed activities (busy intervals)
 * - Orders tasks by: dependency order, priority (desc), deadline (asc), estimated (asc)
 * - Supports chunking across multiple slots
 * - Respects buffer minutes between tasks
 * - Performs simple dependency handling via topological sort (cycles cause involved tasks to be unscheduled)
 *
 * This class is pure Java and suitable for unit testing.
 */
public class SchedulerEngine {

    private final int bufferMinutes;

    public SchedulerEngine() { this(10); }
    public SchedulerEngine(int bufferMinutes) { this.bufferMinutes = Math.max(0, bufferMinutes); }

    public ScheduleResult generateDailySchedule(List<TaskRequest> tasks,
                                                List<FixedActivity> fixedActivities,
                                                long dayStartMillis,
                                                long dayEndMillis) {
        if (tasks == null) tasks = new ArrayList<>();
        if (fixedActivities == null) fixedActivities = new ArrayList<>();

        // Validate window
        if (dayEndMillis <= dayStartMillis) {
            return new ScheduleResult(new ArrayList<>(), tasks);
        }

        // Build busy intervals
        List<Interval> busy = new ArrayList<>();
        for (FixedActivity fa : fixedActivities) {
            long s = Math.max(fa.startTime, dayStartMillis);
            long e = Math.min(fa.startTime + (long)fa.durationMinutes * 60_000L, dayEndMillis);
            if (e > s) busy.add(new Interval(s, e));
        }
        Collections.sort(busy, Comparator.comparingLong(i -> i.start));
        List<Interval> mergedBusy = mergeIntervals(busy);

        // Build available slots
        List<Slot> slots = buildSlotsFromBusy(mergedBusy, dayStartMillis, dayEndMillis);

        // Prepare tasks: map + incoming degree for dependencies
        Map<Long, TaskRequest> idMap = new HashMap<>();
        for (TaskRequest t : tasks) idMap.put(t.taskId, t);

        Map<Long, Integer> indegree = new HashMap<>();
        Map<Long, List<Long>> graph = new HashMap<>();
        for (TaskRequest t : tasks) {
            indegree.put(t.taskId, 0);
            graph.put(t.taskId, new ArrayList<>());
        }

        for (TaskRequest t : tasks) {
            if (t.dependsOnIds != null) {
                for (Long dep : t.dependsOnIds) {
                    if (!idMap.containsKey(dep)) continue; // missing dep ignored
                    graph.get(dep).add(t.taskId);
                    indegree.put(t.taskId, indegree.getOrDefault(t.taskId, 0) + 1);
                }
            }
        }

        // Kahn's algorithm for topological sort combined with priority ordering
        Queue<Long> queue = new ArrayDeque<>();
        for (Map.Entry<Long,Integer> e : indegree.entrySet()) if (e.getValue() == 0) queue.add(e.getKey());

        List<TaskRequest> ordered = new ArrayList<>();
        while (!queue.isEmpty()) {
            // pick best candidate from queue based on priority/deadline/estimate
            List<TaskRequest> candidates = new ArrayList<>();
            for (Long id : queue) candidates.add(idMap.get(id));
            candidates.sort((a,b) -> {
                if (a.priority != b.priority) return Integer.compare(b.priority, a.priority);
                int dl = Long.compare(a.deadline, b.deadline);
                if (dl != 0) return dl;
                return Integer.compare(a.estimatedMinutes, b.estimatedMinutes);
            });
            TaskRequest pick = candidates.get(0);
            // remove pick from queue
            queue.remove(pick.taskId);
            ordered.add(pick);
            // reduce indegree of neighbors
            for (Long nbr : graph.get(pick.taskId)) {
                indegree.put(nbr, indegree.get(nbr) - 1);
                if (indegree.get(nbr) == 0) queue.add(nbr);
            }
        }

        // Any tasks remaining with indegree >0 are part of cycles -> unscheduled
        Set<Long> inCycle = new HashSet<>();
        for (Map.Entry<Long,Integer> e : indegree.entrySet()) if (e.getValue() > 0) inCycle.add(e.getKey());

        List<ScheduledTask> scheduled = new ArrayList<>();
        List<TaskRequest> unscheduled = new ArrayList<>();

        // Try to schedule ordered tasks
        for (TaskRequest task : ordered) {
            if (inCycle.contains(task.taskId)) {
                unscheduled.add(task);
                continue;
            }
            int remaining = task.estimatedMinutes;
            boolean placed = false;

            // first pass: try to place single contiguous chunk
            for (Slot slot : slots) {
                int available = slot.remainingMinutes();
                if (available <= 0) continue;
                if (available >= remaining) {
                    long start = slot.currentStartMillis();
                    long end = start + (long)remaining * 60_000L;
                    scheduled.add(new ScheduledTask(task.taskId, task.name, start, end, remaining, false));
                    slot.advanceByMinutes(remaining + bufferMinutes);
                    placed = true;
                    remaining = 0;
                    break;
                }
            }

            // second pass: chunking across slots if allowed
            if (!placed && task.allowChunking) {
                for (Slot slot : slots) {
                    int available = slot.remainingMinutes();
                    if (available <= 0) continue;
                    int usable = Math.max(0, available - bufferMinutes);
                    if (usable <= 0) continue;
                    int take = Math.min(usable, remaining);
                    long start = slot.currentStartMillis();
                    long end = start + (long)take * 60_000L;
                    scheduled.add(new ScheduledTask(task.taskId, task.name, start, end, take, true));
                    slot.advanceByMinutes(take + bufferMinutes);
                    remaining -= take;
                    if (remaining <= 0) { placed = true; break; }
                }
            }

            if (!placed) unscheduled.add(task);
        }

        // Add tasks that were not reachable in ordering (e.g., due to missing from map) but exist in original list
        for (TaskRequest t : tasks) if (!idMap.containsKey(t.taskId)) unscheduled.add(t);

        return new ScheduleResult(scheduled, unscheduled);
    }

    private static List<Interval> mergeIntervals(List<Interval> intervals) {
        List<Interval> out = new ArrayList<>();
        for (Interval it : intervals) {
            if (out.isEmpty()) out.add(new Interval(it.start, it.end));
            else {
                Interval last = out.get(out.size()-1);
                if (it.start <= last.end) last.end = Math.max(last.end, it.end);
                else out.add(new Interval(it.start, it.end));
            }
        }
        return out;
    }

    private static List<Slot> buildSlotsFromBusy(List<Interval> busy, long dayStart, long dayEnd) {
        List<Slot> slots = new ArrayList<>();
        long cursor = dayStart;
        for (Interval b : busy) {
            if (b.start > cursor) slots.add(new Slot(cursor, b.start));
            cursor = Math.max(cursor, b.end);
        }
        if (cursor < dayEnd) slots.add(new Slot(cursor, dayEnd));
        return slots;
    }

    // --- helpers ---
    private static class Interval { long start; long end; Interval(long s,long e){start=s;end=e;} }
    private static class Slot { long start; long end; long cursor; Slot(long s,long e){start=s;end=e;cursor=s;} int remainingMinutes(){ long ms = Math.max(0L,end-cursor); return (int)(ms/60_000L);} long currentStartMillis(){return cursor;} void advanceByMinutes(int m){ long ms=(long)m*60_000L; cursor = Math.min(end, cursor+ms);} }
}
