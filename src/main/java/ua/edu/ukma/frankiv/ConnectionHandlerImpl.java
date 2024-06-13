package ua.edu.ukma.frankiv;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionHandlerImpl implements ConnectionHandler {
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    public void handleConnection() throws Exception {
        MessageReceiver receiver = new FakeMessageReceiver();
        MessageSender sender = new FakeMessageSender();
        MessageParser parser = new MessageParser(receiver, sender);
        executorService.submit(parser::run);
    }
}