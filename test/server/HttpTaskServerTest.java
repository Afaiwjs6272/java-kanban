package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Status;
import model.Task;
import model.adapter.DurationTypeAdapter;
import model.adapter.LocalDateTimeAdapter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.TaskManager;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    private HttpTaskServer server;
    private TaskManager taskManager;
    private Gson gson;

    private Task task;

    HttpTaskServerTest() throws IOException {
        this.server = new HttpTaskServer();
        this.taskManager = new InMemoryTaskManager();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting()
                .create();
    }

    @BeforeEach
    public void setUp() throws Exception {
        taskManager.deleteTasks();
        taskManager.deleteSubTasks();
        taskManager.deleteEpics();
        task = new Task("ass", "assa", Status.NEW, Duration.ZERO, LocalDateTime.now());
        taskManager.addTask(task);
        server.start();
    }

    @AfterEach
    public void shutDown() {
        server.stop();
    }

    @Test
    public void deleteTasks() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/v1/tasks");
        HttpRequest getTasksRequest = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> getTasksResponse = client.send(getTasksRequest, HttpResponse.BodyHandlers.ofString());

        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(getTasksResponse.body(), taskType);

        assertFalse(actual.contains(task));
    }


    @Test
    public void testAddTask() throws IOException, InterruptedException {
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/api/v1/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = taskManager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("ass", tasksFromManager.get(0).getTaskName(), "Некорректное имя задачи");
    }

    @Test
    public void testAddTaskAndRetrieve() throws Exception {
        Task newTask = new Task("Test Task", "Description", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        String taskJson = gson.toJson(newTask);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/v1/tasks");
        HttpRequest addTaskRequest = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> addTaskResponse = client.send(addTaskRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, addTaskResponse.statusCode(), "Не удалось добавить задачу");

        HttpRequest getTasksRequest = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> getTasksResponse = client.send(getTasksRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, getTasksResponse.statusCode(), "Не удалось получить список задач");

        List<Task> tasks = gson.fromJson(getTasksResponse.body(), new TypeToken<List<Task>>() {
        }.getType());
        assertFalse(tasks.isEmpty(), "Список задач должен содержать добавленную задачу");
        Task retrievedTask = tasks.stream().filter(t -> t.getTaskName().equals(newTask.getTaskName())).findFirst().orElse(null);
        assertNotNull(retrievedTask, "Добавленная задача не найдена в списке");
        assertEquals(newTask.getDescription(), retrievedTask.getDescription(), "Описание задачи не совпадает");
    }
}