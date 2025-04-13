/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.network.client;

import dan200.computercraft.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sound.SoundEntry;
import net.minecraft.client.sound.SoundRepository;
import net.minecraft.core.lang.I18n;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

import javax.annotation.Nonnull;

/**
 * Starts or stops a record on the client, depending on if {@link #soundEvent} is {@code null}.
 * <p>
 * Used by disk drives to play record items.
 *
 * @see dan200.computercraft.shared.peripheral.diskdrive.TileDiskDrive
 */
public class PlayRecordClientMessage implements NetworkMessage {
    private BlockPos pos;
    private String name;
    private SoundEntry soundEntry;

    public PlayRecordClientMessage(BlockPos pos, SoundEntry entry, String name) {
        this.pos = pos;
        this.name = name;
        soundEntry = entry;
    }

    public PlayRecordClientMessage(BlockPos pos) {
        this.pos = pos;
        name = null;
        soundEntry = null;
    }

    @Override
    public void encodeToUniversalPacket(@Nonnull UniversalPacket buf) {
        buf.writeInt(pos.x);
        buf.writeInt(pos.y);
        buf.writeInt(pos.z);
        if (soundEntry == null) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            buf.writeString(name);
            buf.writeString(soundEntry.name);
        }
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket buf) {
        final int x = buf.readInt();
        final int y = buf.readInt();
        final int z = buf.readInt();
        pos = new BlockPos(x, y, z);
        if (buf.readBoolean()) {
            name = buf.readString();
            soundEntry = SoundRepository.SOUNDS.getSoundEntry(buf.readString());
        } else {
            name = null;
            soundEntry = null;
        }
    }

    @Override
    public void handle(NetworkContext context) {
        Minecraft mc = Minecraft.getMinecraft();

        mc.sndManager.playMusic(soundEntry, pos.x, pos.y, pos.z, 1.0F, 1.0F);

        if (name != null) {
            mc.hudIngame.setRecordPlayingMessage(I18n.getInstance().translateKey(name));
        }
    }
}
