package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import exceptions.CollisionTaskException;
import model.Epic;
import model.SubTask;
import model.Task;
import service.Endpoint;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


public class HttpTaskServer {
    private static final int PORT = 8080;
    private HttpServer server;
    private static  Gson gson = new Gson();
    TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        server = HttpServer.create(new InetSocketAddress("localhost",PORT), 0);
        server.createContext("/tasks", new Handler(taskManager));
    }

    public void start() {
        System.out.println("Launching server on " + PORT + " port");
        server.start();
    }

    public void stop() {
        server.stop(1);
    }

    private static class Handler extends BaseHttpHandler implements HttpHandler {
        private final TaskManager taskManager;

        public Handler(TaskManager taskManager) {
            this.taskManager = taskManager;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();
            String url = exchange.getRequestURI().toString();
            String[] urlPath = url.split("/");
            Endpoint endpoint = getEndpoint(requestMethod, url);

            switch (endpoint) {
                case GET_TASKS:
                    handlerGetTasks(exchange, urlPath[2]);
                    break;
                case GET_PRIORITIZED:
                    handlerPrioritizedTasks(exchange);
                    break;
                case GET_HISTORY:
                    handlerGetHistory(exchange);
                    break;
                case GET_BY_ID:
                    handlerGetTaskById(exchange, urlPath);
                    break;
                case DELETE_TASKS:
                    handlerDeleteTasks(exchange, urlPath[2]);
                    break;
                case DELETE_BY_ID:
                    handlerDeleteTaskById(exchange, urlPath);
                    break;
                case POST_TASK:
                    if (urlPath.length == 5 && urlPath[3].equals("update")) {
                        handlerPostUpdateTask(exchange,urlPath);
                    } else {
                        handlerPostAddTask(exchange);
                    }
                    break;
                default:
                    sendNotFound(exchange, "Incorrect request");
            }
        }

        private Endpoint getEndpoint(String requestMethod, String url) {
            String []urlPath = url.split("/");

            if (requestMethod.equals("GET")) {
                switch (url) {
                    case "/tasks/":
                        return Endpoint.GET_PRIORITIZED;
                    case "/tasks/history":
                        return Endpoint.GET_HISTORY;
                    case "/tasks/task":
                    case "/tasks/epic":
                    case "/tasks/subtask":
                        return Endpoint.GET_TASKS;
                }
                if (urlPath[urlPath.length - 1].startsWith("?id")) {
                    return Endpoint.GET_BY_ID;
                }
            } else if (requestMethod.equals("DELETE")) {
                switch (url) {
                    case "/tasks/task":
                    case "/tasks/epic":
                    case "/tasks/subtask":
                        return Endpoint.DELETE_TASKS;
                    }
                if (urlPath[urlPath.length - 1].startsWith("?id")) {
                    return Endpoint.DELETE_BY_ID;
                }
            } else if (requestMethod.equals("POST")) {
                return Endpoint.POST_TASK;
            }
            return  Endpoint.UNKNOWN;
        }

        private void handlerGetTasks(HttpExchange exchange, String typTask) throws IOException {
            switch (typTask) {
                case "task":
                   sendText(exchange, gson.toJson(taskManager.getTasks()));
                  return;
                case "epic":
                    sendText(exchange, gson.toJson(taskManager.getEpics()));
                    return;
                case "subtask":
                    sendText(exchange, gson.toJson(taskManager.getSubTasks()));
                    return;
                default:
                    sendNotFound(exchange, "Incorrect request");
            }
        }

        private void handlerPrioritizedTasks(HttpExchange exchange) throws IOException {
            List<Task> prioritized = taskManager.getPrioritizedTasks();

            if (prioritized.isEmpty()) {
                sendNotFound(exchange, "The task list is empty");
                return;
            }

            sendText(exchange, gson.toJson(prioritized));
        }

        private void handlerGetHistory(HttpExchange exchange) throws IOException {
            List<Task> history = taskManager.getPrioritizedTasks();

            if (history.isEmpty()) {
                sendNotFound(exchange, "The task history list is empty");
                return;
            }

            sendText(exchange, gson.toJson(history));
        }

        private void handlerGetTaskById(HttpExchange exchange, String[] urlPath) throws IOException {
            Optional<Integer> optionalId = getTaskId(urlPath);

            if (optionalId.isEmpty()) {
                sendNotFound(exchange, "Not Found");
                return;
            }
            int taskId = optionalId.get();

            if (urlPath[2].equals("task")) {
                try {
                    sendText(exchange, gson.toJson(taskManager.getTaskbyId(taskId)));

                } catch (NoSuchElementException exception) {
                    sendNotFound(exchange, "Not Found");
                }
                return;
            } else if (urlPath[2].equals("epic")) {
                try {
                    sendText(exchange, gson.toJson(taskManager.getEpicbyId(taskId)));
                    return;
                } catch (NoSuchElementException exception) {
                    sendNotFound(exchange, "Not Found");
                }
            } else if (urlPath[2].equals("subtask")) {
                try {
                    sendText(exchange, gson.toJson(taskManager.getSubTaskbyId(taskId)));
                    return;
                } catch (NoSuchElementException exception) {
                    sendNotFound(exchange, "Not Found");
                }
            } else if (urlPath.length == 5 && urlPath[3].equals("subtask")) {
                try {
                    sendText(exchange, gson.toJson(taskManager.getsEpicSubtasks(taskManager.getEpicbyId(taskId))));
                    return;
                } catch (NoSuchElementException exception) {
                    sendNotFound(exchange, "Not Found");
                }
            } else {
                sendNotFound(exchange, "Incorrect request");
            }
        }

        private void handlerDeleteTasks(HttpExchange exchange, String typTask) throws IOException {
            switch (typTask) {
                case "task":
                    taskManager.deleteTask();
                    sendText(exchange, gson.toJson(taskManager.getTasks()));
                    return;
                case "epic":
                    taskManager.deleteEpics();
                    sendText(exchange, gson.toJson(taskManager.getEpics()));
                    return;
                case "subtask":
                    taskManager.deleteSubTask();
                    sendText(exchange, gson.toJson(taskManager.getSubTasks()));
                    return;
                default:
                    sendNotFound(exchange, "Incorrect request");
            }
        }

        private void handlerDeleteTaskById(HttpExchange exchange, String[] urlPath) throws IOException {
            Optional<Integer> optionalId = getTaskId(urlPath);

            if (optionalId.isEmpty()) {
                sendNotFound(exchange, "Not Found");
                return;
            }
            int taskId = optionalId.get();

            if (urlPath[2].equals("task")) {
                taskManager.deleteTaskById(taskId);
                try {
                    sendText(exchange,"The task has been removed.");
                    return;
                } catch (NoSuchElementException exception) {
                    sendNotFound(exchange, "Not Found");
                }
            } else if (urlPath[2].equals("epic")) {
                taskManager.deleteEpicById(taskId);
                try {
                    sendText(exchange,"The epic has been removed.");
                    return;
                } catch (NoSuchElementException exception) {
                    sendNotFound(exchange, "Not Found");
                }
            } else if (urlPath[2].equals("subtask")) {
                try {
                    taskManager.deleteSubTaskById(taskId);
                    sendText(exchange,"The subtask has been removed.");
                    return;
                } catch (NoSuchElementException exception) {
                    sendNotFound(exchange, "Not Found");
                }
            } else
                sendNotFound(exchange, "Incorrect request");
        }

        private void handlerPostAddTask(HttpExchange exchange) throws IOException {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            String path = exchange.getRequestURI().getPath();
            Task task;
            Epic epic;
            SubTask subTask;
            switch (path) {
                case "/tasks/task":
                    task = gson.fromJson(body, Task.class);
                    try {
                        taskManager.addTask(task);
                        sendText(exchange,"The task has been created");
                    } catch (CollisionTaskException exception) {
                        sendHasInteractions(exchange, "The task to add intersects with the existing one");
                    }
                    return;
                case "/tasks/epic":
                    epic = gson.fromJson(body, Epic.class);
                    try {
                        taskManager.addEpic(epic);
                        sendText(exchange,"The epic has been created");
                    } catch (CollisionTaskException exception) {
                        sendHasInteractions(exchange, "The epic to add intersects with the existing one");
                    }
                    return;
                case "/tasks/subtask":
                    subTask = gson.fromJson(body, SubTask.class);
                    try {
                        taskManager.addSubTask(subTask);
                        sendText(exchange,"The subtask has been created");
                    } catch (CollisionTaskException exception) {
                        sendHasInteractions(exchange, "The subtask to add intersects with the existing one");;
                    }
                    return;
                default:
                    sendNotFound(exchange,"Not Found");
            }
        }

        private void handlerPostUpdateTask(HttpExchange exchange, String[] urlPath) throws IOException {
            Optional<Integer> optionalId = getTaskId(urlPath);
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            String path = exchange.getRequestURI().getPath();
            Task newTask;
            Epic newEpic;
            SubTask newSubTask;

            if (optionalId.isEmpty()) {
                sendNotFound(exchange, "Not Found");
                return;
            }
            int taskIdToUpdate = optionalId.get();

            if (urlPath[2].equals("task")) {
                newTask = gson.fromJson(body, Task.class);
                try {
                    taskManager.updateTask(taskManager.getTaskbyId(taskIdToUpdate), newTask);
                    sendText(exchange, "The task has been update");
                } catch (CollisionTaskException exception) {
                    sendHasInteractions(exchange, "The task to update intersects with the existing one");
                }
                return;
            } else if (urlPath[2].equals("epic")) {
                newEpic = gson.fromJson(body, Epic.class);
                try {
                    taskManager.updateEpic(taskManager.getEpicbyId(taskIdToUpdate), newEpic);
                    sendText(exchange, "The epic has been update");
                } catch (CollisionTaskException exception) {
                    sendHasInteractions(exchange, "The epic to update intersects with the existing one");
                }
                return;
            }  else if (urlPath[2].equals("subtask")) {
                newSubTask = gson.fromJson(body, SubTask.class);
                try {
                    taskManager.updateSubTask(taskManager.getSubTaskbyId(taskIdToUpdate), newSubTask);
                    sendText(exchange, "The subtask has been update");
                } catch (CollisionTaskException exception) {
                    sendHasInteractions(exchange, "The subtask to update intersects with the existing one");
                }
            }
        }

        private Optional<Integer> getTaskId(String[] urlPath) {
            try {
                return Optional.of(Integer.parseInt(urlPath[urlPath.length - 1].split("=")[1]));
            } catch (NumberFormatException exception) {
                return Optional.empty();
            }
        }
    }
}