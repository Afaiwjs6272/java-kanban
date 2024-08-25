package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import model.Epic;
import model.SubTask;
import model.Task;
import model.adapter.DurationTypeAdapter;
import model.adapter.LocalDateTimeAdapter;
import service.InMemoryTaskManager;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.regex.Pattern;


public class HttpTaskServer extends BaseHttpHandler {
    public static final int PORT = 8080;
    static GsonBuilder gsonBuilder;
    private final TaskManager taskManager = new InMemoryTaskManager();
    private static HttpServer server;
    private static Gson gson;

    public HttpTaskServer() throws IOException {
        gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting();
        gson = gsonBuilder.create();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/api/v1/tasks", new TaskHandler());
        server.createContext("/api/v1/subtasks", new SubTaskHandler());
        server.createContext("/api/v1/epics", new EpicHandler());
        server.createContext("/api/v1/history", new HistoryHandler());
        server.createContext("/api/v1/prioritized", new PrioritizedHandler());
    }

    public class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) {
            try {
                String path = httpExchange.getRequestURI().getPath();
                String method = httpExchange.getRequestMethod();
                switch (method) {
                    case "GET": {
                        if (Pattern.matches("^/api/v1/tasks$", path)) {
                            String response = gson.toJson(taskManager.getAllTasks());
                            sendText(httpExchange, response);
                            return;
                        }
                        if (Pattern.matches("^/api/v1/tasks/\\d+$", path)) {
                            String pathId = path.replaceFirst("/api/v1/tasks/", "");
                            int id = parsePathId(pathId);
                            if (id != -1) {
                                String response = gson.toJson(taskManager.printTaskById(id));
                                sendText(httpExchange, response);
                                break;
                            } else {
                                sendNotFound(httpExchange, "Некорректный id - " + pathId);
                            }
                        }
                        break;
                    }
                    case "POST": {
                        if (Pattern.matches("^/api/v1/tasks/$", path)) {
                            String response = readText(httpExchange);
                            Task task = gson.fromJson(response, Task.class);
                            boolean taskExists = taskManager.getAllTasks().stream()
                                    .map(Task::getId)
                                    .anyMatch(id -> id == task.getId());

                            if (taskExists) {
                                taskManager.updateTask(task);
                            } else {
                                taskManager.addTask(task);
                            }
                            httpExchange.sendResponseHeaders(201, task.getId());
                        } else {
                            sendHasInteractions(httpExchange, "Задача пересекается с существующими");
                        }
                        break;
                    }
                    case "DELETE": {
                        if (Pattern.matches("^/api/v1/tasks/\\d+$", path)) {
                            String pathId = path.replaceFirst("/api/v1/tasks/", "");
                            int id = parsePathId(pathId);
                            if (id != -1 && id < taskManager.getAllTasks().size() + 1) {
                                taskManager.deleteByTaskId(id);
                                sendText(httpExchange, "Удалили задачу - " + id);
                            } else {
                                sendNotFound(httpExchange, "Неверный id - " + pathId);
                            }
                        }
                        break;
                    }
                    default: {
                        System.out.println("Данный метод нельзя вызвать " + method);
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                httpExchange.close();
            }
        }
    }


    public class EpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) {
            try {
                String path = httpExchange.getRequestURI().getPath();
                String method = httpExchange.getRequestMethod();
                switch (method) {
                    case "GET": {
                        if (Pattern.matches("^/api/v1/epics$", path)) {
                            String response = gson.toJson(taskManager.getAllEpics());
                            sendText(httpExchange, response);
                        }
                        if (Pattern.matches("^/api/v1/epics/\\d+$", path)) {
                            String pathId = path.replaceFirst("/api/v1/epics/", "");
                            int id = parsePathId(pathId);
                            if (id != -1) {
                                String response = gson.toJson(taskManager.printEpicById(id));
                                sendText(httpExchange, response);
                                break;
                            } else {
                                sendNotFound(httpExchange, "Некорректный id - " + pathId);
                            }
                        }
                        break;
                    }
                    case "POST": {
                        if (Pattern.matches("^/api/v1/epics$", path)) {
                            String response = readText(httpExchange);
                            Epic epic = gson.fromJson(response, Epic.class);
                            boolean epicExists = taskManager.getAllEpics().stream()
                                    .map(Epic::getId)
                                    .anyMatch(id -> id == epic.getId());

                            if (epicExists) {
                                taskManager.updateTask(epic);
                            } else {
                                taskManager.addTask(epic);
                            }
                            httpExchange.sendResponseHeaders(201, epic.getId());
                        } else {
                            sendHasInteractions(httpExchange, "Задача пересекается с существующими");
                        }
                        break;
                    }
                    case "DELETE": {
                        if (Pattern.matches("^/api/v1/epics/\\d+$", path)) {
                            String pathId = path.replaceFirst("/api/v1/epics/", "");
                            int id = parsePathId(pathId);
                            if (id != -1 && id < taskManager.getAllEpics().size() + 1) {
                                taskManager.deleteByEpicId(id);
                                sendText(httpExchange, "Удалили эпик с id - " + id);
                            } else {
                                httpExchange.sendResponseHeaders(405, 0);
                            }
                        }
                        break;
                    }
                    default: {
                        System.out.println("Данный метод нельзя вызвать " + method);
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                httpExchange.close();
            }
        }
    }

    public class SubTaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) {
            try {
                String path = httpExchange.getRequestURI().getPath();
                String method = httpExchange.getRequestMethod();
                switch (method) {
                    case "GET": {
                        if (Pattern.matches("^/api/v1/subtasks$", path)) {
                            String response = gson.toJson(taskManager.getAllSubTasks());
                            sendText(httpExchange, response);
                        }
                        if (Pattern.matches("^api/v1/subtasks\\d+$", path)) {
                            String pathId = path.replaceFirst("api/v1/subtasks/", "");
                            int id = parsePathId(pathId);
                            if (id != -1) {
                                String response = gson.toJson(taskManager.printSubById(id));
                                sendText(httpExchange, response);
                                break;
                            } else {
                                sendNotFound(httpExchange, "Некорректный id - " + pathId);
                            }
                        }
                        break;
                    }
                    case "POST": {
                        if (Pattern.matches("^/api/v1/subtasks$", path)) {
                            String response = readText(httpExchange);
                            SubTask sub = gson.fromJson(response, SubTask.class);
                            boolean subExists = taskManager.getAllSubTasks().stream()
                                    .map(SubTask::getId)
                                    .anyMatch(id -> id == sub.getId());

                            if (subExists) {
                                taskManager.updateTask(sub);
                            } else {
                                taskManager.addTask(sub);
                            }
                            httpExchange.sendResponseHeaders(201, sub.getId());
                        } else {
                            sendHasInteractions(httpExchange, "Задача пересекается с существующими");
                        }
                        break;
                    }
                    case "DELETE": {
                        if (Pattern.matches("^api/v1/subtasks\\d+$", path)) {
                            String pathId = path.replaceFirst("api/v1/subtasks/", "");
                            int id = parsePathId(pathId);
                            if (id != -1 && id < taskManager.getAllSubTasks().size() + 1) {
                                taskManager.deleteBySubId(id);
                                sendText(httpExchange, "Удалили подзадачу - " + id);
                            } else {
                                sendNotFound(httpExchange, "Неверный id - " + pathId);
                            }
                        }
                        break;
                    }
                    default: {
                        System.out.println("Данный метод нельзя вызвать " + method);
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                httpExchange.close();
            }
        }
    }

    public class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) {
            try {
                String path = httpExchange.getRequestURI().getPath();
                if (Pattern.matches("^/api/v1/history$", path)) {
                    String response = gson.toJson(taskManager.getHistory());
                    sendText(httpExchange, response);
                } else {
                    sendNotFound(httpExchange, "Задача не найдена");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                httpExchange.close();
            }
        }
    }

    public class PrioritizedHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) {
            try {
                String path = httpExchange.getRequestURI().getPath();
                if (Pattern.matches("^/api/v1/prioritized$", path)) {
                    String response = gson.toJson(taskManager.getPrioritizedTasks());
                    sendText(httpExchange, response);
                } else {
                    sendNotFound(httpExchange, "Задача не найдена");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                httpExchange.close();
            }
        }
    }

    public void start() {
        System.out.println("Сервер работает");
        server.start();
    }

    public void stop() {
        System.out.println("Сервер закончил работу");
        server.stop(0);
    }

    public int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}

