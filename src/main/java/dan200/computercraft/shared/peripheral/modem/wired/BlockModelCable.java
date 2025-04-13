package dan200.computercraft.shared.peripheral.modem.wired;

import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;

public class BlockModelCable<T extends BlockLogic> extends BlockModelStandard<T> {
    public static final IconCoordinate CABLE_CORE = TextureRegistry.getTexture("computercraft:block/cable_core");
    public static final IconCoordinate CABLE_SIDE = TextureRegistry.getTexture("computercraft:block/cable_side");

    public BlockModelCable(Block<T> block) {
        super(block);
    }

    public int mode = 0;

    public boolean render(Tessellator tessellator, int x, int y, int z) {
        AABB bounds = this.block.getBlockBoundsFromState(renderBlocks.blockAccess, x, y, z);

        renderBlocks.enableAO = true;
        renderBlocks.cache.setupCache(this.block, renderBlocks.blockAccess, x, y, z);

        TileEntity tileEntity = renderBlocks.blockAccess.getTileEntity(x, y, z);

        if (tileEntity instanceof TileCable) {
            final TileCable state = (TileCable) tileEntity;

            if (state.blockStateCable) {
                setBounds(bounds, CableShapes.SHAPE_CABLE_CORE);
                if (!state.blockStateNorth) {
                    if (state.blockStateSouth && !state.blockStateWest && !state.blockStateEast && !state.blockStateUp && !state.blockStateDown) {
                        mode = 1;
                    }
                    this.renderSide(tessellator, bounds, x, y, z, Side.NORTH, 0);
                    mode = 0;
                }
                if (!state.blockStateSouth) {
                    if (state.blockStateNorth && !state.blockStateWest && !state.blockStateEast && !state.blockStateUp && !state.blockStateDown) {
                        mode = 1;
                    }
                    this.renderSide(tessellator, bounds, x, y, z, Side.SOUTH, 0);
                    mode = 0;
                }
                if (!state.blockStateEast) {
                    if (state.blockStateWest && !state.blockStateSouth && !state.blockStateNorth && !state.blockStateUp && !state.blockStateDown) {
                        mode = 1;
                    }
                    if (!state.blockStateWest && !state.blockStateSouth && !state.blockStateNorth && !state.blockStateUp && !state.blockStateDown) {
                        mode = 1;
                    }

                    this.renderSide(tessellator, bounds, x, y, z, Side.EAST, 0);
                    mode = 0;
                }
                if (!state.blockStateWest) {
                    if (state.blockStateEast && !state.blockStateSouth && !state.blockStateNorth && !state.blockStateUp && !state.blockStateDown) {
                        mode = 1;
                    }
                    if (!state.blockStateEast && !state.blockStateSouth && !state.blockStateNorth && !state.blockStateUp && !state.blockStateDown) {
                        mode = 1;
                    }
                    this.renderSide(tessellator, bounds, x, y, z, Side.WEST, 0);
                    mode = 0;
                }
                if (!state.blockStateUp) {
                    if (state.blockStateDown && !state.blockStateSouth && !state.blockStateNorth && !state.blockStateWest && !state.blockStateEast) {
                        mode = 1;
                    }
                    this.renderSide(tessellator, bounds, x, y, z, Side.TOP, 0);
                    mode = 0;
                }
                if (!state.blockStateDown) {
                    if (state.blockStateUp && !state.blockStateSouth && !state.blockStateNorth && !state.blockStateWest && !state.blockStateEast) {
                        mode = 1;
                    }
                    this.renderSide(tessellator, bounds, x, y, z, Side.BOTTOM, 0);
                    mode = 0;
                }

                if (state.blockStateNorth) {
                    setBounds(bounds, CableShapes.SHAPE_CABLE_ARM.get(Direction.NORTH));
                    this.renderSide(tessellator, bounds, x, y, z, Side.TOP, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.BOTTOM, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.EAST, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.WEST, 0);
                }
                if (state.blockStateSouth) {
                    setBounds(bounds, CableShapes.SHAPE_CABLE_ARM.get(Direction.SOUTH));
                    this.renderSide(tessellator, bounds, x, y, z, Side.TOP, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.BOTTOM, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.EAST, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.WEST, 0);
                }
                if (state.blockStateEast) {
                    setBounds(bounds, CableShapes.SHAPE_CABLE_ARM.get(Direction.EAST));
                    this.renderSide(tessellator, bounds, x, y, z, Side.TOP, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.BOTTOM, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.NORTH, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.SOUTH, 0);
                }
                if (state.blockStateWest) {
                    setBounds(bounds, CableShapes.SHAPE_CABLE_ARM.get(Direction.WEST));
                    this.renderSide(tessellator, bounds, x, y, z, Side.TOP, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.BOTTOM, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.NORTH, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.SOUTH, 0);
                }
                if (state.blockStateUp) {
                    setBounds(bounds, CableShapes.SHAPE_CABLE_ARM.get(Direction.UP));
                    this.renderSide(tessellator, bounds, x, y, z, Side.NORTH, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.SOUTH, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.EAST, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.WEST, 0);
                }
                if (state.blockStateDown) {
                    setBounds(bounds, CableShapes.SHAPE_CABLE_ARM.get(Direction.DOWN));
                    this.renderSide(tessellator, bounds, x, y, z, Side.NORTH, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.SOUTH, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.EAST, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.WEST, 0);
                }
            }
        }

        this.resetRenderBlocks();
        return true;
    }

    private static void setBounds(AABB bounds, AABB shape) {
        bounds.set(shape.minX, shape.minY, shape.minZ, shape.maxX, shape.maxY, shape.maxZ);
    }


    public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int data) {
        if (mode == 0) {
           return CABLE_SIDE;
        }
        return CABLE_CORE;
    }
}
