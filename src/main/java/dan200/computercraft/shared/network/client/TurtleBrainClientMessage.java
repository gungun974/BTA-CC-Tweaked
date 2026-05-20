package dan200.computercraft.shared.network.client;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.shared.turtle.blocks.TileTurtle;
import dan200.computercraft.shared.turtle.core.TurtleBrain;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class TurtleBrainClientMessage implements NetworkMessage {
    private int x;
    private int y;
    private int z;
    private CompoundTag brainData;

    public TurtleBrainClientMessage() {
    }

    public TurtleBrainClientMessage(TilePosc pos, TurtleBrain brain) {
        this.x = pos.x();
        this.y = pos.y();
        this.z = pos.z();
        brainData = new CompoundTag();
        brain.writeToNBT(brainData);
    }

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket packet) {
        packet.writeInt(x);
        packet.writeInt(y);
        packet.writeInt(z);

        packet.writeCompoundTag(brainData);
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket packet) {
        x = packet.readInt();
        y = packet.readInt();
        z = packet.readInt();

        brainData = packet.readCompoundTag();
    }

    @Override
    public void handle(NetworkContext context) {
        if (!EnvironmentHelper.isServerEnvironment()) {
            clientHandler(context);
        }
    }

    @Environment(EnvType.CLIENT)
    private void clientHandler(NetworkContext context) {
        World world = Minecraft.getMinecraft().currentWorld;

        if (world == null) {
            return;
        }

        TileEntity tileEntity = world.getTileEntity(new TilePos(x, y, z));

        if (!(tileEntity instanceof TileTurtle)) {
            return;
        }

        ((TileTurtle) tileEntity).updateBrainFromNBT(brainData);
    }
}
