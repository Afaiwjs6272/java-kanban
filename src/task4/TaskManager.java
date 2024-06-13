import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int id = 1;
    public final HashMap<Integer, Task> Tasks = new HashMap<>();
    public final HashMap<Integer, SubTask> SubTasks = new HashMap<>();
    public final HashMap<Integer, Epic> Epics = new HashMap<>();


    public void addTask(Task task) {
        task.setId(id);
        id++;
        Tasks.put(task.getId(), task);
    }

    public void addSub(SubTask sub) {
        sub.setId(id);
        Epic epic = Epics.get(sub.getEpicId());
        epic.addSubTask(sub);
        SubTasks.put(sub.getId(), sub);
        syncEpic(epic);
        id++;
    }

    public void addEpic(Epic epic) {
        epic.setId(id);
        id++;
        Epics.put(epic.getId(), epic);
    }

    public void updateTask(Task task) {
        if (!Tasks.containsKey(task.getId())) {
            System.out.println("айдишник не найден");
        } else {
            Tasks.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (!Epics.containsKey(epic.getId())) {
            System.out.println("айдишник не найден");
        } else {
            Tasks.put(epic.getId(), epic);
        }
    }

    public void updateSub(SubTask sub) {
        if (!SubTasks.containsKey(sub.getId())) {
            System.out.println("айдишник не найден");
        } else {
            int epicId = sub.getEpicId();
            SubTask oldSubTask = SubTasks.get(sub.getId());
            SubTasks.replace(sub.getId(), sub);
            Epic epic = Epics.get(epicId);
            ArrayList<SubTask> subTasksList = epic.getSubTaskList();
            subTasksList.remove(oldSubTask);
            subTasksList.add(sub);
            epic.setSubTasksList(subTasksList);
            syncEpic(epic);
        }
    }

    public void printTasks() {
        System.out.println(Tasks.values());
    }

    public void printEpics() {
        System.out.println(Epics.values());
    }

    public void printSubTasks() {
        System.out.println(SubTasks.values());
    }

    public void deleteTasks() {
        Tasks.clear();
    }

    public void deleteEpics() {
        Epics.clear();
        SubTasks.clear();
    }

    public void deleteSubTasks() {
        SubTasks.clear();
        for (Epic epic : Epics.values()) {
            epic.deleteSubTask();
            epic.setStatus(Status.NEW);
        }
    }


    public void deleteByTaskId(int id) {
        Tasks.remove(id);
    }

    public void deleteByEpicId(int id) {
        Epics.remove(id);
        ArrayList<SubTask> epics = Epics.get(id).getSubTaskList();
        for (SubTask subTask : epics) {
            SubTasks.remove(subTask.getId());
        }
    }

    public void deleteBySubId(int id) {
        SubTask subTask = SubTasks.get(id);
        int epicID = subTask.getEpicId();
        SubTasks.remove(id);
        Epic epic = Epics.get(epicID);
        ArrayList<SubTask> subTasksList = epic.getSubTaskList();
        subTasksList.remove(subTask);
        epic.setSubTasksList(subTasksList);
        syncEpic(epic);
    }


    private static void syncEpic(Epic epic) {
        int doneSub = 0;
        int newSub = 0;
        ArrayList<SubTask> allSubTasks = epic.getSubTaskList();

        for (SubTask subTask : allSubTasks) {
            if (subTask.getStatus() == Status.NEW) {
                newSub++;
            }
            if (subTask.getStatus() == Status.DONE) {
                doneSub++;
            }
        }

        if (newSub == allSubTasks.size()) {
            epic.setStatus(Status.NEW);
        } else if (doneSub == allSubTasks.size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }


    public void printTaskById(int id) {
        if (!Tasks.containsKey(id)) {
            System.out.println("не найден ключ");
        } else {
            Tasks.get(id);
        }
    }

    public void printSubById(int id) {
        if (!SubTasks.containsKey(id)) {
            System.out.println("не найден ключ");
        } else {
            SubTasks.get(id);
        }
    }

    public void printEpicById(int id) {
        if (!Epics.containsKey(id)) {
            System.out.println("не найден ключ");
        } else {
            Epics.get(id);
        }
    }
}


