package service;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {
    protected void sendText(HttpExchange exchange, String text) throws IOException {
        if(text.isBlank()) {
            exchange.sendResponseHeaders(200,0);
        } else {
            byte[] resp = text.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(200, resp.length);
            exchange.getResponseBody().write(resp);
            exchange.close();
        }
    }

    protected void sendNotFound(HttpExchange exchange, String text) throws IOException {
        exchange.sendResponseHeaders(404, 0);

    }

    protected void sendHasInteractions(HttpExchange exchange, String text) throws IOException  {
        exchange.sendResponseHeaders(404, 0);
//для отправки ответа, если при создании или обновлении задача пересекается с уже существующими.
    }
}
