/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.network.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

/**
 * Starts or stops a record on the client, depending on if {@link #soundEntry} is {@code null}.
 * <p>
 * Used by disk drives to play record items.
 *
 * @see dan200.computercraft.shared.peripheral.diskdrive.TileDiskDrive
 */
public class PlayRecordClientMessage implements NetworkMessage {
    private TilePosc pos;
    private String name;
    private String soundEntry;

    public PlayRecordClientMessage(TilePosc pos, String entry, String name) {
        this.pos = pos;
        this.name = name;
        this.soundEntry = entry;
    }

    public PlayRecordClientMessage(TilePosc pos) {
        this.pos = pos;
        name = null;
        soundEntry = null;
    }

    public PlayRecordClientMessage() {
    }

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket buf) {
        buf.writeInt(pos.x());
        buf.writeInt(pos.y());
        buf.writeInt(pos.z());
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
        pos = new TilePos(x, y, z);
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
        if (!EnvironmentHelper.isServerEnvironment()) {
            clientHandler();
        }
    }

    @Environment(EnvType.CLIENT)
    private void clientHandler() {
        Minecraft mc = Minecraft.getMinecraft();

        mc.sndManager.playMusic(soundEntry, pos.x(), pos.y(), pos.z(), 1.0F, 1.0F);

        if (name != null) {
            mc.hudIngame.setRecordPlayingMessage(I18n.getInstance().translateKey(name));
        }
    }
}
