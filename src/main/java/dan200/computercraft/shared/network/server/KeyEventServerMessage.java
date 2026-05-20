/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.network.server;

import dan200.computercraft.shared.computer.core.IContainerComputer;
import dan200.computercraft.shared.computer.core.InputState;
import dan200.computercraft.shared.computer.core.ServerComputer;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class KeyEventServerMessage extends ComputerServerMessage {
    public static final int TYPE_DOWN = 0;
    public static final int TYPE_REPEAT = 1;
    public static final int TYPE_UP = 2;

    private int type;
    private int key;

    public KeyEventServerMessage(int instanceId, int type, int key) {
        super(instanceId);
        this.type = type;
        this.key = key;
    }

    public KeyEventServerMessage() {
    }

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket buf) {
        super.encodeToUniversalPacket(buf);
        buf.writeByte(type);
        buf.writeInt(key);
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket buf) {
        super.decodeFromUniversalPacket(buf);
        type = buf.readByte();
        key = buf.readInt();
    }

    @Override
    protected void handle(@NotNull ServerComputer computer, @NotNull IContainerComputer container) {
        InputState input = container.getInput();
        if (type == TYPE_UP) {
            input.keyUp(key);
        } else {
            input.keyDown(key, type == TYPE_REPEAT);
        }
    }
}
