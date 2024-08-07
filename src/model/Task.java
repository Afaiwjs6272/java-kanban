package model;

import java.time.Duration;
import java.time.LocalDateTime;


public class Task {
    private String taskName;
    private String description;
    private Status status;
    private Type type;
    private Duration duration;
    private LocalDateTime startTime;
    private int id;

    public Task(String taskName, String description, Status status, Duration duration, LocalDateTime startTime) {
        this.taskName = taskName;
        this.description = description;
        this.status = status;
        this.type = Type.TASK;
        this.duration = duration;
        this.startTime = startTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getType() {
        return type;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration.toMinutes());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }


    @Override
    public String toString() {
        return "TASK{ taskName = " + taskName + '\'' +
                " description = " + description + '\'' +
                " id = " + id + '\'' +
                " status = " + status + " }";
    }

    public String toFileString() {
        return id + "," + type + "," + taskName + "," + status + "," + description + getDuration() + "," + getStartTime();
    }
}
