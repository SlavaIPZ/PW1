package ua.edu.ukma.frankiv;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.CRC32;

public class Packet {
    private static byte bMagic;
    private byte bSrc;
    private long bPktId;
    private int wLen;
    private short wCrc16;
    private byte[] bMsq;
    private short wCrc16Msq;

    public Packet(byte[] packet) {
        ByteBuffer wrapped = ByteBuffer.wrap(packet);
        this.bMagic = wrapped.get();
        this.bSrc = wrapped.get();
        this.bPktId = wrapped.getLong();
        this.wLen = wrapped.getInt();
        byte[] headerBytes = getHeaderBytesArray(bMagic, bSrc, bPktId, wLen);
        CRC32 crc = new CRC32();
        crc.update(headerBytes);
        short crcValue = (short) crc.getValue();
        this.wCrc16 = wrapped.getShort();
        if (crcValue != wCrc16)
            throw new IllegalArgumentException("Invalid packet");
        this.bMsq = new byte[this.wLen];
        wrapped.get(this.bMsq);
        crc.reset();
        crc.update(this.bMsq);
        short crcMsqValue = (short) crc.getValue();
        this.wCrc16Msq = wrapped.getShort();
        if (crcMsqValue != wCrc16Msq)
            throw new IllegalArgumentException("Invalid packet");
    }

    private byte[] getHeaderBytesArray(byte bMagic, byte bSrc, long bPktId, int wLen) {
        List<Byte> headerBytes = new ArrayList<>();
        headerBytes.add(bMagic);
        headerBytes.add(bSrc);
        for(byte b : ByteBuffer.allocate(8).putLong(bPktId).array())
            headerBytes.add(b);
        for(byte b : ByteBuffer.allocate(4).putInt(wLen).array())
            headerBytes.add(b);
        byte[] result = new byte[headerBytes.size()];
        for (int i = 0; i < headerBytes.size(); i++) {
            result[i] = headerBytes.get(i);
        }
        return result;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "bSrc=" + bSrc +
                ", bPktId=" + bPktId +
                ", wLen=" + wLen +
                ", wCrc16=" + wCrc16 +
                ", bMsq=" + Arrays.toString(bMsq) +
                ", wCrc16Msq=" + wCrc16Msq +
                '}';
    }

    public Message getMessage() throws Exception {
        return new Message(bMsq);
    }
}