package dan200.computercraft.shared.peripheral.modem.wired;

import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.generic.BlockModelGeneric;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.useless.dragonfly.models.block.StaticBlockModel;

public class BlockModelCable<T extends BlockLogic> extends BlockModelGeneric<T> {
    public final @NotNull StaticBlockModel arm;
    public final @NotNull StaticBlockModel core_any;
    public final @NotNull StaticBlockModel core_facing;

    public final @NotNull StaticBlockModel off;
    public final @NotNull StaticBlockModel off_peripheral;
    public final @NotNull StaticBlockModel on;
    public final @NotNull StaticBlockModel on_peripheral;

    public BlockModelCable(Block<T> block) {
        super(block, BlockModelDispatcher.loadDataModel("computercraft:block/cable_core_any").asModel());
        this.arm = BlockModelDispatcher.loadDataModel("computercraft:block/cable_arm").asModel();
        this.core_any = BlockModelDispatcher.loadDataModel("computercraft:block/cable_core_any").asModel();
        this.core_facing = BlockModelDispatcher.loadDataModel("computercraft:block/cable_core_facing").asModel();

        this.off = BlockModelDispatcher.loadDataModel("computercraft:block/wired_modem_off").asModel();
        this.off_peripheral = BlockModelDispatcher.loadDataModel("computercraft:block/wired_modem_full_peripheral").asModel();
        this.on = BlockModelDispatcher.loadDataModel("computercraft:block/wired_modem_on").asModel();
        this.on_peripheral = BlockModelDispatcher.loadDataModel("computercraft:block/wired_modem_on_peripheral").asModel();
    }

    @Override
    public boolean renderAttached(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource, @NotNull TilePosc tilePos, boolean cullFaces, @Nullable IconCoordinate overrideTexture) {
        TileEntity tileEntity = worldSource.getTileEntity(tilePos);

        if (tileEntity instanceof TileCable state) {

            if (state.blockStateCable) {
                int connections = (state.blockStateNorth ? 1 : 0)
                    + (state.blockStateSouth ? 1 : 0)
                    + (state.blockStateEast ? 1 : 0)
                    + (state.blockStateWest ? 1 : 0)
                    + (state.blockStateDown ? 1 : 0)
                    + (state.blockStateUp ? 1 : 0);

                if (connections == 1) {
                    if (state.blockStateNorth || state.blockStateSouth) {
                        core_facing.renderAttached(this, tessellator, worldSource, tilePos, 0, 0, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                    } else if (state.blockStateEast || state.blockStateWest) {
                        core_facing.renderAttached(this, tessellator, worldSource, tilePos, 0, 1, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                    } else {
                        core_facing.renderAttached(this, tessellator, worldSource, tilePos, 1, 0, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                    }
                } else {
                    core_any.renderAttached(this, tessellator, worldSource, tilePos, 0, 0, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                }

                if (state.blockStateNorth) {
                    arm.renderAttached(this, tessellator, worldSource, tilePos, 0, 2, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                }
                if (state.blockStateSouth) {
                    arm.renderAttached(this, tessellator, worldSource, tilePos, 0, 0, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                }
                if (state.blockStateEast) {
                    arm.renderAttached(this, tessellator, worldSource, tilePos, 0, 1, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                }
                if (state.blockStateWest) {
                    arm.renderAttached(this, tessellator, worldSource, tilePos, 0, 3, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                }
                if (state.blockStateUp) {
                    arm.renderAttached(this, tessellator, worldSource, tilePos, 3, 0, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                }
                if (state.blockStateDown) {
                    arm.renderAttached(this, tessellator, worldSource, tilePos, 1, 0, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                }
            }

            switch (state.blockStateModem) {
                case None -> {
                }
                case DownOff ->
                    off.renderAttached(this, tessellator, worldSource, tilePos, 3, 0, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                case UpOff ->
                    off.renderAttached(this, tessellator, worldSource, tilePos, 1, 0, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                case NorthOff ->
                    off.renderAttached(this, tessellator, worldSource, tilePos, 0, 0, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                case SouthOff ->
                    off.renderAttached(this, tessellator, worldSource, tilePos, 0, 2, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                case WestOff ->
                    off.renderAttached(this, tessellator, worldSource, tilePos, 0, 1, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                case EastOff ->
                    off.renderAttached(this, tessellator, worldSource, tilePos, 0, 3, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                case DownOn ->
                    on.renderAttached(this, tessellator, worldSource, tilePos, 3, 0, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                case UpOn ->
                    on.renderAttached(this, tessellator, worldSource, tilePos, 1, 0, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                case NorthOn ->
                    on.renderAttached(this, tessellator, worldSource, tilePos, 0, 0, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                case SouthOn ->
                    on.renderAttached(this, tessellator, worldSource, tilePos, 0, 2, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                case WestOn ->
                    on.renderAttached(this, tessellator, worldSource, tilePos, 0, 1, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                case EastOn ->
                    on.renderAttached(this, tessellator, worldSource, tilePos, 0, 3, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                case DownOffPeripheral ->
                    off_peripheral.renderAttached(this, tessellator, worldSource, tilePos, 3, 0, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                case UpOffPeripheral ->
                    off_peripheral.renderAttached(this, tessellator, worldSource, tilePos, 1, 0, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                case NorthOffPeripheral ->
                    off_peripheral.renderAttached(this, tessellator, worldSource, tilePos, 0, 0, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                case SouthOffPeripheral ->
                    off_peripheral.renderAttached(this, tessellator, worldSource, tilePos, 0, 2, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                case WestOffPeripheral ->
                    off_peripheral.renderAttached(this, tessellator, worldSource, tilePos, 0, 1, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                case EastOffPeripheral ->
                    off_peripheral.renderAttached(this, tessellator, worldSource, tilePos, 0, 3, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                case DownOnPeripheral ->
                    on_peripheral.renderAttached(this, tessellator, worldSource, tilePos, 3, 0, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                case UpOnPeripheral ->
                    on_peripheral.renderAttached(this, tessellator, worldSource, tilePos, 1, 0, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                case NorthOnPeripheral ->
                    on_peripheral.renderAttached(this, tessellator, worldSource, tilePos, 0, 0, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                case SouthOnPeripheral ->
                    on_peripheral.renderAttached(this, tessellator, worldSource, tilePos, 0, 2, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                case WestOnPeripheral ->
                    on_peripheral.renderAttached(this, tessellator, worldSource, tilePos, 0, 1, 0, 0, 0, 0, false, cullFaces, overrideTexture);
                case EastOnPeripheral ->
                    on_peripheral.renderAttached(this, tessellator, worldSource, tilePos, 0, 3, 0, 0, 0, 0, false, cullFaces, overrideTexture);
            }
        }

        return true;
    }

    @Override
    public @NotNull StaticBlockModel getModel(@NotNull WorldSource source, @NotNull TilePosc tilePosc) {
        return core_any;
    }
}
