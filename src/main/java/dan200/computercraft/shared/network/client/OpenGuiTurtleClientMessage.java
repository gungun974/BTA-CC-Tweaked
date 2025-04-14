package dan200.computercraft.shared.network.client;

import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.inventory.ContainerComputerBase;
import dan200.computercraft.shared.turtle.blocks.TileTurtle;
import dan200.computercraft.shared.turtle.inventory.MenuTurtle;
import dan200.computercraft.shared.turtle.inventory.ScreenTurtle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Screen;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import turniplabs.halplibe.helper.network.UniversalPacket;

import javax.annotation.Nonnull;

public class OpenGuiTurtleClientMessage extends OpenGuiContainerMessage<TileTurtle> {
    private int instanceId;
    private ComputerFamily family;
    private int width;
    private int height;

    public OpenGuiTurtleClientMessage(
        TileTurtle container,
        int instanceId,
        ComputerFamily family,
        int width,
        int height
    ) {
        super(container);
        this.instanceId = instanceId;
        this.family = family;
        this.width = width;
        this.height = height;
    }

    public OpenGuiTurtleClientMessage() {
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

    @Override
    @Environment(EnvType.CLIENT)
    protected Screen getScreenInstance(ContainerInventory playerInventory) {
        ContainerComputerBase container = new ContainerComputerBase(instanceId, family);

        return new ScreenTurtle<>(
            container,
            width,
            height,
            playerInventory,
            new TileTurtle()
        );
    }

    @Override
    protected MenuAbstract getMenuInstance(Container playerInventory, TileTurtle container) {
        return new MenuTurtle(playerInventory, container);
    }
}
