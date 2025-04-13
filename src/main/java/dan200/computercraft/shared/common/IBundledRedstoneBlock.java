/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.common;

import dan200.computercraft.BlockPos;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;

public interface IBundledRedstoneBlock {
    boolean getBundledRedstoneConnectivity(World world, BlockPos pos, Direction side);

    int getBundledRedstoneOutput(World world, BlockPos pos, Direction side);
}
