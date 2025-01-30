/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.computer.inventory;

import dan200.computercraft.shared.computer.blocks.TileComputerBase;

public class ContainerComputer extends ContainerComputerBase
{
    public ContainerComputer( TileComputerBase tile )
    {
        super( tile.createServerComputer().getInstanceID(), tile.getFamily() );
    }

//    public ContainerComputer( int i, PlayerInventory playerInventory, PacketByteBuf packetByteBuf )
//    {
//        super( ComputerCraftRegistry.ModContainers.COMPUTER, i, playerInventory, packetByteBuf );
//    }
}
