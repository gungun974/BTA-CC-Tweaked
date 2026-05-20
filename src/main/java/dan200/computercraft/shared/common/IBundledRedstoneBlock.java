/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.common;


import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;

public interface IBundledRedstoneBlock {
    boolean getBundledRedstoneConnectivity(World world, TilePosc pos, Direction side);

    int getBundledRedstoneOutput(World world, TilePosc pos, Direction side);
}
