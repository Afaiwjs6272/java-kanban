package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    HistoryManager manager;

    @BeforeEach
    public void createManager() {
        manager = new InMemoryHistoryManager();
    }

    @Test
    public void historyShouldNotBeClear() {
        Task task = new Task("r", "f", Status.NEW, Duration.ofMinutes(10), LocalDateTime.of(2024, 8, 20, 15, 20));
        manager.add(task);
        final List<Task> history = manager.getHistory();
        assertEquals(1, history.size());
        assertNotNull(history);
    }


    @Test
    public void shouldDeleteFirstTask() throws Exception {
        TaskManager manager = new InMemoryTaskManager();
        manager.addTask(new Task("r", "f", Status.NEW, Duration.ofMinutes(11), LocalDateTime.of(2024, 8, 20, 15, 22)));
        manager.addTask(new Task("rsad", "fas", Status.NEW, Duration.ofMinutes(31), LocalDateTime.of(2024, 8, 20, 16, 20)));
        manager.printTaskById(1);
        manager.printTaskById(1);
        manager.printTaskById(2);
        List<Task> history = manager.getHistory();
        assertEquals(history.size(), 1);
    }

    @Test
    public void historyIsClear() {
        TaskManager manager = new InMemoryTaskManager();
        List<Task> history = manager.getHistory();

        assertEquals(history.size(), 0);
    }

    @Test
    public void testAddDuplicateTask() {
        Task task = new Task("asd", "Task", Status.NEW, Duration.ofMinutes(1), LocalDateTime.now());
        manager.add(task);
        manager.add(task);

        List<Task> history = manager.getHistory();
        assertEquals(0, history.size());
    }

    @Test
    public void testRemoveFromMiddle() throws Exception {
        TaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("ass", "Task 1", Status.NEW, Duration.ofMinutes(2), LocalDateTime.now());
        Task task2 = new Task("asss", "Task 2", Status.DONE, Duration.ofMinutes(4), LocalDateTime.now().plusMinutes(10));
        Task task3 = new Task("aass", "Task 3", Status.IN_PROGRESS, Duration.ofMinutes(5), LocalDateTime.now().plusMinutes(20));

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.printTaskById(2);
        taskManager.printTaskById(2);
        taskManager.printTaskById(1);
        taskManager.printTaskById(3);

        List<Task> history = taskManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task3, history.get(1));
    }

    @Test
    public void testRemoveLast() throws Exception {
        TaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("ass", "Task 1", Status.NEW, Duration.ofMinutes(2), LocalDateTime.now());
        Task task2 = new Task("asss", "Task 2", Status.DONE, Duration.ofMinutes(4), LocalDateTime.now().plusMinutes(10));
        Task task3 = new Task("aass", "Task 3", Status.IN_PROGRESS, Duration.ofMinutes(5), LocalDateTime.now().plusMinutes(20));

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.printTaskById(3);
        taskManager.printTaskById(3);
        taskManager.printTaskById(1);
        taskManager.printTaskById(2);

        List<Task> history = taskManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }
}