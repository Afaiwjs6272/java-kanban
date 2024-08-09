package service;

import exception.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File savedFile;
    private int nextId;

    public FileBackedTaskManager(String path) {
        savedFile = new File(path);
    }


    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(savedFile, StandardCharsets.UTF_8))) {
            bw.write("id,type,name,status,description,duration,startTime,epic");
            bw.newLine();

            tasks.values().stream()
                    .map(Task::toFileString)
                    .forEach(line -> {
                        try {
                            bw.write(line);
                            bw.newLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            epics.values().stream()
                    .map(Epic::toString)
                    .forEach(line -> {
                        try {
                            bw.write(line);
                            bw.newLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            subTasks.values().stream()
                    .map(SubTask::toFileString)
                    .forEach(line -> {
                        try {
                            bw.write(line);
                            bw.newLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении: " + e.getMessage());
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file.getPath());
        InMemoryTaskManager manager = new InMemoryTaskManager();

        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            // Пропускаем заголовок
            br.readLine();

            br.lines()
                    .map(line -> line.split(","))
                    .forEach(splitText -> {
                        try {
                            int id = Integer.parseInt(splitText[0].trim());
                            String type = splitText[1].trim();
                            String name = splitText[2].trim();
                            Status status = Status.valueOf(splitText[3].trim());
                            String description = splitText[4].trim();
                            Duration duration = Duration.parse(splitText[5].trim());
                            LocalDateTime startTime = LocalDateTime.parse(splitText[6].trim());

                            if (type.equals("Task")) {
                                taskManager.tasks.put(id, new Task(name, description, status, duration, startTime));
                            } else if (type.equals("SubTask")) {
                                int parentTaskId = Integer.parseInt(splitText[7].trim());
                                taskManager.subTasks.put(id, new SubTask(name, description, status, parentTaskId, duration, startTime));
                            } else if (type.equals("Epic")) {
                                Epic epic = new Epic(name, description, duration, startTime);
                                epic.setStatus(status);
                                taskManager.epics.put(id, epic);
                            }
                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                    });

            int maxId = taskManager.tasks.keySet().stream()
                    .max(Integer::compare)
                    .orElse(0);
            int nextId = maxId + 1;
            manager.id = nextId;

            taskManager.subTasks.values().forEach(subTask -> {
                Epic epic = taskManager.epics.get(subTask.getEpicId());
                if (epic != null) {
                    epic.addSubTask(subTask);
                }
            });

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении файла: " + e.getMessage());
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