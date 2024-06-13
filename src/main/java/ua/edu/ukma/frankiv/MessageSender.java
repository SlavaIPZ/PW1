package ua.edu.ukma.frankiv;

public interface MessageSender {
    void sendMessage(Packet message) throws Exception;
}
