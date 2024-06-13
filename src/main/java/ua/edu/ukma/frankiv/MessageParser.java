package ua.edu.ukma.frankiv;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MessageParser {
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final MessageReceiver messageReceiver;
    private final MessageSender messageSender;

    public MessageParser(MessageReceiver messageReceiver, MessageSender messageSender) {
        this.messageReceiver = messageReceiver;
        this.messageSender = messageSender;
    }

    public void run() {
        try {
            Packet packet;
            while ((packet = messageReceiver.receiveMessage()) != null) {
                process(packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            shutdown();
        }
    }


    private void process(Packet packet) {
        executorService.submit(() -> {
            try {
                Message message = packet.getMessage();
                MessageProcessor messageProcessor = new MessageProcessor();
                Packet responseMessage = messageProcessor.process(message);
                messageSender.sendMessage(responseMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }

    public void processSingleMessage(Packet packet) {
        try {
            process(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}