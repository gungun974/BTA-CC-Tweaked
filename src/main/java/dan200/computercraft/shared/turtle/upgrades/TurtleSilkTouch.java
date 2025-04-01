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
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;

import javax.annotation.Nonnull;

public class TurtleSilkTouch extends TurtleTool
{
    public TurtleSilkTouch(int id, String adjective, Item item )
    {
        super( id, adjective, item );
        dropCause = EnumDropCause.SILK_TOUCH;
    }

    public TurtleSilkTouch(int id, Item item )
    {
        super( id, item );
        dropCause = EnumDropCause.SILK_TOUCH;
    }

    public TurtleSilkTouch(int id, ItemStack craftItem, ItemStack toolItem )
    {
        super( id, craftItem, toolItem );
        dropCause = EnumDropCause.SILK_TOUCH;
    }
}
