import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.modelParametrs.StatusTask;
import tracker.controllers.HistoryManager;
import tracker.controllers.InMemoryTaskManager;
import tracker.controllers.Managers;
import tracker.controllers.TaskManager;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    TaskManager taskManager ;
    HistoryManager historyManager;
    Epic epic;
    Epic epic2;
    Task task;
    Task task2;
    Subtask subtask;
    Subtask subtask2;

    @BeforeEach
    void BeforeEach(){
        taskManager = new InMemoryTaskManager();
        historyManager = Managers.getDefaultHistory();
        epic = new Epic("Название задачи 1","Описание задачи 1");
        epic2 = new Epic("Название задачи 2","Описание задачи 2");
        taskManager.createEpic(epic);
        taskManager.createEpic(epic2);
        task = new Task("Название задачи 1","Описание задачи 1", LocalDateTime.of(2022,10,20,12,0),
                90);
        task2 = new Task("Название задачи 2","Описание задачи 2", LocalDateTime.of(2022,10,20,16,0),
                90);
        taskManager.createTask(task);
        taskManager.createTask(task2);
        subtask = new Subtask("Название подзадачи 1","Описание задачи 1", null,
                0,1);
        subtask2 = new Subtask("Название подзадачи 2","Описание задачи 2", LocalDateTime.of(2022,10,20,14,0),
                90,1);
        taskManager.createSubtask(subtask, StatusTask.NEW);
        taskManager.createSubtask(subtask2,StatusTask.NEW);
    }

    @Test
    void shouldBeGetHistory(){
        historyManager.add(epic);
        historyManager.add(task);
        historyManager.add(subtask);

        assertNotNull(taskManager.history());
        assertEquals(3,historyManager.getHistory().size());
    }

    @Test
    void shouldBeNullSizeListInHistory(){
        assertEquals(0,historyManager.getHistory().size());
    }

    @Test
    void shouldBeDoubleTasksInListInHistory(){
        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(epic);
        historyManager.add(epic2);
        historyManager.add(task);
        assertNotNull(historyManager.getHistory());
        assertEquals(4,historyManager.getHistory().size());
        assertEquals(task2,historyManager.getHistory().get(0));
    }

    @Test
    void shouldBeDeleteTasksInBeginningListInHistory(){
        historyManager.add(subtask2);
        historyManager.add(subtask);
        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(epic);
        historyManager.add(epic2);
        historyManager.remove(6);
        assertNotNull(historyManager.getHistory());
        assertEquals(5,historyManager.getHistory().size());
        assertEquals(subtask,historyManager.getHistory().get(0));
    }

    @Test
    void shouldBeDeleteTasksInEndListInHistory(){
        historyManager.add(subtask2);
        historyManager.add(subtask);
        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(epic);
        historyManager.add(epic2);
        historyManager.remove(2);
        assertNotNull(historyManager.getHistory());
        assertEquals(5,historyManager.getHistory().size());
        assertEquals(epic,historyManager.getHistory().get(4));
    }

    @Test
    void shouldBeDeleteTasksInMiddleListInHistory(){
        historyManager.add(subtask2);
        historyManager.add(subtask);
        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(epic);
        historyManager.add(epic2);
        historyManager.remove(3);
        assertNotNull(historyManager.getHistory());
        assertEquals(5,historyManager.getHistory().size());
        assertEquals(task2,historyManager.getHistory().get(2));
    }
}