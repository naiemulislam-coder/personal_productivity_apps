package com.naiemulislam.productivity.util.scheduler;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SchedulerEngineTest {

    @Test
    public void testSimpleSchedulingWithoutConflicts() {
        long now = System.currentTimeMillis();
        long start = now;
        long end = start + 1000L * 60L * 60L * 6L; // 6 hours

        List<TaskRequest> tasks = new ArrayList<>();
        tasks.add(new TaskRequest(1L, "A", 60, 1, end, false));
        tasks.add(new TaskRequest(2L, "B", 120, 2, end, false));
        tasks.add(new TaskRequest(3L, "C", 30, 0, end, false));

        List<com.naiemulislam.productivity.data.entity.FixedActivity> fixed = new ArrayList<>();

        SchedulerEngine engine = new SchedulerEngine(5);
        ScheduleResult res = engine.generateDailySchedule(tasks, fixed, start, end);

        // total minutes available = 360 minutes, tasks need 210 -> all fit
        assertEquals(3, res.scheduledTasks.size());
        assertEquals(0, res.unscheduledTasks.size());
    }

    @Test
    public void testSchedulingWithFixedActivityAndChunking() {
        long now = System.currentTimeMillis();
        long start = now;
        long end = start + 1000L * 60L * 60L * 5L; // 5 hours = 300 mins

        List<TaskRequest> tasks = new ArrayList<>();
        TaskRequest big = new TaskRequest(10L, "BigTask", 200, 2, end, true);
        tasks.add(big);
        tasks.add(new TaskRequest(11L, "Small", 30, 1, end, false));

        List<com.naiemulislam.productivity.data.entity.FixedActivity> fixed = new ArrayList<>();
        // fixed activity blocks 2 hours in middle
        com.naiemulislam.productivity.data.entity.FixedActivity fa = new com.naiemulislam.productivity.data.entity.FixedActivity();
        fa.name = "Meeting";
        fa.startTime = start + 1000L * 60L * 60L * 1L; // +1h
        fa.durationMinutes = 120; // 2h
        fixed.add(fa);

        SchedulerEngine engine = new SchedulerEngine(10);
        ScheduleResult res = engine.generateDailySchedule(tasks, fixed, start, end);

        // total available: 300 - 120 = 180 mins. Small(30) + BigTask(200) -> BigTask can't fit fully -> unscheduled should include BigTask
        assertTrue(res.unscheduledTasks.stream().anyMatch(t -> t.taskId == 10L));
    }

    @Test
    public void testDependencyOrderingAndCycleDetection() {
        long now = System.currentTimeMillis();
        long start = now;
        long end = start + 1000L * 60L * 60L * 4L; // 4 hours = 240 mins

        List<TaskRequest> tasks = new ArrayList<>();
        TaskRequest t1 = new TaskRequest(1L, "T1", 60, 1, end, true);
        TaskRequest t2 = new TaskRequest(2L, "T2", 60, 1, end, true);
        TaskRequest t3 = new TaskRequest(3L, "T3", 30, 1, end, true);
        // t2 depends on t1
        List<Long> d2 = new ArrayList<>(); d2.add(1L); t2.withDependencies(d2);
        // create a cycle: t1 depends on t2 -> cycle among 1 and 2
        List<Long> d1 = new ArrayList<>(); d1.add(2L); t1.withDependencies(d1);

        tasks.add(t1); tasks.add(t2); tasks.add(t3);

        SchedulerEngine engine = new SchedulerEngine(5);
        ScheduleResult res = engine.generateDailySchedule(tasks, new ArrayList<>(), start, end);

        // t1 and t2 are in cycle -> should be unscheduled; t3 should be scheduled
        assertTrue(res.unscheduledTasks.stream().anyMatch(t -> t.taskId == 1L));
        assertTrue(res.unscheduledTasks.stream().anyMatch(t -> t.taskId == 2L));
        assertTrue(res.scheduledTasks.stream().anyMatch(s -> s.taskId == 3L));
    }
}
