import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;
import service.TaskManager;

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
        Task task = new Task("r", "f", Status.NEW);
        manager.add(task);
        final List<Task> history = manager.getHistory();
        assertEquals(1, history.size());
        assertNotNull(history);
    }


    @Test
    void shouldDeleteFirstTask() throws Exception {
        TaskManager manager = new InMemoryTaskManager();
        manager.addTask(new Task("r", "f", Status.NEW));
        manager.addTask(new Task("rsad", "fas", Status.NEW));
        manager.printTaskById(1);
        manager.printTaskById(1);
        manager.printTaskById(2);
        List<Task> history = manager.getHistory();
        assertEquals(history.size(), 1);
    }
}