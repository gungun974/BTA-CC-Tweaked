/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.upgrades;

import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TurtleSilkTouch extends TurtleTool {
    public TurtleSilkTouch(int id, Item item) {
        super(id, item);
        dropCause = EnumDropCause.SILK_TOUCH;
    }

    public TurtleSilkTouch(int id, ItemStack craftItem, ItemStack toolItem) {
        super(id, craftItem, toolItem);
        dropCause = EnumDropCause.SILK_TOUCH;
    }

    @Override
    public @NotNull String getUnlocalisedAdjective() {
        return "upgrade.minecraft.golden_pickaxe.adjective";
    }
}
