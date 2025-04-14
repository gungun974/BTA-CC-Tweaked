/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.pocket.core;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.ComputerCraft;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.pocket.IPocketAccess;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.core.computer.ComputerSide;
import dan200.computercraft.shared.common.IColouredItem;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.pocket.items.ItemPocketComputer;
import dan200.computercraft.shared.util.BlockPos;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.world.World;
import turniplabs.halplibe.helper.network.NetworkHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;

import static dan200.computercraft.shared.pocket.items.ItemPocketComputer.NBT_LIGHT;

public class PocketServerComputer extends ServerComputer implements IPocketAccess {
    private IPocketUpgrade upgrade;
    private Entity entity;
    private ItemStack stack;

    public PocketServerComputer(World world, int computerID, String label, int instanceID, ComputerFamily family) {
        super(world, computerID, label, instanceID, family, ComputerCraft.pocketTermWidth, ComputerCraft.pocketTermHeight);
    }

    @Nullable
    @Override
    public Entity getEntity() {
        Entity entity = this.entity;
        if (entity == null || stack == null || !entity.isAlive()) {
            return null;
        }

        if (entity instanceof Player) {
            ContainerInventory inventory = ((Player) entity).inventory;
            for (ItemStack itemStack : inventory.mainInventory) {
                if (itemStack == stack) {
                    return entity;
                }
            }
            return null;
        }
        if (entity instanceof Mob) {
            Mob living = (Mob) entity;
            return living.getHeldItem() == stack ? entity : null;
        }

        return null;
    }

    @Override
    public int getColour() {
        return IColouredItem.getColourBasic(stack);
    }

    @Override
    public void setColour(int colour) {
        IColouredItem.setColourBasic(stack, colour);
        updateUpgradeNBTData();
    }

    @Override
    public int getLight() {
        CompoundTag tag = getUserData();
        return tag.containsKey(NBT_LIGHT) ? tag.getInteger(NBT_LIGHT) : -1;
    }

    @Override
    public void setLight(int colour) {
        CompoundTag tag = getUserData();
        if (colour >= 0 && colour <= 0xFFFFFF) {
            if (!tag.containsKey(NBT_LIGHT) || tag.getInteger(NBT_LIGHT) != colour) {
                tag.putInt(NBT_LIGHT, colour);
                updateUserData();
            }
        } else if (tag.containsKey(NBT_LIGHT)) {
            tag.getValue().remove(NBT_LIGHT);
            updateUserData();
        }
    }

    @Nonnull
    @Override
    public CompoundTag getUpgradeNBTData() {
        return ItemPocketComputer.getUpgradeInfo(stack);
    }

    @Override
    public void updateUpgradeNBTData() {
        if (entity instanceof Player) {
            ((Player) entity).inventory.setChanged();
        }
    }

    @Override
    public void invalidatePeripheral() {
        IPeripheral peripheral = upgrade == null ? null : upgrade.createPeripheral(this);
        setPeripheral(ComputerSide.BACK, peripheral);
    }

    @Nonnull
    @Override
    public Map<Integer, IPeripheral> getUpgrades() {
        return upgrade == null ? Collections.emptyMap() : Collections.singletonMap(upgrade.getUpgradeID(), getPeripheral(ComputerSide.BACK));
    }

    public IPocketUpgrade getUpgrade() {
        return upgrade;
    }

    /**
     * Set the upgrade for this pocket computer, also updating the item stack.
     * <p>
     * Note this method is not thread safe - it must be called from the server thread.
     *
     * @param upgrade The new upgrade to set it to, may be {@code null}.
     */
    public void setUpgrade(IPocketUpgrade upgrade) {
        if (this.upgrade == upgrade) {
            return;
        }

        synchronized (this) {
            ItemPocketComputer.setUpgrade(stack, upgrade);
            updateUpgradeNBTData();
            this.upgrade = upgrade;
            invalidatePeripheral();
        }
    }

    public synchronized void updateValues(Entity entity, @Nonnull ItemStack stack, IPocketUpgrade upgrade) {
        if (entity != null) {
            setWorld(entity.world);
            setPosition(new BlockPos((int) entity.x, (int) entity.y, (int) entity.z));
        }

        // If a new entity has picked it up then rebroadcast the terminal to them
        if (entity != this.entity && entity instanceof Player) {
            markTerminalChanged();
        }

        this.entity = entity;
        this.stack = stack;

        if (this.upgrade != upgrade) {
            this.upgrade = upgrade;
            invalidatePeripheral();
        }
    }

    @Override
    public void broadcastState(boolean force) {
        super.broadcastState(force);

        if ((hasTerminalChanged() || force) && entity instanceof Player) {
            // Broadcast the state to the current entity if they're not already interacting with it.
            Player player = (Player) entity;
            if (!isInteracting(player)) {
                NetworkHandler.sendToPlayer(player, createTerminalPacket());
            }
        }
    }
}
