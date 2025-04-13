/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.network.client;

import dan200.computercraft.core.terminal.Terminal;
import dan200.computercraft.shared.util.IoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import turniplabs.halplibe.helper.network.UniversalPacket;

import javax.annotation.Nullable;
import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * A snapshot of a terminal's state.
 * <p>
 * This is somewhat memory inefficient (we build a buffer, only to write it elsewhere), however it means we get a complete and accurate description of a
 * terminal, which avoids a lot of complexities with resizing terminals, dirty states, etc...
 */
public class TerminalState {
    public final boolean colour;

    public final int width;
    public final int height;

    public final int selectedSlot;

    private final boolean compress;

    @Nullable
    private final ByteBuf buffer;

    private ByteBuf compressed;

    public TerminalState(boolean colour, @Nullable Terminal terminal, int selectedSlot) {
        this(colour, terminal, selectedSlot, true);
    }

    public TerminalState(boolean colour, @Nullable Terminal terminal, int selectedSlot, boolean compress) {
        this.colour = colour;
        this.selectedSlot = selectedSlot;
        this.compress = compress;

        if (terminal == null) {
            width = height = 0;
            buffer = null;
        } else {
            width = terminal.getWidth();
            height = terminal.getHeight();

            UniversalPacket packet = new UniversalPacket();
            terminal.write(packet);

            buffer = Unpooled.buffer();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);

            try {
                try {
                    packet.rawWrite(dos);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                try {
                    dos.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                byte[] bytes = baos.toByteArray();
                buffer.writeBytes(bytes);
            } finally {
                try {
                    dos.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public TerminalState(UniversalPacket buf) {
        colour = buf.readBoolean();
        selectedSlot = buf.readByte();
        compress = buf.readBoolean();

        if (buf.readBoolean()) {
            width = buf.readInt();
            height = buf.readInt();

            int length = buf.readInt();
            buffer = readCompressed(buf, length, compress);
        } else {
            width = height = 0;
            buffer = null;
        }
    }

    private static ByteBuf readCompressed(UniversalPacket buf, int length, boolean compress) {
        if (compress) {
            ByteBuf buffer = Unpooled.buffer();
            InputStream stream = null;
            try {
                stream = new GZIPInputStream(buf.readBytesAsStream(length));
                byte[] swap = new byte[8192];
                while (true) {
                    int bytes = stream.read(swap);
                    if (bytes == -1) {
                        break;
                    }
                    buffer.writeBytes(swap, 0, bytes);
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            } finally {
                IoUtil.closeQuietly(stream);
            }
            return buffer;
        } else {
            ByteBuf buffer = Unpooled.buffer(length);
            buf.readBytes(buffer.array(), length);
            return buffer;
        }
    }

    public void write(UniversalPacket buf) {
        buf.writeBoolean(colour);
        buf.writeByte((byte) selectedSlot);
        buf.writeBoolean(compress);

        buf.writeBoolean(buffer != null);
        if (buffer != null) {
            buf.writeInt(width);
            buf.writeInt(height);

            ByteBuf sendBuffer = getCompressed();

            int length = sendBuffer.readableBytes();
            buf.writeInt(length);

            byte[] byteArray = new byte[sendBuffer.readableBytes()];
            sendBuffer.readBytes(byteArray);
            buf.writeBytes(byteArray);

            buf.writeBytes(sendBuffer.readerIndex(), sendBuffer.readableBytes());
            sendBuffer.release();
            compressed = null;
        }
    }

    private ByteBuf getCompressed() {
        if (buffer == null) {
            throw new NullPointerException("buffer");
        }
        if (!compress) {
            return buffer;
        }
        if (compressed != null) {
            return compressed;
        }

        ByteBuf compressed = Unpooled.directBuffer();
        OutputStream stream = null;
        try {
            stream = new GZIPOutputStream(new ByteBufOutputStream(compressed));
            stream.write(buffer.array(), buffer.arrayOffset(), buffer.readableBytes());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            IoUtil.closeQuietly(stream);
        }

        return this.compressed = compressed;
    }

    public boolean hasTerminal() {
        return buffer != null;
    }

    public int size() {
        return buffer == null ? 0 : buffer.readableBytes();
    }

    public void apply(Terminal terminal) {
        if (buffer == null) {
            throw new NullPointerException("buffer");
        }
        UniversalPacket packet = new UniversalPacket();

        byte[] byteArray = new byte[buffer.readableBytes()];
        buffer.readBytes(byteArray);
        packet.writeBytes(byteArray);

        terminal.read(packet);
    }
}
