package ua.edu.ukma.frankiv;

public class MessageProcessor {
    public Packet process(Message message) throws Exception {
        System.out.println("Processed message:" + new String(message.getMessage()));
        String response = "Ok";
        Sender sender = new Sender();
        Packet packet = sender.sendMessage(message.getcType(), message.getbUserId(), response);
        return packet;
    }
}