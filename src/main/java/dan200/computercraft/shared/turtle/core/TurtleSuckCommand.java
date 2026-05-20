/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.core;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleCommand;
import dan200.computercraft.api.turtle.TurtleAnimation;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.event.TurtleEvent;
import dan200.computercraft.api.turtle.event.TurtleInventoryEvent;
import dan200.computercraft.shared.util.InventoryUtil;
import dan200.computercraft.shared.util.ItemStorage;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.joml.primitives.AABBd;

import java.util.List;

public class TurtleSuckCommand implements ITurtleCommand {
    private final InteractDirection direction;
    private final int quantity;

    public TurtleSuckCommand(InteractDirection direction, int quantity) {
        this.direction = direction;
        this.quantity = quantity;
    }

    @NotNull
    @Override
    public TurtleCommandResult execute(@NotNull ITurtleAccess turtle) {
        // Sucking nothing is easy
        if (quantity == 0) {
            turtle.playAnimation(TurtleAnimation.WAIT);
            return TurtleCommandResult.success();
        }

        // Get world direction from direction
        Direction direction = this.direction.toWorldDir(turtle);

        // Get inventory for thing in front
        World world = turtle.getWorld();
        TilePosc turtlePosition = turtle.getPosition();
        TilePosc blockPosition = turtlePosition.add(direction, new TilePos());
        Direction side = direction.opposite();

        Container inventory = InventoryUtil.getInventory(world, blockPosition, side);

        // Fire the event, exiting if it is cancelled.
        TurtleInventoryEvent.Suck event = new TurtleInventoryEvent.Suck(turtle, world, blockPosition, inventory);
        if (TurtleEvent.post(event)) {
            return TurtleCommandResult.failure(event.getFailureMessage());
        }

        if (inventory != null) {
            // Take from inventory of thing in front
            ItemStack stack = InventoryUtil.takeItems(quantity, ItemStorage.wrap(inventory));
            if (stack == null) {
                return TurtleCommandResult.failure("No items to take");
            }

            // Try to place into the turtle
            ItemStack remainder = InventoryUtil.storeItems(stack, turtle.getItemHandler(), turtle.getSelectedSlot());
            if (remainder != null) {
                // Put the remainder back in the inventory
                InventoryUtil.storeItems(remainder, ItemStorage.wrap(inventory));
            }

            // Return true if we consumed anything
            if (remainder != stack) {
                turtle.playAnimation(TurtleAnimation.WAIT);
                return TurtleCommandResult.success();
            } else {
                return TurtleCommandResult.failure("No space for items");
            }
        } else {
            // Suck up loose items off the ground
            AABBd aabb = new AABBd(blockPosition.x(),
                blockPosition.y(),
                blockPosition.z(),
                blockPosition.x() + 1.0,
                blockPosition.y() + 1.0,
                blockPosition.z() + 1.0);
            List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, aabb);
            if (list.isEmpty()) {
                return TurtleCommandResult.failure("No items to take");
            }

            for (EntityItem entity : list) {
                // Suck up the item
                ItemStack stack = entity.item
                    .copy();

                ItemStack storeStack;
                ItemStack leaveStack;
                if (stack.stackSize > quantity) {
                    storeStack = stack.splitStack(quantity);
                    leaveStack = stack;
                } else {
                    storeStack = stack;
                    leaveStack = null;
                }

                ItemStack remainder = InventoryUtil.storeItems(storeStack, turtle.getItemHandler(), turtle.getSelectedSlot());

                if (remainder != storeStack) {
                    if (remainder == null && leaveStack == null) {
                        entity.remove();
                    } else if (remainder == null) {
                        entity.item = leaveStack;
                    } else if (leaveStack == null) {
                        entity.item = remainder;
                    } else {
                        leaveStack.stackSize += remainder.stackSize;
                        entity.item = leaveStack;
                    }

                    // Play fx
                    world.playBlockEvent(turtlePosition, 1000, 0); // BLOCK_DISPENSER_DISPENSE
                    turtle.playAnimation(TurtleAnimation.WAIT);
                    return TurtleCommandResult.success();
                }
            }


            return TurtleCommandResult.failure("No space for items");
        }
    }
}
