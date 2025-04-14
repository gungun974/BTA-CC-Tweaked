/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.util;

import dan200.computercraft.fabric.Helper;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

public final class RedstoneUtil {
    public static void propagateRedstoneOutput(World world, BlockPos pos, Direction side) {
        BlockLogic block = Helper.getBlockLogic(world, pos.x, pos.y, pos.z);

        if (block == null) {
            return;
        }

        int blockId = block.id();

        // Propagate ordinary output. See BlockRedstoneDiode.notifyNeighbors
        for (Side s : Side.sides) {
            if (s.getDirection() == side.getOpposite()) {
                continue;
            }
            world.notifyBlocksOfNeighborChange(pos.x + s.getOffsetX(), pos.y + s.getOffsetY(), pos.z + s.getOffsetZ(), blockId);
        }

    }
}
