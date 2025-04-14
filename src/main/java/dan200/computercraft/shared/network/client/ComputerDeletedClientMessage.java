/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.network.client;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.fabric.Helper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class ComputerDeletedClientMessage extends ComputerClientMessage {
    public ComputerDeletedClientMessage(int instanceId) {
        super(instanceId);
    }

    public ComputerDeletedClientMessage() {
    }

    @Override
    public void handle(NetworkContext context) {
        if (!Helper.isServerEnvironment()) {
            clientHandler();
        }
    }

    @Environment(EnvType.CLIENT)
    private void clientHandler() {
        ComputerCraft.clientComputerRegistry.remove(getInstanceId());
    }
}
