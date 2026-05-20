/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.core;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleCommand;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import net.minecraft.core.block.Block;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TurtleCompareCommand implements ITurtleCommand {
    private final InteractDirection direction;

    public TurtleCompareCommand(InteractDirection direction) {
        this.direction = direction;
    }

    @NotNull
    @Override
    public TurtleCommandResult execute(@NotNull ITurtleAccess turtle) {
        // Get world direction from direction
        Direction direction = this.direction.toWorldDir(turtle);

        // Get currently selected stack
        ItemStack selectedStack = turtle.getInventory()
            .getItem(turtle.getSelectedSlot());

        // Get stack representing thing in front
        World world = turtle.getWorld();
        TilePosc oldPosition = turtle.getPosition();
        TilePosc newPosition = oldPosition.add(direction, new TilePos());

        ItemStack lookAtStack = null;
        if (!world.isAirBlock(newPosition)) {
            Block<?> lookAtBlock = world.getBlockType(newPosition);
            // See if the block drops anything with the same ID as itself
            // (try 5 times to try and beat random number generators)
            for (int i = 0; i < 5 && lookAtStack == null; i++) {
                ItemStack[] drops = Objects.requireNonNull(lookAtBlock).getBreakResult(world, EnumDropCause.PICK_BLOCK, newPosition, world.getBlockData(newPosition), world.getTileEntity(newPosition));
                if (drops != null) {
                    for (ItemStack drop : drops) {
                        if (drop.getItem().equals(lookAtBlock.asItem())) {
                            lookAtStack = drop;
                            break;
                        }
                    }
                }
            }

            // Last resort: roll our own (which will probably be wrong)
            if (lookAtStack == null) {
                lookAtStack = new ItemStack(lookAtBlock);
            }
        }

        // Compare them
        return Objects.requireNonNull(selectedStack).getItem().equals(Objects.requireNonNull(lookAtStack).getItem()) ? TurtleCommandResult.success() : TurtleCommandResult.failure();
    }
}
