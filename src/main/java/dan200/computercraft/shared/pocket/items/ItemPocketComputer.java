/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.pocket.items;

import com.google.common.base.Objects;
import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.ComputerCraft;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.filesystem.IMount;
import dan200.computercraft.api.media.IMedia;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.core.computer.ComputerSide;
import dan200.computercraft.fabric.Helper;
import dan200.computercraft.fabric.IComputerPlayer;
import dan200.computercraft.shared.PocketUpgrades;
import dan200.computercraft.shared.common.IColouredItem;
import dan200.computercraft.shared.computer.core.ClientComputer;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.core.ComputerState;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.computer.inventory.ContainerComputer;
import dan200.computercraft.shared.computer.items.IComputerItem;
import dan200.computercraft.shared.pocket.apis.PocketAPI;
import dan200.computercraft.shared.pocket.core.PocketServerComputer;
import dan200.computercraft.shared.pocket.inventory.ScreenPocketComputer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemPocketComputer extends Item implements IComputerItem, IMedia, IColouredItem {
    public static final String NBT_LIGHT = "Light";
    private static final String NBT_UPGRADE = "Upgrade";
    private static final String NBT_UPGRADE_INFO = "UpgradeInfo";
    private static final String NBT_INSTANCE = "Instanceid";
    private static final String NBT_SESSION = "SessionId";

    private final ComputerFamily family;

    public ItemPocketComputer(NamespaceID namespaceId, int id, ComputerFamily family) {
        super(namespaceId, id);
        this.family = family;
    }

    public static ServerComputer getServerComputer(@Nonnull ItemStack stack) {
        int session = getSessionID(stack);
        if (session != ComputerCraft.serverComputerRegistry.getSessionID()) return null;

        int instanceID = getInstanceID(stack);
        return instanceID >= 0 ? ComputerCraft.serverComputerRegistry.get(instanceID) : null;
    }

    @Environment(EnvType.CLIENT)
    public static ComputerState getState(@Nonnull ItemStack stack) {
        ClientComputer computer = getClientComputer(stack);
        return computer == null ? ComputerState.OFF : computer.getState();
    }

    private static ClientComputer getClientComputer(@Nonnull ItemStack stack) {
        int instanceID = getInstanceID(stack);
        return instanceID >= 0 ? ComputerCraft.clientComputerRegistry.get(instanceID) : null;
    }

    @Environment(EnvType.CLIENT)
    public static int getLightState(@Nonnull ItemStack stack) {
        ClientComputer computer = getClientComputer(stack);
        if (computer != null && computer.isOn()) {
            CompoundTag computerNBT = computer.getUserData();
            if (computerNBT != null && computerNBT.containsKey(NBT_LIGHT)) {
                return computerNBT.getInteger(NBT_LIGHT);
            }
        }
        return -1;
    }

    public static void setUpgrade(@Nonnull ItemStack stack, IPocketUpgrade upgrade) {
        CompoundTag compound = stack.getData();

        if (upgrade == null) {
            compound.getValue().remove(NBT_UPGRADE);
        } else {
            compound.putInt(NBT_UPGRADE,
                upgrade.getUpgradeID());
        }

        compound.getValue().remove(NBT_UPGRADE_INFO);

        stack.setData(compound);
    }

    public static CompoundTag getUpgradeInfo(@Nonnull ItemStack stack) {
        return stack.getData();
        //return stack.getOrCreateSubTag( NBT_UPGRADE_INFO );
    }

    //    @Nullable
    //    @Override
    //    public String getCreatorModId( ItemStack stack )
    //    {
    //        IPocketUpgrade upgrade = getUpgrade( stack );
    //        if( upgrade != null )
    //        {
    //            // If we're a non-vanilla, non-CC upgrade then return whichever mod this upgrade
    //            // belongs to.
    //            String mod = PocketUpgrades.getOwner( upgrade );
    //            if( mod != null && !mod.equals( ComputerCraft.MOD_ID ) ) return mod;
    //        }
    //
    //        return super.getCreatorModId( stack );
    //    }

    public static IPocketUpgrade getUpgrade(@Nonnull ItemStack stack) {
        CompoundTag compound = stack.getData();
        return compound.containsKey(NBT_UPGRADE) ? PocketUpgrades.get(compound.getInteger(NBT_UPGRADE)) : null;

    }

    private static void setComputerID(@Nonnull ItemStack stack, int computerID) {
        CompoundTag tag = stack.getData();
        tag.putInt(NBT_ID, computerID);
        stack.setData(tag);
    }

    public static ClientComputer createClientComputer(@Nonnull ItemStack stack) {
        int instanceID = getInstanceID(stack);
        if (instanceID >= 0) {
            if (!ComputerCraft.clientComputerRegistry.contains(instanceID)) {
                ComputerCraft.clientComputerRegistry.add(instanceID, new ClientComputer(instanceID));
            }
            return ComputerCraft.clientComputerRegistry.get(instanceID);
        }
        return null;
    }

    private static int getInstanceID(@Nonnull ItemStack stack) {
        CompoundTag nbt = stack.getData();
        return nbt.containsKey(NBT_INSTANCE) ? nbt.getInteger(NBT_INSTANCE) : -1;
    }


    // IComputerItem implementation

    private static int getSessionID(@Nonnull ItemStack stack) {
        CompoundTag nbt = stack.getData();
        return nbt.containsKey(NBT_SESSION) ? nbt.getInteger(NBT_SESSION) : -1;
    }

    private static void setInstanceID(@Nonnull ItemStack stack, int instanceID) {
        CompoundTag tag = stack.getData();
        tag.putInt(NBT_INSTANCE, instanceID);
        stack.setData(tag);
    }

    private static void setSessionID(@Nonnull ItemStack stack, int sessionID) {
        CompoundTag tag = stack.getData();
        tag.putInt(NBT_SESSION, sessionID);
        stack.setData(tag);
    }

    @Override
    public ItemStack onUseItem(ItemStack itemstack, World world, Player player) {
        ItemStack stack = player.getHeldItem();
        if (!Helper.isClientWorld()) {
            PocketServerComputer computer = createServerComputer(world, player.inventory, player, stack);

            boolean stop = false;
            if (computer != null) {
                computer.turnOn();

                IPocketUpgrade upgrade = getUpgrade(stack);
                if (upgrade != null) {
                    computer.updateValues(player, stack, upgrade);
                    stop = upgrade.onRightClick(world, computer, computer.getPeripheral(ComputerSide.BACK));
                }
            }

            if (!stop && computer != null) {
                computer.sendTerminalState(player);
                ((IComputerPlayer) player).setCurrentContainerComputer(new ContainerComputer(this, world, player.inventory, player, stack));
                computer.sendOpenComputerGui(player, ScreenPocketComputer.class);
            }
        }
        return stack;
    }

    // IMedia

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, World world, @Nonnull Entity entity, int slotNum, boolean selected) {
        if (!Helper.isClientWorld()) {
            // Server side
            ContainerInventory inventory = entity instanceof Player ? ((Player) entity).inventory : null;
            PocketServerComputer computer = createServerComputer(world, inventory, entity, stack);
            if (computer != null) {
                IPocketUpgrade upgrade = getUpgrade(stack);

                // Ping computer
                computer.keepAlive();
                computer.setWorld(world);
                computer.updateValues(entity, stack, upgrade);

                // Sync ID
                int id = computer.getID();
                if (id != getComputerID(stack)) {
                    setComputerID(stack, id);
                    if (inventory != null) {
                        inventory.setChanged();
                    }
                }

                // Sync label
                String label = computer.getLabel();
                if (!Objects.equal(label, getLabel(stack))) {
                    setLabel(stack, label);
                    if (inventory != null) {
                        inventory.setChanged();
                    }
                }

                // Update pocket upgrade
                if (upgrade != null) {
                    upgrade.update(computer, computer.getPeripheral(ComputerSide.BACK));
                }
            }
        } else {
            // Client side
            createClientComputer(stack);
        }
    }

    @Override
    public String getTranslatedName(ItemStack stack) {
        I18n i18n = I18n.getInstance();
        String baseString = stack.getItemKey();
        IPocketUpgrade upgrade = getUpgrade(stack);
        if (upgrade != null) {
            return i18n.translateKeyAndFormat(baseString + ".upgraded.name", i18n.translateKey(upgrade.getUnlocalisedAdjective()));
        } else {
            return super.getTranslatedName(stack);
        }
    }

    @Override
    public String getTranslatedDescription(ItemStack stack) {
        I18n i18n = I18n.getInstance();

        String text = super.getTranslatedDescription(stack);

        if (getLabel(stack) == null) {
            int id = getComputerID(stack);
            if (id >= 0) {
                text += "\n" + i18n.translateKeyAndFormat("gui.computercraft.tooltip.computer_id", id);
            }
        }

        return text;
    }

    public void addToCreativeMenu(@Nonnull List<ItemStack> stacks) {
        stacks.add(create(-1, null, -1, null));
        for (IPocketUpgrade upgrade : PocketUpgrades.getVanillaUpgrades()) {
            stacks.add(create(-1, null, -1, upgrade));
        }
    }

    public ItemStack create(int id, String label, int colour, IPocketUpgrade upgrade) {
        ItemStack result = new ItemStack(this);

        CompoundTag tag = result.getData();

        if (id >= 0) {
            tag
                .putInt(NBT_ID, id);
        }
        if (label != null) {
            result.setCustomName(label);
        }
        if (upgrade != null) {
            tag
                .putInt(NBT_UPGRADE,
                    upgrade.getUpgradeID()
                );
        }
        if (colour != -1) {
            tag
                .putInt(NBT_COLOUR, colour);
        }

        result.setData(tag);

        return result;
    }

    public PocketServerComputer createServerComputer(final World world, ContainerInventory inventory, Entity entity, @Nonnull ItemStack stack) {
        if (Helper.isClientWorld()) {
            return null;
        }

        PocketServerComputer computer;
        int instanceID = getInstanceID(stack);
        int sessionID = getSessionID(stack);
        int correctSessionID = ComputerCraft.serverComputerRegistry.getSessionID();

        if (instanceID >= 0 && sessionID == correctSessionID && ComputerCraft.serverComputerRegistry.contains(instanceID)) {
            computer = (PocketServerComputer) ComputerCraft.serverComputerRegistry.get(instanceID);
        } else {
            if (instanceID < 0 || sessionID != correctSessionID) {
                instanceID = ComputerCraft.serverComputerRegistry.getUnusedInstanceID();
                setInstanceID(stack, instanceID);
                setSessionID(stack, correctSessionID);
            }
            int computerID = getComputerID(stack);
            if (computerID < 0) {
                computerID = ComputerCraftAPI.createUniqueNumberedSaveDir(world, "computer");
                setComputerID(stack, computerID);
            }
            computer = new PocketServerComputer(world, computerID, getLabel(stack), instanceID, getFamily());
            computer.updateValues(entity, stack, getUpgrade(stack));
            computer.addAPI(new PocketAPI(computer));
            ComputerCraft.serverComputerRegistry.add(instanceID, computer);
            if (inventory != null) {
                inventory.setChanged();
            }
        }
        computer.setWorld(world);
        return computer;
    }

    @Override
    public String getLabel(@Nonnull ItemStack stack) {
        return IComputerItem.super.getLabel(stack);
    }

    @Override
    public ComputerFamily getFamily() {
        return family;
    }

    @Override
    public ItemStack withFamily(@Nonnull ItemStack stack, @Nonnull ComputerFamily family) {
        return PocketComputerItemFactory.create(getComputerID(stack), getLabel(stack), getColour(stack), family, getUpgrade(stack));
    }

    @Override
    public boolean setLabel(@Nonnull ItemStack stack, String label) {
        if (label != null) {
            stack.setCustomName(label);
        } else {
            stack.removeCustomName();
        }
        return true;
    }

    @Override
    public IMount createDataMount(@Nonnull ItemStack stack, @Nonnull World world) {
        int id = getComputerID(stack);
        if (id >= 0) {
            return ComputerCraftAPI.createSaveDirMount(world, "computer/" + id, ComputerCraft.computerSpaceLimit);
        }
        return null;
    }
}
