package ua.edu.ukma.frankiv;
import org.junit.Test;

import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

public class StoreCommunicationTest {
    @Test
    public void testTCPCommunication() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        int port = 8080;
        InetAddress address = InetAddress.getLocalHost();

        StoreServerTCP server = new StoreServerTCP(port);
        executorService.submit(() -> {
            try {
                server.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread.sleep(2000);

        StoreClientTCP client = new StoreClientTCP(address.getHostAddress(), port);
        String response = client.sendMessage(1, 0, "Test message");

        server.stop();
        client.stopConnection();

        assertEquals("Response to: Test message", response);
    }

    }