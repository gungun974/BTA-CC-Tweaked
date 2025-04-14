package dan200.computercraft.shared.network.client;

import dan200.computercraft.shared.peripheral.printer.MenuPrinter;
import dan200.computercraft.shared.peripheral.printer.ScreenPrinter;
import dan200.computercraft.shared.peripheral.printer.TilePrinter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Screen;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;

public class OpenGuiPrinterClientMessage extends OpenGuiContainerMessage<TilePrinter> {
    public OpenGuiPrinterClientMessage(TilePrinter container) {
        super(container);
    }

    public OpenGuiPrinterClientMessage() {}

    @Override
    @Environment(EnvType.CLIENT)
    protected Screen getScreenInstance(ContainerInventory playerInventory) {
        return new ScreenPrinter(playerInventory, new TilePrinter());
    }

    @Override
    protected MenuAbstract getMenuInstance(Container playerInventory, TilePrinter container) {
        return new MenuPrinter(playerInventory, container);
    }
}
