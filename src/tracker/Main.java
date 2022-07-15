package tracker;


import tracker.controllers.*;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;



public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        Task task = new Task("Уехать","Приехать",LocalDateTime.of(2022,7,15,10,0),Duration.ofMinutes(90));
        Epic epic = new Epic("Переезд", "Увезти вещи");

        Epic epics = new Epic("Купить продукты", "Сходить в Магнит");

        Subtask subtask = new Subtask("Заказать машину", "Позвонить по номеру телефона",null,
                null, epic);
        Subtask subtasks = new Subtask("Загрузить машину", "Поднести вещи к машине",LocalDateTime.of(2022,7,15,23,0),
                Duration.ofMinutes(90), epic);
        Subtask laundry = new Subtask("Загрузить стиралку", "Переложить вещи из корзины",LocalDateTime.of(2022,7,15,
                14,0),
                Duration.ofMinutes(90), epic);
        Subtask difference = new Subtask("Загрузить ", "Переложить вещи ",null, null, epic);
        Subtask lau = new Subtask("Загрузить стиралку", "Переложить",LocalDateTime.of(2022,7,15,
                16,0),
                Duration.ofMinutes(90), epic);

        taskManager.createTask(task);

        taskManager.createEpic(epic);
        taskManager.createEpic(epics);
        taskManager.createSubtask(subtask, StatusTask.NEW);
        taskManager.createSubtask(subtasks, StatusTask.NEW);
        taskManager.createSubtask(laundry,StatusTask.NEW);
        taskManager.updateSubtask(subtask, StatusTask.DONE);
        taskManager.updateSubtask(subtasks, StatusTask.DONE);
        taskManager.createSubtask(difference,StatusTask.NEW);
        taskManager.createSubtask(lau,StatusTask.NEW);
        taskManager.getEpic(3);
        taskManager.getEpic(2);
        System.out.println(taskManager.history());
        taskManager.getSubtask(4);
        System.out.println(taskManager.getEpic(2).getStartTime());
        System.out.println(taskManager.history());
        taskManager.getSubtask(5);
        taskManager.getSubtask(6);
        taskManager.getSubtask(6);
        taskManager.getSubtask(4);
        taskManager.getSubtask(6);
        taskManager.deleteAllTask();
        System.out.println(taskManager.getPrioritizedTasks());
    }
}