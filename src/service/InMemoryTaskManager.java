package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int id = 1;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager history = Managers.getDefaultHistory();


    @Override
    public void addTask(Task task) {
        task.setId(id);
        id++;
        tasks.put(task.getId(), task);
    }

    @Override
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

    @Override
    public void addEpic(Epic epic) {
        epic.setId(id);
        id++;
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            System.out.println("айдишник не найден");
        } else {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic newEpic){
        Epic oldEpic = epics.get(newEpic.getId());
        if(oldEpic != null){
            oldEpic.setTaskName(newEpic.getTaskName());
            oldEpic.setDescription(newEpic.getDescription());
        }
    }


    @Override
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

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }


    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.deleteSubTask();
            syncEpic(epic);
        }
    }


    @Override
    public void deleteByTaskId(int id) {
        tasks.remove(id);
    }

    @Override
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

    @Override
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


    @Override
    public Task printTaskById(int id) {
        history.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public SubTask printSubById(int id) {
        history.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public Epic printEpicById(int id) {
        history.add(epics.get(id));
        return epics.get(id);
    }


    @Override
    public ArrayList<SubTask> getSubtaskByEpic(int epicId) {
        if (!epics.containsKey(epicId)) {
            return null;
        }
        Epic epic = epics.get(epicId);
        return new ArrayList<>(epic.getSubTaskList());
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
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


