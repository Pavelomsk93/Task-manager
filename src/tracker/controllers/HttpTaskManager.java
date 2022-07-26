package tracker.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import tracker.adapters.LocalDateTimeAdapter;
import tracker.client.KVTaskClient;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.modelParametrs.TaskType;

import java.time.LocalDateTime;
import java.util.List;


public class HttpTaskManager extends FileBackedTasksManager {

    private final KVTaskClient kvTaskClient = new KVTaskClient("http://localhost:8078");
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class,new LocalDateTimeAdapter())
            .create();

    public HttpTaskManager()  {
        load();

    }

    @Override
    public void save() {
            kvTaskClient.put("task", gson.toJson(this.tasks));
            kvTaskClient.put("epic", gson.toJson(this.epics));
            kvTaskClient.put("subtask", gson.toJson(this.subtasks));
            kvTaskClient.put("history", gson.toJson(this.historyManager.getHistory()));
    }

    @Override
    public void load() {
        try {
            List<Task> tasks = gson.fromJson(kvTaskClient.load("task"),
                    new TypeToken<List<Task>>() {
                    }.getType());
            for (Task task : tasks) {
                createTask(task);
            }
        }  catch (NullPointerException e) {
            System.out.println("Список сохраненных задач пуст");
        }

        try {
            List<Epic> epics = gson.fromJson(kvTaskClient.load("epic"),
                    new TypeToken<List<Epic>>(){}.getType());

            for (Epic epic : epics) {
                createEpic(epic);
            }
        } catch (NullPointerException e) {
            System.out.println("Список сохраненных эпиков пуст");
        }

        try {
            List<Subtask> subtasks = gson.fromJson(kvTaskClient.load("subtask"),
                    new TypeToken<List<Subtask>>(){}.getType());
            for (Subtask subtask : subtasks) {
                createSubtask(subtask,subtask.getStatus());
            }
        } catch (NullPointerException e) {
            System.out.println("Список сохраненных подзадач пуст");
        }
        try {
            List<Task> history = gson.fromJson(kvTaskClient.load("history"),
                    new TypeToken<List<Task>>(){}.getType());
            for (Task task : history) {
                if (task.getTaskType() == TaskType.TASK) {
                    getTask(task.getId());
                } else if (task.getTaskType() == TaskType.EPIC) {
                    getEpic(task.getId());
                } else {
                    getSubtask(task.getId());
                }
            }
        } catch (NullPointerException e) {
            System.out.println("Список сохраненной истории пуст");
        }
    }
}

