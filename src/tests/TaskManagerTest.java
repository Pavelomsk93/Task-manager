import org.junit.jupiter.api.Test;
import tracker.modelParametrs.StatusTask;
import tracker.controllers.TaskManager;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    T taskManager;


    @Test
    void shouldBeNotAddAfterCreateNewTaskAndNullValue() {
        int id = taskManager.createTask(null);
        Task savedTask = taskManager.getTask(id);
        List<Task> tasks = taskManager.getAllTask();
        assertEquals(0, tasks.size(), "Неверное количество задач");
        assertNull(savedTask);
    }

    @Test
    void shouldBeAddTaskAfterCreateNewTask() {
        Task task = new Task("Название задачи 1", "Описание задачи 1", LocalDateTime.of(2022, 10, 20, 12, 0),
                Duration.ofMinutes(90));
        int id = taskManager.createTask(task);
        Task savedTask = taskManager.getTask(id);
        List<Task> tasks = taskManager.getAllTask();
        assertEquals(1, tasks.size(), "Неверное количество задач");
        assertNotNull(savedTask);
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldBeAddedToTheTaskListAfterCreatingMultipleTasks() {
        Task task = new Task("Название задачи 1", "Описание задачи 1", LocalDateTime.of(2022, 10, 20, 12, 0),
                Duration.ofMinutes(90));
        Task task2 = new Task("Название задачи 2", "Описание задачи 2", LocalDateTime.of(2022, 10, 20, 14, 0),
                Duration.ofMinutes(90));
        int id = taskManager.createTask(task);
        int id2 = taskManager.createTask(task2);
        Task savedTask = taskManager.getTask(id);
        Task savedTask2 = taskManager.getTask(id2);
        List<Task> tasks = new ArrayList<>();
        tasks.add(savedTask);
        tasks.add(savedTask2);
        assertNotNull(taskManager.getAllTask());
        assertEquals(2, taskManager.getAllTask().size());
        assertEquals(tasks, taskManager.getAllTask());
    }

    @Test
    void shouldBeAddedToTheTaskListAfterNotCreatingTasks() {
        assertNotNull(taskManager.getAllTask());
        assertEquals(0, taskManager.getAllTask().size());
    }

    @Test
    void shouldBeDeleteToTheTaskListAfterCreatingMultipleTasks() {
        Task task = new Task("Название задачи 1", "Описание задачи 1", LocalDateTime.of(2022, 10, 20, 12, 0),
                Duration.ofMinutes(90));
        Task task2 = new Task("Название задачи 2", "Описание задачи 2", LocalDateTime.of(2022, 10, 20, 14, 0),
                Duration.ofMinutes(90));
        taskManager.createTask(task);
        taskManager.createTask(task2);
        taskManager.deleteAllTask();
        assertNotNull(taskManager.getAllTask());
        assertEquals(0, taskManager.getAllTask().size());
    }

    @Test
    void shouldBeDeleteToTheTaskListAfterNotCreatingTasks() {
        taskManager.deleteAllTask();
        assertNotNull(taskManager.getAllTask());
        assertEquals(0, taskManager.getAllTask().size());
    }

    @Test
    void shouldBeTaskReceivedAfterTheIdIsPassed() {
        Task task = new Task("Название задачи 1", "Описание задачи 1", LocalDateTime.of(2022, 10, 20, 12, 0),
                Duration.ofMinutes(90));
        int id = taskManager.createTask(task);
        Task savedTask = taskManager.getTask(id);
        assertEquals(task, savedTask);
        assertNotNull(savedTask);
    }

    @Test
    void shouldBeTaskNoReceivedAfterTheIdIsPassed() {
        Task task = new Task("Название задачи 1", "Описание задачи 1", LocalDateTime.of(2022, 10, 20, 12, 0),
                Duration.ofMinutes(90));
        taskManager.createTask(task);
        Task noTask = taskManager.getTask(2);
        assertNull(noTask);
    }

    @Test
    void shouldBeTaskDeletedAfterTheIdIsPassed() {
        Task task = new Task("Название задачи 1", "Описание задачи 1", LocalDateTime.of(2022, 10, 20, 12, 0),
                Duration.ofMinutes(90));
        Task task2 = new Task("Название задачи 2", "Описание задачи 2", LocalDateTime.of(2022, 10, 20, 14, 0),
                Duration.ofMinutes(90));
        int id = taskManager.createTask(task);
        taskManager.createTask(task2);
        taskManager.deleteTask(id);
        assertNotNull(taskManager.getAllTask());
        assertEquals(1, taskManager.getAllTask().size());
    }

    @Test
    void shouldBeTaskNoDeletedAfterTheIdIsPassed() {
        Task task = new Task("Название задачи 1", "Описание задачи 1", LocalDateTime.of(2022, 10, 20, 12, 0),
                Duration.ofMinutes(90));
        Task task2 = new Task("Название задачи 2", "Описание задачи 2", LocalDateTime.of(2022, 10, 20, 14, 0),
                Duration.ofMinutes(90));
        taskManager.createTask(task);
        taskManager.createTask(task2);
        taskManager.deleteTask(3);
        assertEquals(2, taskManager.getAllTask().size());
        assertNotNull(taskManager.getAllTask());
    }

    @Test
    void shouldBeNotAddAfterCreateNewEpicAndNullValue() {
        int id = taskManager.createEpic(null);
        Epic savedEpic = taskManager.getEpic(id);
        List<Epic> epics = taskManager.getAllEpic();
        assertEquals(0, epics.size(), "Неверное количество задач");
        assertNull(savedEpic);
    }

    @Test
    void shouldBeAddEpicAfterCreateNewEpic() {
        Epic epic = new Epic("Название задачи 1", "Описание задачи 1");
        int id = taskManager.createEpic(epic);
        Epic savedEpic = taskManager.getEpic(id);
        List<Epic> epics = taskManager.getAllEpic();
        assertEquals(1, epics.size(), "Неверное количество задач");
        assertNotNull(savedEpic);
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldBeAddedToTheEpicListAfterCreatingMultipleEpics() {
        Epic epic = new Epic("Название задачи 1", "Описание задачи 1");
        Epic epic2 = new Epic("Название задачи 2", "Описание задачи 2");
        taskManager.createEpic(epic);
        taskManager.createEpic(epic2);
        assertNotNull(taskManager.getAllEpic());
        assertEquals(2, taskManager.getAllEpic().size());
    }

    @Test
    void shouldBeAddedToTheEpicListAfterNotCreatingEpics() {
        assertNotNull(taskManager.getAllEpic());
        assertEquals(0, taskManager.getAllEpic().size());
    }

    @Test
    void shouldBeDeleteToTheEpicListAfterCreatingMultipleEpics() {
        Epic epic = new Epic("Название задачи 1", "Описание задачи 1");
        Epic epic2 = new Epic("Название задачи 2", "Описание задачи 2");
        taskManager.createEpic(epic);
        taskManager.createEpic(epic2);
        taskManager.deleteAllEpic();
        assertNotNull(taskManager.getAllEpic());
        assertEquals(0, taskManager.getAllEpic().size());
    }

    @Test
    void shouldBeDeleteToTheEpicListAfterNotCreatingEpics() {
        taskManager.deleteAllEpic();
        assertNotNull(taskManager.getAllEpic());
        assertEquals(0, taskManager.getAllEpic().size());
    }

    @Test
    void shouldBeEpicReceivedAfterTheIdIsPassed() {
        Epic epic = new Epic("Название задачи 1", "Описание задачи 1");
        int id = taskManager.createEpic(epic);
        Epic savedEpic = taskManager.getEpic(id);
        assertEquals(epic, savedEpic);
        assertNotNull(savedEpic);
    }

    @Test
    void shouldBeEpicNoReceivedAfterTheIdIsPassed() {
        Epic epic = new Epic("Название задачи 1", "Описание задачи 1");
        taskManager.createEpic(epic);
        Epic noEpic = taskManager.getEpic(2);
        assertNull(noEpic);
    }

    @Test
    void shouldBeEpicDeletedAfterTheIdIsPassed() {
        Epic epic = new Epic("Название задачи 1", "Описание задачи 1");
        Epic epic2 = new Epic("Название задачи 2", "Описание задачи 2");
        int id = taskManager.createEpic(epic);
        taskManager.createEpic(epic2);
        taskManager.deleteEpic(id);
        assertNotNull(taskManager.getAllEpic());
        assertEquals(1, taskManager.getAllEpic().size());
    }

    @Test
    void shouldBeEpicNoDeletedAfterTheIdIsPassed() {
        Epic epic = new Epic("Название задачи 1", "Описание задачи 1");
        Epic epic2 = new Epic("Название задачи 2", "Описание задачи 2");
        taskManager.createEpic(epic);
        taskManager.createEpic(epic2);
        taskManager.deleteEpic(3);
        assertEquals(2, taskManager.getAllEpic().size());
        assertNotNull(taskManager.getAllEpic());
    }

    @Test
    void shouldBeNotAddAfterCreateNewSubtaskAndNullValue() {
        int id = taskManager.createSubtask(null, StatusTask.NEW);
        Subtask savedSubtask = taskManager.getSubtask(id);
        List<Subtask> subtasks = taskManager.getAllSubtask();
        assertEquals(0, subtasks.size(), "Неверное количество задач");
        assertNull(savedSubtask);
    }

    @Test
    void shouldBeAddSubtaskAfterCreateNewSubtask() {
        Epic epic = new Epic("Название задачи 1", "Описание задачи 1");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Название подзадачи 1", "Описание задачи 1", LocalDateTime.of(2022, 10, 20, 12, 0),
                Duration.ofMinutes(90), epic);
        int id = taskManager.createSubtask(subtask, StatusTask.NEW);
        Subtask savedSubtask = taskManager.getSubtask(id);
        List<Subtask> subtasks = taskManager.getAllSubtask();
        assertEquals(1, subtasks.size(), "Неверное количество задач");
        assertNotNull(savedSubtask);
        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldBeAddedToTheSubtaskListAfterCreatingMultipleSubtasks() {
        Epic epic = new Epic("Название задачи 1", "Описание задачи 1");
        Subtask subtask = new Subtask("Название подзадачи 1", "Описание задачи 1", LocalDateTime.of(2022, 10, 20, 12, 0),
                Duration.ofMinutes(90), epic);
        Subtask subtask2 = new Subtask("Название подзадачи 2", "Описание задачи 2", LocalDateTime.of(2022, 10, 20, 15, 0),
                Duration.ofMinutes(90), epic);
        int id = taskManager.createSubtask(subtask, StatusTask.NEW);
        int id2 = taskManager.createSubtask(subtask2, StatusTask.NEW);
        Subtask savedSubtask = taskManager.getSubtask(id);
        Subtask savedSubtask2 = taskManager.getSubtask(id2);
        List<Subtask> subtasks = new ArrayList<>();
        subtasks.add(savedSubtask);
        subtasks.add(savedSubtask2);
        assertNotNull(taskManager.getAllSubtask());
        assertEquals(2, taskManager.getAllSubtask().size());
        assertEquals(subtasks, taskManager.getAllSubtask());
    }

    @Test
    void shouldBeAddedToTheSubtaskListAfterNotCreatingSubtasks() {
        assertNotNull(taskManager.getAllSubtask());
        assertEquals(0, taskManager.getAllSubtask().size());
    }

    @Test
    void shouldBeDeleteToTheSubtaskListAfterCreatingMultipleSubtasks() {
        Epic epic = new Epic("Название задачи 1", "Описание задачи 1");
        Subtask subtask = new Subtask("Название подзадачи 1", "Описание задачи 1", LocalDateTime.of(2022, 10, 20, 12, 0),
                Duration.ofMinutes(90), epic);
        Subtask subtask2 = new Subtask("Название подзадачи 2", "Описание задачи 2", LocalDateTime.of(2022, 10, 20, 15, 0),
                Duration.ofMinutes(90), epic);
        taskManager.createSubtask(subtask, StatusTask.NEW);
        taskManager.createSubtask(subtask2, StatusTask.NEW);
        taskManager.deleteAllSubtask();
        assertNotNull(taskManager.getAllSubtask());
        assertEquals(0, taskManager.getAllSubtask().size());
    }

    @Test
    void shouldBeDeleteToTheSubtaskListAfterNotCreatingSubtasks() {
        taskManager.deleteAllSubtask();
        assertNotNull(taskManager.getAllSubtask());
        assertEquals(0, taskManager.getAllSubtask().size());
    }

    @Test
    void shouldBeSubtaskReceivedAfterTheIdIsPassed() {
        Epic epic = new Epic("Название задачи 1", "Описание задачи 1");
        Subtask subtask = new Subtask("Название подзадачи 1", "Описание задачи 1", LocalDateTime.of(2022, 10, 20, 12, 0),
                Duration.ofMinutes(90), epic);
        int id = taskManager.createSubtask(subtask, StatusTask.NEW);
        Subtask savedSubtask = taskManager.getSubtask(id);
        assertEquals(subtask, savedSubtask);
        assertNotNull(savedSubtask);
    }

    @Test
    void shouldBeSubtaskNoReceivedAfterTheIdIsPassed() {
        Epic epic = new Epic("Название задачи 1", "Описание задачи 1");
        Subtask subtask = new Subtask("Название подзадачи 1", "Описание задачи 1", LocalDateTime.of(2022, 10, 20, 12, 0),
                Duration.ofMinutes(90), epic);
        taskManager.createSubtask(subtask, StatusTask.NEW);
        Subtask noSubtask = taskManager.getSubtask(2);
        assertNull(noSubtask);
    }

    @Test
    void shouldBeSubtaskDeletedAfterTheIdIsPassed() {
        Epic epic = new Epic("Название задачи 1", "Описание задачи 1");
        Subtask subtask = new Subtask("Название подзадачи 1", "Описание задачи 1", LocalDateTime.of(2022, 10, 20, 12, 0),
                Duration.ofMinutes(90), epic);
        Subtask subtask2 = new Subtask("Название подзадачи 2", "Описание задачи 2", LocalDateTime.of(2022, 10, 20, 15, 0),
                Duration.ofMinutes(90), epic);
        int id = taskManager.createSubtask(subtask, StatusTask.NEW);
        taskManager.createSubtask(subtask2, StatusTask.NEW);
        taskManager.deleteSubtask(id, epic);
        assertNotNull(taskManager.getAllSubtask());
        assertEquals(1, taskManager.getAllSubtask().size());
    }

    @Test
    void shouldBeSubtaskNoDeletedAfterTheIdIsPassed() {
        Epic epic = new Epic("Название задачи 1", "Описание задачи 1");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Название подзадачи 1", "Описание задачи 1", LocalDateTime.of(2022, 10, 20, 12, 0),
                Duration.ofMinutes(90), epic);
        Subtask subtask2 = new Subtask("Название подзадачи 2", "Описание задачи 2", LocalDateTime.of(2022, 10, 20, 15, 0),
                Duration.ofMinutes(90), epic);
        taskManager.createSubtask(subtask, StatusTask.NEW);
        taskManager.createSubtask(subtask2, StatusTask.NEW);
        taskManager.deleteSubtask(4, epic);
        assertEquals(2, taskManager.getAllSubtask().size());
        assertNotNull(taskManager.getAllSubtask());
    }

    @Test
    void shouldBeAnEmptySetAfterAddEpic() {
        Epic epic = new Epic("Название задачи 1", "Описание задачи 1");
        Epic epic2 = new Epic("Название задачи 2", "Описание задачи 2");
        taskManager.createEpic(epic);
        taskManager.createEpic(epic2);
        assertEquals(0, taskManager.getPrioritizedTasks().size());
    }

    @Test
    void shouldBeAddTasksInSet() {
        Epic epic = new Epic("Название задачи 1", "Описание задачи 1");
        Epic epic2 = new Epic("Название задачи 2", "Описание задачи 2");
        taskManager.createEpic(epic);
        taskManager.createEpic(epic2);
        Task task = new Task("Название задачи 1", "Описание задачи 1", LocalDateTime.of(2022, 10, 20, 12, 0),
                Duration.ofMinutes(90));
        Task task2 = new Task("Название задачи 2", "Описание задачи 2", LocalDateTime.of(2022, 10, 20, 16, 0),
                Duration.ofMinutes(90));
        taskManager.createTask(task);
        taskManager.createTask(task2);
        Subtask subtask = new Subtask("Название подзадачи 1", "Описание задачи 1", null,
                null, epic);
        Subtask subtask2 = new Subtask("Название подзадачи 2", "Описание задачи 2", LocalDateTime.of(2022, 10, 20, 14, 0),
                Duration.ofMinutes(90), epic);
        taskManager.createSubtask(subtask, StatusTask.NEW);
        taskManager.createSubtask(subtask2, StatusTask.NEW);
        assertEquals(4, taskManager.getPrioritizedTasks().size());
    }

    @Test
    void shouldTheTimeOfTheTasksIntersect() {
        Epic epic = new Epic("Название задачи 1", "Описание задачи 1");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Название подзадачи 1", "Описание задачи 1", LocalDateTime.of(2022, 10, 20, 12, 0),
                Duration.ofMinutes(90), epic);
        int id = taskManager.createSubtask(subtask, StatusTask.NEW);
        Subtask subtask2 = new Subtask("Название подзадачи 2", "Описание задачи 2", LocalDateTime.of(2022, 10, 20, 13, 0),
                Duration.ofMinutes(90), epic);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> taskManager.createSubtask(subtask2, StatusTask.NEW));
        assertEquals("Даты пересекаются у задач с номерами: " + id + " и " + subtask2.getId(), ex.getMessage());
    }
}
