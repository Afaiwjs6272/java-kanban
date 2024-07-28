package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        Task task = new Task("der", "df", Status.NEW);
        Task task1 = new Task("dert", "ddf", Status.NEW);

        if (task.getId() == task1.getId()) {
            assertTrue(task.equals(task1));
        }
    }

    @Test
    public void checkSubTasksIdOnSame() {
        SubTask subTask = new SubTask("des", "daee", Status.NEW, 0);
        SubTask subTask1 = new SubTask("dws", "dds", Status.NEW, 0);

        if (subTask1.getId() == subTask.getId()) {
            assertTrue(subTask.equals(subTask1));
        }
    }

    @Test
    public void checkEpicIdOnSame() {
        Epic epic = new Epic("Tes", "ders");
        Epic epic1 = new Epic("dssd", "dcd");

        if (epic.getId() == epic1.getId()) {
            assertTrue(epic.equals(epic1));
        }
    }

    @Test
    public void shouldAdded2Tasks() throws Exception {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        Task task1 = new Task("Test", "ter", Status.NEW);

        manager.addTask(task);
        manager.addTask(task1);

        final List<Task> tasks = manager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void shouldAdded2SubTasks() throws Exception {
        SubTask subTask = new SubTask("Test addNewTask", "Test addNewTask description", Status.NEW, 1);
        SubTask subTask1 = new SubTask("Test", "ter", Status.NEW, 1);

        Epic epic = new Epic("sds", "sdsa");

        manager.addEpic(epic);
        manager.addSub(subTask);
        manager.addSub(subTask1);

        final List<SubTask> subs = manager.getAllSubTasks();

        assertNotNull(subs, "Задачи не возвращаются.");
        assertEquals(2, subs.size(), "Неверное количество задач.");
        assertEquals(subTask, subs.get(0), "Задачи не совпадают.");
    }

    @Test
    public void shouldAdded2Epics() throws Exception {
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        Epic epic1 = new Epic("Test", "ter");

        manager.addEpic(epic);
        manager.addEpic(epic1);

        final List<Epic> epics = manager.getAllEpics();

        assertNotNull(epic, "Задачи не возвращаются.");
        assertEquals(2, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    public void tasksShouldBeClear() throws Exception {
        Task task = new Task("sdsa", "sdaas", Status.NEW);
        Task task1 = new Task("dass", "saasd", Status.NEW);

        manager.addTask(task);
        manager.addTask(task1);
        manager.deleteTasks();

        final List<Task> tasks = manager.getAllTasks();

        assertEquals(0, tasks.size());
    }

    @Test
    public void subTasksShouldBeClear() throws Exception {
        SubTask subTask = new SubTask("sdsa", "sdaas", Status.NEW, 1);
        SubTask subTask1 = new SubTask("dass", "saasd", Status.NEW, 1);

        Epic epic = new Epic("sdsd", "ddas");

        manager.addEpic(epic);
        manager.addSub(subTask);
        manager.addSub(subTask1);

        manager.deleteSubTasks();

        final List<SubTask> subs = manager.getAllSubTasks();

        assertEquals(0, subs.size());
    }

    @Test
    public void epicsShouldBeClear() throws Exception {
        Epic epic = new Epic("sdsa", "sdaas");
        Epic epic1 = new Epic("dass", "saasd");

        manager.addEpic(epic);
        manager.addEpic(epic1);
        manager.deleteEpics();

        final List<Epic> epics = manager.getAllEpics();

        assertEquals(0, epics.size());
    }

    @Test
    public void shouldReturnSubTaskByEpic() throws Exception {
        assertNull(manager.getSubtaskByEpic(0));

        Epic epic = new Epic("SSSD", "ddss");

        SubTask subTask = new SubTask("dfsdf", "dssd", Status.NEW, 1);
        SubTask subTask1 = new SubTask("dff", "dssdsd", Status.NEW, 1);

        manager.addEpic(epic);
        manager.addSub(subTask);
        manager.addSub(subTask1);

        List<SubTask> subs = manager.getSubtaskByEpic(1);

        assertEquals(2, subs.size());
    }

    @Test
    public void shouldDeleteByTaskId() throws Exception {
        Task task = new Task("dsfs", "dsff", Status.NEW);

        manager.addTask(task);
        manager.deleteByTaskId(1);

        List<Task> tasks = manager.getAllTasks();

        assertTrue(tasks.isEmpty());
    }

    @Test
    public void shouldDeleteBySubTaskId() throws Exception {
        Epic epic = new Epic("ddssf", "dssf");

        SubTask subTask = new SubTask("dsad", "dsfff", Status.NEW, 1);

        manager.addEpic(epic);
        manager.addSub(subTask);
        manager.deleteBySubId(2);

        List<SubTask> subs = manager.getAllSubTasks();

        assertTrue(subs.isEmpty());
    }

    @Test
    public void shouldDeleteByEpicId() throws Exception {
        Epic epic = new Epic("dsfs", "dsff");

        SubTask subTask = new SubTask("dsad", "dsfff", Status.NEW, 1);
        SubTask subTask1 = new SubTask("dsdss", "sdds", Status.NEW, 1);

        manager.addEpic(epic);
        manager.addSub(subTask);
        manager.addSub(subTask1);
        manager.deleteByEpicId(1);

        List<SubTask> subTasks = manager.getAllSubTasks();
        assertTrue(subTasks.isEmpty());
    }

    @Test
    public void shouldUpdateTask() throws Exception {

        manager.addTask(new Task("dssff", "Ddss", Status.NEW));
        Task updatedTask = new Task("ssdff", "dasdd", Status.NEW);
        updatedTask.setId(1);

        manager.updateTask(updatedTask);

        Task task = manager.printTaskById(1);

        assertEquals(updatedTask, task);
    }

    @Test
    public void shouldUpdateSubTask() throws Exception {
        manager.addEpic(new Epic("dssd", "sssd"));
        manager.addSub(new SubTask("dssff", "Ddss", Status.NEW, 1));
        SubTask updatedSub = new SubTask("ssdff", "dasdd", Status.NEW, 1);
        updatedSub.setId(2);

        manager.updateSub(updatedSub);

        SubTask subTask = manager.printSubById(2);

        assertEquals(updatedSub, subTask);
    }

    @Test
    public void shouldUpdateEpic() throws Exception {

        manager.addEpic(new Epic("dssff", "Ddss"));
        Epic updatedEpic = new Epic("ssdff", "dasdd");
        updatedEpic.setId(1);

        manager.updateEpic(updatedEpic);

        Epic epic = manager.printEpicById(1);

        assertEquals(updatedEpic, epic);
    }
}