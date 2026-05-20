/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.items;

import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.shared.common.IColouredItem;
import dan200.computercraft.shared.computer.items.IComputerItem;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ITurtleItem extends IComputerItem, IColouredItem {
    @Nullable
    ITurtleUpgrade getUpgrade(@NotNull ItemStack stack, @NotNull TurtleSide side);

    int getFuelLevel(@NotNull ItemStack stack);

    int getOverlay(@NotNull ItemStack stack);
}
