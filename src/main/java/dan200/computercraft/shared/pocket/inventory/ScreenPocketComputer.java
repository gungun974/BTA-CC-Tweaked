package dan200.computercraft.shared.pocket.inventory;

import dan200.computercraft.client.gui.GuiComputer;
import dan200.computercraft.shared.computer.inventory.ContainerComputerBase;

public class ScreenPocketComputer<T extends ContainerComputerBase> extends GuiComputer<T> {

    public ScreenPocketComputer(T container, int termWidth, int termHeight) {
        super(container, termWidth, termHeight);
    }
}
