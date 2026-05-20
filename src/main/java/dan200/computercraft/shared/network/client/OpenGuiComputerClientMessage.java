package dan200.computercraft.shared.network.client;

import dan200.computercraft.client.gui.GuiComputer;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.inventory.ContainerComputerBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class OpenGuiComputerClientMessage implements NetworkMessage {
    private int instanceId;
    private ComputerFamily family;
    private int width;
    private int height;

    public OpenGuiComputerClientMessage(int instanceId, ComputerFamily family, int width, int height) {
        this.instanceId = instanceId;
        this.family = family;
        this.width = width;
        this.height = height;
    }

    public OpenGuiComputerClientMessage() {
    }

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket buf) {
        buf.writeInt(instanceId);
        buf.writeEnumConstant(family);
        buf.writeInt(width);
        buf.writeInt(height);
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket buf) {
        instanceId = buf.readInt();
        family = buf.readEnumConstant(ComputerFamily.class);
        width = buf.readInt();
        height = buf.readInt();
    }

    @Override
    public void handle(NetworkContext context) {
        if (!EnvironmentHelper.isServerEnvironment()) {
            clientHandler();
        }
    }

    @Environment(EnvType.CLIENT)
    private void clientHandler() {
        ContainerComputerBase container = new ContainerComputerBase(instanceId, family);

        Minecraft.getMinecraft().displayScreen(new GuiComputer<>(container, width, height));
    }
}
