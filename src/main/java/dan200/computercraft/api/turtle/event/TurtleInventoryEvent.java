/*
 * This file is part of the public ComputerCraft API - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. This API may be redistributed unmodified and in full only.
 * For help using the API, and posting your mods, visit the forums at computercraft.info.
 */
package dan200.computercraft.api.turtle.event;

import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Fired when a turtle attempts to interact with an inventory.
 */
public abstract class TurtleInventoryEvent extends TurtleBlockEvent {
    private final Container handler;

    protected TurtleInventoryEvent(@NotNull ITurtleAccess turtle, @NotNull TurtleAction action, @NotNull World world,
                                   @NotNull TilePosc pos, @Nullable Container handler) {
        super(turtle, action, world, pos);
        this.handler = handler;
    }

    /**
     * Get the inventory being interacted with.
     *
     * @return The inventory being interacted with, {@code null} if the item will be dropped to/sucked from the world.
     */
    @Nullable
    public Container getItemHandler() {
        return handler;
    }

    /**
     * Fired when a turtle attempts to suck from an inventory.
     *
     * @see TurtleAction#SUCK
     */
    public static class Suck extends TurtleInventoryEvent {
        public Suck(@NotNull ITurtleAccess turtle, @NotNull World world, @NotNull TilePosc pos, @Nullable Container handler) {
            super(turtle, TurtleAction.SUCK, world, pos, handler);
        }
    }

    /**
     * Fired when a turtle attempts to drop an item into an inventory.
     *
     * @see TurtleAction#DROP
     */
    public static class Drop extends TurtleInventoryEvent {
        private final ItemStack stack;

        public Drop(@NotNull ITurtleAccess turtle, @NotNull World world, @NotNull TilePosc pos, @Nullable Container handler,
                    @NotNull ItemStack stack) {
            super(turtle, TurtleAction.DROP, world, pos, handler);

            Objects.requireNonNull(stack, "stack cannot be null");
            this.stack = stack;
        }

        /**
         * The item which will be inserted into the inventory/dropped on the ground.
         *
         * @return The item stack which will be dropped. This should <b>not</b> be modified.
         */
        @NotNull
        public ItemStack getStack() {
            return stack;
        }
    }
}
