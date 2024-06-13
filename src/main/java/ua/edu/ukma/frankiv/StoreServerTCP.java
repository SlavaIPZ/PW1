package ua.edu.ukma.frankiv;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.IntStream;

public class StoreServerTCP {
    private ExecutorService executorService = Executors.newFixedThreadPool(10);
    private ServerSocket serverSocket;

    public StoreServerTCP(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void start() throws IOException {
        try {
            while (true) {
                executorService.submit(new ClientHandler(serverSocket.accept()));
            }
        } catch (SocketException e) {
            System.out.println("Stoping the server");
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (InputStream in = clientSocket.getInputStream()) {
                try (OutputStream out = clientSocket.getOutputStream()) {
                    byte[] sendHeaderBytes = new byte[Packet.HEADER_LENGTH];
                    in.read(sendHeaderBytes);
                    int messageLength = Packet.readLength(sendHeaderBytes);
                    byte[] sendBytes = Arrays.copyOf(sendHeaderBytes, sendHeaderBytes.length + messageLength + 2);
                    in.read(sendBytes, sendHeaderBytes.length, messageLength + 2);
                    Packet sendPacket = new Packet(sendBytes);

                    String clientMessage = new String(sendPacket.getMessage().getMessage());
                    String respMessage = "Response to: " + clientMessage;

                    Sender sender = new Sender();
                    byte[] respBytes = sender.sendMessageBytes(0, 0, respMessage);
                    out.write(respBytes);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}