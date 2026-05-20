/*
 * This file is part of the public ComputerCraft API - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. This API may be redistributed unmodified and in full only.
 * For help using the API, and posting your mods, visit the forums at computercraft.info.
 */
package dan200.computercraft.api.peripheral;


import net.minecraft.core.util.helper.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link net.minecraft.core.block.entity.TileEntity} which may act as a peripheral.
 * <p>
 * If you need more complex capabilities (such as handling TEs not belonging to your mod), you should use {@link IPeripheralProvider}.
 */
public interface IPeripheralTile {
    /**
     * Get the peripheral on the given {@code side}.
     *
     * @param side The side to get the peripheral from.
     * @return A peripheral, or {@code null} if there is not a peripheral here.
     * @see IPeripheralProvider#getPeripheral(net.minecraft.core.world.World, net.minecraft.core.world.pos.TilePosc, net.minecraft.core.util.helper.Direction)
     */
    @Nullable
    IPeripheral getPeripheral(@NotNull Direction side);
}
