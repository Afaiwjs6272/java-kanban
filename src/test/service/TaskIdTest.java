package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.*;

import java.security.spec.ECParameterSpec;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TasksIdTest {
    TasksId manager;

    @BeforeEach
    public void createManager(){
        manager = new TasksId();
    }

    @Test
    public void shouldReturnTaskById(){
        Task task = new Task("dsdf", "asda",Status.NEW);
        manager.add(task);

        Task task1 = manager.getTask(0);
        assertEquals(task1, manager.getTask(task.getId()));
    }

    @Test
    public void shouldReturnSubTaskById(){
        SubTask subTask = new SubTask("dsdf", "asda",Status.NEW,1);
        manager.add(subTask);

        SubTask subTask1 = manager.getSubtask(0);
        assertEquals(subTask1, manager.getSubtask(subTask.getId()));
    }

    @Test
    public void shouldReturnEpicById(){
        Epic epic = new Epic("dsdf", "asda");
        manager.add(epic);

        Epic epic1 = manager.getEpic(0);
        assertEquals(epic1, manager.getEpic(epic.getId()));
    }


}