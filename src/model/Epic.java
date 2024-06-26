package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTaskList = new ArrayList<>();

    public Epic(String taskName, String description) {
        super(taskName, description, Status.NEW);
    }

    public void addSubTask (SubTask subTask){
        subTaskList.add(subTask);
    }


    public ArrayList<SubTask> getSubTaskList() {
        return new ArrayList<>(subTaskList);
    }

    public void deleteSingleSubTask(int subTaskId) {
        for (int i = 0; i < subTaskList.size(); i++) {
            if (subTaskList.get(i).getId() == subTaskId) {
                subTaskList.remove(i);
                break;
            }
        }
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