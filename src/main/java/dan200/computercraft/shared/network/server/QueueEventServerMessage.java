/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.network.server;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.shared.computer.core.IContainerComputer;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.util.NBTUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.helper.network.UniversalPacket;

/**
 * Queue an event on a {@link ServerComputer}.
 *
 * @see dan200.computercraft.shared.computer.core.ClientComputer#queueEvent(String)
 * @see ServerComputer#queueEvent(String)
 */
public class QueueEventServerMessage extends ComputerServerMessage {
    private String event;
    private Object[] args;

    public QueueEventServerMessage(int instanceId, @NotNull String event, @Nullable Object[] args) {
        super(instanceId);
        this.event = event;
        this.args = args;
    }

    public QueueEventServerMessage() {
    }

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket buf) {
        super.encodeToUniversalPacket(buf);
        buf.writeString(event);
        buf.writeBoolean(args != null);
        if (args != null) {
            buf.writeCompoundTag(NBTUtil.encodeObjects(args));
        }
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket buf) {
        super.decodeFromUniversalPacket(buf);
        event = buf.readString();
        boolean hasArgs = buf.readBoolean();

        if (hasArgs) {
            CompoundTag args = buf.readCompoundTag();
            this.args = NBTUtil.decodeObjects(args);
        }
    }

    @Override
    protected void handle(@NotNull ServerComputer computer, @NotNull IContainerComputer container) {
        computer.queueEvent(event, args);
    }
}
