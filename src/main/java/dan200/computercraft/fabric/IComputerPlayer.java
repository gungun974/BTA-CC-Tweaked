package dan200.computercraft.fabric;

import dan200.computercraft.shared.computer.core.IContainerComputer;

import javax.annotation.Nullable;

public interface IComputerPlayer {
    @Nullable
    IContainerComputer getCurrentContainerComputer();

    void setCurrentContainerComputer(IContainerComputer container);
}
