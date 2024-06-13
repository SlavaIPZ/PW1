package ua.edu.ukma.frankiv;

import java.util.concurrent.ThreadLocalRandom;

public class FakeMessageReceiver implements MessageReceiver {
    @Override
    public Packet receiveMessage() throws Exception {
        Sender sender = new Sender();
        int cType = ThreadLocalRandom.current().nextInt(1, 7);
        int bUserId = ThreadLocalRandom.current().nextInt();
        String message = "Fake message";
        return sender.sendMessage(cType, bUserId, message);
    }


}