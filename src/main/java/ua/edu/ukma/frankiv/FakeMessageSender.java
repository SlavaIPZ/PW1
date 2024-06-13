package ua.edu.ukma.frankiv;
public class FakeMessageSender implements MessageSender {
    public void sendMessage(Packet packet) throws Exception {
        System.out.println("Sending message: " + new String(packet.getMessage().getMessage()));

    }
}