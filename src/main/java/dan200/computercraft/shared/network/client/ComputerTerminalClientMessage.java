/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.network.client;

import dan200.computercraft.fabric.Helper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import turniplabs.halplibe.helper.network.UniversalPacket;

import javax.annotation.Nonnull;

public class ComputerTerminalClientMessage extends ComputerClientMessage {
    protected TerminalState state;

    public ComputerTerminalClientMessage(int instanceId, TerminalState state) {
        super(instanceId);
        this.state = state;
    }

    public ComputerTerminalClientMessage() {
    }

    @Override
    public void encodeToUniversalPacket(@Nonnull UniversalPacket buf) {
        super.encodeToUniversalPacket(buf);
        state.write(buf);
    }

    @Override
    public void decodeFromUniversalPacket(@Nonnull UniversalPacket buf) {
        super.decodeFromUniversalPacket(buf);
        state = new TerminalState(buf);
    }

    @Override
    public void handle(NetworkContext context) {
        if (!Helper.isServerEnvironment()) {
            clientHandler();
        }
    }

    @Environment(EnvType.CLIENT)
    private void clientHandler() {
        getComputer().read(state);
    }
}
