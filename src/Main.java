import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.FileBackedTaskManager;
import service.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        /* InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Task washDish = new Task("помыть посуду", "только ложки", Status.NEW);
        inMemoryTaskManager.addTask(washDish);

        Epic putElephantInFridge = new Epic("положить слона в холодильник", "в 3 действия");
        inMemoryTaskManager.addEpic(putElephantInFridge);

        SubTask openFridge = new SubTask("открыть холодильник", "1 действие", Status.NEW, 2);
        inMemoryTaskManager.addSub(openFridge);

        SubTask putElephant = new SubTask("положить слона", "2 действие", Status.NEW, 2);
        inMemoryTaskManager.addSub(putElephant);

        SubTask closeFridge = new SubTask("закрыть холодильник", "3 действие", Status.NEW, 2);
        inMemoryTaskManager.addSub(closeFridge);

        inMemoryTaskManager.getAllTasks();
        inMemoryTaskManager.getAllEpics();
        inMemoryTaskManager.getAllSubTasks();

        inMemoryTaskManager.printEpicById(2);
        inMemoryTaskManager.printTaskById(1);

        washDish.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.updateTask(washDish);

        inMemoryTaskManager.updateEpic(putElephantInFridge);

        inMemoryTaskManager.getAllTasks();
        inMemoryTaskManager.getAllEpics();

        inMemoryTaskManager.deleteByEpicId(2);

        inMemoryTaskManager.getAllEpics();

        inMemoryTaskManager.printEpicById(2); */

        FileBackedTaskManager taskManager = new FileBackedTaskManager("tasks.txt");
        InMemoryTaskManager manager = new InMemoryTaskManager();


        Task task = new Task("ssd", "ssaa", Status.NEW, Duration.ofMinutes(13), LocalDateTime.now().plusMinutes(14));
        Task task1 = new Task("ssd", "ssaa", Status.NEW, Duration.ofMinutes(13), LocalDateTime.now().plusMinutes(30));

        manager.addTask(task);
        manager.addTask(task1);
        manager.addTaskToSet(task);
        manager.addTaskToSet(task1);
        manager.getAllTasks();
        manager.getPrioritizedTasks();

        SubTask elephant = new SubTask("Subtask 1", "Description 1", Status.NEW, 2, Duration.ofMinutes(1), LocalDateTime.now());
        SubTask fridge = new SubTask("Subtask 2", "Description 2", Status.NEW, 2, Duration.ofMinutes(2), LocalDateTime.now().plusDays(1));

        Epic putElephantInFridge = new Epic("положить слона в холодильник", "в 3 действия", Duration.ofMinutes(0), LocalDateTime.now());
        putElephantInFridge.addSubTask(elephant);
        putElephantInFridge.addSubTask(fridge);
        taskManager.addEpic(putElephantInFridge);

    }
}
