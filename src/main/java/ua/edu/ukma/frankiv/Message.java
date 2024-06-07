package ua.edu.ukma.frankiv;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class Message {
    private int cType;
    private int bUserId;
    private String key = "ThisIsASecretKey";

    private Cipher cipher = Cipher.getInstance("AES");

    private SecretKey skeySpec = new SecretKeySpec(key.getBytes(), "AES");

    private byte[] message;

    public Message(int cType, int bUserId, String message) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.cType = cType;
        this.bUserId = bUserId;
        this.message = message.getBytes();
    }

    public Message(byte[] message) throws Exception {
        ByteBuffer wrapped = ByteBuffer.wrap(message);
        this.cType = wrapped.getInt();
        this.bUserId = wrapped.getInt();
        byte[] messageBytes = new byte[wrapped.array().length - 8];
        wrapped.get(messageBytes);
        this.message = decryptMessage(messageBytes);
    }

    public byte[] messageToBytes() throws Exception {
        byte[] encryptedMessage = encryptMessage();
        ByteBuffer buffer = ByteBuffer.allocate(8 + encryptedMessage.length);
        buffer.putInt(cType);
        buffer.putInt(bUserId);
        buffer.put(encryptedMessage);
        return buffer.array();
    }

    public byte[] encryptMessage() throws Exception {
        System.out.println("Enc"+Arrays.toString(message));
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        return cipher.doFinal(message);
    }


    public byte[] decryptMessage(byte[] encMessage) throws Exception {
        System.out.println("Idi nahyuyi"+Arrays.toString(encMessage));
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        return cipher.doFinal(encMessage);
    }

    public byte[] getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "cType=" + cType +
                ", bUserId=" + bUserId +
                ", key='" + key + '\'' +
                ", message=" + Arrays.toString(message) +
                '}';
    }
}