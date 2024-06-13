package ua.edu.ukma.frankiv;

import org.junit.Assert;
import org.junit.Test;

public class Tester {



    @Test
    public void testReceiver() throws Exception {
        Sender sender = new Sender();
        String message = "Hello, World!";
        Packet packet = sender.sendMessage(1,2,message);
        Message msg = packet.getMessage();
        String text = new String(msg.getMessage());
        System.out.println(msg);
        System.out.println(text);
        Assert.assertNotNull(message);
    }
}
