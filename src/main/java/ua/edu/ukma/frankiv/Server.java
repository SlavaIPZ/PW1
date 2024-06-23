package ua.edu.ukma.frankiv;


import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;

public class Server {
    private HttpServer server;
    
    public void start() throws IOException, SQLException {
        WarehouseService warehouseService = new WarehouseService(DatabaseConnection.getConnection());
        GoodHandler goodHandler = new GoodHandler(warehouseService);
        LoginHandler loginHandler = new LoginHandler();

        server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/login", loginHandler);
        server.createContext("/api/good", goodHandler);
        server.setExecutor(null);
        server.start();
    }

    public void stop() {
        server.stop(2);
    }
    
    public static void main(String[] args) throws IOException, SQLException {
        Server server = new Server();

        server.start();
    }
}