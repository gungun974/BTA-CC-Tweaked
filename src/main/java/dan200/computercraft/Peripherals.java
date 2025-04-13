/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import dan200.computercraft.fabric.Helper;
import dan200.computercraft.shared.peripheral.generic.GenericPeripheralProvider;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;

public final class Peripherals {
    private static final Collection<IPeripheralProvider> providers = new LinkedHashSet<>();

    private Peripherals() {
    }

    public static synchronized void register(@Nonnull IPeripheralProvider provider) {
        Objects.requireNonNull(provider, "provider cannot be null");
        providers.add(provider);
    }

    @Nullable
    public static IPeripheral getPeripheral(World world, BlockPos pos, Direction side) {
        return pos.y < World.HEIGHT_BLOCKS && (Helper.isServerEnvironment() || Helper.isSinglePlayer()) ? getPeripheralAt(world, pos, side) : null;
    }

    @Nullable
    private static IPeripheral getPeripheralAt(World world, BlockPos pos, Direction side) {
        // Try the handlers in order:
        for (IPeripheralProvider peripheralProvider : providers) {
            try {
                return peripheralProvider.getPeripheral(world, pos, side);
            } catch (Exception e) {
                ComputerCraft.log.error("Peripheral provider " + peripheralProvider + " errored.", e);
            }
        }

        return GenericPeripheralProvider.getPeripheral(world, pos, side);
    }

}
