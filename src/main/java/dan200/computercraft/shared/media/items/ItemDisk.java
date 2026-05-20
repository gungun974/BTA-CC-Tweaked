/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.media.items;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.ComputerCraft;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.filesystem.IMount;
import dan200.computercraft.api.media.IMedia;
import dan200.computercraft.shared.common.ComputerCraftBlocks;
import dan200.computercraft.shared.common.ComputerCraftItems;
import dan200.computercraft.shared.common.IColouredItem;
import dan200.computercraft.shared.peripheral.diskdrive.TileDiskDrive;
import dan200.computercraft.shared.util.Colour;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.helper.EnvironmentHelper;

import java.util.List;

public class ItemDisk extends Item implements IMedia, IColouredItem {
    private static final String NBT_ID = "DiskId";

    public ItemDisk(@NotNull NamespaceID namespaceId, @NotNull String translationKey, int id) {
        super(namespaceId, translationKey, id);
    }

    @NotNull
    public static ItemStack createFromIDAndColour(int id, String label, int colour) {
        ItemStack stack = new ItemStack(ComputerCraftItems.DISK);
        setDiskID(stack, id);
        ComputerCraftItems.DISK.setLabel(stack, label);
        IColouredItem.setColourBasic(stack, colour);
        return stack;
    }

    private static void setDiskID(@NotNull ItemStack stack, int id) {
        if (id >= 0) {
            stack.getData()
                .putInt(NBT_ID, id);
        }
    }

    public static int getDiskID(@NotNull ItemStack stack) {
        CompoundTag nbt = stack.getData();
        return nbt.containsKey(NBT_ID) ? nbt.getInteger(NBT_ID) : -1;
    }

    @Override
    public boolean onUseOnBlock(
        @NotNull ItemStack selfStack, @NotNull World world, @Nullable Player player, @NotNull TilePosc blockPos, @NotNull Side side, double xHit, double yHit
    ) {
        if (world.getBlockType(blockPos).id() == ComputerCraftBlocks.DISK_DRIVE.id()) {
            if (!EnvironmentHelper.isClientWorld()) {
                return ((TileDiskDrive) world.getTileEntity(blockPos)).onInteracted(player, side, xHit, yHit);
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    @NotNull
    public String getTranslatedDescription(ItemStack stack) {
        I18n i18n = I18n.getInstance();

        String text = super.getTranslatedDescription(stack);

        if (getLabel(stack) == null) {
            int id = getDiskID(stack);
            if (id >= 0) {
                text += "\n" + i18n.translateKeyAndFormat("gui.computercraft.tooltip.disk_id", id);
            }
        }

        return text;
    }

    public void addToCreativeMenu(@NotNull List<ItemStack> list) {
        for (int colour = 0; colour < 16; colour++) {
            list.add(createFromIDAndColour(-1, null, Colour.VALUES[colour].getHex()));
        }
    }

    @Override
    public String getLabel(@NotNull ItemStack stack) {
        return stack.hasCustomName() ? stack.getCustomName() : null;
    }

    @Override
    public boolean setLabel(@NotNull ItemStack stack, String label) {
        if (label != null) {
            stack.setCustomName(label);
        } else {
            stack.removeCustomName();
        }
        return true;
    }

    @Override
    public IMount createDataMount(@NotNull ItemStack stack, @NotNull World world) {
        int diskID = getDiskID(stack);
        if (diskID < 0) {
            diskID = ComputerCraftAPI.createUniqueNumberedSaveDir(world, "disk");
            setDiskID(stack, diskID);
        }
        return ComputerCraftAPI.createSaveDirMount(world, "disk/" + diskID, ComputerCraft.floppySpaceLimit);
    }

    @Override
    public int getColour(@NotNull ItemStack stack) {
        int colour = IColouredItem.getColourBasic(stack);
        return colour == -1 ? Colour.WHITE.getHex() : colour;
    }
}
