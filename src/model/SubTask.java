package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private final int epicId;
    private int id;
    private final Type type;

    public SubTask(String taskName, String description, Status status, int epicId, Duration duration, LocalDateTime startTime) {
        super(taskName, description, status, duration, startTime);
        this.epicId = epicId;
        this.type = Type.SUBTASK;
    }

    public int getEpicId() {
        return epicId;
    }

    public Type getType() {
        return type;
    }

    @Override
    public LocalDateTime getEndTime() {
        return super.getEndTime();
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SUBTASK{ taskName = " + getTaskName() + '\'' +
                ", description = " + getDescription() + '\'' +
                ", id = " + epicId + '\'' +
                ", status = " + getStatus() + " }";
    }

    @Override
    public String toFileString() {
        return id + "," + type + "," + getTaskName() + "," + getStatus() + "," + getDescription() + "," + getDuration() + "," + getStartTime() + "," + getEpicId();
    }
}
