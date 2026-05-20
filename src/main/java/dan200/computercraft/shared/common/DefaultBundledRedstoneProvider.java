/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.common;

import dan200.computercraft.api.redstone.IBundledRedstoneProvider;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

public class DefaultBundledRedstoneProvider implements IBundledRedstoneProvider {
    public static int getDefaultBundledRedstoneOutput(World world, TilePosc pos, Direction side) {
        BlockLogic block = world.getBlockType(pos).getLogic();
        if (block instanceof IBundledRedstoneBlock generic) {
            if (generic.getBundledRedstoneConnectivity(world, pos, side)) {
                return generic.getBundledRedstoneOutput(world, pos, side);
            }
        }
        return -1;
    }

    @Override
    public int getBundledRedstoneOutput(@NotNull World world, @NotNull TilePosc pos, @NotNull Direction side) {
        return getDefaultBundledRedstoneOutput(world, pos, side);
    }
}
