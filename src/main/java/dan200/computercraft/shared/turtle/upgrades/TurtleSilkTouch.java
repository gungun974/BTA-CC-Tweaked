/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.upgrades;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleVerb;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Direction;
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
    public boolean isItemSuitable(@NotNull ItemStack stack) {
        if (!ComputerCraft.turtlesCanUseSilkTouch) {
            return false;
        }
        return super.isItemSuitable(stack);
    }

    @Override
    public @NotNull TurtleCommandResult useTool(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side, @NotNull TurtleVerb verb, @NotNull Direction direction) {
        if (!ComputerCraft.turtlesCanUseSilkTouch) {
            return TurtleCommandResult.failure();
        }
        return super.useTool(turtle, side, verb, direction);
    }

    @Override
    public @NotNull String getUnlocalisedAdjective() {
        return "upgrade.minecraft.golden_pickaxe.adjective";
    }
}
