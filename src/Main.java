import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.InMemoryTaskManager;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

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

        inMemoryTaskManager.printEpicById(2);


    }
}

