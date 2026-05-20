/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.network.server;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.shared.computer.core.ServerComputer;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class RequestComputerMessage implements NetworkMessage {
    private int instance;

    public RequestComputerMessage(int instance) {
        this.instance = instance;
    }

    public RequestComputerMessage() {
    }

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket buf) {
        buf.writeInt(instance);
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket buf) {
        instance = buf.readInt();
    }

    @Override
    public void handle(NetworkContext context) {
        ServerComputer computer = ComputerCraft.serverComputerRegistry.get(instance);
        if (computer != null) {
            computer.sendComputerState(context.player);
        }
    }
}
