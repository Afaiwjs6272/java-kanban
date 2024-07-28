package service;

import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    public List<Task> tasks;
    private File savedFile;

    public FileBackedTaskManager(String path) {
        tasks = new ArrayList<>();
        savedFile = new File(path);
    }

    public void save() {
        try (PrintWriter writer = new PrintWriter(savedFile)) {
            writer.println("id,type,name,status,description,epicId");

            for (Task task : tasks) {
                StringBuilder sb = new StringBuilder();
                sb.append(task.getId()).append(",");
                if (task.getType().equals(Type.SUBTASK)) {
                    sb.append("SubTask,");
                } else if (task.getType().equals(Type.EPIC)) {
                    sb.append("Epic,");
                } else {
                    sb.append("Task,");
                }
                sb.append(task.getTaskName()).append(",");
                sb.append(task.getStatus()).append(",");
                sb.append(task.getDescription()).append(",");
                if (task instanceof SubTask) {
                    sb.append(((SubTask) task).getEpicId());
                } else {
                    sb.append("");
                }
                writer.println(sb.toString());
            }
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении файла: " + e.getMessage());
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
        FileBackedTaskManager taskManager = new FileBackedTaskManager("tasks.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            boolean headersSkipped = false;
            while ((line = br.readLine()) != null) {
                if (!headersSkipped) {
                    headersSkipped = true;
                    continue;
                }

                String[] splitText = line.split(",");

                String type = splitText[1].trim();
                String statusStr = splitText[3].trim();
                String name = splitText[2].trim();
                String description = splitText[4].trim();

                if (type.equals("Task")) {
                    Status status = Status.valueOf(statusStr);
                    taskManager.tasks.add(new Task(name, description, status));
                } else if (type.equals("SubTask")) {
                    int parentTaskId = Integer.parseInt(splitText[5].trim());
                    Status status = Status.valueOf(statusStr);
                    taskManager.tasks.add(new SubTask(name, description, status, parentTaskId));
                } else if (type.equals("Epic")) {
                    taskManager.tasks.add(new Epic(name, description));
                } else {
                    System.out.println("Неизвестный тип: " + type);
                }
            }
        }

        return taskManager;
    }

    @Override
    public void addTask(Task task) throws Exception {
        super.addTask(task);
        tasks.add(task);
        save();
    }

    @Override
    public void addSub(SubTask sub) throws Exception {
        super.addSub(sub);
        tasks.add(sub);
        save();
    }

    @Override
    public void addEpic(Epic epic) throws Exception {
        super.addEpic(epic);
        tasks.add(epic);
        save();
    }

    @Override
    public void updateTask(Task task) throws Exception {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) throws Exception {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void updateSub(SubTask sub) throws Exception {
        super.updateSub(sub);
        save();
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return super.getAllTasks();
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return super.getAllEpics();
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return super.getAllSubTasks();
    }

    @Override
    public void deleteTasks() throws Exception {
        super.deleteTasks();
        tasks.clear();
        save();
    }

    @Override
    public void deleteEpics() throws Exception {
        super.deleteEpics();
        tasks.removeIf(task -> task instanceof Epic);
        save();
    }

    @Override
    public void deleteSubTasks() throws Exception {
        super.deleteSubTasks();
        tasks.removeIf(task -> task instanceof SubTask);
        save();
    }

    @Override
    public void deleteByTaskId(int id) throws Exception {
        super.deleteByTaskId(id);
        save();
    }

    @Override
    public void deleteByEpicId(int id) throws Exception {
        super.deleteByEpicId(id);
        save();
    }

    @Override
    public void deleteBySubId(int id) throws Exception {
        super.deleteBySubId(id);
        save();
    }

    @Override
    public Task printTaskById(int id) {
        return super.printTaskById(id);
    }

    @Override
    public SubTask printSubById(int id) {
        return super.printSubById(id);
    }

    @Override
    public Epic printEpicById(int id) {
        return super.printEpicById(id);
    }

    @Override
    public ArrayList<SubTask> getSubtaskByEpic(int epicId) {
        return super.getSubtaskByEpic(epicId);
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }
}