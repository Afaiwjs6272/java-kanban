package model;

public class SubTask extends Task {
    private int epicId;
    private Type type;

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
    public String toString() {
        return "SUBTASK{ taskName = " + getTaskName() + '\'' +
                ", description = " + getDescription() + '\'' +
                ", id = " + epicId + '\'' +
                ", status = " + getStatus() + " }";
    }
}
