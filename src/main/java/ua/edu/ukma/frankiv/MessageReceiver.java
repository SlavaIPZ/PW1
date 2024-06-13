package ua.edu.ukma.frankiv;

public interface MessageReceiver {
    Packet receiveMessage() throws Exception;
}