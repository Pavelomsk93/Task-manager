package tracker.controllers;

import com.google.gson.Gson;
import tracker.client.KVTaskClient;


import java.io.IOException;
import java.net.URI;

public class HttpTaskManager extends FileBackedTasksManager {

    KVTaskClient client;
    URI url;
    String key;

    public HttpTaskManager(URI url) throws IOException, InterruptedException {
        client = new KVTaskClient(url);
        this.key = client.getKey();
        this.url = url;
    }

    @Override
    public   void save() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        client.put(key,json);
    }
}
