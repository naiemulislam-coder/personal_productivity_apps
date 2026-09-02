package com.naiemulislam.productivity.util.scheduler;

/**
 * Lightweight DTO representing a scheduling request for a task.
 */
public class TaskRequest {
    public long taskId;
    public String name;
    public int estimatedMinutes;
    public int priority; // 0=Low,1=Medium,2=High
    public long deadline; // epoch millis
    public boolean allowChunking;

    public TaskRequest(long taskId, String name, int estimatedMinutes, int priority, long deadline, boolean allowChunking) {
        this.taskId = taskId;
        this.name = name;
        this.estimatedMinutes = estimatedMinutes;
        this.priority = priority;
        this.deadline = deadline;
        this.allowChunking = allowChunking;
    }
}
