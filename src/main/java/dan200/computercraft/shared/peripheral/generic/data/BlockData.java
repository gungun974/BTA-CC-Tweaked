/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.generic.data;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.collection.NamespaceID;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BlockData {
    @Nonnull
    public static <T extends Map<? super String, Object>> T fill(@Nonnull T data, int id, int metadata, NamespaceID namespaceID) {
        data.put("id", id);
        data.put("metadata", metadata);
        data.put("name", namespaceID.toString());

        Block<?> block = Objects.requireNonNull(Blocks.getBlock(id));

        Map<Object, Object> tagsTable = new HashMap<>();

        for (Tag<Block<?>> blockTag : BlockTags.TAG_LIST) {
            if (block.hasTag(blockTag)) {
                tagsTable.put(blockTag.getName(), true);
            }
        }

        data.put( "tags", tagsTable );

        Map<Object, Object> groupsTable = new HashMap<>();

        for (List<ItemStack> itemGroup : Registries.ITEM_GROUPS) {
            for (ItemStack itemStack : itemGroup) {
                if (itemStack.getItem().equals(block.asItem())) {
                    String key = Registries.ITEM_GROUPS.getKey(itemGroup);
                    groupsTable.put(key, true);
                }
            }
        }

        data.put( "groups", groupsTable );

        return data;
    }
}
