package task4;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task washDish = new Task("помыть посуду", "только ложки", Status.NEW);
        taskManager.addTask(washDish);

        Epic putElephantInFridge = new Epic("положить слона в холодильник", "в 3 действия");
        taskManager.addEpic(putElephantInFridge);

        SubTask openFridge = new SubTask("открыть холодильник", "1 действие", Status.NEW, 2);
        taskManager.addSub(openFridge);

        SubTask putElephant = new SubTask("положить слона", "2 действие", Status.NEW, 2);
        taskManager.addSub(putElephant);

        SubTask closeFridge = new SubTask("закрыть холодильник", "3 действие", Status.NEW, 2);
        taskManager.addSub(closeFridge);

        taskManager.getAllTasks();
        taskManager.getAllEpics();
        taskManager.getAllSubTasks();

        taskManager.printEpicById(2);
        taskManager.printTaskById(1);

        washDish.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(washDish);

        taskManager.updateEpic(putElephantInFridge);

        taskManager.getAllTasks();
        taskManager.getAllEpics();

        taskManager.deleteByEpicId(2);

        taskManager.getAllEpics();

        taskManager.printEpicById(2);
    }
}

