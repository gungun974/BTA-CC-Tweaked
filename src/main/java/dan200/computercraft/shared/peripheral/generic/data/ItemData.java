/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.generic.data;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import dan200.computercraft.shared.util.NBTUtil;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.item.tag.ItemTags;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Data providers for items.
 */
public class ItemData {
    @Nonnull
    public static <T extends Map<? super String, Object>> T fillBasicSafe(@Nonnull T data, @Nonnull ItemStack stack) {
        data.put("id", DataHelpers.getId(stack.getItem()));
        data.put("metadata", stack.getMetadata());
        data.put("name", stack.getItem().namespaceID.toString());
        data.put("count", stack.stackSize);

        return data;
    }

    @Nonnull
    public static <T extends Map<? super String, Object>> T fillBasic(@Nonnull T data, @Nonnull ItemStack stack) {
        fillBasicSafe(data, stack);
        String hash = NBTUtil.getNBTHash(stack.getData());
        if (hash != null) data.put("nbt", hash);

        return data;
    }

    @Nonnull
    public static <T extends Map<? super String, Object>> T fill(@Nonnull T data, ItemStack stack) {
        if (stack == null) return data;

        fillBasic(data, stack);

        if (stack.hasCustomName()) {
            data.put("displayName", stack.getCustomName());
        } else {
            data.put("displayName", stack.getDisplayName());
        }
        data.put("maxCount", stack.getMaxStackSize());

        if (stack.isItemStackDamageable()) {
            data.put("damage", stack.getItemDamageForDisplay());
            data.put("maxDamage", stack.getMaxDamage());
        }

        if (stack.isItemDamaged()) {
            data.put("durability", (double) stack.getItemDamageForDisplay() / stack.getMaxDamage());
        }

        Map<Object, Object> tagsTable = new HashMap<>();

        Item item = stack.getItem();

        for (Tag<Item> itemTag : ItemTags.TAG_LIST) {
            if (item.hasTag(itemTag)) {
                tagsTable.put(itemTag.getName(), true);
            }
        }

        if (item instanceof ItemBlock) {
            for (Tag<Block<?>> blockTag : BlockTags.TAG_LIST) {
                if (((ItemBlock<?>) item).getBlock().hasTag(blockTag)) {
                    tagsTable.put(blockTag.getName(), true);
                }
            }
        }

        data.put( "tags", tagsTable );

        Map<Object, Object> groupsTable = new HashMap<>();

        for (List<ItemStack> itemGroup : Registries.ITEM_GROUPS) {
            for (ItemStack itemStack : itemGroup) {
                if (itemStack.getItem().equals(item)) {
                    String key = Registries.ITEM_GROUPS.getKey(itemGroup);
                    groupsTable.put(key, true);
                }
            }
        }

        data.put( "groups", groupsTable );

        CompoundTag tag = stack.getData();
        if (tag.containsKey("display")) {
            CompoundTag displayTag = tag.getCompound("display");
            if (displayTag.containsKey("Lore")) {
                ListTag loreTag = displayTag.getList("Lore");
                data.put("lore", loreTag.getValue().stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .collect(Collectors.toList()));
            }
        }

        /*
         * Used to hide some data from ItemStack tooltip.
         * @see https://minecraft.gamepedia.com/Tutorials/Command_NBT_tags
         * @see ItemStack#getTooltip
         */
        int hideFlags = tag.getInteger("HideFlags");

        if (tag.getBoolean("Unbreakable") && (hideFlags & 4) == 0) {
            data.put("unbreakable", true);
        }

        return data;
    }
}
