import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.StatusTask;
import tracker.controllers.FileBackedTasksManager;
import tracker.controllers.Managers;
import tracker.controllers.TaskManager;
import tracker.model.Epic;
import tracker.model.Subtask;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;


import static org.junit.jupiter.api.Assertions.*;
import static tracker.controllers.FileBackedTasksManager.loadFromFile;

 public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {


    @BeforeEach
    void updateTaskManager() {
       Path path = Paths.get("src\\tracker\\resources\\tasks.csv");
       taskManager = Managers.getFileBacked();
       File file = taskManager.getFile();
       try {
          if(file.exists()) {
             Files.delete(path);
             file = Files.createFile(path).toFile();
          }else{
             file = new File("src\\tracker\\resources\\tasks.csv");
          }
          taskManager = new FileBackedTasksManager(file);
       }catch(IOException e){
          e.printStackTrace();
       }
    }

    @Test
     void shouldBeGetTaskAfterLoadFile (){
        Epic epic = new Epic("Название задачи 1","Описание задачи 1");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Название подзадачи 1","Описание задачи 1", LocalDateTime.of(2022,10,20,12,0),
                Duration.ofMinutes(90),epic);
        Subtask subtask2 = new Subtask("Название подзадачи 2","Описание задачи 2", LocalDateTime.of(2022,10,20,15,0),
                Duration.ofMinutes(90),epic);
        taskManager.createSubtask(subtask,StatusTask.NEW);
        taskManager.createSubtask(subtask2,StatusTask.NEW);
        TaskManager managerFile = loadFromFile(new File("src\\tracker\\resources\\tasks.csv"));
        assertEquals(2,managerFile.getAllSubtask().size());
    }

    @Test
    void shouldBeEmptyTasksListAfterLoadFile (){
       TaskManager managerFile = loadFromFile(new File("src\\tracker\\resources\\tasks.csv"));
       assertEquals(0,managerFile.getAllTask().size());
    }

    @Test
    void shouldBeEmptySubtaskListInEpicAfterLoadFile (){
       Epic epic = new Epic("Название задачи 1","Описание задачи 1");
       taskManager.createEpic(epic);
       TaskManager managerFile = loadFromFile(new File("src\\tracker\\resources\\tasks.csv"));
       assertNotNull(managerFile.getEpic(1).getSubTaskList());
       assertEquals(0,managerFile.getEpic(1).getSubTaskList().size());
    }
    @Test
    void shouldBeEmptyHistoryListAfterLoadFile (){
       Epic epic = new Epic("Название задачи 1","Описание задачи 1");
       taskManager.createEpic(epic);
       Subtask subtask = new Subtask("Название подзадачи 1","Описание задачи 1", LocalDateTime.of(2022,10,20,12,0),
               Duration.ofMinutes(90),epic);
       Subtask subtask2 = new Subtask("Название подзадачи 2","Описание задачи 2", LocalDateTime.of(2022,10,20,15,0),
               Duration.ofMinutes(90),epic);
       taskManager.createSubtask(subtask,StatusTask.NEW);
       taskManager.createSubtask(subtask2,StatusTask.NEW);
       TaskManager managerFile = loadFromFile(new File("src\\tracker\\resources\\tasks.csv"));
       assertNotNull(managerFile.history());
       assertEquals(0,managerFile.history().size());
    }



}