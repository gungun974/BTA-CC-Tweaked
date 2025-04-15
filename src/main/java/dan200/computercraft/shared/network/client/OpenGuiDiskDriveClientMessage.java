package dan200.computercraft.shared.network.client;

import dan200.computercraft.shared.peripheral.diskdrive.MenuDiskDrive;
import dan200.computercraft.shared.peripheral.diskdrive.ScreenDiskDrive;
import dan200.computercraft.shared.peripheral.diskdrive.TileDiskDrive;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Screen;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;

public class OpenGuiDiskDriveClientMessage extends OpenGuiContainerMessage<TileDiskDrive> {
    public OpenGuiDiskDriveClientMessage(TileDiskDrive container) {
        super(container);
    }

    public OpenGuiDiskDriveClientMessage() {
        super(new TileDiskDrive());
    }

    @Override
    @Environment(EnvType.CLIENT)
    protected Screen getScreenInstance(ContainerInventory playerInventory, TileDiskDrive container) {
        return new ScreenDiskDrive(playerInventory, container);
    }

    @Override
    protected MenuAbstract getMenuInstance(Container playerInventory, TileDiskDrive container) {
        return new MenuDiskDrive(playerInventory, container);
    }
}
