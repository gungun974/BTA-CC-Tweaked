package dan200.computercraft.shared.network.client;

import dan200.computercraft.fabric.Helper;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.inventory.ContainerComputerBase;
import net.minecraft.client.gui.Screen;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import turniplabs.halplibe.helper.network.NetworkHandler;
import turniplabs.halplibe.helper.network.UniversalPacket;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;

public class OpenContainerComputerGuiClientMessage<A> extends OpenGuiContainerMessage<A> {
    private int instanceId;
    private ComputerFamily family;
    private int width;
    private int height;

    public OpenContainerComputerGuiClientMessage(
        Player player, A container, String screen, MenuAbstractSupplier<MenuAbstract, A> menu,
        int instanceId,
        ComputerFamily family,
        int width,
        int height
    ) {
        super(player, container, screen, menu);
        this.instanceId = instanceId;
        this.family = family;
        this.width = width;
        this.height = height;
    }

    public OpenContainerComputerGuiClientMessage() {
    }

    public static <C extends TileEntity> void SendToPlayer(Player player, C tileEntity, String screen, MenuAbstractSupplier<MenuAbstract, C> menu,
                                                           int instanceId,
                                                           ComputerFamily family,
                                                           int width,
                                                           int height
    ) {
        OpenContainerComputerGuiClientMessage<C> message = new OpenContainerComputerGuiClientMessage<>(player, tileEntity, screen, menu,
            instanceId,
            family,
            width,
            height
        );
        NetworkHandler.sendToPlayer(player, message);
        if (Helper.isServerEnvironment()) {
            message.serverSetWindow2(player);
        }
    }

    public static <C extends Item> void SendToPlayer(Player player, C item, String screen, MenuAbstractSupplier<MenuAbstract, C> menu,
                                                     int instanceId,
                                                     ComputerFamily family,
                                                     int width,
                                                     int height
    ) {
        OpenContainerComputerGuiClientMessage<C> message = new OpenContainerComputerGuiClientMessage<>(player, item, screen, menu,
            instanceId,
            family,
            width,
            height
        );
        NetworkHandler.sendToPlayer(player, message);
        if (Helper.isServerEnvironment()) {
            message.serverSetWindow2(player);
        }
    }

    @Override
    public void encodeToUniversalPacket(@Nonnull UniversalPacket buf) {
        super.encodeToUniversalPacket(buf);
        buf.writeInt(instanceId);
        buf.writeEnumConstant(family);
        buf.writeInt(width);
        buf.writeInt(height);
    }

    @Override
    public void decodeFromUniversalPacket(@Nonnull UniversalPacket buf) {
        super.decodeFromUniversalPacket(buf);
        instanceId = buf.readInt();
        family = buf.readEnumConstant(ComputerFamily.class);
        width = buf.readInt();
        height = buf.readInt();
    }

    protected Screen getScreenInstance(ContainerInventory inventory) {
        ContainerComputerBase container = new ContainerComputerBase(instanceId, family);

        try {
            Class<?> screenClass = Class.forName(this.screen);

            return (Screen) screenClass.getConstructor(ContainerComputerBase.class, int.class, int.class, ContainerInventory.class, this.container.getClass()).newInstance(
                container,
                width,
                height,
                inventory,
                this.container
            );
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException |
                 ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
