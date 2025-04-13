/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.util;

import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemDye;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.DyeColor;

import javax.annotation.Nullable;

public final class ColourUtils {
    @Nullable
    private ColourUtils() {
    }

    public static DyeColor getStackColour(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof ItemDye ? DyeColor.colorFromItemMeta(stack.getMetadata()) : null;
    }
}
