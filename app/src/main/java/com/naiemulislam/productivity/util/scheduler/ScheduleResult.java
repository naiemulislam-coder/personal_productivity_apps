package com.naiemulislam.productivity.util.scheduler;

import java.util.List;

/**
 * Output wrapper for the schedule generation.
 */
public class ScheduleResult {
    public final List<ScheduledTask> scheduledTasks;
    public final List<TaskRequest> unscheduledTasks;

    public ScheduleResult(List<ScheduledTask> scheduledTasks, List<TaskRequest> unscheduledTasks) {
        this.scheduledTasks = scheduledTasks;
        this.unscheduledTasks = unscheduledTasks;
    }
}
