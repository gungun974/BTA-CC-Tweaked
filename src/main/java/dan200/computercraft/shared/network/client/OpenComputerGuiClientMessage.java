package dan200.computercraft.shared.network.client;

import dan200.computercraft.client.gui.GuiComputer;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.inventory.ContainerComputerBase;
import net.minecraft.client.Minecraft;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

import javax.annotation.Nonnull;

public class OpenComputerGuiClientMessage implements NetworkMessage
{
    private int instanceId;
    private ComputerFamily family;
    private int width;
    private int height;

    public OpenComputerGuiClientMessage( int instanceId, ComputerFamily family, int width, int height )
    {
        this.instanceId = instanceId;
        this.family = family;
        this.width = width;
        this.height = height;
    }

    public OpenComputerGuiClientMessage()
    {
    }

    @Override
    public void encodeToUniversalPacket( @Nonnull UniversalPacket buf )
    {
        buf.writeInt( instanceId );
        buf.writeEnumConstant(family);
        buf.writeInt( width );
        buf.writeInt( height );
    }

    @Override
    public void decodeFromUniversalPacket( @Nonnull UniversalPacket buf ) {
        instanceId = buf.readInt();
        family = buf.readEnumConstant(ComputerFamily.class);
        width = buf.readInt();
        height = buf.readInt();
    }

    @Override
    public void handle(NetworkContext context)
    {
        ContainerComputerBase container = new ContainerComputerBase(instanceId, family);

        Minecraft.getMinecraft().displayScreen(new GuiComputer<>(container, width, height));
    }
}
