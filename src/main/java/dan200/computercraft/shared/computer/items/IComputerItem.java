/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.computer.items;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import net.minecraft.core.item.ItemStack;

import javax.annotation.Nonnull;

public interface IComputerItem {
    String NBT_ID = "ComputerId";

    default int getComputerID(@Nonnull ItemStack stack) {
        CompoundTag nbt = stack.getData();
        return nbt != null && nbt.containsKey(NBT_ID) ? nbt.getInteger(NBT_ID) : -1;
    }

    default String getLabel(@Nonnull ItemStack stack) {
        return stack.hasCustomName() ? stack.getCustomName() : null;
    }

    ComputerFamily getFamily();

    ItemStack withFamily(@Nonnull ItemStack stack, @Nonnull ComputerFamily family);
}
