package dan200.computercraft.shared.peripheral.modem.wireless;

import dan200.computercraft.client.BlockModelCorrectVeryRotable;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.useless.dragonfly.models.block.StaticBlockModel;

public class BlockModelWirelessModem<T extends BlockLogic> extends BlockModelCorrectVeryRotable<T> {
    public final @NotNull StaticBlockModel on;
    public final @NotNull StaticBlockModel off;

    public BlockModelWirelessModem(Block<T> block, String modelPrefix) {
        super(block, BlockModelDispatcher.loadDataModel(modelPrefix + "_off").asModel());
        this.on = BlockModelDispatcher.loadDataModel(modelPrefix + "_on").asModel();
        this.off = BlockModelDispatcher.loadDataModel(modelPrefix + "_off").asModel();
    }

    @Override
    public @NotNull StaticBlockModel getModel(@NotNull WorldSource source, @NotNull TilePosc tilePosc) {
        int currentMetadata = source.getBlockData(tilePosc);

        final boolean isOn = ((currentMetadata >> 3) & 0b1) == 1;

        if (isOn) {
            return on;
        }

        return off;
    }
}
