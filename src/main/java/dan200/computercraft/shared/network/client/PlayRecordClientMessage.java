/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.network.client;

import dan200.computercraft.fabric.Helper;
import dan200.computercraft.shared.util.BlockPos;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
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
    private String soundEntry;

    public PlayRecordClientMessage(BlockPos pos, String entry, String name) {
        this.pos = pos;
        this.name = name;
        this.soundEntry = entry;
    }

    public PlayRecordClientMessage(BlockPos pos) {
        this.pos = pos;
        name = null;
        soundEntry = null;
    }

    public PlayRecordClientMessage() {}

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
            buf.writeString(soundEntry);
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
            soundEntry = buf.readString();
        } else {
            name = null;
            soundEntry = null;
        }
    }

    @Override
    public void handle(NetworkContext context) {
        if (!Helper.isServerEnvironment()) {
            clientHandler();
        }
    }

    @Environment(EnvType.CLIENT)
    private void clientHandler() {
        Minecraft mc = Minecraft.getMinecraft();

        mc.sndManager.playMusic(soundEntry, pos.x, pos.y, pos.z, 1.0F, 1.0F);

        if (name != null) {
            mc.hudIngame.setRecordPlayingMessage(I18n.getInstance().translateKey(name));
        }
    }
}
