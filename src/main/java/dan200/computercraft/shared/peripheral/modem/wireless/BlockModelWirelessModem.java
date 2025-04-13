package dan200.computercraft.shared.peripheral.modem.wireless;

import dan200.computercraft.shared.peripheral.modem.ModemShapes;
import net.minecraft.client.render.block.color.BlockColor;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelFullyRotatable;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicFullyRotatable;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.WorldSource;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

public class BlockModelWirelessModem<T extends BlockLogic> extends BlockModelFullyRotatable<T> {
    public BlockModelWirelessModem(Block<T> block) {
        super(block);
    }

    @Override
    public boolean render(Tessellator tessellator, int x, int y, int z) {
        return super.render(tessellator, x, y, z);
    }

    @Override
    public void renderBlockOnInventory(Tessellator tessellator, int metadata, float brightness, float alpha, @Nullable Integer lightmapCoordinate) {
        Direction dir = BlockLogicFullyRotatable.metaToDirection(metadata);
        switch (dir) {
            case DOWN:
                renderBlocks.uvRotateEast = 3;
                renderBlocks.uvRotateWest = 3;
                renderBlocks.uvRotateSouth = 3;
                renderBlocks.uvRotateNorth = 3;
                break;
            case NORTH:
                renderBlocks.uvRotateSouth = 1;
                renderBlocks.uvRotateNorth = 2;
                break;
            case SOUTH:
                renderBlocks.uvRotateSouth = 2;
                renderBlocks.uvRotateNorth = 1;
                renderBlocks.uvRotateTop = 3;
                renderBlocks.uvRotateBottom = 3;
                break;
            case WEST:
                renderBlocks.uvRotateEast = 1;
                renderBlocks.uvRotateWest = 2;
                renderBlocks.uvRotateTop = 2;
                renderBlocks.uvRotateBottom = 1;
                break;
            case EAST:
                renderBlocks.uvRotateEast = 2;
                renderBlocks.uvRotateWest = 1;
                renderBlocks.uvRotateTop = 1;
                renderBlocks.uvRotateBottom = 2;
        }


        if (renderBlocks.useInventoryTint) {
            int color = ((BlockColor) BlockColorDispatcher.getInstance().getDispatch(this.block)).getFallbackColor(metadata);
            float r = (float)(color >> 16 & 255) / 255.0F;
            float g = (float)(color >> 8 & 255) / 255.0F;
            float b = (float)(color & 255) / 255.0F;
            GL11.glColor4f(r * brightness, g * brightness, b * brightness, alpha);
        } else {
            GL11.glColor4f(brightness, brightness, brightness, alpha);
        }

        float yOffset = 0.5F;
        AABB bounds = ModemShapes.getBounds( BlockWirelessModem.metaToDirection(metadata).getOpposite());
        GL11.glTranslatef(-0.5F, 0.0F - yOffset, -0.5F);
        this.renderBlockWithBounds(tessellator, bounds, metadata, brightness, alpha, lightmapCoordinate);
        GL11.glTranslatef(0.5F, yOffset, 0.5F);

        this.resetRenderBlocks();
    }

    public IconCoordinate getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        int currentMetadata = blockAccess.getBlockMetadata(x, y, z);
        int index = Sides.orientationLookUpHorizontal[6 * Math.min(currentMetadata & 7, 5) + side.getId()];
        if (index >= Sides.orientationLookUpHorizontal.length) {
            return this.blockTextures.get(Side.BOTTOM);
        } else if (index != Side.BOTTOM.getId()) {
            IconCoordinate originalFront = this.blockTextures.get(Side.NORTH);

            final boolean isOn = ((currentMetadata >> 3) & 0b1) == 1;

            if (isOn) {
                return TextureRegistry.getTexture(originalFront.namespaceId.namespace() + ":block/" + originalFront.namespaceId.value() + "_on");
            }

            return originalFront;
        } else {
            return this.blockTextures.get(Side.getSideById(index));
        }
    }



}
