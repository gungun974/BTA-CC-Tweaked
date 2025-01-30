package dan200.computercraft.shared.network.client;

import dan200.computercraft.PacketByteBuf;
import dan200.computercraft.client.gui.GuiComputer;
import dan200.computercraft.core.computer.Computer;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.inventory.ContainerComputerBase;
import dan200.computercraft.shared.network.NetworkMessage;
import net.minecraft.client.Minecraft;

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
    public void toBytes( @Nonnull PacketByteBuf buf )
    {
        buf.writeInt( instanceId );
        buf.writeEnumConstant(family);
        buf.writeInt( width );
        buf.writeInt( height );
    }

    @Override
    public void fromBytes( @Nonnull PacketByteBuf buf ) {
        instanceId = buf.readInt();
        family = buf.readEnumConstant(ComputerFamily.class);
        width = buf.readInt();
        height = buf.readInt();
    }

    @Override
    public void handle(NetworkContext context)
    {
        ContainerComputerBase container = new ContainerComputerBase(instanceId, family);

        Minecraft.getMinecraft().displayScreen(new GuiComputer(container, width, height));
    }
}
