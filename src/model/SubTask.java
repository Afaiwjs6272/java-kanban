package model;

public class SubTask extends Task {
    private final int epicId;
    private int id;
    private final Type type;

    public SubTask(String taskName, String description, Status status, int epicId) {
        super(taskName, description, status);
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
        return id + "," + type + "," + getTaskName() + "," + getStatus() + "," + getDescription() + "," + getEpicId();
    }
}
