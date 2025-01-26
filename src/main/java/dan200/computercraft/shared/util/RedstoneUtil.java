/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.util;

import dan200.computercraft.BlockPos;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;

public final class RedstoneUtil
{
    public static void propagateRedstoneOutput(World world, BlockPos pos, Direction side )
    {
        //TODO: Redstone output
        /*
        // Propagate ordinary output. See BlockRedstoneDiode.notifyNeighbors
        BlockState block = world.getBlockState( pos );
        BlockPos neighbourPos = pos.offset( side );
        world.updateNeighbor( neighbourPos, block.getBlock(), pos );
        world.updateNeighborsExcept( neighbourPos, block.getBlock(), side.getOpposite() );
         */
    }
}
