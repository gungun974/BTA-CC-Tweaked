/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.network.client;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.shared.computer.core.ClientComputer;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

import javax.annotation.Nonnull;

/**
 * A packet, which performs an action on a {@link ClientComputer}.
 */
public abstract class ComputerClientMessage implements NetworkMessage
{
    private int instanceId;

    public ComputerClientMessage( int instanceId )
    {
        this.instanceId = instanceId;
    }

    public ComputerClientMessage()
    {
    }

    public int getInstanceId()
    {
        return instanceId;
    }

    @Override
    public void encodeToUniversalPacket( @Nonnull UniversalPacket buf )
    {
        buf.writeInt( instanceId );
    }

    @Override
    public void decodeFromUniversalPacket( @Nonnull UniversalPacket buf )
    {
        instanceId = buf.readInt();
    }

    public ClientComputer getComputer()
    {
        ClientComputer computer = ComputerCraft.clientComputerRegistry.get( instanceId );
        if( computer == null )
        {
            ComputerCraft.clientComputerRegistry.add( instanceId, computer = new ClientComputer( instanceId ) );
        }
        return computer;
    }
}
