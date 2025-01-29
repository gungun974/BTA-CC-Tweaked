/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.network;

import dan200.computercraft.PacketByteBuf;
import net.minecraft.core.net.handler.PacketHandler;

import javax.annotation.Nonnull;

/**
 * The base interface for any message which will be sent to the client or server.
 */
public interface NetworkMessage
{
    /**
     * Write this packet to a buffer.
     *
     * This may be called on any thread, so this should be a pure operation.
     *
     * @param buf The buffer to write data to.
     */
    void toBytes( @Nonnull PacketByteBuf buf );

    /**
     * Read this packet from a buffer.
     *
     * This may be called on any thread, so this should be a pure operation.
     *
     * @param buf The buffer to read data from.
     */
    default void fromBytes( @Nonnull PacketByteBuf buf )
    {
        throw new IllegalStateException( "Should have been registered using a \"from bytes\" method" );
    }

    /**
     * Handle this {@link NetworkMessage}.
     */
    void handle(PacketHandler packetHandler);
}
