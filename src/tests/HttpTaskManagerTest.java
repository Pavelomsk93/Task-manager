import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.adapters.LocalDateTimeAdapter;
import tracker.client.KVTaskClient;
import tracker.controllers.FileBackedTasksManager;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.servers.HttpTaskServer;
import tracker.servers.KVServer;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTest {

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    private HttpTaskServer server;
    private KVServer kvServer;
    private HttpClient client;


    @BeforeEach
    public void startServer() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        server = new HttpTaskServer();
        server.start();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    public void stopServer() {
        server.stop();
        kvServer.stop();
    }

    @Test
    public void shouldCreatedNewEpic() throws InterruptedException, IOException {
        Epic epicOne = new Epic("Epic 1", "Описание Epic 1");
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String json = gson.toJson(epicOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains(epicOne.getTitle()));
    }

    @Test
    public void shouldGetEpic() throws IOException, InterruptedException {
        Epic epicOne = new Epic("Epic 1", "Описание Epic 1");
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String json = gson.toJson(epicOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlGetEpic = URI.create("http://localhost:8080/tasks/epic/?id=" + 1);
        HttpRequest requestGetEpic = HttpRequest.newBuilder().uri(urlGetEpic).GET().build();
        HttpResponse<String> responseGetTask = client.send(requestGetEpic, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseGetTask.statusCode());
        System.out.println(responseGetTask.body());
        assertTrue(responseGetTask.body().contains(epicOne.getTitle()));
    }

    @Test
    public void shouldGetWrongBodyEpic() throws IOException, InterruptedException {
        Epic epicOne = new Epic("Epic 1", "Описание Epic 1");
        Epic epicTwo = new Epic("Epic 2", "Описание Epic 2");
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String json = gson.toJson(epicOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlGetEpic = URI.create("http://localhost:8080/tasks/epic/?id=" + epicOne.getId());
        HttpRequest requestGetEpic = HttpRequest.newBuilder().uri(urlGetEpic).GET().build();
        HttpResponse<String> responseGetTask = client.send(requestGetEpic, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseGetTask.statusCode());
        assertFalse(responseGetTask.body().contains(epicTwo.getTitle()));
    }

    @Test
    public void shouldGetAllEpics() throws IOException, InterruptedException {
        Epic epicOne = new Epic("Epic 1", "Описание Epic 1");
        Epic epicTwo = new Epic("Epic 2", "Описание Epic 2");
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String json = gson.toJson(epicOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        String json2 = gson.toJson(epicTwo);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestGetEpic = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseGetEpic = client.send(requestGetEpic, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseGetEpic.statusCode());
        assertTrue(responseGetEpic.body().contains(epicOne.getTitle()));
        assertTrue(responseGetEpic.body().contains(epicTwo.getTitle()));
    }

    @Test
    public void shouldRetireEpicById() throws IOException, InterruptedException {
        Epic epicOne = new Epic("Epic 1", "Описание Epic 1");
        Epic epicTwo = new Epic("Epic 2", "Описание Epic 2");
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String json = gson.toJson(epicOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        String json2 = gson.toJson(epicTwo);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        URI urlDelete = URI.create("http://localhost:8080/tasks/epic/?id=" + 1);

        HttpRequest requestDeleteEpic = HttpRequest.newBuilder().uri(urlDelete).DELETE().build();
        HttpResponse<String> responseDelete = client.send(requestDeleteEpic, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseDelete.statusCode());

        HttpRequest requestGetEpic2 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseGetEpic2 = client.send(requestGetEpic2, HttpResponse.BodyHandlers.ofString());
        assertTrue(responseGetEpic2.body().contains(epicTwo.getTitle()));
    }

    @Test
    public void shouldRetireAllEpics() throws IOException, InterruptedException {
        Epic epicOne = new Epic("Epic 1", "Описание Epic 1");
        Epic epicTwo = new Epic("Epic 2", "Описание Epic 2");
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String json = gson.toJson(epicOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        String json2 = gson.toJson(epicTwo);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestGetEpic = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseGetEpic = client.send(requestGetEpic, HttpResponse.BodyHandlers.ofString());
        assertTrue(responseGetEpic.body().contains(epicOne.getTitle()));
        assertTrue(responseGetEpic.body().contains(epicTwo.getTitle()));

        HttpRequest requestDeleteTask = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> responseDelete = client.send(requestDeleteTask, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseDelete.statusCode());

        HttpRequest requestGetEpic2 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseGetEpic2 = client.send(requestGetEpic2, HttpResponse.BodyHandlers.ofString());

        assertFalse(responseGetEpic2.body().contains(epicOne.getTitle()));
        assertFalse(responseGetEpic2.body().contains(epicTwo.getTitle()));
    }

    @Test
    public void shouldCreatedNewSubtask() throws IOException, InterruptedException {
        Epic epicOne = new Epic("Epic 1", "Описание Epic 1");
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic");
        String jsonEpic = gson.toJson(epicOne);
        HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic).build();
        client.send(requestEpic, HttpResponse.BodyHandlers.ofString());
        Subtask subtaskOne = new Subtask("Subtask 1", "Описание Subtask 1", LocalDateTime.of(2022, Month.MAY, 2, 15,
                0), 90, 1);
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String json = gson.toJson(subtaskOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains(subtaskOne.getTitle()));
    }

    @Test
    public void shouldGetSubTask() throws IOException, InterruptedException {
        Epic epicOne = new Epic("Epic 1", "Описание Epic 1");
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic");
        String jsonEpic = gson.toJson(epicOne);
        final HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic).build();
        client.send(requestEpic, HttpResponse.BodyHandlers.ofString());
        Subtask subtaskOne = new Subtask("Subtask 1", "Описание Subtask 1", LocalDateTime.of(2022, Month.MAY, 2, 15,
                0), 90, 1);
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String jsonSubTask = gson.toJson(subtaskOne);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonSubTask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlGetSubTask = URI.create("http://localhost:8080/tasks/subtask/?id=" + 2);
        HttpRequest requestGetSubTask = HttpRequest.newBuilder().uri(urlGetSubTask).GET().build();
        HttpResponse<String> responseGetTask = client.send(requestGetSubTask, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseGetTask.statusCode());
        assertTrue(responseGetTask.body().contains(subtaskOne.getTitle()));
    }

    @Test
    public void shouldGetWrongBodySubtask() throws IOException, InterruptedException {
        Epic epicOne = new Epic("Epic 1", "Описание Epic 1");
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic");
        String jsonEpic = gson.toJson(epicOne);
        HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic).build();
        client.send(requestEpic, HttpResponse.BodyHandlers.ofString());
        Subtask subtaskOne = new Subtask("Subtask 1", "Описание Subtask 1", LocalDateTime.of(2022, Month.MAY, 2, 15,
                0), 90, 1);
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String jsonSubtask = gson.toJson(subtaskOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtaskTwo = new Subtask("Subtask 2", "Описание Subtask 2",
                LocalDateTime.of(2022, 5, 3, 15, 0), 90, 1);
        URI url2 = URI.create("http://localhost:8080/tasks/subtask");
        String jsonSubtask2 = gson.toJson(subtaskTwo);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(jsonSubtask2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());
        URI urlGetSubtask = URI.create("http://localhost:8080/tasks/subtask/?id=" + 2);
        HttpRequest requestGetSubtask = HttpRequest.newBuilder().uri(urlGetSubtask).GET().build();
        HttpResponse<String> responseGetTask = client.send(requestGetSubtask, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseGetTask.statusCode());
        assertFalse(responseGetTask.body().contains(subtaskTwo.getTitle()));
    }

    @Test
    public void shouldGetAllSubtasks() throws IOException, InterruptedException {
        Epic epicOne = new Epic("Epic 1", "Описание Epic 1");
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic");
        String jsonEpic = gson.toJson(epicOne);
        HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic).build();
        client.send(requestEpic, HttpResponse.BodyHandlers.ofString());
        Subtask subtaskOne = new Subtask("Subtask 1", "Описание Subtask 1", LocalDateTime.of(2022, Month.MAY, 2, 15,
                0), 90, 1);
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String jsonSubtask = gson.toJson(subtaskOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtaskTwo = new Subtask("Subtask 2", "Описание Subtask 2",
                LocalDateTime.of(2022, 5, 3, 15, 0), 90, 1);
        String jsonSubtask2 = gson.toJson(subtaskTwo);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(jsonSubtask2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestGetSubtask = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseGetSubtasks = client.send(requestGetSubtask, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseGetSubtasks.statusCode());

        assertTrue(responseGetSubtasks.body().contains(subtaskOne.getTitle()));
        assertTrue(responseGetSubtasks.body().contains(subtaskTwo.getTitle()));
    }

    @Test
    public void shouldRetireSubtaskById() throws IOException, InterruptedException {
        Epic epicOne = new Epic("Epic 1", "Описание Epic 1");
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic");
        String jsonEpic = gson.toJson(epicOne);
        HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic).build();
        client.send(requestEpic, HttpResponse.BodyHandlers.ofString());
        Subtask subtaskOne = new Subtask("Subtask 1", "Описание Subtask 1", LocalDateTime.of(2022, Month.MAY, 2, 15,
                0), 90, 1);
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String jsonSubtask = gson.toJson(subtaskOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtaskTwo = new Subtask("Subtask 2", "Описание Subtask 2",
                LocalDateTime.of(2022, 5, 3, 15, 0), 90, 1);
        String jsonSubtask2 = gson.toJson(subtaskTwo);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(jsonSubtask2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        URI urlDelete = URI.create("http://localhost:8080/tasks/subtask/?id=" + subtaskOne.getId());
        HttpRequest requestDelete = HttpRequest.newBuilder().uri(urlDelete).DELETE().build();
        HttpResponse<String> responseDelete = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseDelete.statusCode());

        HttpRequest requestSubtask2 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseGetSubtasks2 = client.send(requestSubtask2, HttpResponse.BodyHandlers.ofString());
        assertTrue(responseGetSubtasks2.body().contains(subtaskTwo.getTitle()));
    }

    @Test
    public void shouldRetireAllSubtasks() throws IOException, InterruptedException {
        Epic epicOne = new Epic("Epic 1", "Описание Epic 1");
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic");
        String jsonEpic = gson.toJson(epicOne);
        HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic).build();
        client.send(requestEpic, HttpResponse.BodyHandlers.ofString());
        Subtask subtaskOne = new Subtask("Subtask 1", "Описание Subtask 1", LocalDateTime.of(2022, Month.MAY, 2, 15,
                0), 90, 1);
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String jsonSubtask = gson.toJson(subtaskOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtaskTwo = new Subtask("Subtask 2", "Описание Subtask 2",
                LocalDateTime.of(2022, 5, 3, 15, 0), 90, 1);
        String jsonSubtask2 = gson.toJson(subtaskTwo);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(jsonSubtask2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestDelete = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> responseDelete = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseDelete.statusCode());

        HttpRequest requestSubtask2 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseGetSubtasks2 = client.send(requestSubtask2, HttpResponse.BodyHandlers.ofString());

        assertFalse(responseGetSubtasks2.body().contains(subtaskOne.getTitle()));
        assertFalse(responseGetSubtasks2.body().contains(subtaskTwo.getTitle()));
    }

    @Test
    public void shouldGetEpicSubtasks() throws IOException, InterruptedException {
        Epic epicOne = new Epic("Epic 1", "Описание Epic 1");
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic");
        String jsonEpic = gson.toJson(epicOne);
        final HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic).build();
        client.send(requestEpic, HttpResponse.BodyHandlers.ofString());
        Subtask subtaskOne = new Subtask("Subtask 1", "Описание Subtask 1", LocalDateTime.of(2022, Month.MAY, 2, 15,
                0), 90, 1);
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String jsonSubtask = gson.toJson(subtaskOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtaskTwo = new Subtask("Subtask 2", "Описание Subtask 2",
                LocalDateTime.of(2022, 5, 3, 15, 0), 90, 1);
        String jsonSubtask2 = gson.toJson(subtaskTwo);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(jsonSubtask2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        URI urlGetEpic = URI.create("http://localhost:8080/tasks/epic/?id=" + 1);
        HttpRequest requestGetEpic = HttpRequest.newBuilder().uri(urlGetEpic).GET().build();
        HttpResponse<String> responseGetEpic = client.send(requestGetEpic, HttpResponse.BodyHandlers.ofString());

        URI urlGetSubtasks = URI.create("http://localhost:8080/tasks/subtask/epic/?id=" + 1);
        HttpRequest requestGetSubtask = HttpRequest.newBuilder().uri(urlGetSubtasks).GET().build();
        HttpResponse<String> responseGetSubtasks = client.send(requestGetSubtask, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseGetSubtasks.statusCode());
        Epic epicJson = gson.fromJson(responseGetEpic.body(), Epic.class);
        assertTrue(responseGetSubtasks.body().contains(epicJson.getSubTaskList().get(0).getTitle()));
        assertTrue(responseGetSubtasks.body().contains(epicJson.getSubTaskList().get(1).getTitle()));
    }

    @Test
    public void shouldCreatedNewTask() throws IOException, InterruptedException {
        Task taskOne = new Task("Task 1", "Описание Task 1",
                LocalDateTime.of(2022, 5, 27, 11, 0), 90);
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(taskOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains(taskOne.getTitle()));
    }

    @Test
    public void shouldGetTask() throws IOException, InterruptedException {
        Task taskOne = new Task("Task 1", "Описание Task 1",
                LocalDateTime.of(2022, 5, 27, 11, 0), 90);
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(taskOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlGetTask = URI.create("http://localhost:8080/tasks/task/?id=" + 1);
        HttpRequest requestGetTask = HttpRequest.newBuilder().uri(urlGetTask).GET().build();
        HttpResponse<String> responseGetTask = client.send(requestGetTask, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseGetTask.statusCode());
        assertTrue(responseGetTask.body().contains(taskOne.getTitle()));
    }

    @Test
    public void shouldGetWrongBodyTask() throws IOException, InterruptedException {
        Task taskOne = new Task("Task 1", "Описание Task 1",
                LocalDateTime.of(2022, 5, 27, 11, 0), 90);
        Task taskTwo = new Task("Task 2", "Описание Task 2",
                LocalDateTime.of(2022, 5, 6, 12, 0), 90);
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(taskTwo);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlGetTask = URI.create("http://localhost:8080/tasks/task/?id=" + 1);
        HttpRequest requestGetTask = HttpRequest.newBuilder().uri(urlGetTask).GET().build();
        HttpResponse<String> responseGetTask = client.send(requestGetTask, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseGetTask.statusCode());
        assertFalse(responseGetTask.body().contains(taskOne.getTitle()));
    }

    @Test
    public void shouldGetAllTasks() throws IOException, InterruptedException {
        Task taskOne = new Task("Task 1", "Описание Task 1",
                LocalDateTime.of(2022, 5, 27, 11, 0), 90);
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(taskOne); // 'task1' here
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        Task taskTwo = new Task("Task 2", "Описание Task 2",
                LocalDateTime.of(2022, 5, 6, 12, 0), 90);
        String json2 = gson.toJson(taskTwo); // 'task2' here
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestGetTask = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseGetTasks = client.send(requestGetTask, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseGetTasks.statusCode());
        assertTrue(responseGetTasks.body().contains(taskOne.getTitle()));
        assertTrue(responseGetTasks.body().contains(taskTwo.getTitle()));
    }

    @Test
    public void shouldRetireTaskById() throws IOException, InterruptedException {
        Task taskOne = new Task("Task 1", "Описание Task 1",
                LocalDateTime.of(2022, 5, 27, 11, 0), 90);
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(taskOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        Task taskTwo = new Task("Task 2", "Описание Task 2",
                LocalDateTime.of(2022, 5, 6, 12, 0), 90);
        String json2 = gson.toJson(taskTwo);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        URI urlDelete = URI.create("http://localhost:8080/tasks/task/?id=" + 1);
        HttpRequest requestDeleteTask = HttpRequest.newBuilder().uri(urlDelete).DELETE().build();
        HttpResponse<String> responseDelete = client.send(requestDeleteTask, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseDelete.statusCode());

        HttpRequest requestGetTask2 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseGetTasks2 = client.send(requestGetTask2, HttpResponse.BodyHandlers.ofString());
        assertFalse(responseGetTasks2.body().contains(taskOne.getTitle()));
        assertTrue(responseGetTasks2.body().contains(taskTwo.getTitle()));
    }

    @Test
    public void shouldRetireAllTasks() throws IOException, InterruptedException {
        Task taskOne = new Task("Task 1", "Описание Task 1",
                LocalDateTime.of(2022, 5, 27, 11, 0), 90);
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(taskOne); //
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        Task taskTwo = new Task("Task 2", "Описание Task 2",
                LocalDateTime.of(2022, 5, 6, 12, 0), 90);
        String json2 = gson.toJson(taskTwo);
        final HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestDeleteTask = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> responseDelete = client.send(requestDeleteTask, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseDelete.statusCode());

        URI urlGetTasks2 = URI.create("http://localhost:8080/tasks/task");
        HttpRequest requestGetTask2 = HttpRequest.newBuilder().uri(urlGetTasks2).GET().build();
        HttpResponse<String> responseGetTasks2 = client.send(requestGetTask2, HttpResponse.BodyHandlers.ofString());
        assertFalse(responseGetTasks2.body().contains(taskOne.getTitle()));
        assertFalse(responseGetTasks2.body().contains(taskTwo.getTitle()));
    }

    @Test
    public void shouldGetPrioritizedTasks() throws IOException, InterruptedException {

        Epic epicOne = new Epic("Epic 1", "Описание Epic 1");
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic"); // Прописываем http запрос
        String jsonEpic = gson.toJson(epicOne); // Парсим объект в json
        HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic).build();
        client.send(requestEpic, HttpResponse.BodyHandlers.ofString());

        Task taskOne = new Task("Task 1", "Описание Task 1",
                LocalDateTime.of(2022, 5, 27, 11, 0), 90);
        URI urlTask = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(taskOne);
        HttpRequest.BodyPublisher body3 = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request3 = HttpRequest.newBuilder().uri(urlTask).POST(body3).build();
        client.send(request3, HttpResponse.BodyHandlers.ofString());

        URI UrlPrioritized = URI.create("http://localhost:8080/tasks");
        HttpRequest requestPrioritized = HttpRequest.newBuilder().uri(UrlPrioritized).GET().build();
        HttpResponse<String> response = client.send(requestPrioritized, HttpResponse.BodyHandlers.ofString());
        server.stop();
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains(taskOne.getTitle()));
        assertFalse(response.body().contains(epicOne.getTitle()));
    }

    @Test
    public void shouldGetHistory() throws IOException, InterruptedException {

        Epic epicOne = new Epic("Epic 1", "Описание Epic 1");
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic"); // Прописываем http запрос
        String jsonEpic = gson.toJson(epicOne); // Парсим объект в json
        final HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic).build();
        client.send(requestEpic, HttpResponse.BodyHandlers.ofString());

        Subtask subtaskOne = new Subtask("Subtask 1", "Описание Subtask 1", LocalDateTime.of(2022, Month.MAY, 2, 15,
                0), 90, 1);
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String jsonSubTask = gson.toJson(subtaskOne);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonSubTask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        Task taskOne = new Task("Task 1", "Описание Task 1",
                LocalDateTime.of(2022, 5, 27, 11, 0), 90);
        URI url3 = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(taskOne);
        final HttpRequest.BodyPublisher body3 = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request3 = HttpRequest.newBuilder().uri(url3).POST(body3).build();
        client.send(request3, HttpResponse.BodyHandlers.ofString());

        URI urlGetTask = URI.create("http://localhost:8080/tasks/task/?id=" + 3);
        HttpRequest requestGetTask = HttpRequest.newBuilder().uri(urlGetTask).GET().build();
        client.send(requestGetTask, HttpResponse.BodyHandlers.ofString());

        URI urlGetSubTask = URI.create("http://localhost:8080/tasks/subtask/?id=" + 2);
        HttpRequest requestGetSubTask = HttpRequest.newBuilder().uri(urlGetSubTask).GET().build();
        client.send(requestGetSubTask, HttpResponse.BodyHandlers.ofString());

        URI urlGetEpic = URI.create("http://localhost:8080/tasks/epic/?id=" + 1);
        HttpRequest requestGetEpic = HttpRequest.newBuilder().uri(urlGetEpic).GET().build();
        client.send(requestGetEpic, HttpResponse.BodyHandlers.ofString());

        URI urlHistory = URI.create("http://localhost:8080/tasks/history");
        HttpRequest requestHistory = HttpRequest.newBuilder().uri(urlHistory).GET().build();
        HttpResponse<String> response = client.send(requestHistory, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains(epicOne.getTitle()));
        assertTrue(response.body().contains(subtaskOne.getTitle()));
    }

    @Test
    public void shouldWrongMethodEpic() throws InterruptedException, IOException {
        Epic epicOne = new Epic("Epic 1", "Описание Epic 1");
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String json = gson.toJson(epicOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void shouldWrongMethodTask() throws InterruptedException, IOException {
        Task taskOne = new Task("Task 1", "Описание Task 1",
                LocalDateTime.of(2022, 5, 27, 11, 0), 90);
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(taskOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    public void shouldWrongMethodSubtask() throws InterruptedException, IOException {
        Subtask subtaskOne = new Subtask("Subtask 1", "Описание Subtask 1", LocalDateTime.of(2022, Month.MAY, 2, 15,
                0), 90, 1);
        URI urlSubtask = URI.create("http://localhost:8080/tasks/subtask");
        String jsonSubtask = gson.toJson(subtaskOne);
        HttpRequest.BodyPublisher bodySubtask = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest requestSubtask = HttpRequest.newBuilder().uri(urlSubtask).PUT(bodySubtask).build();
        HttpResponse<String> responseSubtask = client.send(requestSubtask, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, responseSubtask.statusCode());
    }

    @Test
    public void shouldWrongMethodSubtaskByEpic() throws InterruptedException, IOException {
        Epic epicOne = new Epic("Epic 1", "Описание Epic 1");
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic"); // Прописываем http запрос
        String jsonEpic = gson.toJson(epicOne); // Парсим объект в json
        final HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic).build();
        client.send(requestEpic, HttpResponse.BodyHandlers.ofString());
        Subtask subtaskOne = new Subtask("Subtask 1", "Описание Subtask 1", LocalDateTime.of(2022, Month.MAY, 2, 15,
                0), 90, 1);
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String jsonSubtask = gson.toJson(subtaskOne);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlHistory = URI.create("http://localhost:8080/tasks/history");
        HttpRequest requestHistory = HttpRequest.newBuilder().uri(urlHistory).DELETE().build();
        HttpResponse<String> response = client.send(requestHistory, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    public void shouldWrongMethodTasks() throws InterruptedException, IOException {
        Epic epicOne = new Epic("Epic 1", "Описание Epic 1");
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic"); // Прописываем http запрос
        String jsonEpic = gson.toJson(epicOne); // Парсим объект в json
        HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(urlEpic).POST(bodyEpic).build();
        client.send(requestEpic, HttpResponse.BodyHandlers.ofString());

        Subtask subtaskOne = new Subtask("Subtask 1", "Описание Subtask 1", LocalDateTime.of(2022, Month.MAY, 2, 15,
                0), 90, 1);
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String jsonSubtask = gson.toJson(subtaskOne);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        Subtask subtaskTwo = new Subtask("Subtask 2", "Описание Subtask 2",
                LocalDateTime.of(2022, 5, 3, 15, 0), 90, 1);
        String jsonSubtask2 = gson.toJson(subtaskTwo);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(jsonSubtask2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        Task taskOne = new Task("Task 1", "Описание Task 1",
                LocalDateTime.of(2022, 5, 27, 11, 0), 90);
        URI urlTask = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(taskOne);
        HttpRequest.BodyPublisher body3 = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request3 = HttpRequest.newBuilder().uri(urlTask).POST(body3).build();
        client.send(request3, HttpResponse.BodyHandlers.ofString());

        URI UrlPrioritized = URI.create("http://localhost:8080/tasks");
        HttpRequest requestPrioritized = HttpRequest.newBuilder().uri(UrlPrioritized).DELETE().build();
        HttpResponse<String> response = client.send(requestPrioritized, HttpResponse.BodyHandlers.ofString());
        server.stop();
        assertEquals(404, response.statusCode());
    }
}
