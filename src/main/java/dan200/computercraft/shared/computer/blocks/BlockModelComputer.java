package dan200.computercraft.shared.computer.blocks;

import dan200.computercraft.client.BlockModelCorrectRotable;
import dan200.computercraft.shared.computer.core.ComputerState;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.useless.dragonfly.models.block.StaticBlockModel;

public class BlockModelComputer<T extends BlockLogic> extends BlockModelCorrectRotable<T> {
    public final @NotNull StaticBlockModel on;
    public final @NotNull StaticBlockModel blinking;
    public final @NotNull StaticBlockModel off;

    public BlockModelComputer(Block<T> block, String modelPrefix) {
        super(block, BlockModelDispatcher.loadDataModel(modelPrefix + "_blinking").asModel());
        this.on = BlockModelDispatcher.loadDataModel(modelPrefix + "_on").asModel();
        this.blinking = BlockModelDispatcher.loadDataModel(modelPrefix + "_blinking").asModel();
        this.off = BlockModelDispatcher.loadDataModel(modelPrefix + "_off").asModel();
    }

    @Override
    public @NotNull StaticBlockModel getModel(@NotNull WorldSource source, @NotNull TilePosc tilePosc) {
        int currentMetadata = source.getBlockData(tilePosc);

        final ComputerState currentState = ComputerState.class.getEnumConstants()[(currentMetadata >> 3) & 0b11];

        return switch (currentState) {
            case OFF -> off;
            case ON -> on;
            case BLINKING -> blinking;
        };
    }
}
