/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.network.client;

import turniplabs.halplibe.helper.network.UniversalPacket;

import javax.annotation.Nonnull;

public class ComputerTerminalClientMessage extends ComputerClientMessage
{
    protected TerminalState state;

    public ComputerTerminalClientMessage( int instanceId, TerminalState state )
    {
        super( instanceId );
        this.state = state;
    }

    public ComputerTerminalClientMessage()
    {
    }

    @Override
    public void encodeToUniversalPacket( @Nonnull UniversalPacket buf )
    {
        super.encodeToUniversalPacket( buf );
        state.write( buf );
    }

    @Override
    public void decodeFromUniversalPacket( @Nonnull UniversalPacket buf )
    {
        super.decodeFromUniversalPacket( buf );
        state = new TerminalState( buf );
    }

    @Override
    public void handle(NetworkContext context)
    {
        getComputer().read( state );
    }
}
