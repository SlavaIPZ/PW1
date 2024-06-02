package ua.edu.ukma.frankiv;

public class Receiver {
    public byte[] receiveMessage(Packet packet, String key) throws Exception {
        if (!packet.isValid()) {
            throw new IllegalArgumentException("Invalid packet");
        }

        Message message = packet.getMessage();

        return message.decryptMessage(key);
    }
}