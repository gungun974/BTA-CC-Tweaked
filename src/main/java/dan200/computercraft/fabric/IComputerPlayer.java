package dan200.computercraft.fabric;

import dan200.computercraft.shared.computer.core.IContainerComputer;
import org.jetbrains.annotations.Nullable;

public interface IComputerPlayer {
    @Nullable
    IContainerComputer getCurrentContainerComputer();

    void setCurrentContainerComputer(IContainerComputer container);
}
