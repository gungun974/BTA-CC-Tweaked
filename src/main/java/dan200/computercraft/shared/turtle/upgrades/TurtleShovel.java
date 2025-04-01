/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.upgrades;

import dan200.computercraft.BlockPos;
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

import javax.annotation.Nonnull;

public class TurtleShovel extends TurtleTool
{
    public TurtleShovel( int id, String adjective, Item item )
    {
        super( id, adjective, item );
    }

    public TurtleShovel( int id, Item item )
    {
        super( id, item );
    }

    public TurtleShovel(int id, ItemStack craftItem, ItemStack toolItem )
    {
        super( id, craftItem, toolItem );
    }

    @Nonnull
    @Override
    public TurtleCommandResult useTool( @Nonnull ITurtleAccess turtle, @Nonnull TurtleSide side, @Nonnull TurtleVerb verb, @Nonnull Direction direction )
    {
        if( verb == TurtleVerb.DIG )
        {
            ItemStack shovel = item.copy();
            ItemStack remainder = TurtlePlaceCommand.deploy( shovel, turtle, direction, null, null );
            if( remainder != shovel )
            {
                return TurtleCommandResult.success();
            }
        }
        return super.useTool( turtle, side, verb, direction );
    }

    @Override
    protected boolean canBreakBlock(World world, BlockPos pos )
    {
        if( !super.canBreakBlock( world, pos ) )
        {
            return false;
        }

        Block<?> block = world.getBlock(pos.x, pos.y, pos.z);
        if (block == null) {
            return false;
        }

        Material material = block.getMaterial();
        return material == Material.dirt || material == Material.grass || material == Material.moss || material == Material.clay || material == Material.topSnow || material == Material.snow || material == Material.plant || material == Material.cactus || material == Material.leaves;
    }
}
