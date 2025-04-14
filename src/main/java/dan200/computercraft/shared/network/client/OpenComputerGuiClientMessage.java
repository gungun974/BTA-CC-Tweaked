package dan200.computercraft.shared.network.client;

import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.inventory.ContainerComputerBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Screen;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;

public class OpenComputerGuiClientMessage implements NetworkMessage {
    protected Class<?> screen;

    private int instanceId;
    private ComputerFamily family;
    private int width;
    private int height;

    public OpenComputerGuiClientMessage(Class<? extends Screen> screen, int instanceId, ComputerFamily family, int width, int height) {
        this.screen = screen;

        this.instanceId = instanceId;
        this.family = family;
        this.width = width;
        this.height = height;
    }

    public OpenComputerGuiClientMessage() {
    }

    @Override
    public void encodeToUniversalPacket(@Nonnull UniversalPacket buf) {
        buf.writeString(screen.getName());

        buf.writeInt(instanceId);
        buf.writeEnumConstant(family);
        buf.writeInt(width);
        buf.writeInt(height);
    }

    @Override
    public void decodeFromUniversalPacket(@Nonnull UniversalPacket buf) {
        try {
            screen = Class.forName(buf.readString());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        instanceId = buf.readInt();
        family = buf.readEnumConstant(ComputerFamily.class);
        width = buf.readInt();
        height = buf.readInt();
    }

    protected Screen getScreenInstance(ContainerComputerBase container) {
        try {
            return (Screen) screen.getConstructor(ContainerComputerBase.class, int.class, int.class).newInstance(container, width, height);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handle(NetworkContext context) {
        ContainerComputerBase container = new ContainerComputerBase(instanceId, family);

        Minecraft.getMinecraft().displayScreen(getScreenInstance(container));
    }
}
