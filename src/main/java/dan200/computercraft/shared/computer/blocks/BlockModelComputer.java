package dan200.computercraft.shared.computer.blocks;

import net.minecraft.client.render.block.model.BlockModelHorizontalRotation;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.world.WorldSource;

public class BlockModelComputer<T extends BlockLogic> extends BlockModelHorizontalRotation<T> {
    public BlockModelComputer(Block<T> block) {
        super(block);
    }

    public IconCoordinate getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        int data = blockAccess.getBlockMetadata(x, y, z);
        int index = Sides.orientationLookUpHorizontal[6 * Math.min(data, 5) + side.getId()];
        if (index >= Sides.orientationLookUpHorizontal.length) {
            return this.blockTextures.get(Side.BOTTOM);
        } else if (index == Side.NORTH.getId()) {
            IconCoordinate originalFront = this.blockTextures.get(Side.NORTH);
//            Container container = (Container)blockAccess.getTileEntity(x, y, z);
//            if (container != null) {
//                boolean hasOutput = container.getItem(2) != null;
//                if (hasOutput) {
//                    return TextureRegistry.getTexture(originalFront.namespaceId.namespace + ":block/" + originalFront.namespaceId.value + "_filled");
//                }
//            }

            return originalFront;
        } else {
            return this.blockTextures.get(Side.getSideById(index));
        }
    }
}
