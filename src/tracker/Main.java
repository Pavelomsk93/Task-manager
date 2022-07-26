package tracker;

import tracker.controllers.*;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.modelParametrs.StatusTask;
import tracker.servers.HttpTaskServer;
import tracker.servers.KVServer;

import java.io.IOException;
import java.time.LocalDateTime;



public class Main {

    public static void main(String[] args) throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        HttpTaskServer taskServer = new HttpTaskServer();
        taskServer.start();

        TaskManager taskManager = Managers.getDefault();
        Task taskOne = new Task("task 1", "Описание task 1",
                 LocalDateTime.of(2022, 5, 1,   12, 0),90);

        Task taskTwo = new Task("task 2", "Описание task 2",
                LocalDateTime.of(2022, 5, 1,   14, 0),90);

        Epic epicOne  = new Epic("Epic 1", "Описание Epic 1");
        Epic epicTwo  = new Epic("Epic 2", "Описание Epic 2");

        Subtask subtaskOne = new Subtask("Subtask 1", "Описание Subtask 1",
                  LocalDateTime.of(2022, 5, 1, 10, 0),90, 3);
        Subtask subtaskTwo = new Subtask("Subtask 2", "Описание Subtask 2",
                 LocalDateTime.of(2022, 5, 1, 8, 0),90, 3);

        taskManager.createTask(taskOne);
        taskManager.createTask(taskTwo);
        taskManager.createEpic(epicOne);
        taskManager.createEpic(epicTwo);
        taskManager.createSubtask(subtaskOne,StatusTask.NEW);
        taskManager.createSubtask(subtaskTwo,StatusTask.NEW);
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getEpic(3);
        taskManager.getEpic(4);
        taskManager.getSubtask(5);
        taskManager.getSubtask(6);
        System.out.println("История");
        System.out.println(taskManager.history());
        System.out.println("\nПриоритетные задачи");
        System.out.println(taskManager.getPrioritizedTasks());
        System.out.println("\nВсе задачи");
        System.out.println(taskManager.getAllTask());
        System.out.println("\nВсе эпики'");
        System.out.println(taskManager.getAllEpic());
        System.out.println("\nВсе сабтаски");
        System.out.println(taskManager.getAllSubtask());
        taskServer.stop();
        kvServer.stop();
    }
}