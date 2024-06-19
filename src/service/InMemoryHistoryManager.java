package service;

import model.Task;
import service.HistoryManager;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    TasksId tasksId = new TasksId();


    @Override
    public void add(Task task) {
        if (tasksId.tasksHistory.size() == 10){
            tasksId.tasksHistory.removeFirst();
        } else {
            tasksId.tasksHistory.addLast(task);
        }
    }

    @Override
    public LinkedList<Task> getHistory() {
        return tasksId.tasksHistory;
    }
}
