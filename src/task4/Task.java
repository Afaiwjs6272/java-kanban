package task4;

public class Task {
    private String taskName;
    private String description;
    private Status status;
    private int id;

    public Task(String taskName, String description, Status status) {
        this.taskName = taskName;
        this.description = description;
        this.status = status;
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


    @Override
    public String toString(){
        return "TASK{ taskName = " + taskName + '\'' +
                " description = " + description + '\'' +
                " id = " + id + '\'' +
                " status = " + status + " }";
    }
}
