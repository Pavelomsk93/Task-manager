package tracker.controllers;

import tracker.modelParametrs.StatusTask;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected  int id = 0;
    HistoryManager historyManager = Managers.getDefaultHistory();
    public final Set<Task> prioritizedTasks =
            new TreeSet<>(new Task.TaskComparator()) ;

    public  int getId() {
        id++;
        return id;
    }
    @Override
    public List<Task> getPrioritizedTasks(){
        return  new ArrayList<>(prioritizedTasks);
    }

    @Override
    public List<Task> history() {
        return historyManager.getHistory();
    }

    @Override
    public int createTask(Task task) {
        if(task!=null){
            task.setId(getId());
            updateTask(task);
            validatorTimeTasks(task);
            prioritizedTasks.add(task);
            return task.getId();
        }else{
            return 0;
        }
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public List<Task> getAllTask() {
        List<Task> allTask = new ArrayList<>();
        for (Task goal : tasks.values()) {
            allTask.add(goal);
            historyManager.add(goal);
        }
        return allTask;
    }

    @Override
    public void deleteAllTask() {
        for(Integer goal:tasks.keySet()){
            historyManager.remove(goal);
        }
        tasks.clear();
    }

    @Override
    public Task getTask(int id) {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        } else {
            return null;
        }
    }

    @Override
    public void deleteTask(int id) {
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public int createEpic(Epic epic) {
        if(epic!=null){
            epic.setId(getId());
            updateEpic(epic);
            return epic.getId();
        }else{
            return 0;
        }

    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public List<Epic> getAllEpic() {
        List<Epic> allEpic = new ArrayList<>();
        for (Epic goal : epics.values()) {
            allEpic.add(goal);
            historyManager.add(goal);
        }
        return allEpic;
    }

    @Override
    public void deleteAllEpic() {
        for(Integer goal:subtasks.keySet()){
            historyManager.remove(goal);
        }
        subtasks.clear();
        for(Integer goal:epics.keySet()){
            historyManager.remove(goal);
        }
        epics.clear();
    }

    @Override
    public Epic getEpic(int id) {
        if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));
            return epics.get(id);
        } else {
            return null;
        }
    }

    @Override
    public void deleteEpic(int id) {
        if(epics.containsKey(id)){
            for (Subtask sub : epics.get(id).getSubTaskList()) {
                historyManager.remove(sub.getId());
                subtasks.remove(sub.getId());
            }
            historyManager.remove(id);
            epics.remove(id);
        }
    }

    @Override
    public int createSubtask(Subtask subtask, StatusTask status) {
        if(subtask!=null){
            subtask.setId(getId());
            updateSubtask(subtask, status);
            subtask.getEpic().addSubtaskList(subtask);
            subtask.getEpic().setStatus(updateStatus(subtask.getEpic()));
            if(subtask.getStartTime()!=null||subtask.getDuration()!=null){
                validatorTimeTasks(subtask);
            }
            prioritizedTasks.add(subtask);
            return subtask.getId();
        }else{
            return 0;
        }
    }

    @Override
    public void updateSubtask(Subtask subtask, StatusTask status) {
        subtask.setStatus(status);
        subtasks.put(subtask.getId(), subtask);
        subtask.getEpic().setStatus(updateStatus(subtask.getEpic()));
    }

    @Override
    public List<Subtask> getAllSubtask() {
        List<Subtask> allSubtask = new ArrayList<>();
        for (Subtask goal : subtasks.values()) {
            allSubtask.add(goal);
            historyManager.add(goal);
        }
        return allSubtask;
    }

    @Override
    public void deleteAllSubtask() {
        for(Integer goal:subtasks.keySet()){
            historyManager.remove(goal);
        }
        subtasks.clear();
        for (Epic goal : epics.values()) {
            goal.setStatus(StatusTask.NEW);
        }
    }

    @Override
    public Subtask getSubtask(int id) {
        if (subtasks.containsKey(id)) {
            historyManager.add(subtasks.get(id));
            return subtasks.get(id);
        } else {
            return null;
        }
    }

    @Override
    public void deleteSubtask(int id, Epic epic) {
        if(subtasks.containsKey(id)){
            historyManager.remove(id);
            subtasks.remove(id);
            epic.setStatus(updateStatus(epic));
        }
    }

    public StatusTask updateStatus(Epic epic) {
        StatusTask status;
        if (epic.getSubTaskList() == null) {
            status = StatusTask.NEW;
            return status;
        }
        int newStatus = 0;
        int doneStatus = 0;
        for (Subtask subtasks : epic.getSubTaskList()) {
            if (subtasks.getStatus() == StatusTask.NEW) {
                newStatus++;
            }
            if (subtasks.getStatus() == StatusTask.DONE) {
                doneStatus++;
            }
        }
        if (newStatus == epic.getSubTaskList().size()) {
            status = StatusTask.NEW;
        } else if (doneStatus == epic.getSubTaskList().size()) {
            status = StatusTask.DONE;
        } else {
            status = StatusTask.IN_PROGRESS;
        }
        return status;
    }

    @Override
    public void validatorTimeTasks(Task task) {
        for (Task priorityTask : prioritizedTasks) {
            if (priorityTask.getStartTime()!=null&&!task.getStartTime().isBefore(priorityTask.getStartTime()) && !task.getStartTime().isAfter(priorityTask.getEndTime())) {
                throw new IllegalArgumentException("Даты пересекаются у задач с номерами: " + priorityTask.getId() + " и " + task.getId());
            }
        }
    }



}

