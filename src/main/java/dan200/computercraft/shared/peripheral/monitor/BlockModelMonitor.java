package dan200.computercraft.shared.peripheral.monitor;

import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.world.WorldSource;

public class BlockModelMonitor<T extends BlockLogic> extends BlockModelStandard<T> {
    private final String texturePrefix;

    public BlockModelMonitor(Block<T> block, String texturePrefix) {
        super(block);
        this.texturePrefix = texturePrefix;
    }

    @Override
    public boolean render(Tessellator tessellator, int x, int y, int z) {
        int meta = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
        Direction dir = BlockMonitor.metaToFacing(meta);
        Direction orientation = BlockMonitor.metaToOrientation(meta);

        if (orientation == Direction.UP) {
            switch (dir) {
                case NORTH:
                    renderBlocks.uvRotateSouth = 2;
                    renderBlocks.uvRotateNorth = 1;
                    renderBlocks.uvRotateTop = 3;
                    renderBlocks.uvRotateBottom = 3;
                    renderBlocks.uvRotateEast = 3;
                    break;
                case SOUTH:
                    renderBlocks.uvRotateSouth = 1;
                    renderBlocks.uvRotateNorth = 2;
                    renderBlocks.uvRotateWest = 3;
                    break;
                case WEST:
                    renderBlocks.uvRotateTop = 1;
                    renderBlocks.uvRotateBottom = 2;
                    renderBlocks.uvRotateEast = 2;
                    renderBlocks.uvRotateWest = 1;
                    renderBlocks.uvRotateNorth = 3;
                    break;
                case EAST:
                    renderBlocks.uvRotateTop = 2;
                    renderBlocks.uvRotateBottom = 1;
                    renderBlocks.uvRotateEast = 1;
                    renderBlocks.uvRotateWest = 2;
                    renderBlocks.uvRotateSouth = 3;
            }
        } else if (orientation == Direction.DOWN) {
            switch (dir) {
                case NORTH:
                    renderBlocks.uvRotateSouth = 1;
                    renderBlocks.uvRotateNorth = 2;
                    renderBlocks.uvRotateEast = 3;
                    break;
                case SOUTH:
                    renderBlocks.uvRotateSouth = 2;
                    renderBlocks.uvRotateNorth = 1;
                    renderBlocks.uvRotateWest = 3;
                    renderBlocks.uvRotateTop = 3;
                    renderBlocks.uvRotateBottom = 3;
                    break;
                case WEST:
                    renderBlocks.uvRotateTop = 2;
                    renderBlocks.uvRotateBottom = 1;
                    renderBlocks.uvRotateEast = 1;
                    renderBlocks.uvRotateWest = 2;
                    renderBlocks.uvRotateNorth = 3;
                    break;
                case EAST:
                    renderBlocks.uvRotateTop = 1;
                    renderBlocks.uvRotateBottom = 2;
                    renderBlocks.uvRotateEast = 2;
                    renderBlocks.uvRotateWest = 1;
                    renderBlocks.uvRotateSouth = 3;
            }
        } else {
            switch (dir) {
                case NORTH:
                    break;
                case SOUTH:
                    renderBlocks.uvRotateTop = 3;
                    renderBlocks.uvRotateBottom = 3;
                    break;
                case WEST:
                    renderBlocks.uvRotateTop = 2;
                    renderBlocks.uvRotateBottom = 1;
                    break;
                case EAST:
                    renderBlocks.uvRotateTop = 1;
                    renderBlocks.uvRotateBottom = 2;
            }
        }

        boolean result = this.renderStandardBlock(tessellator, this.block.getBlockBoundsFromState(renderBlocks.blockAccess, x, y, z), x, y, z);
        this.resetRenderBlocks();
        return result;
    }

    @Override
    public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int data) {
        int index = Sides.orientationLookUpHorizontal[6 * Math.min(data & 7, 5) + side.getId()];
        return index >= Sides.orientationLookUpHorizontal.length
            ? this.blockTextures.get(Side.BOTTOM)
            : super.getBlockTextureFromSideAndMetadata(Side.getSideById(index), data);
    }

    public IconCoordinate getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        int currentMetadata = blockAccess.getBlockMetadata(x, y, z);

        Direction facing = BlockMonitor.metaToFacing(currentMetadata);

        int index = Sides.orientationLookUpHorizontal[6 * Math.min(facing.getId(), 5) + side.getId()];

        MonitorEdgeState edgeState = BlockMonitor.metaToState(currentMetadata);

        Direction orientation = BlockMonitor.metaToOrientation(currentMetadata);

        if (index >= Sides.orientationLookUpHorizontal.length) {
            return this.blockTextures.get(Side.BOTTOM);
        }

        Side orientedSide = Side.getSideById(index);

        IconCoordinate originalFront = this.blockTextures.get(Side.NORTH);

        if (orientation == Direction.UP) {
            switch (orientedSide) {
                case NORTH:
                    orientedSide = Side.BOTTOM;
                    break;
                case BOTTOM:
                    orientedSide = Side.SOUTH;
                    break;
                case SOUTH:
                    orientedSide = Side.TOP;
                    break;
                case TOP:
                    orientedSide = Side.NORTH;
            }
        } else if (orientation == Direction.DOWN) {
            switch (orientedSide) {
                case NORTH:
                    orientedSide = Side.TOP;
                    break;
                case TOP:
                    orientedSide = Side.SOUTH;
                    break;
                case SOUTH:
                    orientedSide = Side.BOTTOM;
                    break;
                case BOTTOM:
                    orientedSide = Side.NORTH;
            }

            if (orientedSide == Side.NORTH && facing != Direction.SOUTH) {
                switch (edgeState) {
                    case NONE:
                        break;
                    case L:
                        edgeState = MonitorEdgeState.R;
                        break;
                    case R:
                        edgeState = MonitorEdgeState.L;
                        break;
                    case LR:
                        break;
                    case U:
                        break;
                    case D:
                        break;
                    case UD:
                        break;
                    case RD:
                        edgeState = MonitorEdgeState.LD;
                        break;
                    case LD:
                        edgeState = MonitorEdgeState.RD;
                        break;
                    case RU:
                        edgeState = MonitorEdgeState.LU;
                        break;
                    case LU:
                        edgeState = MonitorEdgeState.RU;
                        break;
                    case LRD:
                        break;
                    case RUD:
                        edgeState = MonitorEdgeState.LUD;
                        break;
                    case LUD:
                        edgeState = MonitorEdgeState.RUD;
                        break;
                    case LRU:
                        break;
                    case LRUD:
                        break;
                }
            }
        }

        switch (edgeState) {
            case NONE:
                switch (orientedSide) {
                    case NORTH:
                        return TextureRegistry.getTexture(texturePrefix + "_16");
                    case TOP:
                    case BOTTOM:
                        return TextureRegistry.getTexture(texturePrefix + "_0");
                    case SOUTH:
                        return TextureRegistry.getTexture(texturePrefix + "_4");
                    default:
                        return TextureRegistry.getTexture(texturePrefix + "_4");
                }
            case L:
                switch (orientedSide) {
                    case NORTH:
                        return TextureRegistry.getTexture(texturePrefix + "_19");
                    case TOP:
                    case BOTTOM:
                        return TextureRegistry.getTexture(texturePrefix + "_1");
                    case SOUTH:
                        return TextureRegistry.getTexture(texturePrefix + "_33");
                    default:
                        return TextureRegistry.getTexture(texturePrefix + "_4");
                }
            case R:
                switch (orientedSide) {
                    case NORTH:
                        return TextureRegistry.getTexture(texturePrefix + "_17");
                    case TOP:
                    case BOTTOM:
                        return TextureRegistry.getTexture(texturePrefix + "_3");
                    case SOUTH:
                        return TextureRegistry.getTexture(texturePrefix + "_35");
                    default:
                        return TextureRegistry.getTexture(texturePrefix + "_4");
                }
            case LR:
                switch (orientedSide) {
                    case NORTH:
                        return TextureRegistry.getTexture(texturePrefix + "_18");
                    case TOP:
                    case BOTTOM:
                        return TextureRegistry.getTexture(texturePrefix + "_2");
                    case SOUTH:
                        return TextureRegistry.getTexture(texturePrefix + "_34");
                    default:
                        return TextureRegistry.getTexture(texturePrefix + "_4");
                }
            case U:
                switch (orientedSide) {
                    case NORTH:
                        return TextureRegistry.getTexture(texturePrefix + "_22");
                    case TOP:
                    case BOTTOM:
                        return TextureRegistry.getTexture(texturePrefix + "_0");
                    case SOUTH:
                        return TextureRegistry.getTexture(texturePrefix + "_38");
                    default:
                        return TextureRegistry.getTexture(texturePrefix + "_38");
                }
            case D:
                switch (orientedSide) {
                    case NORTH:
                        return TextureRegistry.getTexture(texturePrefix + "_20");
                    case TOP:
                    case BOTTOM:
                        return TextureRegistry.getTexture(texturePrefix + "_0");
                    case SOUTH:
                        return TextureRegistry.getTexture(texturePrefix + "_36");
                    default:
                        return TextureRegistry.getTexture(texturePrefix + "_36");
                }
            case UD:
                switch (orientedSide) {
                    case NORTH:
                        return TextureRegistry.getTexture(texturePrefix + "_21");
                    case TOP:
                    case BOTTOM:
                        return TextureRegistry.getTexture(texturePrefix + "_0");
                    case SOUTH:
                        return TextureRegistry.getTexture(texturePrefix + "_37");
                    default:
                        return TextureRegistry.getTexture(texturePrefix + "_37");
                }
            case RD:
                switch (orientedSide) {
                    case NORTH:
                        return TextureRegistry.getTexture(texturePrefix + "_29");
                    case TOP:
                    case BOTTOM:
                        return TextureRegistry.getTexture(texturePrefix + "_3");
                    case SOUTH:
                        return TextureRegistry.getTexture(texturePrefix + "_47");
                    default:
                        return TextureRegistry.getTexture(texturePrefix + "_7");
                }
            case LD:
                switch (orientedSide) {
                    case NORTH:
                        return TextureRegistry.getTexture(texturePrefix + "_31");
                    case TOP:
                    case BOTTOM:
                        return TextureRegistry.getTexture(texturePrefix + "_1");
                    case SOUTH:
                        return TextureRegistry.getTexture(texturePrefix + "_45");
                    default:
                        return TextureRegistry.getTexture(texturePrefix + "_7");
                }
            case RU:
                switch (orientedSide) {
                    case NORTH:
                        return TextureRegistry.getTexture(texturePrefix + "_23");
                    case TOP:
                    case BOTTOM:
                        return TextureRegistry.getTexture(texturePrefix + "_3");
                    case SOUTH:
                        return TextureRegistry.getTexture(texturePrefix + "_41");
                    default:
                        return TextureRegistry.getTexture(texturePrefix + "_38");
                }
            case LU:
                switch (orientedSide) {
                    case NORTH:
                        return TextureRegistry.getTexture(texturePrefix + "_25");
                    case TOP:
                    case BOTTOM:
                        return TextureRegistry.getTexture(texturePrefix + "_1");
                    case SOUTH:
                        return TextureRegistry.getTexture(texturePrefix + "_39");
                    default:
                        return TextureRegistry.getTexture(texturePrefix + "_38");
                }
            case LRD:
                switch (orientedSide) {
                    case NORTH:
                        return TextureRegistry.getTexture(texturePrefix + "_30");
                    case TOP:
                    case BOTTOM:
                        return TextureRegistry.getTexture(texturePrefix + "_2");
                    case SOUTH:
                        return TextureRegistry.getTexture(texturePrefix + "_46");
                    default:
                        return TextureRegistry.getTexture(texturePrefix + "_7");
                }
            case RUD:
                switch (orientedSide) {
                    case NORTH:
                        return TextureRegistry.getTexture(texturePrefix + "_26");
                    case TOP:
                    case BOTTOM:
                        return TextureRegistry.getTexture(texturePrefix + "_3");
                    case SOUTH:
                        return TextureRegistry.getTexture(texturePrefix + "_44");
                    default:
                        return TextureRegistry.getTexture(texturePrefix + "_37");
                }
            case LUD:
                switch (orientedSide) {
                    case NORTH:
                        return TextureRegistry.getTexture(texturePrefix + "_28");
                    case TOP:
                    case BOTTOM:
                        return TextureRegistry.getTexture(texturePrefix + "_1");
                    case SOUTH:
                        return TextureRegistry.getTexture(texturePrefix + "_42");
                    default:
                        return TextureRegistry.getTexture(texturePrefix + "_37");
                }
            case LRU:
                switch (orientedSide) {
                    case NORTH:
                        return TextureRegistry.getTexture(texturePrefix + "_24");
                    case TOP:
                    case BOTTOM:
                        return TextureRegistry.getTexture(texturePrefix + "_2");
                    case SOUTH:
                        return TextureRegistry.getTexture(texturePrefix + "_40");
                    default:
                        return TextureRegistry.getTexture(texturePrefix + "_38");
                }
            case LRUD:
                switch (orientedSide) {
                    case NORTH:
                        return TextureRegistry.getTexture(texturePrefix + "_27");
                    case TOP:
                    case BOTTOM:
                        return TextureRegistry.getTexture(texturePrefix + "_2");
                    case SOUTH:
                        return TextureRegistry.getTexture(texturePrefix + "_43");
                    default:
                        return TextureRegistry.getTexture(texturePrefix + "_37");
                }
        }

        return originalFront;
    }
}
