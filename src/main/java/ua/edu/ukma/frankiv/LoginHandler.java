package ua.edu.ukma.frankiv;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Map;

public class LoginHandler implements HttpHandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Map<String, String> params = objectMapper.readValue(requestBody, Map.class);

            String login = params.get("login");
            String password = params.get("password");

            if (isValidLogin(login) && isValidPassword(password)) {
                String token = generateToken(login, password);
                sendResponse(exchange, 200, token);
            } else {
                sendResponse(exchange, 401, "Unauthorized");
            }
        } else {
            sendResponse(exchange, 405, "Method Not Allowed");
        }
    }

    private boolean isValidLogin(String login) {
        return login != null && !login.isEmpty() && login.length() >= 5 && login.length() <= 20;
    }

    private boolean isValidPassword(String password) {
        return password != null && !password.isEmpty() && password.length() >= 8 && password.length() <= 20 && password.matches(".*\\d*");
    }

    private String generateToken(String login, String password) {
        return Jwts.builder()
                .setSubject(login)
                .signWith(key)
                .compact();
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}