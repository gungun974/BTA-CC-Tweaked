/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.util;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class InventoryUtil {
    private InventoryUtil() {
    }
    // Methods for comparing things:

    public static boolean areItemsStackable(@Nullable ItemStack a, @Nullable ItemStack b) {
        if (a == null) {
            return false;
        }
        if (b == null) {
            return false;
        }
        return a == b || (a.getItem().equals(b.getItem()) && a.getData().equals(b.getData()));
    }

    // Methods for finding inventories:

    public static Container getInventory(World world, BlockPos pos, Direction side) {
        // Look for tile with inventory
        int y = pos.getY();
        if (y >= 0 && y < World.HEIGHT_BLOCKS) {
            TileEntity tileEntity = world.getTileEntity(pos.x, pos.y, pos.z);

            Container inventory = getInventory(tileEntity);
            return inventory;
        }

        return null;
    }

    public static Container getInventory(TileEntity tileEntity) {
        if (tileEntity instanceof Container) {
            Container inventory = (Container) tileEntity;
            return inventory;
        }

        return null;
    }

    public static ItemStack storeItems(@Nonnull ItemStack itemstack, ItemStorage inventory, int begin) {
        return storeItems(itemstack, inventory, 0, inventory.size(), begin);
    }

    // Methods for placing into inventories:

    public static ItemStack storeItems(ItemStack stack, ItemStorage inventory, int start, int range, int begin) {
        if (stack == null) {
            return null;
        }

        // Inspect the slots in order and try to find empty or stackable slots
        ItemStack remainder = stack.copy();
        for (int i = 0; i < range; i++) {
            int slot = start + (i + begin - start) % range;
            if (remainder == null) {
                break;
            }
            remainder = inventory.store(slot, remainder, false);
        }
        return areItemsEqual(stack, remainder) ? stack : remainder;
    }

    public static boolean areItemsEqual(ItemStack a, ItemStack b) {
        return a == b || ItemStack.areItemStacksEqual(a, b);
    }

    public static ItemStack storeItems(@Nonnull ItemStack itemstack, ItemStorage inventory) {
        return storeItems(itemstack, inventory, 0, inventory.size(), 0);
    }

    // Methods for taking out of inventories

    @Nonnull
    public static ItemStack takeItems(int count, ItemStorage inventory, int begin) {
        return takeItems(count, inventory, 0, inventory.size(), begin);
    }

    public static ItemStack takeItems(int count, ItemStorage inventory, int start, int range, int begin) {
        ItemStack partialStack = null;
        for (int i = 0; i < range; i++) {
            int slot = start + (i + begin - start) % range;

            if (count <= 0) {
                break;
            }

            // If this doesn't slot, abort.
            ItemStack extracted = inventory.take(slot, count, partialStack, false);
            if (extracted == null) {
                continue;
            }

            count -= extracted.stackSize;
            if (partialStack == null) {
                // If we've extracted for this first time, then limit the count to the maximum stack size.
                partialStack = extracted;
                count = Math.min(count, extracted.getMaxStackSize());
            } else {
                partialStack.stackSize += extracted.stackSize;
            }
        }

        return partialStack;
    }

    public static ItemStack takeItems(int count, ItemStorage inventory) {
        return takeItems(count, inventory, 0, inventory.size(), 0);
    }
}
