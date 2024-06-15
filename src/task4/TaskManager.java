package task4;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int id = 1;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();


    public void addTask(Task task) {
        task.setId(id);
        id++;
        tasks.put(task.getId(), task);
    }

    public void addSub(SubTask sub) {
        Epic epic = epics.get(sub.getEpicId());
        if (epic != null) {
            sub.setId(id);
            epic.addSubTask(sub);
            subTasks.put(sub.getId(), sub);
            syncEpic(epic);
            id++;
        }
    }

    public void addEpic(Epic epic) {
        epic.setId(id);
        id++;
        epics.put(epic.getId(), epic);
    }

    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            System.out.println("айдишник не найден");
        } else {
            tasks.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic newEpic){
        Epic oldEpic = epics.get(newEpic.getId());
        if(oldEpic != null){
            oldEpic.setTaskName(newEpic.getTaskName());
            oldEpic.setDescription(newEpic.getDescription());
        }
    }


    public void updateSub(SubTask sub) {
        if (!subTasks.containsKey(sub.getId())) {
            System.out.println("айдишник не найден");
        } else {
            int epicId = sub.getEpicId();
            SubTask oldSubTask = subTasks.get(sub.getId());
            if (oldSubTask.getEpicId() == sub.getEpicId()) {
                subTasks.replace(sub.getId(), sub);
                Epic epic = epics.get(epicId);
                ArrayList<SubTask> subTasksList = epic.getSubTaskList();
                subTasksList.remove(oldSubTask);
                subTasksList.add(sub);
                epic.setSubTasksList(subTasksList);
                syncEpic(epic);
            }
        }
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }


    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteEpics() {
        epics.clear();
        subTasks.clear();
    }

    public void deleteSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.deleteSubTask();
            syncEpic(epic);
        }
    }


    public void deleteByTaskId(int id) {
        tasks.remove(id);
    }

    public void deleteByEpicId(int id) {
        Epic epic = epics.get(id);
        if (epic!= null) {
            ArrayList<SubTask> epics = epic.getSubTaskList();
            for (SubTask subTask : epics) {
                subTasks.remove(subTask.getId());
            }
            epics.remove(id);
        } else {
            System.out.println("Не удалось получить эпик по ID.");
        }
    }

    public void deleteBySubId(int id) {
        SubTask subTask = subTasks.get(id);
        if (subTask!= null) {
            int epicID = subTask.getEpicId();
            subTasks.remove(id);
            Epic epic = epics.get(epicID);
            epic.deleteSingleSubTask(id);
            syncEpic(epic);
        }
    }


    public Task printTaskById(int id) {
        return tasks.get(id);
    }

    public SubTask printSubById(int id) {
        return subTasks.get(id);
    }

    public Epic printEpicById(int id) {
        return epics.get(id);
    }


    public ArrayList<SubTask> getSubtaskByEpic(int epicId) {
        if (!epics.containsKey(epicId)) {
            return null;
        }
        Epic epic = epics.get(epicId);
        return new ArrayList<>(epic.getSubTaskList());
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
}


