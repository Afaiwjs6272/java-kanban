package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    TaskManager manager;
    File temp;

    @BeforeEach
    public void createManager() {
        try {
            temp = File.createTempFile("tmp", ".txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        manager = new FileBackedTaskManager("tmp");
    }

    @Test
    public void shouldAddOneTask() throws Exception {
        try {
            Task task1 = new Task("asd", "gdhsj", Status.NEW);

            manager.addTask(task1);

            assertNotNull(manager);
            assertTrue(temp.exists());
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    @Test
    public void fileShouldBeNull() {
        assertTrue(temp.exists());
        assertEquals(temp.length(), 0);
    }

    @Test
    public void firstLineShouldBeAdd() throws IOException {
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(temp));
            br.write("id,type,name,status,description,epicId");
            br.close();

            BufferedReader reader = new BufferedReader(new FileReader(temp));
            String firstLine = reader.readLine();
            reader.close();

            assertNotNull(firstLine);
            assertEquals(firstLine, "id,type,name,status,description,epicId");
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    @Test
    public void testNewTaskManagerSameAsOld() throws Exception {
        try {
            Task task1 = new Task("1", "Feature", Status.NEW);
            Task task2 = new Task("2", "Bug", Status.IN_PROGRESS);
            Task task3 = new Task("3", "Improvement", Status.DONE);

            FileBackedTaskManager oldTaskManager = new FileBackedTaskManager("tmp");
            oldTaskManager.addTask(task1);
            oldTaskManager.addTask(task2);
            oldTaskManager.addTask(task3);

            FileBackedTaskManager newTaskManager = new FileBackedTaskManager("tmp");
            newTaskManager.addTask(task1);
            newTaskManager.addTask(task2);
            newTaskManager.addTask(task3);

            assertEquals(oldTaskManager.getAllTasks(), newTaskManager.getAllTasks());
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }
}