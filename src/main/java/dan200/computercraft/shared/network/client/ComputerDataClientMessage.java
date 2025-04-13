/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.network.client;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.shared.computer.core.ComputerState;
import dan200.computercraft.shared.computer.core.ServerComputer;
import turniplabs.halplibe.helper.network.UniversalPacket;

import javax.annotation.Nonnull;

/**
 * Provides additional data about a client computer, such as its ID and current state.
 */
public class ComputerDataClientMessage extends ComputerClientMessage {
    private ComputerState state;
    private CompoundTag userData;

    public ComputerDataClientMessage(ServerComputer computer) {
        super(computer.getInstanceID());
        state = computer.getState();
        userData = computer.getUserData();
    }

    public ComputerDataClientMessage() {
    }

    @Override
    public void encodeToUniversalPacket(@Nonnull UniversalPacket buf) {
        super.encodeToUniversalPacket(buf);
        buf.writeEnumConstant(state);
        buf.writeCompoundTag(userData);
    }

    @Override
    public void decodeFromUniversalPacket(@Nonnull UniversalPacket buf) {
        super.decodeFromUniversalPacket(buf);
        state = buf.readEnumConstant(ComputerState.class);
        userData = buf.readCompoundTag();
    }

    @Override
    public void handle(NetworkContext context) {
        getComputer().setState(state, userData);
    }
}
