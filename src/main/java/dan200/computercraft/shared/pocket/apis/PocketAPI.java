/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.pocket.apis;

import dan200.computercraft.BlockPos;
import dan200.computercraft.api.lua.ILuaAPI;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.shared.PocketUpgrades;
import dan200.computercraft.shared.pocket.core.PocketServerComputer;
import dan200.computercraft.shared.util.InventoryUtil;
import dan200.computercraft.shared.util.ItemStorage;
import dan200.computercraft.shared.util.WorldUtil;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;

/**
 * Control the current pocket computer, adding or removing upgrades.
 * <p>
 * This API is only available on pocket computers. As such, you may use its presence to determine what kind of computer you are using:
 *
 * <pre>
 * if pocket then
 *   print("On a pocket computer")
 * else
 *   print("On something else")
 * end
 * </pre>
 *
 * @cc.module pocket
 */
public class PocketAPI implements ILuaAPI {
    private final PocketServerComputer computer;

    public PocketAPI(PocketServerComputer computer) {
        this.computer = computer;
    }

    private static IPocketUpgrade findUpgrade(ItemStack[] inv, int start, IPocketUpgrade previous) {
        for (int i = 0; i < inv.length; i++) {
            ItemStack invStack = inv[(i + start) % inv.length];
            if (invStack != null) {
                IPocketUpgrade newUpgrade = PocketUpgrades.get(invStack);

                if (newUpgrade != null && newUpgrade != previous) {
                    // Consume an item from this stack and exit the loop
                    invStack = invStack.copy();
                    invStack.stackSize -= 1;
                    inv[(i + start) % inv.length] = invStack.stackSize == 0 ? null : invStack;

                    return newUpgrade;
                }
            }
        }

        return null;
    }

    @Override
    public String[] getNames() {
        return new String[]{"pocket"};
    }

    /**
     * Search the player's inventory for another upgrade, replacing the existing one with that item if found.
     * <p>
     * This inventory search starts from the player's currently selected slot, allowing you to prioritise upgrades.
     *
     * @return The result of equipping.
     * @cc.treturn boolean If an item was equipped.
     * @cc.treturn string|nil The reason an item was not equipped.
     */
    @LuaFunction(mainThread = true)
    public final Object[] equipBack() {
        Entity entity = computer.getEntity();
        if (!(entity instanceof Player)) {
            return new Object[]{false, "Cannot find player"};
        }
        Player player = (Player) entity;
        ContainerInventory inventory = player.inventory;
        IPocketUpgrade previousUpgrade = computer.getUpgrade();

        // Attempt to find the upgrade, starting in the main segment, and then looking in the opposite
        // one. We start from the position the item is currently in and loop round to the start.
        IPocketUpgrade newUpgrade = findUpgrade(inventory.mainInventory, inventory.getCurrentItemIndex(), previousUpgrade);
        if (newUpgrade == null) {
            return new Object[]{false, "Cannot find a valid upgrade"};
        }

        // Remove the current upgrade
        if (previousUpgrade != null) {
            ItemStack stack = previousUpgrade.getCraftingItem();
            if (stack != null) {
                stack = InventoryUtil.storeItems(stack, ItemStorage.wrap(inventory), inventory.getCurrentItemIndex());
                if (stack != null) {
                    WorldUtil.dropItemStack(stack, player.world, new BlockPos((int) player.x, (int) player.y, (int) player.z));
                }
            }
        }

        // Set the new upgrade
        computer.setUpgrade(newUpgrade);

        return new Object[]{true};
    }

    /**
     * Remove the pocket computer's current upgrade.
     *
     * @return The result of unequipping.
     * @cc.treturn boolean If the upgrade was unequipped.
     * @cc.treturn string|nil The reason an upgrade was not unequipped.
     */
    @LuaFunction(mainThread = true)
    public final Object[] unequipBack() {
        Entity entity = computer.getEntity();
        if (!(entity instanceof Player)) {
            return new Object[]{false, "Cannot find player"};
        }
        Player player = (Player) entity;
        ContainerInventory inventory = player.inventory;
        IPocketUpgrade previousUpgrade = computer.getUpgrade();

        if (previousUpgrade == null) {
            return new Object[]{false, "Nothing to unequip"};
        }

        computer.setUpgrade(null);

        ItemStack stack = previousUpgrade.getCraftingItem();
        if (stack != null) {
            stack = InventoryUtil.storeItems(stack, ItemStorage.wrap(inventory), inventory.getCurrentItemIndex());
            if (stack != null) {
                WorldUtil.dropItemStack(stack, player.world, new BlockPos((int) player.x, (int) player.y, (int) player.z));
            }
        }

        return new Object[]{true};
    }
}
