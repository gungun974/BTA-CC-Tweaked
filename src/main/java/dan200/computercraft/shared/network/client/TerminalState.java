/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.network.client;

import dan200.computercraft.PacketByteBuf;
import dan200.computercraft.core.terminal.Terminal;
import dan200.computercraft.shared.util.IoUtil;
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.ByteBufInputStream;
//import io.netty.buffer.ByteBufOutputStream;
//import io.netty.buffer.Unpooled;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * A snapshot of a terminal's state.
 *
 * This is somewhat memory inefficient (we build a buffer, only to write it elsewhere), however it means we get a complete and accurate description of a
 * terminal, which avoids a lot of complexities with resizing terminals, dirty states, etc...
 */
public class TerminalState
{
    public final boolean colour;

    public final int width;
    public final int height;

    @Nullable
    public final Terminal terminal;

//    private final boolean compress;

//    @Nullable
//    private final ByteBuf buffer;
//
//    private ByteBuf compressed;

    public TerminalState( boolean colour, @Nullable Terminal terminal )
    {
        //this( colour, terminal, true );

        this.colour = colour;

        if( terminal == null )
        {
            width = height = 0;
            this.terminal = null;
        }
        else
        {
            width = terminal.getWidth();
            height = terminal.getHeight();

            this.terminal = terminal;
        }
    }

//    public TerminalState( boolean colour, @Nullable Terminal terminal, boolean compress )
//    {
//        this.colour = colour;
//        this.compress = compress;
//
//        if( terminal == null )
//        {
//            width = height = 0;
//            buffer = null;
//        }
//        else
//        {
//            width = terminal.getWidth();
//            height = terminal.getHeight();
//
//            ByteBuf buf = buffer = Unpooled.buffer();
//
//            PacketByteBuf packet = new PacketByteBuf();
//
//            packet.writeBytes(buf.array());
//
//            terminal.write( packet );
//        }
//    }
//
//    public TerminalState( PacketByteBuf buf )
//    {
//        colour = buf.readBoolean();
//        compress = buf.readBoolean();
//
//        if( buf.readBoolean() )
//        {
//            width = buf.readInt();
//            height = buf.readInt();
//
//            int length = buf.readInt();
//            buffer = readCompressed( buf, length, compress );
//        }
//        else
//        {
//            width = height = 0;
//            buffer = null;
//        }
//    }
//
//    private static ByteBuf readCompressed(PacketByteBuf buf, int length, boolean compress )
//    {
//        if( compress )
//        {
//            ByteBuf buffer = Unpooled.buffer();
//            InputStream stream = null;
//            try
//            {
//                stream = new GZIPInputStream( buf.readBytesAsStream( length ) );
//                byte[] swap = new byte[8192];
//                while( true )
//                {
//                    int bytes = stream.read( swap );
//                    if( bytes == -1 )
//                    {
//                        break;
//                    }
//                    buffer.writeBytes( swap, 0, bytes );
//                }
//            }
//            catch( IOException e )
//            {
//                throw new UncheckedIOException( e );
//            }
//            finally
//            {
//                IoUtil.closeQuietly( stream );
//            }
//            return buffer;
//        }
//        else
//        {
//            ByteBuf buffer = Unpooled.buffer( length );
//            buf.readBytes( buffer.array(), length );
//            return buffer;
//        }
//    }
//
//    public void write( PacketByteBuf buf )
//    {
//        buf.writeBoolean( colour );
//        buf.writeBoolean( compress );
//
//        buf.writeBoolean( buffer != null );
//        if( buffer != null )
//        {
//            buf.writeInt( width );
//            buf.writeInt( height );
//
//            ByteBuf sendBuffer = getCompressed();
//            buf.writeInt( sendBuffer.readableBytes() );
//            buf.writeBytes(sendBuffer.array());
//            buf.writeBytes(sendBuffer.readerIndex(), sendBuffer.readableBytes() );
//        }
//    }
//
//    private ByteBuf getCompressed()
//    {
//        if( buffer == null )
//        {
//            throw new NullPointerException( "buffer" );
//        }
//        if( !compress )
//        {
//            return buffer;
//        }
//        if( compressed != null )
//        {
//            return compressed;
//        }
//
//        ByteBuf compressed = Unpooled.directBuffer();
//        OutputStream stream = null;
//        try
//        {
//            stream = new GZIPOutputStream( new ByteBufOutputStream( compressed ) );
//            stream.write( buffer.array(), buffer.arrayOffset(), buffer.readableBytes() );
//        }
//        catch( IOException e )
//        {
//            throw new UncheckedIOException( e );
//        }
//        finally
//        {
//            IoUtil.closeQuietly( stream );
//        }
//
//        return this.compressed = compressed;
//    }
//
    public boolean hasTerminal()
    {
//        return buffer != null;
        return terminal != null;
    }
//
//    public int size()
//    {
//        return buffer == null ? 0 : buffer.readableBytes();
//    }
//
//    public void apply( Terminal terminal )
//    {
//        if( buffer == null )
//        {
//            throw new NullPointerException( "buffer" );
//        }
//        PacketByteBuf packet = new PacketByteBuf();
//
//        packet.writeBytes(buffer.array());
//
//        terminal.read( packet );
//    }
}
