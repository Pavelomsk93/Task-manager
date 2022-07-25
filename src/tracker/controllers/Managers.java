package tracker.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Managers {

    public static HistoryManager getDefaultHistory(){
        return   new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
        return new HttpTaskManager();
    }
}
