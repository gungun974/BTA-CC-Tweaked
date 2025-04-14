/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.upgrades;

import dan200.computercraft.shared.util.BlockPos;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleVerb;
import dan200.computercraft.shared.turtle.core.TurtlePlaceCommand;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class TurtleHoe extends TurtleTool {
    public TurtleHoe(int id, Item item) {
        super(id, item);
    }

    public TurtleHoe(int id, ItemStack craftItem, ItemStack toolItem) {
        super(id, craftItem, toolItem);
    }

    @Override
    public @NotNull String getUnlocalisedAdjective() {
        return "upgrade.minecraft.diamond_hoe.adjective";
    }

    @Nonnull
    @Override
    public TurtleCommandResult useTool(@Nonnull ITurtleAccess turtle, @Nonnull TurtleSide side, @Nonnull TurtleVerb verb, @Nonnull Direction direction) {
        if (verb == TurtleVerb.DIG) {
            ItemStack hoe = item.copy();
            ItemStack remainder = TurtlePlaceCommand.deploy(hoe, turtle, direction, null, null);
            if (remainder != hoe) {
                return TurtleCommandResult.success();
            }
        }
        return super.useTool(turtle, side, verb, direction);
    }

    @Override
    protected boolean canBreakBlock(World world, BlockPos pos) {
        if (!super.canBreakBlock(world, pos)) {
            return false;
        }

        Block<?> block = world.getBlock(pos.x, pos.y, pos.z);
        if (block == null) {
            return false;
        }

        Material material = block.getMaterial();
        return material == Material.plant || material == Material.cactus || material == Material.leaves || material == Material.moss;
    }
}
