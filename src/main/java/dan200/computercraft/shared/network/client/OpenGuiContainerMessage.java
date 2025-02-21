package dan200.computercraft.shared.network.client;

import dan200.computercraft.fabric.Helper;
import dan200.computercraft.fabric.mixin.PlayerServerAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.server.entity.player.PlayerServer;
import turniplabs.halplibe.helper.network.NetworkHandler;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;

public class OpenGuiContainerMessage<A extends TileEntity> implements NetworkMessage {
    private int windowId = 0;

    protected A tileEntity;
    protected Class<?> screen;
    private MenuAbstractSupplier<MenuAbstract, A> menu;

    @FunctionalInterface
    public interface MenuAbstractSupplier<T extends MenuAbstract, B extends TileEntity> {
        T get(Container inventory, B tileEntity);
    }

    public OpenGuiContainerMessage(Player player, A tileEntity, Class<? extends ScreenContainerAbstract> screen, MenuAbstractSupplier<MenuAbstract, A> menu) {
        this.tileEntity = tileEntity;
        this.screen = screen;
        this.menu = menu;
        if (Helper.isServerEnvironment()) {
            serverSetWindow(player);
        }
    }

    public static <C extends TileEntity> void SendToPlayer(Player player, C tileEntity, Class<? extends ScreenContainerAbstract> screen, MenuAbstractSupplier<MenuAbstract, C> menu) {
        OpenGuiContainerMessage<C> message = new OpenGuiContainerMessage<>(player, tileEntity, screen, menu);
        NetworkHandler.sendToPlayer(player, message);
        if (Helper.isServerEnvironment()) {
            message.serverSetWindow2(player);
        }
    }

    @Environment(EnvType.SERVER)
    private void serverSetWindow(Player player) {
        if (player instanceof PlayerServer) {
            ((PlayerServerAccessor) player).invokeGetNextWindowId();
            this.windowId = ((PlayerServerAccessor) player).getCurrentWindowId();
        }
    }

    @Environment(EnvType.SERVER)
    protected void serverSetWindow2(Player player) {
        if (player instanceof PlayerServer) {
            player.craftingInventory.onCraftGuiClosed(player);
            player.craftingInventory = menu.get(player.inventory, tileEntity);
            player.craftingInventory.containerId = this.windowId;
            player.craftingInventory.addSlotListener((PlayerServer) player);
        }
    }

    public OpenGuiContainerMessage() {
    }

    @Override
    public void encodeToUniversalPacket(@Nonnull UniversalPacket buf) {
        buf.writeInt(windowId);
        buf.writeString(tileEntity.getClass().getName());
        buf.writeString(screen.getName());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void decodeFromUniversalPacket(@Nonnull UniversalPacket buf) {
        windowId = buf.readInt();
        try {
            Class<?> tileEntityKlass = Class.forName(buf.readString());

            tileEntity = (A) tileEntityKlass.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        try {
            screen = Class.forName(buf.readString());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected Screen getScreenInstance(ContainerInventory inventory) {
        try {
            return (Screen) screen.getConstructor(ContainerInventory.class, tileEntity.getClass()).newInstance(inventory, tileEntity);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

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
