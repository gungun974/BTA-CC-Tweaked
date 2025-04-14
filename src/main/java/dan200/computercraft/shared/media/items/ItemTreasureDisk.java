/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.media.items;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.filesystem.IMount;
import dan200.computercraft.api.media.IMedia;
import dan200.computercraft.core.filesystem.SubMount;
import dan200.computercraft.shared.common.ComputerCraftItems;
import dan200.computercraft.shared.util.Colour;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.world.World;

import javax.annotation.Nonnull;
import java.io.IOException;

public class ItemTreasureDisk extends Item implements IMedia {
    public static final String NBT_TITLE = "Title";
    public static final String NBT_COLOUR = "Colour";
    public static final String NBT_SUB_PATH = "SubPath";

    public ItemTreasureDisk(NamespaceID namespaceId, int id) {
        super(namespaceId, id);
    }

    public static ItemStack create(String subPath, int colourIndex) {
        ItemStack result = new ItemStack(ComputerCraftItems.TREASURE_DISK);
        CompoundTag nbt = result.getData();
        nbt.putString(NBT_SUB_PATH, subPath);

        int slash = subPath.indexOf('/');
        if (slash >= 0) {
            String author = subPath.substring(0, slash);
            String title = subPath.substring(slash + 1);
            nbt.putString(NBT_TITLE, "\"" + title + "\" by " + author);
        } else {
            nbt.putString(NBT_TITLE, "untitled");
        }
        nbt.putInt(NBT_COLOUR, Colour.values()[colourIndex].getHex());

        return result;
    }

    public static int getColour(@Nonnull ItemStack stack) {
        CompoundTag nbt = stack.getData();
        return nbt.containsKey(NBT_COLOUR) ? nbt.getInteger(NBT_COLOUR) : Colour.BLUE.getHex();
    }

    @Nonnull
    private static String getTitle(@Nonnull ItemStack stack) {
        CompoundTag nbt = stack.getData();
        return nbt.containsKey(NBT_TITLE) ? nbt.getString(NBT_TITLE) : "'alongtimeago' by dan200";
    }

    private static IMount getTreasureMount() {
        return ComputerCraftAPI.createResourceMount("computercraft", "lua/treasure");
    }

    @Nonnull
    private static String getSubPath(@Nonnull ItemStack stack) {
        CompoundTag nbt = stack.getData();
        return nbt.containsKey(NBT_SUB_PATH) ? nbt.getString(NBT_SUB_PATH) : "dan200/alongtimeago";
    }

    @Override
    public String getTranslatedDescription(ItemStack stack) {
        String text = super.getTranslatedDescription(stack);

        String label = getTitle(stack);
        if (!label.isEmpty()) {
            text += "\n" + label;
        }

        return text;
    }

    @Override
    public String getLabel(@Nonnull ItemStack stack) {
        return getTitle(stack);
    }

    @Override
    public IMount createDataMount(@Nonnull ItemStack stack, @Nonnull World world) {
        IMount rootTreasure = getTreasureMount();
        String subPath = getSubPath(stack);
        try {
            if (rootTreasure.exists(subPath)) {
                return new SubMount(rootTreasure, subPath);
            } else if (rootTreasure.exists("deprecated/" + subPath)) {
                return new SubMount(rootTreasure, "deprecated/" + subPath);
            } else {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }
}
