/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.network.server;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.PacketByteBuf;
import dan200.computercraft.fabric.mixin.PacketHandlerServerAccessor;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.network.NetworkMessage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.net.handler.PacketHandler;

import javax.annotation.Nonnull;

public class RequestComputerMessage implements NetworkMessage
{
    private int instance;

    public RequestComputerMessage( int instance )
    {
        this.instance = instance;
    }

    public RequestComputerMessage()
    {
    }

    @Override
    public void toBytes( @Nonnull PacketByteBuf buf )
    {
        buf.writeInt( instance );
    }

    @Override
    public void fromBytes( @Nonnull PacketByteBuf buf )
    {
        instance = buf.readInt();
    }

    @Override
    public void handle(NetworkContext context)
    {
        ServerComputer computer = ComputerCraft.serverComputerRegistry.get( instance );
        if( computer != null )
        {
            computer.sendComputerState( context.player );
        }
    }
}
