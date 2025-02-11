package dan200.computercraft.shared.network.client;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.PacketByteBuf;
import dan200.computercraft.client.gui.GuiComputer;
import dan200.computercraft.fabric.Helper;
import dan200.computercraft.fabric.mixin.PlayerServerAccessor;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.inventory.ContainerComputerBase;
import dan200.computercraft.shared.network.NetworkMessage;
import dan200.computercraft.shared.peripheral.diskdrive.MenuDiskDrive;
import dan200.computercraft.shared.peripheral.diskdrive.ScreenDiskDrive;
import dan200.computercraft.shared.peripheral.diskdrive.TileDiskDrive;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.block.BlockLogicFurnace;
import net.minecraft.core.block.entity.TileEntityTrommel;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.menu.MenuTrommel;
import net.minecraft.server.entity.player.PlayerServer;

import javax.annotation.Nonnull;

public class OpenGuiContainerMessage implements NetworkMessage
{
    private int windowId = 0;
    private int inventoryType;

    private TileDiskDrive menu;

    public OpenGuiContainerMessage(Player player, int inventoryType, TileDiskDrive menu)
    {
        this.menu = menu;
        if (Helper.isServerEnvironment()) {
            serverSetWindow(player);
        }

       this.inventoryType = inventoryType;
    }

    @Environment(EnvType.SERVER)
    private void serverSetWindow(Player player) {
        if (player instanceof PlayerServer) {
            ((PlayerServerAccessor)player).invokeGetNextWindowId();
            this.windowId = ((PlayerServerAccessor)player).getCurrentWindowId();

            player.craftingInventory.onCraftGuiClosed(player);
            player.craftingInventory = new MenuDiskDrive(player.inventory, menu);
            player.craftingInventory.containerId = this.windowId;
            player.craftingInventory.addSlotListener((PlayerServer) player);
        }
    }

    public OpenGuiContainerMessage()
    {
    }

    @Override
    public void toBytes( @Nonnull PacketByteBuf buf )
    {
        buf.writeInt( windowId );
        buf.writeInt(inventoryType);
    }

    @Override
    public void fromBytes( @Nonnull PacketByteBuf buf ) {
        windowId = buf.readInt();
        inventoryType = buf.readInt();
    }

    @Override
    public void handle(NetworkContext context)
    {
        if (!Helper.isServerEnvironment()) {
            doClient();
        }
    }

    @Environment(EnvType.CLIENT)
    private void doClient() {
        ComputerCraft.log.info("Packet open {}, {}", windowId, inventoryType);
        //TileDiskDrive tileDiskDrive = (TileDiskDrive) Minecraft.getMinecraft().currentWorld.getTileEntity(x, y ,z);
        TileDiskDrive tileDiskDrive = new TileDiskDrive();
        Minecraft.getMinecraft().displayScreen(new ScreenDiskDrive(Minecraft.getMinecraft().thePlayer.inventory, tileDiskDrive));
        if (Helper.isClientWorld()) {
            Minecraft.getMinecraft().thePlayer.craftingInventory.containerId = windowId;
        }
    }
}
