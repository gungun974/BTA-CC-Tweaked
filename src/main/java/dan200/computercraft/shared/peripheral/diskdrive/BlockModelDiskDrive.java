package dan200.computercraft.shared.peripheral.diskdrive;

import net.minecraft.client.render.block.model.BlockModelHorizontalRotation;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.world.WorldSource;

public class BlockModelDiskDrive<T extends BlockLogic> extends BlockModelHorizontalRotation<T> {
    public BlockModelDiskDrive(Block<T> block) {
        super(block);
    }

    public IconCoordinate getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        int currentMetadata = blockAccess.getBlockMetadata(x, y, z);
        int index = Sides.orientationLookUpHorizontal[6 * Math.min(currentMetadata & 7, 5) + side.getId()];
        if (index >= Sides.orientationLookUpHorizontal.length) {
            return this.blockTextures.get(Side.BOTTOM);
        } else if (index == Side.NORTH.getId()) {
            IconCoordinate originalFront = this.blockTextures.get(Side.NORTH);

            final DiskDriveState currentState = DiskDriveState.class.getEnumConstants()[(currentMetadata >> 3) & 0b11];

            switch (currentState) {
                case FULL:
                    return TextureRegistry.getTexture(originalFront.namespaceId.namespace() + ":block/" + originalFront.namespaceId.value() + "_accepted");
                case INVALID:
                    return TextureRegistry.getTexture(originalFront.namespaceId.namespace() + ":block/" + originalFront.namespaceId.value() + "_rejected");
                case EMPTY:
                default:
                    return originalFront;
            }
        } else {
            return this.blockTextures.get(Side.getSideById(index));
        }
    }
}
