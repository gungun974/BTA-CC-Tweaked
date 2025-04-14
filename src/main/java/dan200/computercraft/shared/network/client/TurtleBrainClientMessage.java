package dan200.computercraft.shared.network.client;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.fabric.Helper;
import dan200.computercraft.shared.turtle.blocks.TileTurtle;
import dan200.computercraft.shared.turtle.core.TurtleBrain;
import dan200.computercraft.shared.util.BlockPos;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class TurtleBrainClientMessage implements NetworkMessage {
    private int x;
    private int y;
    private int z;
    private CompoundTag brainData;

    public TurtleBrainClientMessage() {
    }

    public TurtleBrainClientMessage(BlockPos pos, TurtleBrain brain) {
        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;
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
        if (!Helper.isServerEnvironment()) {
            clientHandler(context);
        }
    }

    @Environment(EnvType.CLIENT)
    private void clientHandler(NetworkContext context) {
        World world = Minecraft.getMinecraft().currentWorld;

        if (world == null) {
            return;
        }

        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (!(tileEntity instanceof TileTurtle)) {
           return;
        }

        ((TileTurtle) tileEntity).updateBrainFromNBT(brainData);
    }
}
