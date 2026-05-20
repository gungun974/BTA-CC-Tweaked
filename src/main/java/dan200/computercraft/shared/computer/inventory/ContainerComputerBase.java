/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.computer.inventory;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.shared.computer.core.*;
import net.minecraft.core.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.helper.EnvironmentHelper;

import java.util.function.Predicate;

public class ContainerComputerBase implements IContainerComputer {
    private final int instanceId;
    private final ComputerFamily family;
    private final InputState input = new InputState(this);

    public ContainerComputerBase(int computerInstanceId, ComputerFamily family) {
        this(
            x -> true,
            computerInstanceId,
            family);
    }

    protected ContainerComputerBase(Predicate<Player> canUse, int computerInstanceId,
                                    ComputerFamily family) {
        this.instanceId = computerInstanceId;
        this.family = family;
    }

    @NotNull
    public ComputerFamily getFamily() {
        return family;
    }

    @Nullable
    @Override
    public IComputer getComputer() {
        if (EnvironmentHelper.isServerEnvironment() || EnvironmentHelper.isSinglePlayer()) {
            return ComputerCraft.serverComputerRegistry.get(instanceId);
        }

        ClientComputer computer = ComputerCraft.clientComputerRegistry.get(instanceId);
        if (computer == null) {
            ComputerCraft.clientComputerRegistry.add(instanceId, computer = new ClientComputer(instanceId));
        }
        return computer;
    }

    @Nullable
    @Override
    public ClientComputer getClientComputer() {
        ClientComputer computer = ComputerCraft.clientComputerRegistry.get(instanceId);
        if (computer == null) {
            ComputerCraft.clientComputerRegistry.add(instanceId, computer = new ClientComputer(instanceId));
        }
        return computer;
    }

    @Nullable
    @Override
    public ServerComputer getServerComputer() {
        return ComputerCraft.serverComputerRegistry.get(instanceId);
    }

    @NotNull
    @Override
    public InputState getInput() {
        return input;
    }
}
