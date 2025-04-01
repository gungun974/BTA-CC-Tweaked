/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.upgrades;

import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;

public class TurtleAxe extends TurtleTool
{
    public TurtleAxe( int id, String adjective, Item item )
    {
        super( id, adjective, item );
    }

    public TurtleAxe( int id, Item item )
    {
        super( id, item );
    }

    public TurtleAxe(int id, ItemStack craftItem, ItemStack toolItem )
    {
        super( id, craftItem, toolItem );
    }

    @Override
    protected int getDamageMultiplier()
    {
        return 6;
    }
}
