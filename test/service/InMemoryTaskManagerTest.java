package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    TaskManager manager;

    @BeforeEach
    public void createManager() {
        manager = new InMemoryTaskManager();
    }


    @Test
    public void checkTasksIdOnSame() {
        Task task = new Task("der", "df", Status.NEW, Duration.ofMinutes(2), LocalDateTime.now());
        Task task1 = new Task("dert", "ddf", Status.NEW, Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(10));

        if (task.getId() == task1.getId()) {
            assertTrue(task.equals(task1));
        }
    }

    @Test
    public void checkSubTasksIdOnSame() {
        SubTask subTask = new SubTask("des", "daee", Status.NEW, 0, Duration.ofMinutes(2), LocalDateTime.now());
        SubTask subTask1 = new SubTask("dws", "dds", Status.NEW, 0, Duration.ofMinutes(2), LocalDateTime.now().plusMinutes(12));

        if (subTask1.getId() == subTask.getId()) {
            assertTrue(subTask.equals(subTask1));
        }
    }

    @Test
    public void checkEpicIdOnSame() {
        Epic epic = new Epic("Tes", "ders", Duration.ofMinutes(2), LocalDateTime.now());
        Epic epic1 = new Epic("dssd", "dcd", Duration.ofMinutes(2), LocalDateTime.now().plusMinutes(10));

        if (epic.getId() == epic1.getId()) {
            assertTrue(epic.equals(epic1));
        }
    }

    @Test
    public void shouldAdded2Tasks() throws Exception {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW, Duration.ofMinutes(2), LocalDateTime.now());
        Task task1 = new Task("Test", "ter", Status.NEW, Duration.ofMinutes(2), LocalDateTime.now().plusMinutes(12));

        manager.addTask(task);
        manager.addTask(task1);

        final List<Task> tasks = manager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void shouldAdded2SubTasks() throws Exception {
        Epic epic = new Epic("SSSD", "ddss", Duration.ofMinutes(2), LocalDateTime.now());

        SubTask subTask = new SubTask("dfsdf", "dssd", Status.NEW, 1, Duration.ofMinutes(2), LocalDateTime.now());
        SubTask subTask1 = new SubTask("dff", "dssdsd", Status.NEW, 1, Duration.ofMinutes(2), LocalDateTime.now().plusMinutes(12));

        manager.addEpic(epic);
        manager.addSub(subTask);
        manager.addSub(subTask1);

        List<SubTask> subs = manager.getSubtaskByEpic(1);

        assertEquals(2, subs.size());
    }

    @Test
    public void shouldAdded2Epics() throws Exception {
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description", Duration.ofMinutes(2), LocalDateTime.now());
        Epic epic1 = new Epic("Test", "ter", Duration.ofMinutes(2), LocalDateTime.now().plusMinutes(10));

        manager.addEpic(epic);
        manager.addEpic(epic1);

        final List<Epic> epics = manager.getAllEpics();

        assertNotNull(epic, "Задачи не возвращаются.");
        assertEquals(2, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    public void tasksShouldBeClear() throws Exception {
        Task task = new Task("sdsa", "sdaas", Status.NEW, Duration.ofMinutes(2), LocalDateTime.now().plusMinutes(2));
        Task task1 = new Task("dass", "saasd", Status.NEW, Duration.ofMinutes(2), LocalDateTime.now().plusMinutes(10));

        manager.addTask(task);
        manager.addTask(task1);
        manager.deleteTasks();

        final List<Task> tasks = manager.getAllTasks();

        assertEquals(0, tasks.size());
    }

    @Test
    public void subTasksShouldBeClear() throws Exception {
        SubTask subTask = new SubTask("sdsa", "sdaas", Status.NEW, 1, Duration.ofMinutes(2), LocalDateTime.now());
        SubTask subTask1 = new SubTask("dass", "saasd", Status.NEW, 1, Duration.ofMinutes(2), LocalDateTime.now().plusMinutes(12));

        Epic epic = new Epic("sdsd", "ddas", Duration.ofMinutes(2), LocalDateTime.now());

        manager.addEpic(epic);
        manager.addSub(subTask);
        manager.addSub(subTask1);

        manager.deleteSubTasks();

        final List<SubTask> subs = manager.getAllSubTasks();

        assertEquals(0, subs.size());
    }

    @Test
    public void epicsShouldBeClear() throws Exception {
        Epic epic = new Epic("sdsa", "sdaas", Duration.ofMinutes(2), LocalDateTime.now());
        Epic epic1 = new Epic("dass", "saasd", Duration.ofMinutes(2), LocalDateTime.now().plusMinutes(12));

        manager.addEpic(epic);
        manager.addEpic(epic1);
        manager.deleteEpics();

        final List<Epic> epics = manager.getAllEpics();

        assertEquals(0, epics.size());
    }

    @Test
    public void shouldReturnSubTaskByEpic() throws Exception {
        assertNull(manager.getSubtaskByEpic(0));

        Epic epic = new Epic("SSSD", "ddss", Duration.ofMinutes(2), LocalDateTime.now());

        SubTask subTask = new SubTask("dfsdf", "dssd", Status.NEW, 1, Duration.ofMinutes(2), LocalDateTime.now());
        SubTask subTask1 = new SubTask("dff", "dssdsd", Status.NEW, 1, Duration.ofMinutes(4), LocalDateTime.now().plusMinutes(12));

        manager.addEpic(epic);
        manager.addSub(subTask);
        manager.addSub(subTask1);

        List<SubTask> subs = manager.getSubtaskByEpic(1);

        assertEquals(2, subs.size());
    }

    @Test
    public void shouldDeleteByTaskId() throws Exception {
        Task task = new Task("dsfs", "dsff", Status.NEW, Duration.ofMinutes(2), LocalDateTime.now());

        manager.addTask(task);
        manager.deleteByTaskId(1);

        List<Task> tasks = manager.getAllTasks();

        assertTrue(tasks.isEmpty());
    }

    @Test
    public void shouldDeleteBySubTaskId() throws Exception {
        Epic epic = new Epic("ddssf", "dssf", Duration.ofMinutes(2), LocalDateTime.now());

        SubTask subTask = new SubTask("dsad", "dsfff", Status.NEW, 1, Duration.ofMinutes(2), LocalDateTime.now().plusMinutes(12));

        manager.addEpic(epic);
        manager.addSub(subTask);
        manager.deleteBySubId(0);

        List<SubTask> subs = manager.getAllSubTasks();

        assertTrue(subs.isEmpty());
    }

    @Test
    public void shouldDeleteByEpicId() throws Exception {
        Epic epic = new Epic("dsfs", "dsff", Duration.ofMinutes(2), LocalDateTime.now());

        SubTask subTask = new SubTask("dsad", "dsfff", Status.NEW, 1, Duration.ofMinutes(2), LocalDateTime.now().plusMinutes(2));
        SubTask subTask1 = new SubTask("dsdss", "sdds", Status.NEW, 1, Duration.ofMinutes(2), LocalDateTime.now().plusMinutes(10));

        manager.addEpic(epic);
        manager.addSub(subTask);
        manager.addSub(subTask1);
        manager.deleteByEpicId(1);

        List<SubTask> subTasks = manager.getAllSubTasks();
        assertTrue(subTasks.isEmpty());
    }

    @Test
    public void shouldUpdateTask() throws Exception {

        manager.addTask(new Task("dssff", "Ddss", Status.NEW, Duration.ofMinutes(2), LocalDateTime.now()));
        Task updatedTask = new Task("ssdff", "dasdd", Status.NEW, Duration.ofMinutes(2), LocalDateTime.now().plusMinutes(10));
        updatedTask.setId(1);

        manager.updateTask(updatedTask);

        Task task = manager.printTaskById(1);

        assertEquals(updatedTask, task);
    }

    @Test
    public void shouldUpdateSubTask() throws Exception {
        manager.addEpic(new Epic("dssd", "sssd", Duration.ofMinutes(2), LocalDateTime.now()));
        manager.addSub(new SubTask("dssff", "Ddss", Status.NEW, 1, Duration.ofMinutes(2), LocalDateTime.now()));
        SubTask updatedSub = new SubTask("ssdff", "dasdd", Status.NEW, 1, Duration.ofMinutes(2), LocalDateTime.now().plusMinutes(12));
        updatedSub.setId(2);

        manager.updateSub(updatedSub);

        SubTask subTask = manager.printSubById(0);

        assertEquals(updatedSub, subTask);
    }

    @Test
    public void shouldUpdateEpic() throws Exception {

        manager.addEpic(new Epic("dssff", "Ddss", Duration.ofMinutes(2), LocalDateTime.now()));
        Epic updatedEpic = new Epic("ssdff", "dasdd", Duration.ofMinutes(4), LocalDateTime.now().plusMinutes(2));
        updatedEpic.setId(1);

        manager.updateEpic(updatedEpic);

        Epic epic = manager.printEpicById(1);

        assertEquals(updatedEpic, epic);
    }

    @Test
    public void allSubtasksStatusNew() throws Exception {
        Epic epic = new Epic("dsd", "sdss", Duration.ofMinutes(2), LocalDateTime.now());
        manager.addEpic(epic);

        SubTask sub = new SubTask("sddd", "dad", Status.NEW, 1, Duration.ofMinutes(2), LocalDateTime.now().plusMinutes(3));
        SubTask sub1 = new SubTask("dsdd", "fssf", Status.NEW, 1, Duration.ofMinutes(2), LocalDateTime.now().plusMinutes(10));

        manager.addSub(sub);
        manager.addSub(sub1);

        assertEquals(epic.getStatus(), Status.NEW);
        assertEquals(epic.calculateEpicsDuration().toMinutes(), 4);
    }

    @Test
    public void allSubtasksStatusDone() throws Exception {
        Epic epic = new Epic("dsd", "sdss", Duration.ofMinutes(2), LocalDateTime.now());
        manager.addEpic(epic);

        SubTask sub = new SubTask("sddd", "dad", Status.DONE, 1, Duration.ofMinutes(2), LocalDateTime.now().plusMinutes(3));
        SubTask sub1 = new SubTask("dsdd", "fssf", Status.DONE, 1, Duration.ofMinutes(2), LocalDateTime.now().plusMinutes(10));

        manager.addSub(sub);
        manager.addSub(sub1);

        assertEquals(epic.getStatus(), Status.DONE);
        assertEquals(epic.calculateEpicsDuration().toMinutes(), 4);
    }

    @Test
    public void addedSubtaskStatusNewAndStatusDone() throws Exception {
        Epic epic = new Epic("dsd", "sdss", Duration.ofMinutes(2), LocalDateTime.now());
        manager.addEpic(epic);

        SubTask sub = new SubTask("sddd", "dad", Status.NEW, 1, Duration.ofMinutes(2), LocalDateTime.now().plusMinutes(3));
        SubTask sub1 = new SubTask("dsdd", "fssf", Status.DONE, 1, Duration.ofMinutes(2), LocalDateTime.now().plusMinutes(10));

        manager.addSub(sub);
        manager.addSub(sub1);

        assertEquals(epic.getStatus(), Status.IN_PROGRESS);
        assertEquals(epic.calculateEpicsDuration().toMinutes(), 4);
    }

    @Test
    public void allSubtasksStatusInProgress() throws Exception {
        Epic epic = new Epic("dsd", "sdss", Duration.ofMinutes(2), LocalDateTime.now());
        manager.addEpic(epic);

        SubTask sub = new SubTask("sddd", "dad", Status.IN_PROGRESS, 1, Duration.ofMinutes(2), LocalDateTime.now().plusMinutes(3));
        SubTask sub1 = new SubTask("dsdd", "fssf", Status.IN_PROGRESS, 1, Duration.ofMinutes(2), LocalDateTime.now().plusMinutes(10));

        manager.addSub(sub);
        manager.addSub(sub1);

        assertEquals(epic.getStatus(), Status.IN_PROGRESS);
        assertEquals(epic.calculateEpicsDuration().toMinutes(), 4);
    }
}
