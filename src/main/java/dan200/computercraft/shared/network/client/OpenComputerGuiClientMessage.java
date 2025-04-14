package dan200.computercraft.shared.network.client;

import dan200.computercraft.fabric.Helper;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.inventory.ContainerComputerBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Screen;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;

public class OpenComputerGuiClientMessage implements NetworkMessage {
    protected String screen;

    private int instanceId;
    private ComputerFamily family;
    private int width;
    private int height;

    public OpenComputerGuiClientMessage(String screen, int instanceId, ComputerFamily family, int width, int height) {
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
        buf.writeString(screen);

        buf.writeInt(instanceId);
        buf.writeEnumConstant(family);
        buf.writeInt(width);
        buf.writeInt(height);
    }

    @Override
    public void decodeFromUniversalPacket(@Nonnull UniversalPacket buf) {
        screen = buf.readString();

        instanceId = buf.readInt();
        family = buf.readEnumConstant(ComputerFamily.class);
        width = buf.readInt();
        height = buf.readInt();
    }

    protected Screen getScreenInstance(ContainerComputerBase container) {
        try {
            Class<?> screenClass = Class.forName(screen);

            return (Screen) screenClass.getConstructor(ContainerComputerBase.class, int.class, int.class).newInstance(container, width, height);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException |
                 ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handle(NetworkContext context) {
        if (!Helper.isServerEnvironment()) {
            clientHandler();
        }
    }

    @Environment(EnvType.CLIENT)
    private void clientHandler() {
        ContainerComputerBase container = new ContainerComputerBase(instanceId, family);

        Minecraft.getMinecraft().displayScreen(getScreenInstance(container));
    }
}
