package dan200.computercraft.shared.network.client;

import dan200.computercraft.fabric.Helper;
import dan200.computercraft.fabric.mixin.PlayerServerAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Screen;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.server.entity.player.PlayerServer;
import turniplabs.halplibe.helper.network.NetworkHandler;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

import javax.annotation.Nonnull;

abstract class OpenGuiContainerMessage<A> implements NetworkMessage {
    protected A container;
    private int windowId = 0;

    public OpenGuiContainerMessage(A container) {
        this.container = container;
    }

    public OpenGuiContainerMessage() {
    }

    public void sendToPlayer(Player player) {
        if (Helper.isServerEnvironment()) {
            serverSetWindow(player);
        }
        NetworkHandler.sendToPlayer(player, this);
        if (Helper.isServerEnvironment()) {
            this.serverSetWindow2(player);
        }
    }

    @Environment(EnvType.SERVER)
    private void serverSetWindow(Player player) {
        if (player instanceof PlayerServer) {
            ((PlayerServerAccessor) player).invokeGetNextWindowId();
            this.windowId = ((PlayerServerAccessor) player).getCurrentWindowId();
        }
    }

    abstract protected MenuAbstract getMenuInstance(Container playerInventory, A container);

    @Environment(EnvType.SERVER)
    protected void serverSetWindow2(Player player) {
        if (player instanceof PlayerServer) {
            player.craftingInventory.onCraftGuiClosed(player);
            player.craftingInventory = getMenuInstance(player.inventory, container);
            player.craftingInventory.containerId = this.windowId;
            player.craftingInventory.addSlotListener((PlayerServer) player);
        }
    }

    @Override
    public void encodeToUniversalPacket(@Nonnull UniversalPacket buf) {
        buf.writeInt(windowId);
    }

    @Override
    public void decodeFromUniversalPacket(@Nonnull UniversalPacket buf) {
        windowId = buf.readInt();
    }

    @Environment(EnvType.CLIENT)
    abstract protected Screen getScreenInstance(ContainerInventory playerInventory);

    @Override
    public void handle(NetworkContext context) {
        if (Helper.isSinglePlayer()) {
            doSinglePlayer();
            return;
        }
        if (Helper.isClientWorld()) {
            doClient();
        }
    }

    @Environment(EnvType.CLIENT)
    private void doClient() {
        Minecraft.getMinecraft().displayScreen(getScreenInstance(Minecraft.getMinecraft().thePlayer.inventory));
        Minecraft.getMinecraft().thePlayer.craftingInventory.containerId = windowId;
    }

    @Environment(EnvType.CLIENT)
    private void doSinglePlayer() {
        Minecraft.getMinecraft().displayScreen(getScreenInstance(Minecraft.getMinecraft().thePlayer.inventory));
    }
}
