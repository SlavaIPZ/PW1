package ua.edu.ukma.frankiv;

import org.junit.Assert;
import org.junit.Test;

import javax.crypto.spec.SecretKeySpec;

public class Tester {
    @Test
    public void testSender() throws Exception {
        Sender sender = new Sender();
        byte[] messageBytes = "Hello, World!".getBytes();
        Packet packet = sender.sendMessage(messageBytes, "ThisIsASecretKey");
        Assert.assertNotNull(packet);
    }

    @Test
    public void testReceiver() throws Exception {
        Sender sender = new Sender();
        Receiver receiver = new Receiver();

        byte[] messageBytes = "Hello, World!".getBytes();
        Packet packet = sender.sendMessage(messageBytes, "ThisIsASecretKey");
        byte[] message = receiver.receiveMessage(packet, "ThisIsASecretKey");

        Assert.assertNotNull(message);
    }
}
