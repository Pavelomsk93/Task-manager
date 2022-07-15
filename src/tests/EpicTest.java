import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.StatusTask;
import tracker.controllers.Managers;
import tracker.controllers.TaskManager;
import tracker.model.Epic;
import tracker.model.Subtask;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EpicTest {

    TaskManager taskManager;

    @BeforeEach
    void BeforeEach(){
        taskManager = Managers.getFileBacked();
    }

    @Test
    void shouldBeDefaultEpicStatusWithEmptySubtaskList(){
        Epic epic = new Epic("Название задачи 1","Описание задачи 1");
        taskManager.createEpic(epic);
        assertNotNull(epic.getStatus());
        assertEquals(StatusTask.NEW,epic.getStatus());
    }

    @Test
    void shouldBeNEWEpicStatusWithAllSubtasksStatusNEW(){
        Epic epic = new Epic("Название задачи 1","Описание задачи 1");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Название подзадачи 1","Описание задачи 1", LocalDateTime.of(2022,10,20,12,0),
                Duration.ofMinutes(90),epic);
        Subtask subtask2 = new Subtask("Название подзадачи 2","Описание задачи 2", LocalDateTime.of(2022,10,20,15,0),
                Duration.ofMinutes(90),epic);
        taskManager.createSubtask(subtask,StatusTask.NEW);
        taskManager.createSubtask(subtask2,StatusTask.NEW);
        assertNotNull(epic.getStatus());
        assertEquals(StatusTask.NEW,epic.getStatus());
    }

    @Test
    void shouldBeDONEEpicStatusWithAllSubtasksStatusDONE(){
        Epic epic = new Epic("Название задачи 1","Описание задачи 1");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Название подзадачи 1","Описание задачи 1", LocalDateTime.of(2022,10,20,12,0),
                Duration.ofMinutes(90),epic);
        Subtask subtask2 = new Subtask("Название подзадачи 2","Описание задачи 2", LocalDateTime.of(2022,10,20,15,0),
                Duration.ofMinutes(90),epic);
        taskManager.createSubtask(subtask,StatusTask.DONE);
        taskManager.createSubtask(subtask2,StatusTask.DONE);
        assertNotNull(epic.getStatus());
        assertEquals(StatusTask.DONE,epic.getStatus());
    }

    @Test
    void shouldBeINPROGRESSEpicStatusWithSubtasksStatusDONEAndNEW(){
        Epic epic = new Epic("Название задачи 1","Описание задачи 1");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Название подзадачи 1","Описание задачи 1", LocalDateTime.of(2022,10,20,12,0),
                Duration.ofMinutes(90),epic);
        Subtask subtask2 = new Subtask("Название подзадачи 2","Описание задачи 2", LocalDateTime.of(2022,10,20,15,0),
                Duration.ofMinutes(90),epic);
        taskManager.createSubtask(subtask,StatusTask.DONE);
        taskManager.createSubtask(subtask2,StatusTask.NEW);
        assertNotNull(epic.getStatus());
        assertEquals(StatusTask.IN_PROGRESS,epic.getStatus());
    }

    @Test
    void shouldBeINPROGRESSEpicStatusWithAllSubtasksStatusINPROGRESS(){
        Epic epic = new Epic("Название задачи 1","Описание задачи 1");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Название подзадачи 1","Описание задачи 1", LocalDateTime.of(2022,10,20,12,0),
                Duration.ofMinutes(90),epic);
        Subtask subtask2 = new Subtask("Название подзадачи 2","Описание задачи 2", LocalDateTime.of(2022,10,20,15,0),
                Duration.ofMinutes(90),epic);
        taskManager.createSubtask(subtask,StatusTask.IN_PROGRESS);
        taskManager.createSubtask(subtask2,StatusTask.IN_PROGRESS);
        assertNotNull(epic.getStatus());
        assertEquals(StatusTask.IN_PROGRESS,epic.getStatus());
    }



}