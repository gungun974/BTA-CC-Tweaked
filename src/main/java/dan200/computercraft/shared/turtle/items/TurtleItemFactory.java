/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.items;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.shared.common.ComputerCraftItems;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.turtle.blocks.ITurtleTile;
import net.minecraft.core.item.ItemStack;

public final class TurtleItemFactory
{
    private TurtleItemFactory() {}

    public static ItemStack create(ITurtleTile turtle )
    {
        ITurtleAccess access = turtle.getAccess();

        return create( turtle.getComputerID(),
            turtle.getLabel(),
            turtle.getColour(),
            turtle.getFamily(),
            access.getUpgrade( TurtleSide.LEFT ),
            access.getUpgrade( TurtleSide.RIGHT ),
            access.getFuelLevel(),
            turtle.getOverlay() );
    }

    public static ItemStack create( int id, String label, int colour, ComputerFamily family, ITurtleUpgrade leftUpgrade, ITurtleUpgrade rightUpgrade,
                                    int fuelLevel, int overlay )
    {
        switch( family )
        {
            case NORMAL:
                return ComputerCraftItems.TURTLE_NORMAL.create( id, label, colour, leftUpgrade, rightUpgrade, fuelLevel, overlay );
            case ADVANCED:
                return ComputerCraftItems.TURTLE_ADVANCED.create( id, label, colour, leftUpgrade, rightUpgrade, fuelLevel, overlay );
            default:
                return null;
        }
    }
}
