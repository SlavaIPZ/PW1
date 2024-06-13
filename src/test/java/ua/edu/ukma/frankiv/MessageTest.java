package ua.edu.ukma.frankiv;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class MessageTest {
    @Test
    public void testMessageProcessing() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        FakeMessageReceiver fakeMessageReceiver = new FakeMessageReceiver();
        FakeMessageSender fakeMessageSender = new FakeMessageSender();
        MessageParser messageParser = new MessageParser(fakeMessageReceiver, fakeMessageSender);

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    Packet packet = fakeMessageReceiver.receiveMessage();
                    messageParser.processSingleMessage(packet);
                    System.out.println(finalI);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }
}