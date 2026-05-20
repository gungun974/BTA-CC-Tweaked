/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.media.items;

import dan200.computercraft.api.media.IMedia;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemDiscMusic;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of IMedia for ItemRecords.
 */
public final class RecordMedia implements IMedia {
    public static final RecordMedia INSTANCE = new RecordMedia();

    private RecordMedia() {
    }

    @Override
    public String getLabel(@NotNull ItemStack stack) {
        return getAudioTitle(stack);
    }

    @Override
    public String getAudioTitle(@NotNull ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemDiscMusic)) {
            return null;
        }

        return ((ItemDiscMusic) item).recordName;
    }

    @Override
    public String getAudio(@NotNull ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemDiscMusic)) {
            return null;
        }

        return ((ItemDiscMusic) item).recordName;
    }
}
