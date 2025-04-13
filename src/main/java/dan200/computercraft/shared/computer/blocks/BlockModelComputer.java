package dan200.computercraft.shared.computer.blocks;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.shared.computer.core.ComputerState;
import net.minecraft.client.render.block.model.BlockModelHorizontalRotation;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
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
        int currentMetadata = blockAccess.getBlockMetadata(x, y, z);
        int index = Sides.orientationLookUpHorizontal[6 * Math.min(currentMetadata & 7, 5) + side.getId()];
        if (index >= Sides.orientationLookUpHorizontal.length) {
            return this.blockTextures.get(Side.BOTTOM);
        } else if (index == Side.NORTH.getId()) {
            IconCoordinate originalFront = this.blockTextures.get(Side.NORTH);

            final ComputerState currentState = ComputerState.class.getEnumConstants()[(currentMetadata >> 3) & 0b11];

            ComputerCraft.log.info("hoi");

            switch (currentState) {
                case ON:
                    return TextureRegistry.getTexture(originalFront.namespaceId.namespace() + ":block/" + originalFront.namespaceId.value() + "_on");
                case BLINKING:
                    return TextureRegistry.getTexture(originalFront.namespaceId.namespace() + ":block/" + originalFront.namespaceId.value() + "_blink");
                case OFF:
                default:
                    return originalFront;
            }
        } else {
            return this.blockTextures.get(Side.getSideById(index));
        }
    }

    public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int currentMetadata) {
        int index = Sides.orientationLookUpHorizontal[6 * Math.min(currentMetadata & 7, 5) + side.getId()];
        if (index >= Sides.orientationLookUpHorizontal.length) {
            return this.blockTextures.get(Side.BOTTOM);
        } else if (index == Side.NORTH.getId()) {
            IconCoordinate originalFront = this.blockTextures.get(Side.NORTH);

            final ComputerState currentState = ComputerState.class.getEnumConstants()[(currentMetadata >> 3) & 0b11];

            switch (currentState) {
                case ON:
                    return TextureRegistry.getTexture(originalFront.namespaceId.namespace() + ":block/" + originalFront.namespaceId.value() + "_on");
                case BLINKING:
                    return TextureRegistry.getTexture(originalFront.namespaceId.namespace() + ":block/" + originalFront.namespaceId.value() + "_blink");
                case OFF:
                default:
                    return originalFront;
            }
        } else {
            return this.blockTextures.get(Side.getSideById(index));
        }
    }
}
