package ua.edu.ukma.frankiv;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class ServerIntegrationTest {
    private static Server server;

    @BeforeClass
    public static void startServer() throws IOException, SQLException {
        server = new Server();
        server.start();
    }

    @AfterClass
    public static void stopServer() throws SQLException {
        server.stop();

        var conn = DatabaseConnection.getConnection();
        try (var statement = conn.prepareStatement("DELETE FROM goods")) {
            statement.execute();
        }
    }

    @Test
    public void testLogin() throws Exception {
        URL url = new URL("http://localhost:8000/login");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        String jsonInputString = "{\"login\": \"validLogin\", \"password\": \"validPassword\"}";
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        assertEquals(200, conn.getResponseCode());
    }

    @Test
    public void testPutGood() throws Exception {
        URL url = new URL("http://localhost:8000/api/good");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setDoOutput(true);
        String jsonInputString = "{\"id\": 1, \"name\": \"Test\", \"quantity\": 10}";
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        assertEquals(201, conn.getResponseCode());
    }

    @Test
    public void testPostGood() throws Exception {
        URL url = new URL("http://localhost:8000/api/good/1");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        String jsonInputString = "{\"name\": \"Test Updated\", \"quantity\": 15}";
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        assertEquals(204, conn.getResponseCode());
    }

    @Test
    public void testGetGood() throws Exception {
        URL url = new URL("http://localhost:8000/api/good/1");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        assertEquals(200, conn.getResponseCode());
    }
    @Test
    public void testDeleteGood() throws Exception {
        URL url = new URL("http://localhost:8000/api/good/1");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        assertEquals(204, conn.getResponseCode());
    }

}