package service;

import model.Task;
import service.HistoryManager;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    List<Task> history = new ArrayList<>(10);


    @Override
    public void add(Task task) {
        if (history.size() == 10){
            history.remove(0);
        } else {
            history.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
