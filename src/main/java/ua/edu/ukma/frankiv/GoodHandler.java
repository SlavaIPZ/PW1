package ua.edu.ukma.frankiv;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class GoodHandler implements HttpHandler {
    private WarehouseService warehouseService;
    private ObjectMapper objectMapper = new ObjectMapper();

    public GoodHandler(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        List<String> parts = Arrays.stream(path.split("/")).filter(x -> !x.isEmpty()).toList();
        int id = parts.size() > 2 && isNumeric(parts.get(2)) ? Integer.parseInt(parts.get(2)) : 0;
        try {
            if ("GET".equals(exchange.getRequestMethod()) && id > 0) {
                Good good = warehouseService.readGood(id);
                if (good != null) {
                    sendResponse(exchange, 200, objectMapper.writeValueAsString(good));
                } else {
                    sendResponse(exchange, 404, "Good not found");
                }
            } else if ("PUT".equals(exchange.getRequestMethod())) {
                Good good = objectMapper.readValue(exchange.getRequestBody(), Good.class);
                Good createdGood = warehouseService.createGood(good.getId(), good.getName(), good.getQuantity());
                sendResponse(exchange, 201, objectMapper.writeValueAsString(createdGood));
            } else if ("POST".equals(exchange.getRequestMethod()) && id > 0) {
                Good good = objectMapper.readValue(exchange.getRequestBody(), Good.class);
                Good updatedGood = warehouseService.updateGood(id, good.getName(), good.getQuantity());
                sendResponse(exchange, 204, objectMapper.writeValueAsString(updatedGood));
            } else if ("DELETE".equals(exchange.getRequestMethod()) && id > 0) {
                warehouseService.deleteGood(id);
                sendResponse(exchange, 204, "");
            } else {
                sendResponse(exchange, 400, "Invalid request");
            }
        } catch (Exception e) {
            System.err.println("Server couldn't handle request: " + e);

            sendResponse(exchange, 500, "Server error");
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}