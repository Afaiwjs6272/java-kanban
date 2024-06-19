package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.*;

public interface TaskManager {

    void addTask(Task task);

    void addSub(SubTask sub);

    void addEpic(Epic epic);

    void updateTask(Task task);

    void updateEpic(Epic newEpic);

    void updateSub(SubTask sub);

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<SubTask> getAllSubTasks();

    void deleteTasks();

    void deleteEpics();

    void deleteSubTasks();

    void deleteByTaskId(int id);

    void deleteByEpicId(int id);

    void deleteBySubId(int id);

    Task printTaskById(int id);

    SubTask printSubById(int id);

    Epic printEpicById(int id);

    List<SubTask> getSubtaskByEpic(int epicId);

    List<Task> getHistory();
}
