/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.util;

import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;

public final class RedstoneUtil {
    public static void propagateRedstoneOutput(World world, TilePosc pos, Direction side) {
        Block<?> block = world.getBlockType(pos);

        // Propagate ordinary output. See BlockRedstoneDiode.notifyNeighbors
        for (Side s : Side.sides) {
            if (s.direction() == side.opposite()) {
                continue;
            }
            world.getBlockType(pos.add(s, new TilePos()))
                .onNeighborChanged(world, pos.add(s, new TilePos()), block);

            for (Side s2 : Side.sides) {
                if (s2.direction() == side) {
                    continue;
                }
                world.getBlockType(pos.add(s, new TilePos()).add(s2))
                    .onNeighborChanged(world, pos.add(s, new TilePos()).add(s2), block);
            }
        }
    }
}
