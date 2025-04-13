/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.media.items;

import dan200.computercraft.api.media.IMedia;
import net.minecraft.client.sound.SoundEntry;
import net.minecraft.client.sound.SoundRepository;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemDiscMusic;
import net.minecraft.core.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * An implementation of IMedia for ItemRecords.
 */
public final class RecordMedia implements IMedia {
    public static final RecordMedia INSTANCE = new RecordMedia();

    private RecordMedia() {
    }

    @Override
    public String getLabel(@Nonnull ItemStack stack) {
        return getAudioTitle(stack);
    }

    @Override
    public String getAudioTitle(@Nonnull ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemDiscMusic)) {
            return null;
        }

        return ((ItemDiscMusic) item).recordName;
    }

    @Override
    public SoundEntry getAudio(@Nonnull ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemDiscMusic)) {
            return null;
        }

        return SoundRepository.SOUNDS.getSoundEntry(((ItemDiscMusic) item).recordName);
    }
}
