import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TypeTask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HttpTaskServerTest {
    private HttpTaskServer httpTaskServer;
    private TaskManager taskManager;
    private static final Gson gson = Managers.getGson();

    final Epic epic1 = new Epic("Epic1_Test", "Epic1_Description");

    final Epic epic2 = new Epic("Epic2_Test", "Epic2_Description");

    private final SubTask subTask1 = new SubTask(TypeTask.SUBTASK, "SubTask_Name1", " SubTask_Descriptio4",
            Duration.ofHours(1).plusMinutes(30),
            LocalDateTime.of(2025, Month.APRIL, 20, 2, 0),
            0);

    private final SubTask subTask2 = new SubTask(TypeTask.SUBTASK, "SubTask_Name2", " SubTask_Description2",
            Duration.ofHours(5).plusMinutes(30),
            LocalDateTime.of(2025, Month.APRIL, 29, 2, 0),
            0);

    private final Task task1 = new Task(TypeTask.TASK, "Task_Name1", " Task_Description1",
            Duration.ofHours(1).plusMinutes(30),
            LocalDateTime.of(2024, Month.JUNE, 11, 5, 0));

    private final Task task2 = new Task(TypeTask.TASK, "Task_Name2", " Task_Description2",
            Duration.ofHours(1).plusMinutes(30),
            LocalDateTime.of(2024, Month.DECEMBER, 11, 5, 0));



    @BeforeEach
    public void setUp() throws IOException {
        taskManager = Managers.getDefault();
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }

    @Test
    void shouldGetTasks() throws IOException, InterruptedException {
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);

        assertEquals(2, actual.size());
    }

    @Test
    void shouldGetSubtasks() throws IOException, InterruptedException {
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<SubTask>>() {}.getType();
        List<SubTask> actual = gson.fromJson(response.body(), taskType);

        assertEquals(2, actual.size());
    }

    @Test
    void shouldGetEpics() throws IOException, InterruptedException {
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Epic>>() {}.getType();
        List<Epic> actual = gson.fromJson(response.body(), taskType);

        assertEquals(1, actual.size());
    }

    @Test
    void shouldGetTaskById() throws IOException, InterruptedException {
        taskManager.addTask(task1);
        int idTask1 = task1.getTaskID();
        taskManager.addTask(task2);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=" + idTask1);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());


        Task task = gson.fromJson(response.body(),Task.class);

        assertEquals(idTask1, task.getTaskID());
        assertEquals(task1.getDescription(), task.getDescription());
    }

    @Test
    void shouldGetEpicById() throws IOException, InterruptedException {
        taskManager.addEpic(epic1);
        int idEpic1 = task1.getTaskID();

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic/?id=" + idEpic1);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());


        Epic epic = gson.fromJson(response.body(),Epic.class);

        assertEquals(idEpic1, epic.getTaskID());
        assertEquals(epic1.getDescription(), epic.getDescription());
    }

    @Test
    void shouldGetSubtaskById() throws IOException, InterruptedException {
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1);
        int idSubtask1 = subTask1.getTaskID();

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/?id=" + idSubtask1);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        SubTask subtask = gson.fromJson(response.body(),SubTask.class);

        assertEquals(idSubtask1, subtask.getTaskID());
        assertEquals(subTask1.getDescription(), subtask.getDescription());
    }

    @Test
    void shouldPostTask() throws IOException, InterruptedException {
        String taskJson = gson.toJson(task1);
        HttpClient httpClient = HttpClient.newHttpClient();

        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = taskManager.getTasks();

        assertNotNull(tasksFromManager);
        assertEquals(1, tasksFromManager.size());
        assertEquals("Task_Name1", tasksFromManager.get(0).getName());
    }

    @Test
    void shouldPostEpic() throws IOException, InterruptedException {
        String taskJson = gson.toJson(epic1);

        HttpClient httpClient = HttpClient.newHttpClient();

        URI uri = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Epic> tasksFromManager = taskManager.getEpics();

        assertNotNull(tasksFromManager);
        assertEquals(1, tasksFromManager.size());
        assertEquals("Epic1_Test", tasksFromManager.get(0).getName());
    }

    @Test
    void shouldPostSubtask() throws IOException, InterruptedException {
        taskManager.addEpic(epic1);

        String taskJson = gson.toJson(subTask1);

        HttpClient httpClient = HttpClient.newHttpClient();

        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<SubTask> tasksFromManager = taskManager.getSubTasks();

        assertNotNull(tasksFromManager);
        assertEquals(1, tasksFromManager.size());
        assertEquals("SubTask_Name1", tasksFromManager.get(0).getName());
    }

    @Test
    void shouldDeleteTasks() throws IOException, InterruptedException {
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);

        assertEquals(0, actual.size());
    }

    @Test
    void shouldDeleteEpics() throws IOException, InterruptedException {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Epic>>() {}.getType();
        List<Epic> actual = gson.fromJson(response.body(), taskType);

        assertEquals(0, actual.size());
    }

    @Test
    void shouldDeleteSubtasks() throws IOException, InterruptedException {
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<SubTask>>() {}.getType();
        List<SubTask> actual = gson.fromJson(response.body(), taskType);

        assertEquals(0, actual.size());
    }

    @Test
    void shouldDeleteTaskById() throws IOException, InterruptedException {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        int idTask1 = task1.getTaskID();

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=" + idTask1);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Task> actual = taskManager.getTasks();

        assertEquals(1, actual.size());
    }

    @Test
    void shouldDeleteEpicById() throws IOException, InterruptedException {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        int idEpick1 = epic1.getTaskID();

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic/?id=" + idEpick1);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Epic> actual = taskManager.getEpics();

        assertEquals(1, actual.size());
    }

    @Test
    void shouldDeleteSubtaskById() throws IOException, InterruptedException {
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        int idSubtask1 = subTask1.getTaskID();

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/?id=" + idSubtask1);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Epic> actual = taskManager.getEpics();

        assertEquals(1, actual.size());
    }

    @Test
    void shouldGetHistory() throws IOException, InterruptedException {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.getTaskbyId(task1.getTaskID());
        taskManager.getTaskbyId(task2.getTaskID());

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);

        assertEquals(2, actual.size());
    }

    @Test
    void shouldGetPrioritized() throws IOException, InterruptedException {
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);

        assertEquals(2, actual.size());
    }

    @Test
    void shouldPostUpdateTask() throws IOException, InterruptedException {
        taskManager.addTask(task1);
        int taskId = task1.getTaskID();
        String taskJson = gson.toJson(task2);
        HttpClient httpClient = HttpClient.newHttpClient();

        URI uri = URI.create("http://localhost:8080/tasks/task/update/?id=" + taskId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = taskManager.getTasks();

        assertNotNull(tasksFromManager);
        assertEquals(1, tasksFromManager.size());
        assertEquals("Task_Name2", tasksFromManager.get(0).getName());
    }

    @Test
    void shouldPostUpdateEpic() throws IOException, InterruptedException {
        taskManager.addEpic(epic1);
        int taskId = epic1.getTaskID();
        String taskJson = gson.toJson(epic2);
        HttpClient httpClient = HttpClient.newHttpClient();

        URI uri = URI.create("http://localhost:8080/tasks/epic/update/?id=" + taskId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Epic> tasksFromManager = taskManager.getEpics();

        assertNotNull(tasksFromManager);
        assertEquals(1, tasksFromManager.size());
        assertEquals("Epic2_Test", tasksFromManager.get(0).getName());
    }

    @Test
    void shouldPostUpdateSubtask() throws IOException, InterruptedException {
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1);
        int taskId = subTask1.getTaskID();
        String taskJson = gson.toJson(subTask2);
        HttpClient httpClient = HttpClient.newHttpClient();

        URI uri = URI.create("http://localhost:8080/tasks/subtask/update/?id=" + taskId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<SubTask> tasksFromManager = taskManager.getSubTasks();

        assertNotNull(tasksFromManager);
        assertEquals(1, tasksFromManager.size());
        assertEquals("SubTask_Name2", tasksFromManager.get(0).getName());
    }
    
    @AfterEach
    public void afterEach() {
        httpTaskServer.stop();
    }
}