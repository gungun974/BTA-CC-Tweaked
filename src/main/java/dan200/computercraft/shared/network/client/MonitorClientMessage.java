/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.network.client;

import dan200.computercraft.BlockPos;
import dan200.computercraft.shared.peripheral.monitor.TileMonitor;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class MonitorClientMessage implements NetworkMessage
{
    private int x;
    private int y;
    private int z;
    private TerminalState state;

    public MonitorClientMessage()
    {
    }

    public MonitorClientMessage( BlockPos pos, TerminalState state )
    {
        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;
        this.state = state;
    }

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket packet) {
        packet.writeInt(x);
        packet.writeInt(y);
        packet.writeInt(z);
        state.write( packet );
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket packet) {
        x = packet.readInt();
        y = packet.readInt();
        z = packet.readInt();
        state.write( packet );
    }

    @Override
    public void handle(NetworkContext context) {
        Player player = context.player;
        if( player == null || player.world == null )
        {
            return;
        }

        TileEntity te = player.world.getTileEntity( x, y, z );
        if( !(te instanceof TileMonitor) )
        {
            return;
        }

        ((TileMonitor) te).read( state );
    }
}
