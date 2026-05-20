package dan200.computercraft.shared.peripheral.monitor;

import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.generic.BlockModelGeneric;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.useless.dragonfly.models.block.StaticBlockModel;

public class BlockModelMonitor<T extends BlockLogic> extends BlockModelGeneric<T> {

    public final @NotNull StaticBlockModel none;

    public final @NotNull StaticBlockModel l;
    public final @NotNull StaticBlockModel r;
    public final @NotNull StaticBlockModel lr;
    public final @NotNull StaticBlockModel u;
    public final @NotNull StaticBlockModel d;

    public final @NotNull StaticBlockModel ud;
    public final @NotNull StaticBlockModel rd;
    public final @NotNull StaticBlockModel ld;
    public final @NotNull StaticBlockModel ru;
    public final @NotNull StaticBlockModel lu;

    public final @NotNull StaticBlockModel lrd;
    public final @NotNull StaticBlockModel rud;
    public final @NotNull StaticBlockModel lud;
    public final @NotNull StaticBlockModel lru;
    public final @NotNull StaticBlockModel lrud;


    public BlockModelMonitor(Block<T> block, String modelPrefix) {
        super(block, BlockModelDispatcher.loadDataModel(modelPrefix + "_item").asModel());
        this.none = BlockModelDispatcher.loadDataModel(modelPrefix).asModel();

        this.l = BlockModelDispatcher.loadDataModel(modelPrefix + "_l").asModel();
        this.r = BlockModelDispatcher.loadDataModel(modelPrefix + "_r").asModel();
        this.lr = BlockModelDispatcher.loadDataModel(modelPrefix + "_lr").asModel();
        this.u = BlockModelDispatcher.loadDataModel(modelPrefix + "_u").asModel();
        this.d = BlockModelDispatcher.loadDataModel(modelPrefix + "_d").asModel();

        this.ud = BlockModelDispatcher.loadDataModel(modelPrefix + "_ud").asModel();
        this.rd = BlockModelDispatcher.loadDataModel(modelPrefix + "_rd").asModel();
        this.ld = BlockModelDispatcher.loadDataModel(modelPrefix + "_ld").asModel();
        this.ru = BlockModelDispatcher.loadDataModel(modelPrefix + "_ru").asModel();
        this.lu = BlockModelDispatcher.loadDataModel(modelPrefix + "_lu").asModel();

        this.lrd = BlockModelDispatcher.loadDataModel(modelPrefix + "_lrd").asModel();
        this.rud = BlockModelDispatcher.loadDataModel(modelPrefix + "_rud").asModel();
        this.lud = BlockModelDispatcher.loadDataModel(modelPrefix + "_lud").asModel();
        this.lru = BlockModelDispatcher.loadDataModel(modelPrefix + "_lru").asModel();
        this.lrud = BlockModelDispatcher.loadDataModel(modelPrefix + "_lrud").asModel();
    }

    @Override
    public boolean renderAttached(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource, @NotNull TilePosc tilePos, boolean cullFaces, @Nullable IconCoordinate overrideTexture) {
        int meta = worldSource.getBlockData(tilePos);

        Direction direction = BlockLogicMonitor.metaToFacing(meta);
        Direction orientation = BlockLogicMonitor.metaToOrientation(meta);

        if (orientation == Direction.UP) {
            return switch (direction) {
                case WEST ->
                    this.getModel(worldSource, tilePos).renderAttached(this, tessellator, worldSource, tilePos, 1, 1, 0, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);
                case EAST ->
                    this.getModel(worldSource, tilePos).renderAttached(this, tessellator, worldSource, tilePos, 1, 3, 0, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);
                case SOUTH ->
                    this.getModel(worldSource, tilePos).renderAttached(this, tessellator, worldSource, tilePos, 1, 2, 0, 0.0, 0.0, 0.0, false, cullFaces, overrideTexture);
                default ->
                    this.getModel(worldSource, tilePos).renderAttached(this, tessellator, worldSource, tilePos, 1, 0, 0, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);
            };
        }

        if (orientation == Direction.DOWN) {
            return switch (direction) {
                case WEST ->
                    this.getModel(worldSource, tilePos).renderAttached(this, tessellator, worldSource, tilePos, -1, 1, 0, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);
                case EAST ->
                    this.getModel(worldSource, tilePos).renderAttached(this, tessellator, worldSource, tilePos, -1, 3, 0, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);
                case SOUTH ->
                    this.getModel(worldSource, tilePos).renderAttached(this, tessellator, worldSource, tilePos, -1, 2, 0, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);
                default ->
                    this.getModel(worldSource, tilePos).renderAttached(this, tessellator, worldSource, tilePos, -1, 0, 0, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);
            };
        }

        return switch (direction) {
            case WEST ->
                this.getModel(worldSource, tilePos).renderAttached(this, tessellator, worldSource, tilePos, 0, 1, 0, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);
            case EAST ->
                this.getModel(worldSource, tilePos).renderAttached(this, tessellator, worldSource, tilePos, 0, 3, 0, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);
            case SOUTH ->
                this.getModel(worldSource, tilePos).renderAttached(this, tessellator, worldSource, tilePos, 0, 2, 0, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);
            default ->
                this.getModel(worldSource, tilePos).renderAttached(this, tessellator, worldSource, tilePos, 0, 0, 0, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);
        };
    }

    @Override
    public @NotNull StaticBlockModel getModel(@NotNull WorldSource source, @NotNull TilePosc tilePosc) {
        int currentMetadata = source.getBlockData(tilePosc);

        MonitorEdgeState edgeState = BlockLogicMonitor.metaToState(currentMetadata);

        return switch (edgeState) {
            case NONE -> this.none;
            case L -> this.l;
            case R -> this.r;
            case LR -> this.lr;
            case U -> this.u;
            case D -> this.d;
            case UD -> this.ud;
            case RD -> this.rd;
            case LD -> this.ld;
            case RU -> this.ru;
            case LU -> this.lu;
            case LRD -> this.lrd;
            case RUD -> this.rud;
            case LUD -> this.lud;
            case LRU -> this.lru;
            case LRUD -> this.lrud;
        };
    }
}
