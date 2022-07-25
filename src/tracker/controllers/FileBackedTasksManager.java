package tracker.controllers;


import tracker.modelParametrs.StatusTask;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.exceptions.ManagerSaveException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FileBackedTasksManager extends InMemoryTaskManager  {

    private final File file;
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy;HH:mm");

    public FileBackedTasksManager(File file) {
        this.file = file;
    }
    public FileBackedTasksManager(){this.file = null;}


    @Override
    public int createTask(Task task) {
        int id =super.createTask(task);
        save();
        return id;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public List<Task> getAllTask() {
        super.getAllTask();
        save();
        return super.getAllTask();
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public Task getTask(int id) {
        super.getTask(id);
        save();
        return super.getTask(id);
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public int createEpic(Epic epic) {
       int id = super.createEpic(epic);
        save();
        return id;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public List<Epic> getAllEpic() {
        super.getAllEpic();
        save();
        return super.getAllEpic();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public Epic getEpic(int id) {
        super.getEpic(id);
        save();
        return super.getEpic(id);
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public int createSubtask(Subtask subtask, StatusTask status) {
       int id = super.createSubtask(subtask, status);
        save();
        return id;
    }

    @Override
    public void updateSubtask(Subtask subtask, StatusTask status) {
        super.updateSubtask(subtask, status);
        save();
    }

    @Override
    public List<Subtask> getAllSubtask() {
        super.getAllSubtask();
        save();
        return super.getAllSubtask();
    }

    @Override
    public void deleteAllSubtask() {
        super.deleteAllSubtask();
        save();
    }

    @Override
    public Subtask getSubtask(int id) {
        super.getSubtask(id);
        save();
        return super.getSubtask(id);
    }

    @Override
    public void deleteSubtask(int id, int epicId) {
        super.deleteSubtask(id, epicId);
        save();
    }

    @Override
    public List<Task> history() {
        super.history();
        save();
        return super.history();
    }
    @Override
    public void validatorTimeTasks(Task task){
        super.validatorTimeTasks(task);
        save();
    }

    @Override
    public List<Task> getPrioritizedTasks(){
        save();
        return super.getPrioritizedTasks();
    }

    public void save() {
        try (final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            bufferedWriter.write("id,type,name,status,description,startTime,duration,epic");
            bufferedWriter.newLine();

            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                bufferedWriter.append(taskToString(entry.getValue()));
                bufferedWriter.newLine();
            }
            for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
                bufferedWriter.append(taskToString(entry.getValue()));
                bufferedWriter.newLine();
            }
            for (Map.Entry<Integer, Subtask> entry : subtasks.entrySet()) {
                bufferedWriter.append(taskToString(entry.getValue()));
                bufferedWriter.newLine();
            }

            bufferedWriter.newLine();

            bufferedWriter.append(toStringHistory(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файл");
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        final FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        fileBackedTasksManager.load();
        return fileBackedTasksManager;
    }


    public void load() {
        try (final BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            bufferedReader.readLine();
            while (true) {
                String str = bufferedReader.readLine();
                if (str!=null&&!str.isEmpty()) {
                    Task task = taskFromString(str);
                    switch (task.getTaskType()) {
                        case TASK:
                            tasks.put(task.getId(), task);
                            prioritizedTasks.add(task);
                            break;
                        case EPIC:
                            epics.put(task.getId(), (Epic) task);
                            break;
                        case SUBTASK:
                            subtasks.put(task.getId(), (Subtask) task);
                            prioritizedTasks.add(task);
                            break;
                    }

                } else {
                    String lineHistory = bufferedReader.readLine();
                    List<Integer> historyTasks = historyFromString(lineHistory);
                    fileHistory(historyTasks);
                    break;
                }
            }
        } catch (IOException e){
            throw new ManagerSaveException("Ошибка чтения из файла");
        }
}

    public void fileHistory (List<Integer> historyTasks){
       if(historyTasks!=null){
           for(Integer task:historyTasks){
               if(tasks.containsKey(task)){
                   historyManager.add(tasks.get(task));
               }else if(epics.containsKey(task)){
                   historyManager.add(epics.get(task));
               }else if(subtasks.containsKey(task)) {
                   historyManager.add(subtasks.get(task));
               }
           }
       }
    }

    public static List<Integer> historyFromString(String value) {
        if(value!=null){
            String[] id = value.split(",");
            List<Integer> history = new ArrayList<>();
            for (String i : id) {
                history.add(Integer.parseInt(i));
            }
            return history;
        }else{
            return null;
        }
    }

    public static String toStringHistory(HistoryManager manager){
        StringBuilder sb = new StringBuilder();
        for (Task task : manager.getHistory()) {
            sb.append(task.getId()).append(",");
        }
        return sb.toString();
    }

    public static String taskToString(Task task){
        String[] array = new String[8];
        array[0] = Integer.toString(task.getId());
        array[1] = task.getTaskType().toString();
        array[2] = task.getTitle();
        array[3] = task.getStatus().toString();
        array[4] = task.getDescription();
        if(task.getClass().equals(Epic.class)) {
            if (((Epic) task).getSubTaskList().size() == 0 ||  task.getStartTime()==null) {
                array[5] = "";
                array[6] = "";
            } else {
                array[5] = task.getStartTime().format(formatter);
                array[6] = Integer.toString(task.getDuration());
            }
        }else{
            if(task.getStartTime()==null){
                array[5] = "";
                array[6] = "";
            }else{
                array[5] = task.getStartTime().format(formatter);
                array[6] = Integer.toString(task.getDuration());
            }
        }
        array[7] = "";
        if(task.getClass().equals(Subtask.class)){
            Subtask subtask = (Subtask) task;
            array[7] = Integer.toString(subtask.getEpicId());
        }
        return String.join(",", array);
    }

    public  Task taskFromString(String value) {
        String[] typeTask = value.split(",");
        if (typeTask[1].equals("TASK")) {
            return new Task(typeTask[2], typeTask[4], Integer.parseInt(typeTask[0]),
                    StatusTask.valueOf(typeTask[3]), LocalDateTime.parse(typeTask[5],formatter) , Integer.parseInt(typeTask[6]));
        }else if (typeTask[1].equals("EPIC")) {
            if(typeTask.length<6){
                return new Epic(typeTask[2], typeTask[4], Integer.parseInt(typeTask[0]),
                        StatusTask.valueOf(typeTask[3]),null,0);
            }else{
                return new Epic(typeTask[2], typeTask[4], Integer.parseInt(typeTask[0]),
                        StatusTask.valueOf(typeTask[3]), LocalDateTime.parse(typeTask[5],formatter) , Integer.parseInt(typeTask[6]));
            }
        } else  {
            Subtask subtask;
            if(typeTask[5].equals("")) {
                subtask = new Subtask(typeTask[2], typeTask[4], Integer.parseInt(typeTask[0]),
                        StatusTask.valueOf(typeTask[3]), null, 0, Integer.parseInt(typeTask[7]));
            }else{
                subtask = new Subtask(typeTask[2], typeTask[4], Integer.parseInt(typeTask[0]),
                        StatusTask.valueOf(typeTask[3]), LocalDateTime.parse(typeTask[5], formatter), Integer.parseInt(typeTask[6]),
                        Integer.parseInt(typeTask[7]));
            }
            epics.get(Integer.parseInt(typeTask[7])).addSubtaskList(subtask);
            return subtask;
        }
    }

    public File getFile(){
        return file;
    }
}
