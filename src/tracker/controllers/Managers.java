package tracker.controllers;


import tracker.servers.KVServer;

import java.io.IOException;

public class Managers {

    public static HistoryManager getDefaultHistory(){
        return   new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
        return new HttpTaskManager("http://localhost:8078");
    }

    public static KVServer  getDefaultKVServer() throws IOException {
        return new KVServer();
    }
}
