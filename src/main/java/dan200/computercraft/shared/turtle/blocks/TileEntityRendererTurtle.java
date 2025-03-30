package dan200.computercraft.shared.turtle.blocks;

import dan200.computercraft.BlockPos;
import dan200.computercraft.ComputerCraft;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.util.phys.Vec3;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class TileEntityRendererTurtle extends TileEntityRenderer<TileTurtle> {
    private final Minecraft mc = Minecraft.getMinecraft();

    public void doRender(Tessellator tessellator, TileTurtle tileEntity, double x, double y, double z, float partialTick) {
        Block<?> block = tileEntity.getBlock();
        if (block != null && block.getLogic() instanceof BlockTurtle) {
            GL11.glEnable(32826);
            GL11.glPushMatrix();

            Vec3 pos = tileEntity.getAccess().getVisualPosition(partialTick);

            GL11.glTranslatef((float)x + (float)pos.x + 1 , (float)y + (float)pos.y + 1, (float)z + (float)pos.z + 1);

            GL11.glTranslatef(-0.5f, 0, -0.5f);

            GL11.glRotatef(-tileEntity.getAccess().getVisualYaw(partialTick) + 180, 0, 1f, 0);

            GL11.glTranslatef(0.5f, 0, 0.5f);

            GL11.glPushMatrix();

            GL11.glScalef(-1, -1, -1);

            this.loadTexture("/assets/computercraft/textures/block/turtle_advanced.png");

            tessellator.startDrawingQuads();

            BlockModel.renderBlocks.enableAO = true;

            BlockModel.renderBlocks.cache.setupCache(block, tileEntity.worldObj, tileEntity.x, tileEntity.y, tileEntity.z);

            AABB aabb = AABB.getTemporaryBB(2/16f, 2/16f, 2/16f, 14/16f, 14/16f, 13/16f);

            renderStandardBlock(tessellator, tileEntity, aabb, tileEntity.x, tileEntity.y, tileEntity.z, 1f, 1f, 1f,
                5.75 / 16, 2.75 / 16, 2.75 / 16, 0,
                8.75 / 16, 0, 5.75 / 16, 2.75 / 16,
                11.5 / 16, 5.75 / 16, 8.5 / 16, 2.75 / 16,
                5.75 / 16, 5.75 / 16, 2.75 / 16, 2.75 / 16,
                8.5 / 16, 5.75 / 16, 5.75 / 16, 2.75 / 16,
                2.75 / 16, 5.75 / 16, 0, 2.75 / 16
                );

            aabb = AABB.getTemporaryBB(3/16f, 6/16f, 13/16f, 13/16f, 13/16f, 15/16f);

            renderStandardBlock(tessellator, tileEntity, aabb, tileEntity.x, tileEntity.y, tileEntity.z, 1f, 1f, 1f,
                11.75 / 16, 0.5 / 16, 9.25 / 16, 0,
                14.25 / 16, 0, 11.75 / 16, 0.5 / 16,
                0, 0, 0, 0,
                11.75 / 16, 2.25 / 16, 9.25 / 16, 0.5 / 16,
                12.25 / 16, 2.25 / 16, 11.75 / 16, 0.5 / 16,
                9.25 / 16, 2.25 / 16, 8.75 / 16, 0.5 / 16
            );

            BlockModel.renderBlocks.enableAO = false;

            tessellator.draw();

            GL11.glPopMatrix();

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
            GL11.glDisable(32826);
        }
    }

    public boolean renderStandardBlock(Tessellator tessellator, TileEntity tileEntity, AABB bounds, int x, int y, int z, float r, float g, float b,
                                       double bottomU1, double bottomV1, double bottomU2, double bottomV2,
                                       double topU1, double topV1, double topU2, double topV2,
                                       double northU1, double northV1, double northU2, double northV2,
                                       double southU1, double southV1, double southU2, double southV2,
                                       double westU1, double westV1, double westU2, double westV2,
                                       double eastU1, double eastV1, double eastU2, double eastV2

                                       ) {
        int meta = tileEntity.getBlockMeta();
        boolean somethingRendered = false;

        for (Side side : Side.sides) {
            somethingRendered |= renderSide(tessellator, tileEntity, bounds, x, y, z, r, g, b, side, meta,
                bottomU1, bottomV1, bottomU2, bottomV2,
                topU1, topV1, topU2, topV2,
                northU1, northV1, northU2, northV2,
                southU1, southV1, southU2, southV2,
                westU1, westV1, westU2, westV2,
                eastU1, eastV1, eastU2, eastV2

                );
        }

        return somethingRendered;
    }


    public final boolean renderSide(
        Tessellator tessellator, TileEntity tileEntity, AABB bounds, int x, int y, int z, float r, float g, float b, Side side, int meta,
        double bottomU1, double bottomV1, double bottomU2, double bottomV2,
        double topU1, double topV1, double topU2, double topV2,
        double northU1, double northV1, double northU2, double northV2,
        double southU1, double southV1, double southU2, double southV2,
        double westU1, double westV1, double westU2, double westV2,
        double eastU1, double eastV1, double eastU2, double eastV2
    ) {
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
                    0,
                    meta,
                    0,
                    -1,
                    0,
                    (float)bounds.minY,
                    0,
                    0,
                    1,
                    (float)bounds.maxZ,
                    (float)bounds.minZ,
                    -1,
                    0,
                    0,
                    1.0F - (float)bounds.minX,
                    1.0F - (float)bounds.maxX,
                bottomU1, bottomV1, bottomU2, bottomV2,
                topU1, topV1, topU2, topV2,
                northU1, northV1, northU2, northV2,
                southU1, southV1, southU2, southV2,
                westU1, westV1, westU2, westV2,
                eastU1, eastV1, eastU2, eastV2
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
                    1,
                    meta,
                    0,
                    1,
                    0,
                    1.0F - (float)bounds.maxY,
                    0,
                    0,
                    1,
                    (float)bounds.maxZ,
                    (float)bounds.minZ,
                    1,
                    0,
                    0,
                    (float)bounds.maxX,
                    (float)bounds.minX,
                    bottomU1, bottomV1, bottomU2, bottomV2,
                    topU1, topV1, topU2, topV2,
                    northU1, northV1, northU2, northV2,
                    southU1, southV1, southU2, southV2,
                    westU1, westV1, westU2, westV2,
                    eastU1, eastV1, eastU2, eastV2
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
                    2,
                    meta,
                    0,
                    0,
                    -1,
                    (float)bounds.minZ,
                    -1,
                    0,
                    0,
                    1.0F - (float)bounds.minX,
                    1.0F - (float)bounds.maxX,
                    0,
                    1,
                    0,
                    (float)bounds.maxY,
                    (float)bounds.minY,
                    bottomU1, bottomV1, bottomU2, bottomV2,
                    topU1, topV1, topU2, topV2,
                    northU1, northV1, northU2, northV2,
                    southU1, southV1, southU2, southV2,
                    westU1, westV1, westU2, westV2,
                    eastU1, eastV1, eastU2, eastV2
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
                    3,
                    meta,
                    0,
                    0,
                    1,
                    1.0F - (float)bounds.maxZ,
                    0,
                    1,
                    0,
                    (float)bounds.maxY,
                    (float)bounds.minY,
                    -1,
                    0,
                    0,
                    1.0F - (float)bounds.minX,
                    1.0F - (float)bounds.maxX,
                    bottomU1, bottomV1, bottomU2, bottomV2,
                    topU1, topV1, topU2, topV2,
                    northU1, northV1, northU2, northV2,
                    southU1, southV1, southU2, southV2,
                    westU1, westV1, westU2, westV2,
                    eastU1, eastV1, eastU2, eastV2
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
                    4,
                    meta,
                    -1,
                    0,
                    0,
                    (float)bounds.minX,
                    0,
                    0,
                    1,
                    (float)bounds.maxZ,
                    (float)bounds.minZ,
                    0,
                    1,
                    0,
                    (float)bounds.maxY,
                    (float)bounds.minY,
                    bottomU1, bottomV1, bottomU2, bottomV2,
                    topU1, topV1, topU2, topV2,
                    northU1, northV1, northU2, northV2,
                    southU1, southV1, southU2, southV2,
                    westU1, westV1, westU2, westV2,
                    eastU1, eastV1, eastU2, eastV2
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
                    5,
                    meta,
                    1,
                    0,
                    0,
                    1.0F - (float)bounds.maxX,
                    0,
                    0,
                    1,
                    (float)bounds.maxZ,
                    (float)bounds.minZ,
                    0,
                    -1,
                    0,
                    1.0F - (float)bounds.minY,
                    1.0F - (float)bounds.maxY,
                    bottomU1, bottomV1, bottomU2, bottomV2,
                    topU1, topV1, topU2, topV2,
                    northU1, northV1, northU2, northV2,
                    southU1, southV1, southU2, southV2,
                    westU1, westV1, westU2, westV2,
                    eastU1, eastV1, eastU2, eastV2
                );
            }
            default:
                throw new IllegalArgumentException("Side " + side + " not expected!");
        }
    }


    public final boolean renderSide(
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
        float rigP,

        double bottomU1, double bottomV1, double bottomU2, double bottomV2,
        double topU1, double topV1, double topU2, double topV2,
        double northU1, double northV1, double northU2, double northV2,
        double southU1, double southV1, double southU2, double southV2,
        double westU1, double westV1, double westU2, double westV2,
        double eastU1, double eastV1, double eastU2, double eastV2
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

    public void renderBottomFace(Tessellator tessellator, AABB bounds, double u1, double v1, double u2, double v2) {
       if (u1 == 0 && v1 == 0 && u2 == 0 && v2 == 0)  {
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
            tessellator.addVertexWithUV(d11, d13, d15, u1, v2);
            tessellator.addVertexWithUV(d11, d13, d14, u2, v2);
            tessellator.addVertexWithUV(d12, d13, d14, u2, v1);
            tessellator.addVertexWithUV(d12, d13, d15, u1, v1);
        }
    }

    public void renderTopFace(Tessellator tessellator, AABB bounds, double u1, double v1, double u2, double v2) {
        if (u1 == 0 && v1 == 0 && u2 == 0 && v2 == 0)  {
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
            tessellator.addVertexWithUV(d12, d13, d15, u1, v2);
            tessellator.addVertexWithUV(d12, d13, d14, u2, v2);
            tessellator.addVertexWithUV(d11, d13, d14, u2, v1);
            tessellator.addVertexWithUV(d11, d13, d15, u1, v1);
        }
    }

    public void renderNorthFace(Tessellator tessellator, AABB bounds, double u1, double v1, double u2, double v2) {
        if (u1 == 0 && v1 == 0 && u2 == 0 && v2 == 0)  {
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

    public void renderSouthFace(Tessellator tessellator, AABB bounds, double u1, double v1, double u2, double v2) {
        if (u1 == 0 && v1 == 0 && u2 == 0 && v2 == 0)  {
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
            tessellator.addVertexWithUV(x0, y1, z0, u1, v2);
            tessellator.addVertexWithUV(x0, y0, z0, u2, v2);
            tessellator.addVertexWithUV(x1, y0, z0, u2, v1);
            tessellator.addVertexWithUV(x1, y1, z0, u1, v1);
        }
    }

    public void renderWestFace(Tessellator tessellator, AABB bounds, double u1, double v1, double u2, double v2) {
        if (u1 == 0 && v1 == 0 && u2 == 0 && v2 == 0)  {
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
            tessellator.addVertexWithUV(d12, d14, d16,u2, v1 );
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
            tessellator.addVertexWithUV(d12, d14, d16, u1, v2);
            tessellator.addVertexWithUV(d12, d14, d15, u2, v2);
            tessellator.addVertexWithUV(d12, d13, d15, u2, v1);
            tessellator.addVertexWithUV(d12, d13, d16, u1, v1);
        }
    }

    public void renderEastFace(Tessellator tessellator, AABB bounds, double u1, double v1, double u2, double v2) {
        if (u1 == 0 && v1 == 0 && u2 == 0 && v2 == 0)  {
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

    private static void drawBase(Tessellator tessellator) {
        float u1 = 11.5f / 16f; // 0.71875
        float v1 = 5.75f / 16f; // 0.359375
        float u2 = 8.5f / 16f;  // 0.53125
        float v2 = 2.75f / 16f; // 0.171875

//        tessellator.addVertexWithUV(0.125, 0.875, 0.125, u2, v1); // haut gauche
//        tessellator.addVertexWithUV(0.875, 0.875, 0.125, u1, v1); // haut droite
//        tessellator.addVertexWithUV(0.875, 0.125, 0.125, u1, v2); // bas droite
//        tessellator.addVertexWithUV(0.125, 0.125, 0.125, u2, v2); // bas gauche

//        tessellator.addVertexWithUV(0, 1, 0, 1, 0); // haut gauche
//        tessellator.addVertexWithUV(1, 1, 0, 0, 0); // haut droite
//        tessellator.addVertexWithUV(1, 0, 0, 0, 1); // bas droite
//        tessellator.addVertexWithUV(0, 0, 0, 1, 1); // bas gauche

        tessellator.addVertexWithUV(0, 1, 1, 0, 1); // haut gauche
        tessellator.addVertexWithUV(1, 1, 1, 1, 1); // haut droite
        tessellator.addVertexWithUV(1, 0, 1, 1, 0); // bas droite
        tessellator.addVertexWithUV(0, 0, 1, 0, 0); // bas gauche

//        u1 = 5.75f / 16f;
//        v1 = 5.75f / 16f;
//        u2 = 2.75f / 16f;
//        v2 = 2.75f / 16f;
//
//        tessellator.addVertexWithUV(0.875, 0.875, 0.8125, u2, v1); // haut droite
//        tessellator.addVertexWithUV(0.125, 0.875, 0.8125, u1, v1); // haut gauche
//        tessellator.addVertexWithUV(0.125, 0.125, 0.8125, u1, v2); // bas gauche
//        tessellator.addVertexWithUV(0.875, 0.125, 0.8125, u2, v2); // bas droite
//
//        u1 = 2.75f / 16f;
//        v1 = 5.75f / 16f;
//        u2 = 0f     / 16f;
//        v2 = 2.75f / 16f;
//
//        tessellator.addVertexWithUV(0.875, 0.875, 0.125, u2, v1); // haut arrière
//        tessellator.addVertexWithUV(0.875, 0.875, 0.8125,u1, v1); // haut avant
//        tessellator.addVertexWithUV(0.875, 0.125, 0.8125,u1, v2); // bas avant
//        tessellator.addVertexWithUV(0.875, 0.125, 0.125, u2, v2); // bas arrière
//
//
//        u1 = 8.5f / 16f;
//        v1 = 5.75f / 16f;
//        u2 = 5.75f / 16f;
//        v2 = 2.75f / 16f;
//
//        tessellator.addVertexWithUV(0.125, 0.875, 0.8125,u2, v1); // haut avant
//        tessellator.addVertexWithUV(0.125, 0.875, 0.125, u1, v1); // haut arrière
//        tessellator.addVertexWithUV(0.125, 0.125, 0.125, u1, v2); // bas arrière
//        tessellator.addVertexWithUV(0.125, 0.125, 0.8125,u2, v2); // bas avant
//
//        u1 = 8.75f / 16f;
//        v1 = 0f;
//        u2 = 5.75f / 16f;
//        v2 = 2.75f / 16f;
//
//        tessellator.addVertexWithUV(0.125, 0.875, 0.8125,u2, v1); // avant gauche
//        tessellator.addVertexWithUV(0.875, 0.875, 0.8125,u1, v1); // avant droite
//        tessellator.addVertexWithUV(0.875, 0.875, 0.125, u1, v2); // arrière droite
//        tessellator.addVertexWithUV(0.125, 0.875, 0.125, u2, v2); // arrière gauche
//
//        u1 = 5.75f / 16f;
//        v1 = 2.75f / 16f;
//        u2 = 2.75f / 16f;
//        v2 = 0f;
//
//        tessellator.addVertexWithUV(0.125, 0.125, 0.125, u2, v1); // arrière gauche
//        tessellator.addVertexWithUV(0.875, 0.125, 0.125, u1, v1); // arrière droite
//        tessellator.addVertexWithUV(0.875, 0.125, 0.8125,u1, v2); // avant droite
//        tessellator.addVertexWithUV(0.125, 0.125, 0.8125,u2, v2); // avant gauche
    }

    private void drawTexturedModalRect(double width, double height, IconCoordinate coordinate) {
        coordinate.parentAtlas.bind();
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-width / 2.0, height / 2.0, 0.0, coordinate.getIconUMin(), coordinate.getIconVMin());
        tessellator.addVertexWithUV(-width / 2.0, -height / 2.0, 0.0, coordinate.getIconUMin(), coordinate.getIconVMax());
        tessellator.addVertexWithUV(width / 2.0, -height / 2.0, 0.0, coordinate.getIconUMax(), coordinate.getIconVMax());
        tessellator.addVertexWithUV(width / 2.0, height / 2.0, 0.0, coordinate.getIconUMax(), coordinate.getIconVMin());
        tessellator.draw();
    }
}
