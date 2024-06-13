import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<SubTask> subTaskList = new ArrayList<>();

    public Epic(String taskName, String description, Status status) {
        super(taskName, description, status);
    }

    public void addSubTask (SubTask subTask){
        subTaskList.add(subTask);
    }


    public ArrayList<SubTask> getSubTaskList() {
        return subTaskList;
    }

    public void setSubTasksList(ArrayList<SubTask> subTaskList) {
        this.subTaskList = subTaskList;
    }

    public void deleteSubTask () {
        subTaskList.clear();
    }



    @Override
    public String toString() {
        return "EPIC{ name= " + getTaskName() + '\'' +
                ", description = " + getDescription() + '\'' +
                ", id = " + getId() +
                ", status = " + getStatus() + '}';
    }
}