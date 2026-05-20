package dan200.computercraft.shared.peripheral.printer;

import dan200.computercraft.client.BlockModelCorrectRotable;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.useless.dragonfly.models.block.StaticBlockModel;

public class BlockModelPrinter<T extends BlockLogic> extends BlockModelCorrectRotable<T> {
    public final @NotNull StaticBlockModel both_full;
    public final @NotNull StaticBlockModel bottom_full;
    public final @NotNull StaticBlockModel empty;
    public final @NotNull StaticBlockModel top_full;

    public BlockModelPrinter(Block<T> block) {
        super(block, BlockModelDispatcher.loadDataModel("computercraft:block/printer_empty").asModel());
        this.both_full = BlockModelDispatcher.loadDataModel("computercraft:block/printer_both_full").asModel();
        this.bottom_full = BlockModelDispatcher.loadDataModel("computercraft:block/printer_bottom_full").asModel();
        this.empty = BlockModelDispatcher.loadDataModel("computercraft:block/printer_empty").asModel();
        this.top_full = BlockModelDispatcher.loadDataModel("computercraft:block/printer_top_full").asModel();
    }

    @Override
    public @NotNull StaticBlockModel getModel(@NotNull WorldSource source, @NotNull TilePosc tilePosc) {
        int currentMetadata = source.getBlockData(tilePosc);

        final boolean currentBottom = ((currentMetadata >> 3) & 0b1) == 1;
        final boolean currentTop = ((currentMetadata >> 4) & 0b1) == 1;

        if (currentBottom && !currentTop) {
            return bottom_full;
        }

        if (!currentBottom && currentTop) {
            return top_full;
        }

        if (currentBottom) {
            return both_full;
        }

        return empty;
    }
}
