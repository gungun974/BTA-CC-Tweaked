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
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.network.UniversalPacket;

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
        super(new TileTurtle());
    }

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket buf) {
        super.encodeToUniversalPacket(buf);
        buf.writeInt(instanceId);
        buf.writeEnumConstant(family);
        buf.writeInt(width);
        buf.writeInt(height);
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket buf) {
        super.decodeFromUniversalPacket(buf);
        instanceId = buf.readInt();
        family = buf.readEnumConstant(ComputerFamily.class);
        width = buf.readInt();
        height = buf.readInt();
    }

    @Override
    @Environment(EnvType.CLIENT)
    protected Screen getScreenInstance(ContainerInventory playerInventory, TileTurtle container) {
        ContainerComputerBase containerComputerBase = new ContainerComputerBase(instanceId, family);

        return new ScreenTurtle<>(
            containerComputerBase,
            width,
            height,
            playerInventory,
            container
        );
    }

    @Override
    protected MenuAbstract getMenuInstance(Container playerInventory, TileTurtle container) {
        return new MenuTurtle(playerInventory, container);
    }
}
