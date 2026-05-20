/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.network.server;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.shared.computer.core.IContainerComputer;
import dan200.computercraft.shared.computer.core.ServerComputer;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

/**
 * A packet, which performs an action on a {@link ServerComputer}.
 * <p>
 * This requires that the sending player is interacting with that computer via a {@link IContainerComputer}.
 */
public abstract class ComputerServerMessage implements NetworkMessage {
    private int instanceId;

    public ComputerServerMessage(int instanceId) {
        this.instanceId = instanceId;
    }

    public ComputerServerMessage() {
    }

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket buf) {
        buf.writeInt(instanceId);
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket buf) {
        instanceId = buf.readInt();
    }

    @Override
    public void handle(NetworkContext context) {
        ServerComputer computer = ComputerCraft.serverComputerRegistry.get(instanceId);
        if (computer == null) {
            return;
        }

        IContainerComputer container = computer.getContainer(context.player);
        if (container == null) {
            return;
        }

        handle(computer, container);
    }

    protected abstract void handle(@NotNull ServerComputer computer, @NotNull IContainerComputer container);
}
