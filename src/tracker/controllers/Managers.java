package tracker.controllers;

import java.io.File;

public class Managers {

    public static InMemoryTaskManager getDefault(){
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory(){
        return   new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getFileBacked() {
        return new FileBackedTasksManager(new File("src\\tracker\\resources\\tasks.csv"));
    }
}
