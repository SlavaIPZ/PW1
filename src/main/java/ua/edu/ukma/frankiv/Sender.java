package ua.edu.ukma.frankiv;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.CRC32;

public class Sender {
    public Packet sendMessage(byte[] messageBytes, String key) throws Exception {
        Message message = new Message(messageBytes);

        byte[] encryptedMessage = message.encryptMessage(key);

        ByteBuffer packetBuffer = ByteBuffer.allocate(16 + encryptedMessage.length + 2).order(ByteOrder.BIG_ENDIAN);
        packetBuffer.put((byte) 0x13); // bMagic
        packetBuffer.put((byte) 0x01); // bSrc
        packetBuffer.putLong(1L); // bPktId
        packetBuffer.putInt(encryptedMessage.length); // wLen
        CRC32 crc = new CRC32();
        crc.update(packetBuffer.array(), 0, 14);
        packetBuffer.putShort((short) crc.getValue()); // wCrc16
        packetBuffer.put(encryptedMessage); // bMsq
        crc.reset();
        crc.update(encryptedMessage);
        packetBuffer.putShort((short) crc.getValue()); // wCrc16Msq

        return new Packet(packetBuffer.array());
    }
}