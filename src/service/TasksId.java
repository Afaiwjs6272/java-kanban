package service;

import model.Epic;
import model.SubTask;
import model.Task;
import service.HistoryManager;

import java.util.*;

public class TasksId implements HistoryManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, SubTask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    public LinkedList<Task> tasksHistory = new LinkedList<>();

    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            tasksHistory.add(task);
        }
        return task;
    }

    public SubTask getSubtask(int id) {
        SubTask subtask = subtasks.get(id);
        if (subtask != null) {
            tasksHistory.add(subtask);
        }
        return subtask;
    }

    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            tasksHistory.add(epic);
        }
        return epic;
    }

    @Override
    public void add(Task task) {
        if (tasksHistory.size() == 10){
            tasksHistory.removeFirst();
        } else {
            tasksHistory.addLast(task);
        }
    }
    @Override
    public LinkedList<Task> getHistory() {
        return tasksHistory;
    }
}
