package dan200.computercraft.shared.turtle.items;

import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.shared.common.ComputerCraftItems;
import dan200.computercraft.shared.common.IColouredItem;
import dan200.computercraft.shared.turtle.blocks.BlockAORenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Font;
import net.minecraft.client.render.ItemRenderer;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.item.model.ItemModelBlock;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.Global;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class TurtleItemModel extends ItemModelBlock {
    public TurtleItemModel(ItemBlock<?> itemBlock) {
        super(itemBlock);
    }

    @Override
    public void renderItemFirstPerson(Tessellator tessellator, ItemRenderer renderer, Player player, ItemStack stack, float partialTick) {
        Minecraft mc = Minecraft.getMinecraft();
        float brightness = 1.0F;
        if (LightmapHelper.isLightmapEnabled()) {
            int lightmapCoord = player.getLightmapCoord(partialTick);
            if (this.itemfullBright) {
                lightmapCoord = LightmapHelper.setBlocklightValue(lightmapCoord, 15);
            }

            LightmapHelper.setLightmapCoord(lightmapCoord);
        } else if (!mc.fullbright && !this.itemfullBright) {
            brightness = player.getBrightness(1.0F);
        }

        float swingProgress = player.getSwingProgress(partialTick);
        float animationProgress2 = MathHelper.sin(swingProgress * (float) Math.PI);
        float animationProgress = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float) Math.PI);
        GL11.glTranslatef(-animationProgress * 0.4F, MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float) Math.PI * 2.0F) * 0.2F, -animationProgress2 * 0.2F);
        GL11.glTranslatef(0.56F, -0.52F - (1.0F - renderer.getEquippedProgress(partialTick)) * 0.6F, -0.71999997F);
        GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
        GL11.glEnable(32826);
        float animationProgress3 = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
        GL11.glRotatef(-animationProgress3 * 20.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-animationProgress * 20.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(-animationProgress * 80.0F, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(0.4F, 0.4F, 0.4F);

        GL11.glRotatef(90, 0, 1f, 0);

        this.renderItem(tessellator, renderer, stack, player, brightness, true);
    }

    @Override
    public void renderItem(
        Tessellator tessellator, ItemRenderer renderer, ItemStack itemstack, @Nullable Entity entity, float brightness, boolean handheldTransform
    ) {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        TextureRegistry.blockAtlas.bind();

        GL11.glRotatef(270, 0, 1f, 0);

        GL11.glScalef(-1, -1, -1);

        GL11.glTranslatef(-0.5f, -0.5f, -0.5f);

        drawTurtle(tessellator, itemstack, brightness, 1.0f);

        GL11.glDisable(3042);
    }

    @Override
    public void renderItemIntoGui(
        Tessellator tessellator, Font font, TextureManager textureManager, ItemStack itemStack, int x, int y, float brightness, float alpha
    ) {
        if (itemStack != null) {
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(2884);
            GL11.glBlendFunc(770, 771);
            TextureRegistry.blockAtlas.bind();
            GL11.glPushMatrix();
            GL11.glTranslatef((float) (x - 2), (float) (y + 3), -3.0F);
            GL11.glScalef(10.0F, 10.0F, 10.0F);
            GL11.glTranslatef(1.0F, 0.5F, 1.0F);
            GL11.glScalef(1.0F, 1.0F, -1.0F);
            GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            if (this.useColor) {
                int color = this.getColor(itemStack);
                float r = (float) (color >> 16 & 0xFF) / 255.0F;
                float g = (float) (color >> 8 & 0xFF) / 255.0F;
                float b = (float) (color & 0xFF) / 255.0F;
                GL11.glColor4f(r * brightness, g * brightness, b * brightness, alpha);
            } else {
                GL11.glColor4f(brightness, brightness, brightness, alpha);
            }

            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            BlockModel.renderBlocks.useInventoryTint = this.useColor;

            GL11.glTranslatef(0.62f, 0, 0.62f);

            GL11.glRotatef(270, 0, 1f, 0);

            GL11.glTranslatef(-0.62f, 0, -0.62f);

            GL11.glScalef(-1, -1, -1);
            GL11.glTranslatef(-1, -1, -1);

            GL11.glTranslatef(-0.12f, 0, -0.12f);

            drawTurtle(tessellator, itemStack, brightness, alpha);


            BlockModel.renderBlocks.useInventoryTint = true;
            GL11.glPopMatrix();

            GL11.glEnable(2884);
            GL11.glDisable(3042);
        }
    }

    @Override
    public void renderAsItemEntity(
        Tessellator tessellator, Entity entity, Random random, ItemStack itemstack, int renderCount, float yaw, float brightness, float partialTick
    ) {
        GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
        TextureRegistry.blockAtlas.bind();
        float itemSize = 0.25F;
        GL11.glScalef(itemSize, itemSize, itemSize);

        for (int i = 0; i < renderCount; i++) {
            GL11.glPushMatrix();
            if (i > 0) {
                float rOffX = (random.nextFloat() * 2.0F - 1.0F) * 0.2F / itemSize;
                float rOffY = (random.nextFloat() * 2.0F - 1.0F) * 0.2F / itemSize;
                float rOffZ = (random.nextFloat() * 2.0F - 1.0F) * 0.2F / itemSize;
                GL11.glTranslatef(rOffX, rOffY, rOffZ);
            }

            if (LightmapHelper.isLightmapEnabled()) {
                brightness = 1.0F;
                LightmapHelper.setLightmapCoord(entity.getLightmapCoord(partialTick));
            }

            if (Global.accessor.isFullbrightEnabled() || this.itemfullBright) {
                brightness = 1.0F;
            }

            GL11.glScalef(-1, -1, -1);

            GL11.glTranslatef(-0.5f, -0.5f, -0.5f);

            drawTurtle(tessellator, itemstack, brightness, 1.0f);
            GL11.glPopMatrix();
        }
    }

    @Override
    public void renderItemInWorld(Tessellator tessellator, Entity entity, ItemStack itemStack, float brightness, float alpha, boolean worldTransform) {
        if (itemStack != null) {
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(2884);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            TextureRegistry.blockAtlas.bind();
            if (this.useColor) {
                int color = this.getColor(itemStack);
                float r = (float) (color >> 16 & 0xFF) / 255.0F;
                float g = (float) (color >> 8 & 0xFF) / 255.0F;
                float b = (float) (color & 0xFF) / 255.0F;
                GL11.glColor4f(r * brightness, g * brightness, b * brightness, alpha);
            } else {
                GL11.glColor4f(brightness, brightness, brightness, alpha);
            }

            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            BlockModel.renderBlocks.useInventoryTint = this.useColor;

            drawTurtle(tessellator, itemStack, brightness, alpha);

            BlockModel.renderBlocks.useInventoryTint = true;

            GL11.glDisable(2884);
            GL11.glDisable(3042);
        }
    }

    public void drawTurtle(Tessellator tessellator, ItemStack itemStack, float brightness, float alpha) {
        int colour = IColouredItem.getColourBasic(itemStack);

        if (colour != -1) {
            float r = (float) (colour >> 16 & 0xFF) / 255.0F;
            float g = (float) (colour >> 8 & 0xFF) / 255.0F;
            float b = (float) (colour & 0xFF) / 255.0F;
            GL11.glColor4f(r * brightness, g * brightness, b * brightness, alpha);

            Minecraft.getMinecraft().textureManager.loadTexture("/assets/computercraft/textures/block/turtle_colour.png").bind();

            tessellator.startDrawingQuads();

            (new BlockAORenderer(AABB.getTemporaryBB(2 / 16f, 2 / 16f, 2 / 16f, 14 / 16f, 14 / 16f, 13 / 16f)))
                .setBottomUV(5.75 / 16f, 8.5 / 16f, 2.75 / 16f, 5.75 / 16f)
                .setTopUV(8.75 / 16f, 5.75 / 16f, 5.75 / 16f, 8.5 / 16f)
                .setNorthUV(11.5 / 16f, 11.5 / 16f, 8.5 / 16f, 8.5 / 16f)
                .setSouthUV(5.75 / 16f, 11.5 / 16f, 2.75 / 16f, 8.5 / 16f)
                .setWestUV(8.5 / 16f, 11.5 / 16f, 5.75 / 16f, 8.555 / 16f)
                .setEastUV(2.75 / 16f, 11.5 / 16f, 0, 8.5 / 16f)
                .render(tessellator, Side.NORTH);

            (new BlockAORenderer(AABB.getTemporaryBB(3 / 16f, 6 / 16f, 13 / 16f, 13 / 16f, 13 / 16f, 15 / 16f)))
                .setBottomUV(11.75 / 16f, 6.25 / 16f, 9.25 / 16f, 5.75 / 16f)
                .setTopUV(14.25 / 16f, 5.75 / 16f, 11.75 / 16f, 6.25 / 16f)
                .setSouthUV(11.75 / 16f, 8 / 16f, 9.25 / 16f, 6.25 / 16f)
                .setWestUV(12.25 / 16f, 8 / 16f, 11.75 / 16f, 6.25 / 16f)
                .setEastUV(9.25 / 16f, 8 / 16f, 8.75 / 16f, 6.25 / 16f)
                .render(tessellator, Side.NORTH);

            tessellator.draw();
        } else if (itemStack.itemID == ComputerCraftItems.TURTLE_ADVANCED.id) {
            Minecraft.getMinecraft().textureManager.loadTexture("/assets/computercraft/textures/block/turtle_advanced.png").bind();
        } else {
            Minecraft.getMinecraft().textureManager.loadTexture("/assets/computercraft/textures/block/turtle_normal.png").bind();
        }

        tessellator.startDrawingQuads();

        GL11.glColor4f(brightness, brightness, brightness, alpha);

        (new BlockAORenderer(AABB.getTemporaryBB(2 / 16f, 2 / 16f, 2 / 16f, 14 / 16f, 14 / 16f, 13 / 16f)))
            .setBottomUV(5.75 / 16, 2.75 / 16, 2.75 / 16, 0)
            .setTopUV(8.75 / 16, 0, 5.75 / 16, 2.75 / 16)
            .setNorthUV(11.5 / 16, 5.75 / 16, 8.5 / 16, 2.75 / 16)
            .setSouthUV(5.75 / 16, 5.75 / 16, 2.75 / 16, 2.75 / 16)
            .setWestUV(8.5 / 16, 5.75 / 16, 5.75 / 16, 2.75 / 16)
            .setEastUV(2.75 / 16, 5.75 / 16, 0, 2.75 / 16)
            .render(tessellator, Side.NORTH);

        (new BlockAORenderer(AABB.getTemporaryBB(3 / 16f, 6 / 16f, 13 / 16f, 13 / 16f, 13 / 16f, 15 / 16f)))
            .setBottomUV(11.75 / 16, 0.5 / 16, 9.25 / 16, 0)
            .setTopUV(14.25 / 16, 0, 11.75 / 16, 0.5 / 16)
            .setSouthUV(11.75 / 16, 2.25 / 16, 9.25 / 16, 0.5 / 16)
            .setWestUV(12.25 / 16, 2.25 / 16, 11.75 / 16, 0.5 / 16)
            .setEastUV(9.25 / 16, 2.25 / 16, 8.75 / 16, 0.5 / 16)
            .render(tessellator, Side.NORTH);

        tessellator.draw();

        if (itemStack.getItem() instanceof ITurtleItem) {
            ITurtleItem item = (ITurtleItem) itemStack.getItem();

            ITurtleUpgrade leftUpgrade = item.getUpgrade(itemStack, TurtleSide.LEFT);

            if (leftUpgrade != null) {
                leftUpgrade.drawItemUpgrade(tessellator, Minecraft.getMinecraft().textureManager, TurtleSide.LEFT);
            }

            ITurtleUpgrade rightUpgrade = item.getUpgrade(itemStack, TurtleSide.RIGHT);

            if (rightUpgrade != null) {
                rightUpgrade.drawItemUpgrade(tessellator, Minecraft.getMinecraft().textureManager, TurtleSide.RIGHT);
            }
        }
    }
}
