package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    HistoryManager manager;

    @BeforeEach
    public void createManager(){
        manager = new InMemoryHistoryManager();
    }

    @Test
    public void shouldAdd2Tasks(){
        Task task = new Task("r","f",Status.NEW);
        Task task1 = new Task("d","g",Status.NEW);
        manager.add(task);
        manager.add(task1);

        LinkedList<Task> history = manager.getHistory();

        assertEquals(2,history.size());
        assertNotNull(history);
    }

    @Test
    public void shouldDeleteFirstElemWhileOverflowing(){
        Task task = new Task("r","f",Status.NEW);
        Task task1 = new Task("d","g",Status.NEW);
        Task task2 = new Task("r","f",Status.NEW);
        Task task3 = new Task("d","g",Status.NEW);
        Task task4 = new Task("r","f",Status.NEW);
        Task task5 = new Task("d","g",Status.NEW);
        Task task6 = new Task("r","f",Status.NEW);
        Task task7 = new Task("d","g",Status.NEW);
        Task task8 = new Task("r","f",Status.NEW);
        Task task9 = new Task("d","g",Status.NEW);
        Task task10 = new Task("d","g",Status.NEW);


        manager.add(task);
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        manager.add(task4);
        manager.add(task5);
        manager.add(task6);
        manager.add(task7);
        manager.add(task8);
        manager.add(task9);
        manager.add(task10);

        LinkedList<Task> history = manager.getHistory();

        assertNotNull(history);
        assertEquals(task1,history.get(0));
    }
}