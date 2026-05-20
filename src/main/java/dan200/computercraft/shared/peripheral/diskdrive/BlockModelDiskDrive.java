package dan200.computercraft.shared.peripheral.diskdrive;

import dan200.computercraft.client.BlockModelCorrectRotable;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.useless.dragonfly.models.block.StaticBlockModel;

public class BlockModelDiskDrive<T extends BlockLogic> extends BlockModelCorrectRotable<T> {
    public final @NotNull StaticBlockModel empty;
    public final @NotNull StaticBlockModel full;
    public final @NotNull StaticBlockModel invalid;

    public BlockModelDiskDrive(Block<T> block) {
        super(block, BlockModelDispatcher.loadDataModel("computercraft:block/disk_drive_empty").asModel());
        this.empty = BlockModelDispatcher.loadDataModel("computercraft:block/disk_drive_empty").asModel();
        this.full = BlockModelDispatcher.loadDataModel("computercraft:block/disk_drive_full").asModel();
        this.invalid = BlockModelDispatcher.loadDataModel("computercraft:block/disk_drive_invalid").asModel();
    }

    @Override
    public @NotNull StaticBlockModel getModel(@NotNull WorldSource source, @NotNull TilePosc tilePosc) {
        int currentMetadata = source.getBlockData(tilePosc);

        final DiskDriveState currentState = DiskDriveState.class.getEnumConstants()[(currentMetadata >> 3) & 0b11];

        return switch (currentState) {
            case EMPTY -> empty;
            case FULL -> full;
            case INVALID -> invalid;
        };
    }
}
