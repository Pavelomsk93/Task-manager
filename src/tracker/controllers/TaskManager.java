package tracker.controllers;

import tracker.modelParametrs.StatusTask;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import java.util.List;


public interface TaskManager {

    int createTask(Task task);

    void updateTask(Task task);

    List<Task> getAllTask();

    void deleteAllTask();

    Task getTask(int id);

    void deleteTask(int id);

    int createEpic(Epic epic);

    void updateEpic(Epic epic);

    List<Epic> getAllEpic();

    void deleteAllEpic();

    Epic getEpic(int id);

    void deleteEpic(int id);

    int createSubtask(Subtask subtask, StatusTask status);

    void updateSubtask(Subtask subtask, StatusTask status);

    List<Subtask> getAllSubtask();

    void deleteAllSubtask();

    Subtask getSubtask(int id);

    void deleteSubtask(int id, int epicId);

    List<Task> history();

    List<Task> getPrioritizedTasks();

    void validatorTimeTasks(Task task);

}
