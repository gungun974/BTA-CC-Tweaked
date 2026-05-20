package dan200.computercraft.shared.network.client;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.client.gui.GuiPrintout;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class OpenGuiPrintoutClientMessage implements NetworkMessage {
    public int itemID;
    public int itemMeta;
    public CompoundTag itemData;

    public OpenGuiPrintoutClientMessage(ItemStack stack) {
        if (stack == null) {
            this.itemID = -1;
            this.itemMeta = 0;
            this.itemData = new CompoundTag();
        } else {
            this.itemID = stack.itemID;
            this.itemMeta = stack.getMetadata();
            this.itemData = stack.getData();
        }
    }

    public OpenGuiPrintoutClientMessage() {
    }

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket buf) {
        buf.writeShort((short) this.itemID);
        buf.writeShort((short) this.itemMeta);
        buf.writeCompoundTag(this.itemData);
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket buf) {
        this.itemID = buf.readShort();
        this.itemMeta = buf.readShort();
        this.itemData = buf.readCompoundTag();
    }

    @Override
    public void handle(NetworkContext context) {
        if (!EnvironmentHelper.isServerEnvironment()) {
            clientHandler();
        }
    }

    @Environment(EnvType.CLIENT)
    private void clientHandler() {
        if (itemID < 0) {
            return;
        }

        Minecraft.getMinecraft().displayScreen(
            new GuiPrintout(new ItemStack(itemID, 1, itemMeta, itemData))
        );
    }
}
