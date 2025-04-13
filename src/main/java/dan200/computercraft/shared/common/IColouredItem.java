/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.common;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.item.ItemStack;

public interface IColouredItem {
    String NBT_COLOUR = "Color";

    static int getColourBasic(ItemStack stack) {
        CompoundTag tag = stack.getData();
        return tag.containsKey(NBT_COLOUR) ? tag.getInteger(NBT_COLOUR) : -1;
    }

    static void setColourBasic(ItemStack stack, int colour) {
        if (colour == -1) {
            CompoundTag tag = stack.getData();
            tag.getValue().remove(NBT_COLOUR);
        } else {
            stack.getData()
                .putInt(NBT_COLOUR, colour);
        }
    }

    default int getColour(ItemStack stack) {
        return getColourBasic(stack);
    }

    default ItemStack withColour(ItemStack stack, int colour) {
        ItemStack copy = stack.copy();
        setColourBasic(copy, colour);
        return copy;
    }
}
