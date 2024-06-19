package service;

import model.Task;

import java.util.*;

public interface HistoryManager {
    void add(Task task);

    LinkedList<Task> getHistory();
}

