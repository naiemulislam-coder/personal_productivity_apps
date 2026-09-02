package com.naiemulislam.productivity.util.scheduler;

/**
 * Result of scheduling: a single scheduled segment for a task.
 */
public class ScheduledTask {
    public long taskId;
    public String taskName;
    public long startTimeMillis;
    public long endTimeMillis;
    public int allocatedMinutes; // minutes allocated in this segment
    public boolean isChunk;

    public ScheduledTask(long taskId, String taskName, long startTimeMillis, long endTimeMillis, int allocatedMinutes, boolean isChunk) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.startTimeMillis = startTimeMillis;
        this.endTimeMillis = endTimeMillis;
        this.allocatedMinutes = allocatedMinutes;
        this.isChunk = isChunk;
    }
}
