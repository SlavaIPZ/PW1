package ua.edu.ukma.frankiv;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.CRC32;

public class Sender {
    public Packet sendMessage(int cType,int bUserID,String message) throws Exception {
        return new Packet(sendMessageBytes(cType, bUserID, message));
    }

    public byte[] sendMessageBytes(int cType,int bUserID,String message) throws Exception {
        Message msg = new Message(cType, bUserID, message);
        byte[] encryptedMessage = msg.messageToBytes();
        ByteBuffer packetBuffer = ByteBuffer.allocate(16 + encryptedMessage.length + 2);
        packetBuffer.put((byte) 0xD); // bMagic
        packetBuffer.put((byte) 0x01); // bSrc
        packetBuffer.putLong(1L); // bPktId
        packetBuffer.putInt(encryptedMessage.length); // wLen
        CRC32 crc = new CRC32();
        crc.update(packetBuffer.array(), 0, 14);
        packetBuffer.putShort((short) crc.getValue()); // wCrc16
        packetBuffer.put(encryptedMessage); // bMsq
        crc.reset();
        crc.update(encryptedMessage);
        packetBuffer.putShort((short) crc.getValue());

        return packetBuffer.array();
    }
}