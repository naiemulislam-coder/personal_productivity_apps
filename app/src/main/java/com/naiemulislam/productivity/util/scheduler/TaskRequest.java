package com.naiemulislam.productivity.util.scheduler;

import java.util.ArrayList;
import java.util.List;

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
    public List<Long> dependsOnIds; // optional dependencies (task ids)

    public TaskRequest(long taskId, String name, int estimatedMinutes, int priority, long deadline, boolean allowChunking) {
        this.taskId = taskId;
        this.name = name;
        this.estimatedMinutes = estimatedMinutes;
        this.priority = priority;
        this.deadline = deadline;
        this.allowChunking = allowChunking;
        this.dependsOnIds = new ArrayList<>();
    }

    public TaskRequest withDependencies(List<Long> deps) { this.dependsOnIds = deps == null ? new ArrayList<>() : deps; return this; }
}
