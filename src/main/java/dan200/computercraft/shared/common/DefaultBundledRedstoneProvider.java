/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.common;

import dan200.computercraft.BlockPos;
import dan200.computercraft.api.redstone.IBundledRedstoneProvider;
import dan200.computercraft.fabric.Helper;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;

import javax.annotation.Nonnull;

public class DefaultBundledRedstoneProvider implements IBundledRedstoneProvider {
    public static int getDefaultBundledRedstoneOutput(World world, BlockPos pos, Direction side) {
        BlockLogic block = Helper.getBlockLogic(world, pos.x, pos.y, pos.z);
        if (block instanceof IBundledRedstoneBlock) {
            IBundledRedstoneBlock generic = (IBundledRedstoneBlock) block;
            if (generic.getBundledRedstoneConnectivity(world, pos, side)) {
                return generic.getBundledRedstoneOutput(world, pos, side);
            }
        }
        return -1;
    }

    @Override
    public int getBundledRedstoneOutput(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull Direction side) {
        return getDefaultBundledRedstoneOutput(world, pos, side);
    }
}
