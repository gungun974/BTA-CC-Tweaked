package dan200.computercraft.shared.turtle.blocks;

import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.shared.common.ComputerCraftBlocks;
import dan200.computercraft.shared.common.ComputerCraftItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.Block;
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

            GL11.glTranslatef((float) x + (float) pos.x + 1, (float) y + (float) pos.y + 1, (float) z + (float) pos.z + 1);

            GL11.glTranslatef(-0.5f, 0, -0.5f);

            float angle = tileEntity.getAccess().getVisualYaw(partialTick);

            GL11.glRotatef(-angle + 180, 0, 1f, 0);

            GL11.glTranslatef(0.5f, 0, 0.5f);

            GL11.glPushMatrix();

            GL11.glScalef(-1, -1, -1);

            if (block.id() == ComputerCraftBlocks.TURTLE_ADVANCED.id()) {
                this.loadTexture("/assets/computercraft/textures/block/turtle_advanced.png");
            } else {
                this.loadTexture("/assets/computercraft/textures/block/turtle_normal.png");
            }

            BlockModel.renderBlocks.enableAO = true;

            BlockModel.renderBlocks.cache.setupCache(block, tileEntity.worldObj, tileEntity.x, tileEntity.y, tileEntity.z);

            tessellator.startDrawingQuads();
            drawBase(tessellator, tileEntity, angle);
            tessellator.draw();


            ITurtleUpgrade leftUpgrade = tileEntity.getAccess().getUpgrade(TurtleSide.LEFT);

            if (leftUpgrade != null) {
                leftUpgrade.drawTileUpgrade(tessellator, this.renderDispatcher.textureManager, tileEntity, angle, TurtleSide.LEFT, partialTick);
            }

            ITurtleUpgrade rightUpgrade = tileEntity.getAccess().getUpgrade(TurtleSide.RIGHT);

            if (rightUpgrade != null) {
                rightUpgrade.drawTileUpgrade(tessellator, this.renderDispatcher.textureManager, tileEntity, angle, TurtleSide.RIGHT, partialTick);
            }

            BlockModel.renderBlocks.enableAO = false;

            GL11.glPopMatrix();

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
            GL11.glDisable(32826);
        }
    }


    private static void drawBase(Tessellator tessellator, TileTurtle tileEntity, float angle) {
        (new BlockAORenderer(AABB.getTemporaryBB(2 / 16f, 2 / 16f, 2 / 16f, 14 / 16f, 14 / 16f, 13 / 16f)))
            .setBottomUV(5.75 / 16, 2.75 / 16, 2.75 / 16, 0)
            .setTopUV(8.75 / 16, 0, 5.75 / 16, 2.75 / 16)
            .setNorthUV(11.5 / 16, 5.75 / 16, 8.5 / 16, 2.75 / 16)
            .setSouthUV(5.75 / 16, 5.75 / 16, 2.75 / 16, 2.75 / 16)
            .setWestUV(8.5 / 16, 5.75 / 16, 5.75 / 16, 2.75 / 16)
            .setEastUV(2.75 / 16, 5.75 / 16, 0, 2.75 / 16)
            .render(tessellator, tileEntity, angle, 1f, 1f, 1f);

        (new BlockAORenderer(AABB.getTemporaryBB(3 / 16f, 6 / 16f, 13 / 16f, 13 / 16f, 13 / 16f, 15 / 16f)))
            .setBottomUV(11.75 / 16, 0.5 / 16, 9.25 / 16, 0)
            .setTopUV(14.25 / 16, 0, 11.75 / 16, 0.5 / 16)
            .setSouthUV(11.75 / 16, 2.25 / 16, 9.25 / 16, 0.5 / 16)
            .setWestUV(12.25 / 16, 2.25 / 16, 11.75 / 16, 0.5 / 16)
            .setEastUV(9.25 / 16, 2.25 / 16, 8.75 / 16, 0.5 / 16)
            .render(tessellator, tileEntity, angle, 1f, 1f, 1f);
    }
}
