package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;


public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer server;
    protected static Gson gson;
    TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        server = HttpServer.create(new InetSocketAddress("localhost",PORT), 0);
        server.createContext("/tasks", new Handler(taskManager));
        gson = Managers.getGson();
    }

    public void start() {
        System.out.println("Launching server on " + PORT + " port");
        server.start();
    }

    public void stop() {
        server.stop(1);
    }
}