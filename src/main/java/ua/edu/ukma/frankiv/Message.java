package ua.edu.ukma.frankiv;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Message {
    private int cType;
    private int bUserId;
    private byte[] message;

    public Message(byte[] messageBytes) {
        ByteBuffer wrapped = ByteBuffer.wrap(messageBytes).order(ByteOrder.BIG_ENDIAN);
        this.cType = wrapped.getInt();
        this.bUserId = wrapped.getInt();
        this.message = new byte[wrapped.remaining()];
        wrapped.get(this.message);
    }

    public byte[] encryptMessage(String key) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        return cipher.doFinal(message);
    }

    public byte[] decryptMessage(String key) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        return cipher.doFinal(message);
    }
}