package dan200.computercraft.shared.peripheral.printer;

import net.minecraft.client.render.block.model.BlockModelHorizontalRotation;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.world.WorldSource;

public class BlockModelPrinter<T extends BlockLogic> extends BlockModelHorizontalRotation<T> {
    public BlockModelPrinter(Block<T> block) {
        super(block);
    }

    public IconCoordinate getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        int currentMetadata = blockAccess.getBlockMetadata(x, y, z);
        int index = Sides.orientationLookUpHorizontal[6 * Math.min(currentMetadata & 7, 5) + side.getId()];
        if (index >= Sides.orientationLookUpHorizontal.length) {
            return this.blockTextures.get(Side.BOTTOM);
        } else if (index == Side.NORTH.getId()) {
            IconCoordinate originalFront = this.blockTextures.get(Side.NORTH);

            final boolean currentBottom = ((currentMetadata >> 3) & 0b1) == 1;
            final boolean currentTop = ((currentMetadata >> 4) & 0b1) == 1;

            if (currentBottom && !currentTop) {
                return TextureRegistry.getTexture(originalFront.namespaceId.namespace() + ":block/printer_front_bottom_tray");
            }

            if (!currentBottom && currentTop) {
                return TextureRegistry.getTexture(originalFront.namespaceId.namespace() + ":block/printer_front_top_tray");
            }

            if (currentBottom && currentTop) {
                return TextureRegistry.getTexture(originalFront.namespaceId.namespace() + ":block/printer_front_both_trays");
            }

            return originalFront;
        } else {
            return this.blockTextures.get(Side.getSideById(index));
        }
    }
}
