/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.upgrades;

import dan200.computercraft.BlockPos;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;

public class TurtleSword extends TurtleTool
{
    public TurtleSword( int id, String adjective, Item item )
    {
        super( id, adjective, item );
    }

    public TurtleSword( int id, Item item )
    {
        super( id, item );
    }

    public TurtleSword(int id, ItemStack craftItem, ItemStack toolItem )
    {
        super( id, craftItem, toolItem );
    }

    @Override
    protected int getDamageMultiplier()
    {
        return 9;
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
        return material == Material.plant || material == Material.leaves || material == Material.cloth || material == Material.web;
    }
}
