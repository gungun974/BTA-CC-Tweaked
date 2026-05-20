/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.upgrades;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleVerb;
import dan200.computercraft.shared.turtle.core.TurtlePlaceCommand;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

public class TurtleShovel extends TurtleTool {
    public TurtleShovel(int id, Item item) {
        super(id, item);
    }

    public TurtleShovel(int id, ItemStack craftItem, ItemStack toolItem) {
        super(id, craftItem, toolItem);
    }

    @Override
    public @NotNull String getUnlocalisedAdjective() {
        return "upgrade.minecraft.diamond_shovel.adjective";
    }

    @NotNull
    @Override
    public TurtleCommandResult useTool(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side, @NotNull TurtleVerb verb, @NotNull Direction direction) {
        if (verb == TurtleVerb.DIG) {
            ItemStack shovel = item.copy();
            ItemStack remainder = TurtlePlaceCommand.deploy(shovel, turtle, direction, null, null);
            if (remainder != shovel) {
                return TurtleCommandResult.success();
            }
        }
        return super.useTool(turtle, side, verb, direction);
    }

    @Override
    protected boolean canBreakBlock(World world, TilePosc pos) {
        if (!super.canBreakBlock(world, pos)) {
            return false;
        }

        Block<?> block = world.getBlockType(pos);
        if (block == null) {
            return false;
        }

        Material material = block.getMaterial();
        return material == Materials.DIRT || material == Materials.GRASS || material == Materials.MOSS || material == Materials.CLAY || material == Materials.TOP_SNOW || material == Materials.SNOW || material == Materials.PLANT || material == Materials.CACTUS || material == Materials.LEAVES;
    }
}
