package dan200.computercraft.shared.network.client;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.client.gui.GuiComputer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.net.handler.PacketHandler;

public class OpenComputerGuiClientMessage extends ComputerTerminalClientMessage
{
    public OpenComputerGuiClientMessage(int instanceID, TerminalState state) {
        super(instanceID, state);
    }

    public OpenComputerGuiClientMessage()
    {
        super();
    }

    @Override
    public void handle(PacketHandler packetHandler)
    {
        super.handle(packetHandler);
        Minecraft.getMinecraft().displayScreen(new GuiComputer(getComputer(), state.width, state.height));
    }
}
