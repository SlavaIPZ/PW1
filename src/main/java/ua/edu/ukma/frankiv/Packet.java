package ua.edu.ukma.frankiv;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.zip.CRC32;

public class Packet {
    private byte bMagic;
    private byte bSrc;
    private long bPktId;
    private int wLen;
    private short wCrc16;
    private byte[] bMsq;
    private short wCrc16Msq;

    public Packet(byte[] packet) {
        ByteBuffer wrapped = ByteBuffer.wrap(packet).order(ByteOrder.BIG_ENDIAN);
        this.bMagic = wrapped.get();
        this.bSrc = wrapped.get();
        this.bPktId = wrapped.getLong();
        this.wLen = wrapped.getInt();
        this.wCrc16 = wrapped.getShort();
        this.bMsq = new byte[this.wLen];
        wrapped.get(this.bMsq);
        this.wCrc16Msq = wrapped.getShort();
    }

    public boolean isValid() {
        CRC32 crc = new CRC32();
        byte[] bytesPkt = ByteBuffer.allocate(14).put(this.bMagic).put(bSrc).putLong(bPktId).putInt(wLen).array();
        crc.update(bytesPkt);
        if ((short) crc.getValue() != wCrc16)
            return false;
        crc.reset();
        crc.update(this.bMsq);
        return (short) crc.getValue() == wCrc16Msq;
    }

    public Message getMessage() {
        return new Message(bMsq);
    }
}