package tracker.servers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import tracker.controllers.FileBackedTasksManager;
import tracker.controllers.Managers;
import tracker.model.Epic;
import tracker.model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpTaskServer {
    private static String hostname;
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static Gson gson = new Gson();
    private final HttpServer server;

    private static FileBackedTasksManager manager ;

    public HttpTaskServer() throws IOException{
        manager = Managers.getFileBacked();
        server = HttpServer.create(new InetSocketAddress(PORT),0);
        server.createContext("/tasks/task",new TaskHandler());
        server.createContext("/tasks/epic",new EpicHandler());
        server.createContext("/tasks/subtask",new SubtaskHandler());
        server.createContext("/tasks/history",new HistoryHandler());
        server.createContext("/tasks",new ListTaskHandler());

    }

    static class TaskHandler implements HttpHandler{

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            OutputStream os = exchange.getResponseBody();
            InputStream is = exchange.getRequestBody();
            String method = exchange.getRequestMethod();
            switch(method){
                case "GET":
                    if(exchange.getRequestURI().getQuery()==null){
                        exchange.sendResponseHeaders(200,0);
                        String taskGson = gson.toJson(manager.getAllTask());
                        try (os) {
                            os.write(taskGson.getBytes());
                        }
                        break;
                    }else  {
                        String[] param = exchange.getRequestURI().getQuery().split("=");
                        int id = Integer.parseInt(param[1]);
                        String taskJson = gson.toJson(manager.getTask(id));
                        try(os){
                            os.write(taskJson.getBytes());
                        }
                    }
                case "POST":
                    String body = new String(is.readAllBytes(), DEFAULT_CHARSET);
                    Task task = gson.fromJson(body,Task.class);
                    exchange.sendResponseHeaders(201, 0);
                    exchange.close();
                    manager.createTask(task);
            }
        }
    }

    static class EpicHandler implements HttpHandler{

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            OutputStream os = exchange.getResponseBody();
            InputStream is = exchange.getRequestBody();
            String method = exchange.getRequestMethod();
            switch(method){
                case "GET":
                    if(exchange.getRequestURI().getQuery()==null){
                        exchange.sendResponseHeaders(200,0);
                        String taskGson = gson.toJson(manager.getAllEpic());
                        try (os) {
                            os.write(taskGson.getBytes());
                        }
                        break;
                    }else  {
                        String[] param = exchange.getRequestURI().getQuery().split("=");
                        int id = Integer.parseInt(param[1]);
                        String taskJson = gson.toJson(manager.getEpic(id));
                        try(os){
                            os.write(taskJson.getBytes());
                        }
                    }
                case "POST":
                    String body = new String(is.readAllBytes(), DEFAULT_CHARSET);
                    Epic epic = gson.fromJson(body,Epic.class);
                    exchange.sendResponseHeaders(201, 0);
                    exchange.close();
                    manager.createTask(epic);
            }
        }
    }

    static class SubtaskHandler implements HttpHandler{

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            OutputStream os = exchange.getResponseBody();
            InputStream is = exchange.getRequestBody();
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            switch(method){
                case "GET":
                    if(path.endsWith("tasks/task/")){
                        exchange.sendResponseHeaders(200,0);
                        String taskGson = gson.toJson(manager.getAllTask());
                        os.write(taskGson.getBytes(DEFAULT_CHARSET));
                        os.close();
                    }
            }
        }
    }

    static class HistoryHandler implements HttpHandler{

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            OutputStream os = exchange.getResponseBody();
            InputStream is = exchange.getRequestBody();
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            switch(method){
                case "GET":
                    if(path.endsWith("tasks/task/")){
                        exchange.sendResponseHeaders(200,0);
                        String taskGson = gson.toJson(manager.getAllTask());
                        os.write(taskGson.getBytes(DEFAULT_CHARSET));
                        os.close();
                    }
            }
        }
    }

    static class ListTaskHandler implements HttpHandler{

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            OutputStream os = exchange.getResponseBody();
            InputStream is = exchange.getRequestBody();
            List<Task> priority = manager.getPrioritizedTasks();
            System.out.println(priority);
            String response = gson.toJson(priority);
            System.out.println(response);
            exchange.sendResponseHeaders(200,0);
            try(os){
                os.write(response.getBytes());
            }
        }

    }
}
