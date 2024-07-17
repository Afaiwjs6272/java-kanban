package model;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String taskName, String description, Status status, int epicId) {
        super(taskName,description,status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }


    @Override
    public String toString() {
        return "SUBTASK{ taskName = " + getTaskName() + '\'' +
                ", description = " + getDescription() + '\'' +
                ", id = " + epicId + '\'' +
                ", status = " + getStatus() + " }";
    }
}
