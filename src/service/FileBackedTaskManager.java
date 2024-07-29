package service;

import exception.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File savedFile;
    private int nextId;

    public FileBackedTaskManager(String path) {
        savedFile = new File(path);
    }

    public int getNextId() {
        return ++this.nextId;
    }

    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(savedFile, StandardCharsets.UTF_8))) {
            bw.write("id,type,name,status,description,epic");
            bw.newLine();
            for (Task task : tasks.values()) {
                bw.write(task.toFileString());
                bw.newLine();
            }
            for (Epic task : epics.values()) {
                bw.write(task.toString());
                bw.newLine();
            }
            for (SubTask task : subTasks.values()) {
                bw.write(task.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении: " + e.getMessage());
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file.getPath());

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

                int newId = taskManager.getNextId();
                Status status = Status.valueOf(statusStr);

                if (type.equals("Task")) {
                    taskManager.tasks.put(newId, new Task(name, description, status));
                } else if (type.equals("SubTask")) {
                    int parentTaskId = Integer.parseInt(splitText[5].trim());
                    taskManager.subTasks.put(newId, new SubTask(name, description, status, parentTaskId));
                } else if (type.equals("Epic")) {
                    Epic epic = new Epic(name, description);
                    epic.setStatus(status);
                    taskManager.epics.put(newId, epic);
                }
            }
            for (SubTask subTask : taskManager.subTasks.values()) {
                Epic epic = taskManager.epics.get(subTask.getEpicId());
                if (epic != null) {
                    epic.addSubTask(subTask);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при записи в файл: " + e.getMessage());
        }

        return taskManager;
    }

    @Override
    public void addTask(Task task) throws Exception {
        super.addTask(task);
        save();
    }

    @Override
    public void addSub(SubTask sub) throws Exception {
        super.addSub(sub);
        save();
    }

    @Override
    public void addEpic(Epic epic) throws Exception {
        super.addEpic(epic);
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
    public void deleteTasks() throws Exception {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpics() throws Exception {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteSubTasks() throws Exception {
        super.deleteSubTasks();
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
}