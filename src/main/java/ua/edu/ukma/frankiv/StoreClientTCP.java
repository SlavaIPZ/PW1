package ua.edu.ukma.frankiv;

import java.io.*;
import java.net.*;
import java.util.Arrays;

public class StoreClientTCP {
    private Socket clientSocket;
    private OutputStream out;
    private InputStream in;

    public StoreClientTCP(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = clientSocket.getOutputStream();
        in = clientSocket.getInputStream();
    }

    public String sendMessage(int cType, int bUserId, String msg) throws Exception {
        Sender sender = new Sender();
        byte[] sendBytes = sender.sendMessageBytes(cType, bUserId, msg);
        out.write(sendBytes);

        byte[] respHeaderBytes = new byte[Packet.HEADER_LENGTH];
        in.read(respHeaderBytes);
        int messageLength = Packet.readLength(respHeaderBytes);
        byte[] respBytes = Arrays.copyOf(respHeaderBytes, respHeaderBytes.length + messageLength + 2);
        in.read(respBytes, respHeaderBytes.length, messageLength + 2);
        Packet respPacket = new Packet(respBytes);

        return new String(respPacket.getMessage().getMessage());
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}