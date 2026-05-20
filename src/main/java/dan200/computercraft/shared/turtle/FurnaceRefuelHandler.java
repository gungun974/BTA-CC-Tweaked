/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle;

import com.google.common.eventbus.Subscribe;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.event.TurtleRefuelEvent;
import dan200.computercraft.shared.util.InventoryUtil;
import dan200.computercraft.shared.util.ItemStorage;
import dan200.computercraft.shared.util.WorldUtil;
import net.minecraft.core.crafting.LookupFuelFurnace;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemBucket;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class FurnaceRefuelHandler implements TurtleRefuelEvent.Handler {
    public static final FurnaceRefuelHandler INSTANCE = new FurnaceRefuelHandler();

    private FurnaceRefuelHandler() {
    }

    @Subscribe
    public static void onTurtleRefuel(TurtleRefuelEvent event) {
        if (event.getHandler() == null && getFuelPerItem(event.getStack()) > 0) {
            event.setHandler(INSTANCE);
        }
    }

    private static int getFuelPerItem(@NotNull ItemStack stack) {
        int burnTime = LookupFuelFurnace.instance.getFuelYield(stack.getItem().id);
        return (burnTime * 5) / 100;
    }

    @Override
    public int refuel(@NotNull ITurtleAccess turtle, @NotNull ItemStack currentStack, int slot, int limit) {
        ItemStorage storage = ItemStorage.wrap(turtle.getInventory());
        ItemStack stack = storage.take(slot, limit, null, false);
        int fuelToGive = getFuelPerItem(stack) * stack.stackSize;

        // Store the replacement item in the inventory
        Item replacementStack = stack.getItem();

        if (replacementStack instanceof ItemBucket) {
            if (ItemBucket.getState(stack) == ItemBucket.STATE_LAVA) {
                ItemBucket.setState(stack, ItemBucket.STATE_EMPTY);
            }
        } else {
            currentStack.stackSize--;
            if (currentStack.stackSize - limit - 1 <= 0) {
                replacementStack = null;
            }
        }

        if (replacementStack != null) {
            ItemStack remainder = InventoryUtil.storeItems(new ItemStack(replacementStack), storage, turtle.getSelectedSlot());
            if (remainder != null) {
                WorldUtil.dropItemStack(remainder,
                    turtle.getWorld(),
                    turtle.getPosition(),
                    turtle.getDirection()
                        .opposite());
            }
        }

        return fuelToGive;
    }
}
