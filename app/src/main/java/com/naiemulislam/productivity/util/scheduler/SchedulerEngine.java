package com.naiemulislam.productivity.util.scheduler;

import com.naiemulislam.productivity.data.entity.FixedActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Deterministic daily scheduling engine (basic implementation).
 *
 * Inputs are TaskRequest list, FixedActivity list and the day's wake/sleep window.
 * Output is a list of ScheduledTask objects representing allocated time ranges.
 *
 * Note: This is a core algorithm suitable for unit-testing and iteration. It's
 * independent from Room entities for scheduling metadata (TaskRequest is a
 * lightweight DTO). Later we can integrate TaskRequest with DB Task entity
 * fields, handle dependencies, priority inheritance, day cut-off logic, etc.
 */
public class SchedulerEngine {

    // default buffer minutes between tasks
    private final int bufferMinutes = 10;

    public SchedulerEngine() {}

    /**
     * Generate a daily schedule between dayStartMillis (inclusive) and dayEndMillis (exclusive).
     * All times use epoch milliseconds (UTC or device local millis consistently).
     *
     * @param tasks           list of tasks to schedule (TaskRequest)
     * @param fixedActivities list of fixed activities that block time (startTime & durationMinutes)
     * @param dayStartMillis  day start (wake time) epoch ms
     * @param dayEndMillis    day end (sleep time) epoch ms
     * @return ScheduleResult containing scheduled tasks and unscheduled tasks
     */
    public ScheduleResult generateDailySchedule(List<TaskRequest> tasks,
                                                List<FixedActivity> fixedActivities,
                                                long dayStartMillis,
                                                long dayEndMillis) {
        // Guard
        if (dayEndMillis <= dayStartMillis) {
            return new ScheduleResult(new ArrayList<>(), tasks); // invalid window -> nothing scheduled
        }

        // Build busy intervals from fixed activities clipped to day window
        List<Interval> busy = new ArrayList<>();
        for (FixedActivity fa : fixedActivities) {
            long faStart = fa.startTime;
            long faEnd = faStart + (long) fa.durationMinutes * 60_000L;
            // Clip to day window
            long s = Math.max(faStart, dayStartMillis);
            long e = Math.min(faEnd, dayEndMillis);
            if (e > s) busy.add(new Interval(s, e));
        }

        // Merge overlapping busy intervals
        Collections.sort(busy, Comparator.comparingLong(i -> i.start));
        List<Interval> mergedBusy = new ArrayList<>();
        for (Interval it : busy) {
            if (mergedBusy.isEmpty()) mergedBusy.add(it);
            else {
                Interval last = mergedBusy.get(mergedBusy.size() - 1);
                if (it.start <= last.end) {
                    last.end = Math.max(last.end, it.end);
                } else mergedBusy.add(it);
            }
        }

        // Build available slots between dayStartMillis and dayEndMillis
        List<Slot> slots = new ArrayList<>();
        long cursor = dayStartMillis;
        for (Interval b : mergedBusy) {
            if (b.start > cursor) {
                slots.add(new Slot(cursor, b.start));
            }
            cursor = Math.max(cursor, b.end);
        }
        if (cursor < dayEndMillis) slots.add(new Slot(cursor, dayEndMillis));

        // Sort tasks by priority desc, deadline asc, estimatedMinutes asc
        List<TaskRequest> sorted = new ArrayList<>(tasks);
        Collections.sort(sorted, (a, b) -> {
            if (a.priority != b.priority) return Integer.compare(b.priority, a.priority);
            int dl = Long.compare(a.deadline, b.deadline);
            if (dl != 0) return dl;
            return Integer.compare(a.estimatedMinutes, b.estimatedMinutes);
        });

        List<ScheduledTask> scheduled = new ArrayList<>();
        List<TaskRequest> unscheduled = new ArrayList<>();

        for (TaskRequest task : sorted) {
            int remaining = task.estimatedMinutes;
            boolean placed = false;

            // Try to find a single slot big enough first
            for (Slot slot : slots) {
                int availableMinutes = slot.remainingMinutes();
                if (availableMinutes <= 0) continue;

                // We require buffer after allocation except when filling to slot end
                int needed = remaining;
                if (availableMinutes >= needed) {
                    long start = slot.currentStartMillis();
                    long end = start + (long) needed * 60_000L;
                    scheduled.add(new ScheduledTask(task.taskId, task.name, start, end, task.estimatedMinutes, false));
                    slot.advanceByMinutes(needed + bufferMinutes);
                    placed = true;
                    remaining = 0;
                    break;
                }
            }

            // If not placed and chunking allowed, try to allocate across multiple slots
            if (!placed && task.allowChunking) {
                for (Slot slot : slots) {
                    int availableMinutes = slot.remainingMinutes();
                    if (availableMinutes <= 0) continue;
                    // allocate as much as possible here (reserve buffer after chunk if slot still has space)
                    int take = Math.max(0, availableMinutes - bufferMinutes);
                    if (take <= 0) {
                        // not enough to allocate meaningful chunk (only buffer would remain)
                        continue;
                    }
                    int toAllocate = Math.min(take, remaining);
                    long start = slot.currentStartMillis();
                    long end = start + (long) toAllocate * 60_000L;
                    scheduled.add(new ScheduledTask(task.taskId, task.name, start, end, toAllocate, true));
                    slot.advanceByMinutes(toAllocate + bufferMinutes);
                    remaining -= toAllocate;
                    if (remaining <= 0) { placed = true; break; }
                }
            }

            if (!placed) {
                // Couldn't schedule fully
                unscheduled.add(task);
            }
        }

        return new ScheduleResult(scheduled, unscheduled);
    }

    // --- helper classes ---
    private static class Interval {
        long start;
        long end;

        Interval(long s, long e) { start = s; end = e; }
    }

    private static class Slot {
        long start;
        long end;
        long cursor; // current cursor of allocation

        Slot(long s, long e) { start = s; end = e; cursor = s; }

        int remainingMinutes() {
            long ms = Math.max(0L, end - cursor);
            return (int) (ms / 60_000L);
        }

        long currentStartMillis() { return cursor; }

        void advanceByMinutes(int minutes) {
            long ms = (long) minutes * 60_000L;
            cursor = Math.min(end, cursor + ms);
        }
    }
}
