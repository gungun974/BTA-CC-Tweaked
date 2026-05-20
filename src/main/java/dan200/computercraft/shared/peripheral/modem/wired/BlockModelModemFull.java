package dan200.computercraft.shared.peripheral.modem.wired;

import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.generic.BlockModelGeneric;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.useless.dragonfly.models.block.StaticBlockModel;

public class BlockModelModemFull<T extends BlockLogic> extends BlockModelGeneric<T> {
    public final @NotNull StaticBlockModel off;
    public final @NotNull StaticBlockModel off_peripheral;
    public final @NotNull StaticBlockModel on;
    public final @NotNull StaticBlockModel on_peripheral;

    public BlockModelModemFull(Block<T> block) {
        super(block, BlockModelDispatcher.loadDataModel("computercraft:block/wired_modem_full_off").asModel());
        this.off = BlockModelDispatcher.loadDataModel("computercraft:block/wired_modem_full_off").asModel();
        this.off_peripheral = BlockModelDispatcher.loadDataModel("computercraft:block/wired_modem_full_off_peripheral").asModel();
        this.on = BlockModelDispatcher.loadDataModel("computercraft:block/wired_modem_full_on").asModel();
        this.on_peripheral = BlockModelDispatcher.loadDataModel("computercraft:block/wired_modem_full_on_peripheral").asModel();
    }

    @Override
    public @NotNull StaticBlockModel getModel(@NotNull WorldSource source, @NotNull TilePosc tilePosc) {
        int currentMetadata = source.getBlockData(tilePosc);

        boolean isModemOn = (currentMetadata & 0b1) == 1;
        boolean isPeripheralOn = (currentMetadata & 0b10) == 2;

        if (isModemOn) {
            if (isPeripheralOn) {
                return this.on_peripheral;
            }
            return this.on;
        }
        if (isPeripheralOn) {
            return this.off_peripheral;
        }
        return this.off;
    }
}
