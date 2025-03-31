package dan200.computercraft.shared.turtle.blocks;

import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.util.phys.AABB;

public class BlockAORenderer {
    private final AABB bounds;

    private double bottomU1 = 0;
    private double bottomV1 = 0;
    private double bottomU2 = 0;
    private double bottomV2 = 0;
    private double topU1 = 0;
    private double topV1 = 0;
    private double topU2 = 0;
    private double topV2 = 0;
    private double northU1 = 0;
    private double northV1 = 0;
    private double northU2 = 0;
    private double northV2 = 0;
    private double southU1 = 0;
    private double southV1 = 0;
    private double southU2 = 0;
    private double southV2 = 0;
    private double westU1 = 0;
    private double westV1 = 0;
    private double westU2 = 0;
    private double westV2 = 0;
    private double eastU1 = 0;
    private double eastV1 = 0;
    private double eastU2 = 0;
    private double eastV2 = 0;

    BlockAORenderer(
        AABB bounds
    ) {
        this.bounds = bounds;
    }

    public BlockAORenderer setBottomUV(
        double bottomU1, double bottomV1, double bottomU2, double bottomV2
    ) {
        this.bottomU1 = bottomU1;
        this.bottomV1 = bottomV1;
        this.bottomU2 = bottomU2;
        this.bottomV2 = bottomV2;

        return this;
    }

    public BlockAORenderer setTopUV(
        double topU1, double topV1, double topU2, double topV2
    ) {
        this.topU1 = topU1;
        this.topV1 = topV1;
        this.topU2 = topU2;
        this.topV2 = topV2;

        return this;
    }

    public BlockAORenderer setNorthUV(
        double northU1, double northV1, double northU2, double northV2
    ) {
        this.northU1 = northU1;
        this.northV1 = northV1;
        this.northU2 = northU2;
        this.northV2 = northV2;

        return this;
    }

    public BlockAORenderer setSouthUV(
        double southU1, double southV1, double southU2, double southV2
    ) {
        this.southU1 = southU1;
        this.southV1 = southV1;
        this.southU2 = southU2;
        this.southV2 = southV2;

        return this;
    }

    public BlockAORenderer setWestUV(
        double westU1, double westV1, double westU2, double westV2
    ) {
        this.westU1 = westU1;
        this.westV1 = westV1;
        this.westU2 = westU2;
        this.westV2 = westV2;

        return this;
    }

    public BlockAORenderer setEastUV(
        double eastU1, double eastV1, double eastU2, double eastV2
    ) {
        this.eastU1 = eastU1;
        this.eastV1 = eastV1;
        this.eastU2 = eastU2;
        this.eastV2 = eastV2;

        return this;
    }

    public boolean render(Tessellator tessellator, TileEntity tileEntity, float angle, float r, float g, float b
    ) {
        int meta = tileEntity.getBlockMeta();
        boolean somethingRendered = false;

        int sideId = Side.NORTH.getId();

        angle = (angle % 360 + 360) % 360;

        switch (Math.round(angle / 90.0f) % 4) {
            case 0:
                sideId = Side.SOUTH.getId();
                break;
            case 1:
                sideId = Side.EAST.getId();
                break;
            case 2:
                sideId = Side.NORTH.getId();
                break;
            case 3:
                sideId = Side.WEST.getId();
                break;
        }

        for (Side side : Side.sides) {
            somethingRendered |= renderSide(tessellator, tileEntity, bounds, sideId, tileEntity.x, tileEntity.y, tileEntity.z, r, g, b, side, meta
            );
        }

        return somethingRendered;
    }


    private boolean renderSide(
        Tessellator tessellator, TileEntity tileEntity, AABB bounds, int sideId, int x, int y, int z, float r, float g, float b, Side side, int meta) {

        int index = Sides.orientationLookUpHorizontal[6 * Math.min(sideId, 5) + side.getId()];

        switch (side) {
            case BOTTOM: {
                return this.renderSide(
                    tessellator,
                    tileEntity,
                    bounds,
                    x,
                    y,
                    z,
                    r,
                    g,
                    b,
                    index,
                    meta,
                    0,
                    -1,
                    0,
                    (float) bounds.minY,
                    0,
                    0,
                    1,
                    (float) bounds.maxZ,
                    (float) bounds.minZ,
                    -1,
                    0,
                    0,
                    1.0F - (float) bounds.minX,
                    1.0F - (float) bounds.maxX
                );
            }
            case TOP: {
                return this.renderSide(
                    tessellator,
                    tileEntity,
                    bounds,
                    x,
                    y,
                    z,
                    r,
                    g,
                    b,
                    index,
                    meta,
                    0,
                    1,
                    0,
                    1.0F - (float) bounds.maxY,
                    0,
                    0,
                    1,
                    (float) bounds.maxZ,
                    (float) bounds.minZ,
                    -1,
                    0,
                    0,
                    (float) bounds.maxX,
                    (float) bounds.minX
                );
            }
            case NORTH: {
                return this.renderSide(
                    tessellator,
                    tileEntity,
                    bounds,
                    x,
                    y,
                    z,
                    r,
                    g,
                    b,
                    index,
                    meta,
                    0,
                    0,
                    -1,
                    (float) bounds.minZ,
                    -1,
                    0,
                    0,
                    1.0F - (float) bounds.minX,
                    1.0F - (float) bounds.maxX,
                    0,
                    -1,
                    0,
                    (float) bounds.maxY,
                    (float) bounds.minY
                );
            }
            case SOUTH: {
                return this.renderSide(
                    tessellator,
                    tileEntity,
                    bounds,
                    x,
                    y,
                    z,
                    r,
                    g,
                    b,
                    index,
                    meta,
                    0,
                    0,
                    1,
                    1.0F - (float) bounds.maxZ,
                    0,
                    1,
                    0,
                    (float) bounds.maxY,
                    (float) bounds.minY,
                    -1,
                    0,
                    0,
                    1.0F - (float) bounds.minX,
                    1.0F - (float) bounds.maxX
                );
            }
            case WEST: {
                return this.renderSide(
                    tessellator,
                    tileEntity,
                    bounds,
                    x,
                    y,
                    z,
                    r,
                    g,
                    b,
                    index,
                    meta,
                    1,
                    0,
                    0,
                    (float) bounds.minX,
                    0,
                    0,
                    1,
                    (float) bounds.maxZ,
                    (float) bounds.minZ,
                    0,
                    -1,
                    0,
                    (float) bounds.maxY,
                    (float) bounds.minY
                );
            }
            case EAST: {
                return this.renderSide(
                    tessellator,
                    tileEntity,
                    bounds,
                    x,
                    y,
                    z,
                    r,
                    g,
                    b,
                    index,
                    meta,
                    -1,
                    0,
                    0,
                    1.0F - (float) bounds.maxX,
                    0,
                    0,
                    1,
                    (float) bounds.maxZ,
                    (float) bounds.minZ,
                    0,
                    -1,
                    0,
                    1.0F - (float) bounds.minY,
                    1.0F - (float) bounds.maxY
                );
            }
            default:
                throw new IllegalArgumentException("Side " + side + " not expected!");
        }
    }


    private boolean renderSide(
        Tessellator tessellator,
        TileEntity tileEntity,
        AABB bounds,
        int x,
        int y,
        int z,
        float r,
        float g,
        float b,
        int side,
        int meta,
        int dirX,
        int dirY,
        int dirZ,
        float depth,
        int topX,
        int topY,
        int topZ,
        float topP,
        float botP,
        int lefX,
        int lefY,
        int lefZ,
        float lefP,
        float rigP
    ) {
        boolean rendered = false;
        BlockModel.renderBlocks.setupLighting(tileEntity.getBlock(), x, y, z, r, g, b, side, meta, dirX, dirY, dirZ, depth, topX, topY, topZ, topP, botP, lefX, lefY, lefZ, lefP, rigP);

        if (side == 0) {
            this.renderBottomFace(tessellator, bounds, bottomU1, bottomV1, bottomU2, bottomV2);
        } else if (side == 1) {
            this.renderTopFace(tessellator, bounds, topU1, topV1, topU2, topV2);
        } else if (side == 2) {
            this.renderNorthFace(tessellator, bounds, northU1, northV1, northU2, northV2);
        } else if (side == 3) {
            this.renderSouthFace(tessellator, bounds, southU1, southV1, southU2, southV2);
        } else if (side == 4) {
            this.renderWestFace(tessellator, bounds, westU1, westV1, westU2, westV2);
        } else if (side == 5) {
            this.renderEastFace(tessellator, bounds, eastU1, eastV1, eastU2, eastV2);
        }

        rendered = true;

        return rendered;
    }

    private void renderBottomFace(Tessellator tessellator, AABB bounds, double u1, double v1, double u2, double v2) {
        if (u1 == 0 && v1 == 0 && u2 == 0 && v2 == 0) {
            return;
        }

        double d11 = 1 - bounds.minX;
        double d12 = 1 - bounds.maxX;
        double d13 = 1 - bounds.minY;
        double d14 = 1 - bounds.minZ;
        double d15 = 1 - bounds.maxZ;
        if (BlockModel.renderBlocks.enableAO) {
            if (LightmapHelper.isLightmapEnabled()) {
                tessellator.setLightmapCoord(BlockModel.renderBlocks.lightmapCoordTopLeft);
            }

            tessellator.setColorOpaque_F(BlockModel.renderBlocks.colorRedTopLeft, BlockModel.renderBlocks.colorGreenTopLeft, BlockModel.renderBlocks.colorBlueTopLeft);
            tessellator.addVertexWithUV(d11, d13, d15, u1, v1);
            if (LightmapHelper.isLightmapEnabled()) {
                tessellator.setLightmapCoord(BlockModel.renderBlocks.lightmapCoordBottomLeft);
            }

            tessellator.setColorOpaque_F(BlockModel.renderBlocks.colorRedBottomLeft, BlockModel.renderBlocks.colorGreenBottomLeft, BlockModel.renderBlocks.colorBlueBottomLeft);
            tessellator.addVertexWithUV(d11, d13, d14, u1, v2);
            if (LightmapHelper.isLightmapEnabled()) {
                tessellator.setLightmapCoord(BlockModel.renderBlocks.lightmapCoordBottomRight);
            }

            tessellator.setColorOpaque_F(BlockModel.renderBlocks.colorRedBottomRight, BlockModel.renderBlocks.colorGreenBottomRight, BlockModel.renderBlocks.colorBlueBottomRight);
            tessellator.addVertexWithUV(d12, d13, d14, u2, v2);
            if (LightmapHelper.isLightmapEnabled()) {
                tessellator.setLightmapCoord(BlockModel.renderBlocks.lightmapCoordTopRight);
            }

            tessellator.setColorOpaque_F(BlockModel.renderBlocks.colorRedTopRight, BlockModel.renderBlocks.colorGreenTopRight, BlockModel.renderBlocks.colorBlueTopRight);
            tessellator.addVertexWithUV(d12, d13, d15, u2, v1);
        } else {
            tessellator.addVertexWithUV(d11, d13, d15, u1, v1);
            tessellator.addVertexWithUV(d11, d13, d14, u1, v2);
            tessellator.addVertexWithUV(d12, d13, d14, u2, v2);
            tessellator.addVertexWithUV(d12, d13, d15, u2, v1);
        }
    }

    private void renderTopFace(Tessellator tessellator, AABB bounds, double u1, double v1, double u2, double v2) {
        if (u1 == 0 && v1 == 0 && u2 == 0 && v2 == 0) {
            return;
        }

        double d11 = 1 - bounds.minX;
        double d12 = 1 - bounds.maxX;
        double d13 = 1 - bounds.maxY;
        double d14 = 1 - bounds.minZ;
        double d15 = 1 - bounds.maxZ;
        if (BlockModel.renderBlocks.enableAO) {
            if (LightmapHelper.isLightmapEnabled()) {
                tessellator.setLightmapCoord(BlockModel.renderBlocks.lightmapCoordTopLeft);
            }

            tessellator.setColorOpaque_F(BlockModel.renderBlocks.colorRedTopLeft, BlockModel.renderBlocks.colorGreenTopLeft, BlockModel.renderBlocks.colorBlueTopLeft);
            tessellator.addVertexWithUV(d12, d13, d15, u2, v2);
            if (LightmapHelper.isLightmapEnabled()) {
                tessellator.setLightmapCoord(BlockModel.renderBlocks.lightmapCoordBottomLeft);
            }

            tessellator.setColorOpaque_F(BlockModel.renderBlocks.colorRedBottomLeft, BlockModel.renderBlocks.colorGreenBottomLeft, BlockModel.renderBlocks.colorBlueBottomLeft);
            tessellator.addVertexWithUV(d12, d13, d14, u2, v1);
            if (LightmapHelper.isLightmapEnabled()) {
                tessellator.setLightmapCoord(BlockModel.renderBlocks.lightmapCoordBottomRight);
            }

            tessellator.setColorOpaque_F(BlockModel.renderBlocks.colorRedBottomRight, BlockModel.renderBlocks.colorGreenBottomRight, BlockModel.renderBlocks.colorBlueBottomRight);
            tessellator.addVertexWithUV(d11, d13, d14, u1, v1);
            if (LightmapHelper.isLightmapEnabled()) {
                tessellator.setLightmapCoord(BlockModel.renderBlocks.lightmapCoordTopRight);
            }

            tessellator.setColorOpaque_F(BlockModel.renderBlocks.colorRedTopRight, BlockModel.renderBlocks.colorGreenTopRight, BlockModel.renderBlocks.colorBlueTopRight);
            tessellator.addVertexWithUV(d11, d13, d15, u1, v2);
        } else {
            tessellator.addVertexWithUV(d12, d13, d15, u2, v2);
            tessellator.addVertexWithUV(d12, d13, d14, u2, v1);
            tessellator.addVertexWithUV(d11, d13, d14, u1, v1);
            tessellator.addVertexWithUV(d11, d13, d15, u1, v2);
        }
    }

    private void renderNorthFace(Tessellator tessellator, AABB bounds, double u1, double v1, double u2, double v2) {
        if (u1 == 0 && v1 == 0 && u2 == 0 && v2 == 0) {
            return;
        }

        double d12 = 1 - bounds.minX;
        double d13 = 1 - bounds.maxX;
        double d14 = 1 - bounds.minY;
        double d15 = 1 - bounds.maxY;
        double d16 = 1 - bounds.minZ;
        if (BlockModel.renderBlocks.enableAO) {
            if (LightmapHelper.isLightmapEnabled()) {
                tessellator.setLightmapCoord(BlockModel.renderBlocks.lightmapCoordTopLeft);
            }

            tessellator.setColorOpaque_F(BlockModel.renderBlocks.colorRedTopLeft, BlockModel.renderBlocks.colorGreenTopLeft, BlockModel.renderBlocks.colorBlueTopLeft);
            tessellator.addVertexWithUV(d12, d15, d16, u2, v1);
            if (LightmapHelper.isLightmapEnabled()) {
                tessellator.setLightmapCoord(BlockModel.renderBlocks.lightmapCoordBottomLeft);
            }

            tessellator.setColorOpaque_F(BlockModel.renderBlocks.colorRedBottomLeft, BlockModel.renderBlocks.colorGreenBottomLeft, BlockModel.renderBlocks.colorBlueBottomLeft);
            tessellator.addVertexWithUV(d13, d15, d16, u1, v1);
            if (LightmapHelper.isLightmapEnabled()) {
                tessellator.setLightmapCoord(BlockModel.renderBlocks.lightmapCoordBottomRight);
            }

            tessellator.setColorOpaque_F(BlockModel.renderBlocks.colorRedBottomRight, BlockModel.renderBlocks.colorGreenBottomRight, BlockModel.renderBlocks.colorBlueBottomRight);
            tessellator.addVertexWithUV(d13, d14, d16, u1, v2);
            if (LightmapHelper.isLightmapEnabled()) {
                tessellator.setLightmapCoord(BlockModel.renderBlocks.lightmapCoordTopRight);
            }

            tessellator.setColorOpaque_F(BlockModel.renderBlocks.colorRedTopRight, BlockModel.renderBlocks.colorGreenTopRight, BlockModel.renderBlocks.colorBlueTopRight);
            tessellator.addVertexWithUV(d12, d14, d16, u2, v2);
        } else {
            tessellator.addVertexWithUV(d12, d15, d16, u2, v1);
            tessellator.addVertexWithUV(d13, d15, d16, u1, v1);
            tessellator.addVertexWithUV(d13, d14, d16, u1, v2);
            tessellator.addVertexWithUV(d12, d14, d16, u2, v2);
        }
    }

    private void renderSouthFace(Tessellator tessellator, AABB bounds, double u1, double v1, double u2, double v2) {
        if (u1 == 0 && v1 == 0 && u2 == 0 && v2 == 0) {
            return;
        }

        double x0 = 1 - bounds.minX;
        double x1 = 1 - bounds.maxX;
        double y0 = 1 - bounds.minY;
        double y1 = 1 - bounds.maxY;
        double z0 = 1 - bounds.maxZ;
        if (BlockModel.renderBlocks.enableAO) {
            if (LightmapHelper.isLightmapEnabled()) {
                tessellator.setLightmapCoord(BlockModel.renderBlocks.lightmapCoordTopLeft);
            }

            tessellator.setColorOpaque_F(BlockModel.renderBlocks.colorRedTopLeft, BlockModel.renderBlocks.colorGreenTopLeft, BlockModel.renderBlocks.colorBlueTopLeft);
            tessellator.addVertexWithUV(x0, y1, z0, u1, v1);
            if (LightmapHelper.isLightmapEnabled()) {
                tessellator.setLightmapCoord(BlockModel.renderBlocks.lightmapCoordBottomLeft);
            }

            tessellator.setColorOpaque_F(BlockModel.renderBlocks.colorRedBottomLeft, BlockModel.renderBlocks.colorGreenBottomLeft, BlockModel.renderBlocks.colorBlueBottomLeft);
            tessellator.addVertexWithUV(x0, y0, z0, u1, v2);
            if (LightmapHelper.isLightmapEnabled()) {
                tessellator.setLightmapCoord(BlockModel.renderBlocks.lightmapCoordBottomRight);
            }

            tessellator.setColorOpaque_F(BlockModel.renderBlocks.colorRedBottomRight, BlockModel.renderBlocks.colorGreenBottomRight, BlockModel.renderBlocks.colorBlueBottomRight);
            tessellator.addVertexWithUV(x1, y0, z0, u2, v2);
            if (LightmapHelper.isLightmapEnabled()) {
                tessellator.setLightmapCoord(BlockModel.renderBlocks.lightmapCoordTopRight);
            }

            tessellator.setColorOpaque_F(BlockModel.renderBlocks.colorRedTopRight, BlockModel.renderBlocks.colorGreenTopRight, BlockModel.renderBlocks.colorBlueTopRight);
            tessellator.addVertexWithUV(x1, y1, z0, u2, v1);
        } else {
            tessellator.addVertexWithUV(x0, y1, z0, u1, v1);
            tessellator.addVertexWithUV(x0, y0, z0, u1, v2);
            tessellator.addVertexWithUV(x1, y0, z0, u2, v2);
            tessellator.addVertexWithUV(x1, y1, z0, u2, v1);
        }
    }

    private void renderWestFace(Tessellator tessellator, AABB bounds, double u1, double v1, double u2, double v2) {
        if (u1 == 0 && v1 == 0 && u2 == 0 && v2 == 0) {
            return;
        }

        double d12 = 1 - bounds.minX;
        double d13 = 1 - bounds.minY;
        double d14 = 1 - bounds.maxY;
        double d15 = 1 - bounds.minZ;
        double d16 = 1 - bounds.maxZ;
        if (BlockModel.renderBlocks.enableAO) {
            if (LightmapHelper.isLightmapEnabled()) {
                tessellator.setLightmapCoord(BlockModel.renderBlocks.lightmapCoordTopLeft);
            }

            tessellator.setColorOpaque_F(BlockModel.renderBlocks.colorRedTopLeft, BlockModel.renderBlocks.colorGreenTopLeft, BlockModel.renderBlocks.colorBlueTopLeft);
            tessellator.addVertexWithUV(d12, d14, d16, u2, v1);
            if (LightmapHelper.isLightmapEnabled()) {
                tessellator.setLightmapCoord(BlockModel.renderBlocks.lightmapCoordBottomLeft);
            }

            tessellator.setColorOpaque_F(BlockModel.renderBlocks.colorRedBottomLeft, BlockModel.renderBlocks.colorGreenBottomLeft, BlockModel.renderBlocks.colorBlueBottomLeft);
            tessellator.addVertexWithUV(d12, d14, d15, u1, v1);
            if (LightmapHelper.isLightmapEnabled()) {
                tessellator.setLightmapCoord(BlockModel.renderBlocks.lightmapCoordBottomRight);
            }

            tessellator.setColorOpaque_F(BlockModel.renderBlocks.colorRedBottomRight, BlockModel.renderBlocks.colorGreenBottomRight, BlockModel.renderBlocks.colorBlueBottomRight);
            tessellator.addVertexWithUV(d12, d13, d15, u1, v2);
            if (LightmapHelper.isLightmapEnabled()) {
                tessellator.setLightmapCoord(BlockModel.renderBlocks.lightmapCoordTopRight);
            }

            tessellator.setColorOpaque_F(BlockModel.renderBlocks.colorRedTopRight, BlockModel.renderBlocks.colorGreenTopRight, BlockModel.renderBlocks.colorBlueTopRight);
            tessellator.addVertexWithUV(d12, d13, d16, u2, v2);
        } else {
            tessellator.addVertexWithUV(d12, d14, d16, u2, v1);
            tessellator.addVertexWithUV(d12, d14, d15, u1, v1);
            tessellator.addVertexWithUV(d12, d13, d15, u1, v2);
            tessellator.addVertexWithUV(d12, d13, d16, u2, v2);
        }
    }

    private void renderEastFace(Tessellator tessellator, AABB bounds, double u1, double v1, double u2, double v2) {
        if (u1 == 0 && v1 == 0 && u2 == 0 && v2 == 0) {
            return;
        }

        double xMin = 1 - bounds.maxX;
        double yMin = 1 - bounds.minY;
        double yMax = 1 - bounds.maxY;
        double zMin = 1 - bounds.minZ;
        double zMax = 1 - bounds.maxZ;
        if (BlockModel.renderBlocks.enableAO) {
            if (LightmapHelper.isLightmapEnabled()) {
                tessellator.setLightmapCoord(BlockModel.renderBlocks.lightmapCoordTopLeft);
            }

            tessellator.setColorOpaque_F(BlockModel.renderBlocks.colorRedTopLeft, BlockModel.renderBlocks.colorGreenTopLeft, BlockModel.renderBlocks.colorBlueTopLeft);
            tessellator.addVertexWithUV(xMin, yMin, zMax, u1, v2);
            if (LightmapHelper.isLightmapEnabled()) {
                tessellator.setLightmapCoord(BlockModel.renderBlocks.lightmapCoordBottomLeft);
            }

            tessellator.setColorOpaque_F(BlockModel.renderBlocks.colorRedBottomLeft, BlockModel.renderBlocks.colorGreenBottomLeft, BlockModel.renderBlocks.colorBlueBottomLeft);
            tessellator.addVertexWithUV(xMin, yMin, zMin, u2, v2);
            if (LightmapHelper.isLightmapEnabled()) {
                tessellator.setLightmapCoord(BlockModel.renderBlocks.lightmapCoordBottomRight);
            }

            tessellator.setColorOpaque_F(BlockModel.renderBlocks.colorRedBottomRight, BlockModel.renderBlocks.colorGreenBottomRight, BlockModel.renderBlocks.colorBlueBottomRight);
            tessellator.addVertexWithUV(xMin, yMax, zMin, u2, v1);
            if (LightmapHelper.isLightmapEnabled()) {
                tessellator.setLightmapCoord(BlockModel.renderBlocks.lightmapCoordTopRight);
            }

            tessellator.setColorOpaque_F(BlockModel.renderBlocks.colorRedTopRight, BlockModel.renderBlocks.colorGreenTopRight, BlockModel.renderBlocks.colorBlueTopRight);
            tessellator.addVertexWithUV(xMin, yMax, zMax, u1, v1);
        } else {
            tessellator.addVertexWithUV(xMin, yMin, zMax, u1, v2);
            tessellator.addVertexWithUV(xMin, yMin, zMin, u2, v2);
            tessellator.addVertexWithUV(xMin, yMax, zMin, u2, v1);
            tessellator.addVertexWithUV(xMin, yMax, zMax, u1, v1);
        }
    }
}
