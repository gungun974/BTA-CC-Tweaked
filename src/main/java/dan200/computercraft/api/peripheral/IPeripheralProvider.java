/*
 * This file is part of the public ComputerCraft API - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. This API may be redistributed unmodified and in full only.
 * For help using the API, and posting your mods, visit the forums at computercraft.info.
 */
package dan200.computercraft.api.peripheral;


import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * This interface is used to create peripheral implementations for blocks.
 * <p>
 * If you have a {@link net.minecraft.core.block.entity.TileEntity} which acts as a peripheral, you may alternatively implement {@link IPeripheralTile}.
 *
 * @see dan200.computercraft.api.ComputerCraftAPI#registerPeripheralProvider(IPeripheralProvider)
 */
@FunctionalInterface
public interface IPeripheralProvider {
    /**
     * Produce an peripheral implementation from a block location.
     *
     * @param world The world the block is in.
     * @param pos   The position the block is at.
     * @param side  The side to get the peripheral from.
     * @return A peripheral, or {@link Optional#empty()} if there is not a peripheral here you'd like to handle.
     * @see dan200.computercraft.api.ComputerCraftAPI#registerPeripheralProvider(IPeripheralProvider)
     */
    IPeripheral getPeripheral(@NotNull World world, @NotNull TilePosc pos, @NotNull Direction side);
}
