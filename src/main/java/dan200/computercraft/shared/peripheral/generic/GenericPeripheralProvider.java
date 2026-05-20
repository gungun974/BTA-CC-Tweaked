/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.generic;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.core.asm.NamedMethod;
import dan200.computercraft.core.asm.PeripheralMethod;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GenericPeripheralProvider {
    @Nullable
    public static IPeripheral getPeripheral(@NotNull World world, @NotNull TilePosc pos, @NotNull Direction side) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile == null) return null;

        ArrayList<SaturatedMethod> saturated = new ArrayList<>(0);

        List<NamedMethod<PeripheralMethod>> tileMethods = PeripheralMethod.GENERATOR.getMethods(tile.getClass());

        if (!tileMethods.isEmpty()) addSaturated(saturated, tile, tileMethods);

        return saturated.isEmpty() ? null : new GenericPeripheral(tile, saturated);
    }

    private static void addSaturated(ArrayList<SaturatedMethod> saturated, Object target, List<NamedMethod<PeripheralMethod>> methods) {
        saturated.ensureCapacity(saturated.size() + methods.size());
        for (NamedMethod<PeripheralMethod> method : methods) {
            saturated.add(new SaturatedMethod(target, method));
        }
    }
}
