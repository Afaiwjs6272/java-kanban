package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.*;

public interface TaskManager {

    static void syncEpic(Epic epic) {
        int doneSub = 0;
        int newSub = 0;
        ArrayList<SubTask> allSubTasks = epic.getSubTaskList();

        for (SubTask subTask : allSubTasks) {
            if (subTask.getStatus() == Status.NEW) {
                newSub++;
            }
            if (subTask.getStatus() == Status.DONE) {
                doneSub++;
            }
        }

        if (newSub == allSubTasks.size()) {
            epic.setStatus(Status.NEW);
        } else if (doneSub == allSubTasks.size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    void addTask(Task task);

    void addSub(SubTask sub);

    void addEpic(Epic epic);

    void updateTask(Task task);

    void updateEpic(Epic newEpic);

    void updateSub(SubTask sub);

    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<SubTask> getAllSubTasks();

    void deleteTasks();

    void deleteEpics();

    void deleteSubTasks();

    void deleteByTaskId(int id);

    void deleteByEpicId(int id);

    void deleteBySubId(int id);

    Task printTaskById(int id);

    SubTask printSubById(int id);

    Epic printEpicById(int id);

    ArrayList<SubTask> getSubtaskByEpic(int epicId);

    LinkedList<Task> getHistory();
}
