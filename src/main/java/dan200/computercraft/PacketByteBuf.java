package dan200.computercraft;

import com.mojang.nbt.NbtIo;
import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.shared.computer.core.ComputerState;
import dan200.computercraft.shared.network.NetworkHandler;
import net.minecraft.core.net.handler.PacketHandler;
import net.minecraft.core.net.packet.Packet;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class PacketByteBuf extends Packet {
    private byte[] buffer;
    private int writeIndex;
    private int readIndex;

    public PacketByteBuf() {
        this.buffer = new byte[0];
        this.writeIndex = 0;
        this.readIndex = 0;
    }

    public void read(DataInputStream dis) throws IOException {
        final byte[] buff = new byte[dis.available()];
        dis.read(buff);
        writeBytes(buff);
    }

    public void write(DataOutputStream dos) throws IOException {
        if (this.buffer != null) {
            dos.write(this.buffer);
        }
    }

    public void handlePacket(PacketHandler packetHandler) {
        ComputerCraft.log.info("Recieve packet size : {}", buffer.length);
        NetworkHandler.receive(packetHandler, this);

    }

    public int getEstimatedSize() {
        return buffer.length;
    }

    public void writeByte(byte value) {
        ensureCapacity(1);
        buffer[writeIndex++] = value;
    }

    public void writeByte(int value) {
        writeByte((byte) value);
    }

    public byte readByte() {
        ensureReadable(1);
        return buffer[readIndex++];
    }

    public void writeBytes(int... values) {
        ensureCapacity(values.length);
        for (int value : values) {
            buffer[writeIndex++] = (byte) value;
        }
    }

    public void writeBytes(byte... values) {
        ensureCapacity(values.length);
        for (int value : values) {
            buffer[writeIndex++] = (byte) value;
        }
    }

    public void readBytes(byte[] destination, int length) {
        if (length > destination.length) {
            throw new IllegalArgumentException("");
        }
        ensureReadable(length);
        System.arraycopy(buffer, readIndex, destination, 0, length);
        readIndex += length;
    }

    public void writeInt(int value) {
        ensureCapacity(4);
        buffer[writeIndex++] = (byte) (value >> 24);
        buffer[writeIndex++] = (byte) (value >> 16);
        buffer[writeIndex++] = (byte) (value >> 8);
        buffer[writeIndex++] = (byte) value;
    }

    public int readInt() {
        ensureReadable(4);
        int value = ((buffer[readIndex++] & 0xFF) << 24) |
            ((buffer[readIndex++] & 0xFF) << 16) |
            ((buffer[readIndex++] & 0xFF) << 8) |
            (buffer[readIndex++] & 0xFF);
        return value;
    }

    public void writeShort(short value) {
        ensureCapacity(2);
        buffer[writeIndex++] = (byte) (value >> 8);
        buffer[writeIndex++] = (byte) value;
    }

    public short readShort() {
        ensureReadable(2);
        return (short) (((buffer[readIndex++] & 0xFF) << 8) |
                    (buffer[readIndex++] & 0xFF));
    }

    public void writeString(String value) {
        byte[] stringBytes = value.getBytes(StandardCharsets.UTF_8);
        writeInt(stringBytes.length); // Écrit la longueur de la chaîne
        ensureCapacity(stringBytes.length);
        System.arraycopy(stringBytes, 0, buffer, writeIndex, stringBytes.length);
        writeIndex += stringBytes.length;
    }

    public String readString() {
        int length = readInt(); // Lit la longueur de la chaîne
        ensureReadable(length);
        String value = new String(buffer, readIndex, length, StandardCharsets.UTF_8);
        readIndex += length;
        return value;
    }

    public void writeBoolean(boolean value) {
        ensureCapacity(1);
        buffer[writeIndex++] = (byte) (value ? 1 : 0);
    }

    public boolean readBoolean() {
        ensureReadable(1);
        return buffer[readIndex++] != 0;
    }

    public void writeDouble(double value) {
        long bits = Double.doubleToLongBits(value);
        writeLong(bits);
    }

    public double readDouble() {
        long bits = readLong();
        return Double.longBitsToDouble(bits);
    }

    public void writeLong(long value) {
        ensureCapacity(8);
        for (int i = 7; i >= 0; i--) {
            buffer[writeIndex++] = (byte) (value >> (i * 8));
        }
    }

    public long readLong() {
        ensureReadable(8);
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value = (value << 8) | (buffer[readIndex++] & 0xFF);
        }
        return value;
    }

    private void ensureCapacity(int length) {
        if (writeIndex + length > buffer.length) {
            buffer = Arrays.copyOf(buffer, buffer.length + length + 64);
        }
    }

    private void ensureReadable(int length) {
        if (readIndex + length > writeIndex) {
            throw new IndexOutOfBoundsException("Not enough data to read.");
        }
    }

    public InputStream readBytesAsStream(int length) {
        ensureReadable(length); // Vérifie qu'il y a suffisamment de données
        return new InputStream() {
            private int remaining = length;

            @Override
            public int read() {
                if (remaining <= 0) {
                    return -1; // Fin du flux
                }
                remaining--;
                return buffer[readIndex++] & 0xFF; // Retourne l'octet suivant
            }

            @Override
            public int read(byte[] b, int off, int len) {
                if (remaining <= 0) {
                    return -1; // Fin du flux
                }
                int toRead = Math.min(len, remaining); // Nombre d'octets à lire
                System.arraycopy(buffer, readIndex, b, off, toRead);
                readIndex += toRead;
                remaining -= toRead;
                return toRead;
            }
        };
    }

    public void writeEnumConstant(Enum<?> instance) {
        int ordinal = instance.ordinal();
        this.writeByte(ordinal);
    }

    public <T extends Enum<T>> T readEnumConstant(Class<T> enumClass) {
        int ordinal = this.readByte();
        T[] enumConstants = enumClass.getEnumConstants();
        return enumConstants[ordinal];
    }

    public void writeCompoundTag(CompoundTag tag) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            NbtIo.writeCompressed(tag, baos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] buffer = baos.toByteArray();
        writeShort((short)buffer.length);
        writeBytes(buffer);
    }

    public CompoundTag readCompoundTag() {
        int length = Short.toUnsignedInt(readShort());
        if (length == 0) {
            return null;
        } else {
            byte[] data = new byte[length];
            readBytes(data, length);
            try {
                return NbtIo.readCompressed(new ByteArrayInputStream(data));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

