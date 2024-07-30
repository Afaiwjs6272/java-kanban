package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.*;

public interface TaskManager {

    void addTask(Task task) throws Exception;

    void addSub(SubTask sub) throws Exception;

    void addEpic(Epic epic) throws Exception;

    void updateTask(Task task) throws Exception;

    void updateEpic(Epic newEpic) throws Exception;

    void updateSub(SubTask sub) throws Exception;

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<SubTask> getAllSubTasks();

    void deleteTasks() throws Exception;

    void deleteEpics() throws Exception;

    void deleteSubTasks() throws Exception;

    void deleteByTaskId(int id) throws Exception;

    void deleteByEpicId(int id) throws Exception;

    void deleteBySubId(int id) throws Exception;

    Task printTaskById(int id);

    SubTask printSubById(int id);

    Epic printEpicById(int id);

    List<SubTask> getSubtaskByEpic(int epicId);

    List<Task> getHistory();
}
