package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected int id = 1;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, SubTask> subTasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final HistoryManager history = Managers.getDefaultHistory();

    protected final Set<Task> taskSet = new TreeSet<>(Comparator.comparing(Task::getStartTime));


    @Override
    public void addTask(Task task) throws Exception {
        boolean match = taskSet.stream().anyMatch(addedTask -> doTheTasksOverlap(task, addedTask));
        if (match) {
            throw new Exception("Задачи пересекаются во времени");
        }
        task.setId(id);
        id++;
        tasks.put(task.getId(), task);
        taskSet.add(task);
    }

    @Override
    public void addSub(SubTask sub) throws Exception {
        boolean match = taskSet.stream().anyMatch(addedSub -> doTheTasksOverlap(sub, addedSub));
        if (match) {
            throw new Exception("Задачи пересекаютс во времени");
        }
        Epic epic = epics.get(sub.getEpicId());
        if (epic != null) {
            sub.setId(id);
            epic.addSubTask(sub);
            subTasks.put(sub.getId(), sub);
            taskSet.add(sub);
            syncEpic(epic);
            id++;
        }
    }

    @Override
    public void addEpic(Epic epic) throws Exception {
        epic.setId(id);
        id++;
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateTask(Task task) throws Exception {
        if (!tasks.containsKey(task.getId())) {
            System.out.println("айдишник не найден");
        } else {
            taskSet.removeIf(existingTask -> existingTask.getId() == (task.getId()));
            boolean match = taskSet.stream().anyMatch(addedTask -> doTheTasksOverlap(task, addedTask));
            if (match) {
                throw new Exception("Задачи пересекаютс во времени");
            }
            tasks.put(task.getId(), task);
            taskSet.add(task);
        }
    }

    @Override
    public void updateEpic(Epic newEpic) throws Exception {
        Epic oldEpic = epics.get(newEpic.getId());
        if (oldEpic != null) {
            oldEpic.setTaskName(newEpic.getTaskName());
            oldEpic.setDescription(newEpic.getDescription());
        }
    }


    @Override
    public void updateSub(SubTask sub) throws Exception {
        if (!subTasks.containsKey(sub.getId())) {
            System.out.println("айдишник не найден");
        } else {
            taskSet.removeIf(existingSub -> existingSub.getId() == (sub.getId()));
            boolean match = taskSet.stream().anyMatch(addedSub -> doTheTasksOverlap(sub, addedSub));
            if (match) {
                throw new Exception("Задачи пересекаютс во времени");
            }
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
    public void deleteTasks() throws Exception {
        tasks.keySet().stream()
                .forEach(history::remove);
        taskSet.removeAll(tasks.values());
        taskSet.clear();
    }


    @Override
    public void deleteEpics() throws Exception {
        epics.keySet().stream()
                .forEach(history::remove);
        epics.clear();

        subTasks.keySet().stream()
                .forEach(history::remove);
        subTasks.clear();
        taskSet.removeAll(subTasks.values());
    }


    @Override
    public void deleteSubTasks() throws Exception {
        subTasks.keySet().stream()
                .forEach(history::remove);
        subTasks.clear();
        taskSet.removeAll(subTasks.values());

        epics.values().stream()
                .forEach(epic -> {
                    epic.deleteSubTask();
                    syncEpic(epic);
                });
    }


    @Override
    public void deleteByTaskId(int id) throws Exception {
        tasks.remove(id);
        history.remove(id);
        taskSet.removeIf(task -> task.getId() == id);
    }

    @Override
    public void deleteByEpicId(int id) throws Exception {
        Epic epic = epics.get(id);
        if (epic != null) {
            epic.getSubTaskList().forEach(subTask -> {
                subTasks.remove(subTask.getId());
                history.remove(subTask.getId());
                taskSet.removeIf(sub -> sub.getId() == id);
            });
            epics.remove(id);
            history.remove(id);
        } else {
            System.out.println("Не удалось получить эпик по ID.");
        }
    }

    @Override
    public void deleteBySubId(int id) throws Exception {
        SubTask subTask = subTasks.get(id);
        if (subTask != null) {
            int epicID = subTask.getEpicId();
            subTasks.remove(id);
            taskSet.removeIf(sub -> sub.getId() == id);
            Epic epic = epics.get(epicID);
            epic.deleteSingleSubTask(id);
            syncEpic(epic);
            history.remove(id);
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


    public Set<Task> getPrioritizedTasks() {
        return taskSet;
    }


    public boolean doTheTasksOverlap(Task task, Task task1) {
        return task.getStartTime().isBefore(task1.getEndTime()) && task.getEndTime().isAfter(task1.getStartTime());
    }


    private static void syncEpic(Epic epic) {
        long newSub = epic.getSubTaskList().stream()
                .filter(subTask -> subTask.getStatus() == Status.NEW)
                .count();

        long doneSub = epic.getSubTaskList().stream()
                .filter(subTask -> subTask.getStatus() == Status.DONE)
                .count();

        if (newSub == epic.getSubTaskList().size()) {
            epic.setStatus(Status.NEW);
        } else if (doneSub == epic.getSubTaskList().size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }

        epic.setStartTime(epic.calculateEpicsStartTime());
        epic.setDuration(epic.calculateEpicsDuration());
        epic.calculateEpicsEndTime();
    }
}


