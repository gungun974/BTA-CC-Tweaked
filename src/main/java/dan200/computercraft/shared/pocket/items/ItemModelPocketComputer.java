package dan200.computercraft.shared.pocket.items;

import dan200.computercraft.shared.common.IColouredItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.Font;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.Random;

public class ItemModelPocketComputer extends ItemModelStandard {
    public static final IconCoordinate POCKET_COMPUTER_FRAME = TextureRegistry.getTexture("computercraft:item/pocket_computer_frame");
    public static final IconCoordinate POCKET_COMPUTER_ON = TextureRegistry.getTexture("computercraft:item/pocket_computer_on");
    public static final IconCoordinate POCKET_COMPUTER_BLINK = TextureRegistry.getTexture("computercraft:item/pocket_computer_blink");
    public static final IconCoordinate POCKET_COMPUTER_LIGHT = TextureRegistry.getTexture("computercraft:item/pocket_computer_light");
    public static final IconCoordinate POCKET_COMPUTER_COLOUR = TextureRegistry.getTexture("computercraft:item/pocket_computer_colour");
    public ItemModelPocketComputer(Item item, String namespace) {
        super(item, namespace);
    }

    private static void draw3DModel(Tessellator tessellator, boolean worldTransform, IconCoordinate tex) {
        tex.parentAtlas.bind();
        int tileWidth = tex.width;
        float uMin = (float) tex.getIconUMin();
        float uMax = (float) tex.getIconUMax();
        float vMin = (float) tex.getIconVMin();
        float vMax = (float) tex.getIconVMax();
        float uDiff = uMin - uMax;
        float vDiff = vMin - vMax;
        float width = 1.0F;
        float foon = 0.5F / (float) tex.parentAtlas.getHeight();
        float goon = 0.0625F * (16.0F / (float) tileWidth);
        GL11.glEnable(32826);
        float thickness = 0.0625F;
        float pixelWidth = 1.0F / (float) tileWidth;
        if (worldTransform) {
            GL11.glTranslatef(-0.5F, -0.5F, 0.03125F);
        }

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.addVertexWithUV(0.0, 0.0, 0.0, uMax, vMax);
        tessellator.addVertexWithUV(1.0, 0.0, 0.0, uMin, vMax);
        tessellator.addVertexWithUV(1.0, 1.0, 0.0, uMin, vMin);
        tessellator.addVertexWithUV(0.0, 1.0, 0.0, uMax, vMin);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        tessellator.addVertexWithUV(0.0, 1.0, -0.0625, uMax, vMin);
        tessellator.addVertexWithUV(1.0, 1.0, -0.0625, uMin, vMin);
        tessellator.addVertexWithUV(1.0, 0.0, -0.0625, uMin, vMax);
        tessellator.addVertexWithUV(0.0, 0.0, -0.0625, uMax, vMax);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);

        for (int i = 0; i < tileWidth; i++) {
            float texProgress = (float) i * pixelWidth;
            float u = uMax + uDiff * texProgress - foon;
            float x = texProgress;
            tessellator.addVertexWithUV(x, 0.0, -0.0625, u, vMax);
            tessellator.addVertexWithUV(x, 0.0, 0.0, u, vMax);
            tessellator.addVertexWithUV(x, 1.0, 0.0, u, vMin);
            tessellator.addVertexWithUV(x, 1.0, -0.0625, u, vMin);
        }

        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);

        for (int i = 0; i < tileWidth; i++) {
            float texProgress = (float) i * pixelWidth;
            float u = uMax + uDiff * texProgress - foon;
            float x = texProgress + goon;
            tessellator.addVertexWithUV(x, 1.0, -0.0625, u, vMin);
            tessellator.addVertexWithUV(x, 1.0, 0.0, u, vMin);
            tessellator.addVertexWithUV(x, 0.0, 0.0, u, vMax);
            tessellator.addVertexWithUV(x, 0.0, -0.0625, u, vMax);
        }

        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);

        for (int i = 0; i < tileWidth; i++) {
            float texProgress = (float) i * pixelWidth;
            float v = vMax + vDiff * texProgress - foon;
            float y = texProgress + goon;
            tessellator.addVertexWithUV(0.0, y, 0.0, uMax, v);
            tessellator.addVertexWithUV(1.0, y, 0.0, uMin, v);
            tessellator.addVertexWithUV(1.0, y, -0.0625, uMin, v);
            tessellator.addVertexWithUV(0.0, y, -0.0625, uMax, v);
        }

        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);

        for (int i = 0; i < tileWidth; i++) {
            float texProgress = (float) i * pixelWidth;
            float v = vMax + vDiff * texProgress - foon;
            float y = texProgress;
            tessellator.addVertexWithUV(1.0, y, 0.0, uMin, v);
            tessellator.addVertexWithUV(0.0, y, 0.0, uMax, v);
            tessellator.addVertexWithUV(0.0, y, -0.0625, uMax, v);
            tessellator.addVertexWithUV(1.0, y, -0.0625, uMin, v);
        }

        tessellator.draw();
    }

    @Override
    public void renderItemInWorld(Tessellator tessellator, Entity entity, ItemStack itemStack, float brightness, float alpha, boolean worldTransform) {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        if (this.useColor) {
            int color = this.getColor(itemStack);
            float r = (float) (color >> 16 & 0xFF) / 255.0F;
            float g = (float) (color >> 8 & 0xFF) / 255.0F;
            float b = (float) (color & 0xFF) / 255.0F;
            GL11.glColor4f(r * brightness, g * brightness, b * brightness, alpha);
        } else {
            GL11.glColor4f(brightness, brightness, brightness, alpha);
        }

        IconCoordinate tex = this.getIcon(entity, itemStack);

        int lightState = ItemPocketComputer.getLightState(itemStack);
        int colour = IColouredItem.getColourBasic(itemStack);

        switch (ItemPocketComputer.getState(itemStack)) {
            case OFF:
                draw3DModel(tessellator, worldTransform, POCKET_COMPUTER_FRAME);
                break;
            case ON:
                draw3DModel(tessellator, worldTransform, POCKET_COMPUTER_ON);
                break;
            case BLINKING:
                draw3DModel(tessellator, worldTransform, POCKET_COMPUTER_BLINK);
        }

        if (colour != -1) {
            float r = (float) (colour >> 16 & 0xFF) / 255.0F;
            float g = (float) (colour >> 8 & 0xFF) / 255.0F;
            float b = (float) (colour & 0xFF) / 255.0F;
            GL11.glColor4f(r * brightness, g * brightness, b * brightness, alpha);
            draw3DModel(tessellator, worldTransform, POCKET_COMPUTER_COLOUR);
        } else {
            draw3DModel(tessellator, worldTransform, tex);
        }

        if (lightState != -1) {
            float r = (float) (lightState >> 16 & 0xFF) / 255.0F;
            float g = (float) (lightState >> 8 & 0xFF) / 255.0F;
            float b = (float) (lightState & 0xFF) / 255.0F;
            GL11.glColor4f(r * brightness, g * brightness, b * brightness, alpha);
            draw3DModel(tessellator, worldTransform, POCKET_COMPUTER_LIGHT);
        }

        GL11.glDisable(32826);
        GL11.glDisable(3042);
    }

    @Override
    public void renderItemIntoGui(
        Tessellator tessellator, Font font, TextureManager textureManager, ItemStack itemStack, int x, int y, float brightness, float alpha
    ) {
        if (itemStack != null) {
            Minecraft mc = Minecraft.getMinecraft();
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glEnable(2884);
            IconCoordinate textureIndex = this.getIcon(mc.thePlayer, itemStack);
            textureIndex.parentAtlas.bind();
            if (this.useColor) {
                int color = this.getColor(itemStack);
                float r = (float) (color >> 16 & 0xFF) / 255.0F;
                float g = (float) (color >> 8 & 0xFF) / 255.0F;
                float b = (float) (color & 0xFF) / 255.0F;
                GL11.glColor4f(r * brightness, g * brightness, b * brightness, alpha);
            } else {
                GL11.glColor4f(brightness, brightness, brightness, alpha);
            }

            int lightState = ItemPocketComputer.getLightState(itemStack);
            int colour = IColouredItem.getColourBasic(itemStack);

            switch (ItemPocketComputer.getState(itemStack)) {
                case OFF:
                    this.renderTexturedQuad(tessellator, x, y, POCKET_COMPUTER_FRAME);
                    break;
                case ON:
                    this.renderTexturedQuad(tessellator, x, y, POCKET_COMPUTER_ON);
                    break;
                case BLINKING:
                    this.renderTexturedQuad(tessellator, x, y, POCKET_COMPUTER_BLINK);
            }

            if (colour != -1) {
                float r = (float) (colour >> 16 & 0xFF) / 255.0F;
                float g = (float) (colour >> 8 & 0xFF) / 255.0F;
                float b = (float) (colour & 0xFF) / 255.0F;
                GL11.glColor4f(r * brightness, g * brightness, b * brightness, alpha);
                this.renderTexturedQuad(tessellator, x, y, POCKET_COMPUTER_COLOUR);
            } else {
                this.renderTexturedQuad(tessellator, x, y, textureIndex);
            }

            if (lightState != -1) {
                float r = (float) (lightState >> 16 & 0xFF) / 255.0F;
                float g = (float) (lightState >> 8 & 0xFF) / 255.0F;
                float b = (float) (lightState & 0xFF) / 255.0F;
                GL11.glColor4f(r * brightness, g * brightness, b * brightness, alpha);
                this.renderTexturedQuad(tessellator, x, y, POCKET_COMPUTER_LIGHT);
            }


            GL11.glEnable(2896);
            GL11.glDisable(2884);
            GL11.glDisable(3042);
        }
    }

    @Override
    public void renderAsItemEntity(
        Tessellator tessellator, @Nullable Entity entity, Random random, ItemStack itemstack, int renderCount, float yaw, float brightness, float partialTick
    ) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.fullbright || this.itemfullBright) {
            brightness = 1.0F;
        }

        EntityRenderDispatcher renderDispatcher = EntityRenderDispatcher.instance;
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        IconCoordinate tex = this.getIcon(entity, itemstack);
        tex.parentAtlas.bind();
        if (this.useColor) {
            int color = this.getColor(itemstack);
            float r = (float) (color >> 16 & 0xFF) / 255.0F;
            float g = (float) (color >> 8 & 0xFF) / 255.0F;
            float b = (float) (color & 0xFF) / 255.0F;
            GL11.glColor4f(r * brightness, g * brightness, b * brightness, 1.0F);
        } else {
            GL11.glColor4f(brightness, brightness, brightness, 1.0F);
        }

        if (LightmapHelper.isLightmapEnabled() && this.itemfullBright && entity != null) {
            int lmc = entity.getLightmapCoord(1.0F);
            lmc = LightmapHelper.setBlocklightValue(lmc, 15);
            LightmapHelper.setLightmapCoord(lmc);
        }

        if (mc.gameSettings.items3D.value) {
            GL11.glPushMatrix();
            GL11.glScaled(1.0, 1.0, 1.0);
            GL11.glRotated(yaw, 0.0, 1.0, 0.0);
            GL11.glTranslated(-0.5, 0.0, -0.05 * (double) (renderCount - 1));

            for (int i = 0; i < renderCount; i++) {
                GL11.glPushMatrix();
                GL11.glTranslated(0.0, 0.0, 0.1 * (double) i);
                this.renderItem(tessellator, renderDispatcher.itemRenderer, itemstack, entity, brightness, false);
                GL11.glPopMatrix();
            }

            GL11.glPopMatrix();
        } else {
            for (int i = 0; i < renderCount; i++) {
                GL11.glPushMatrix();
                if (i > 0) {
                    float rOffX = (random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float rOffY = (random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float rOffZ = (random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    GL11.glTranslatef(rOffX, rOffY, rOffZ);
                }

                GL11.glRotatef(180.0F - renderDispatcher.viewLerpYaw, 0.0F, 1.0F, 0.0F);

                int lightState = ItemPocketComputer.getLightState(itemstack);
                int colour = IColouredItem.getColourBasic(itemstack);

                switch (ItemPocketComputer.getState(itemstack)) {
                    case OFF:
                        this.renderFlat(tessellator, POCKET_COMPUTER_FRAME);
                        this.renderFlat(tessellator, tex);
                        break;
                    case ON:
                        this.renderFlat(tessellator, POCKET_COMPUTER_ON);
                        this.renderFlat(tessellator, tex);
                        break;
                    case BLINKING:
                        this.renderFlat(tessellator, POCKET_COMPUTER_BLINK);
                }

                if (colour != -1) {
                    float r = (float) (colour >> 16 & 0xFF) / 255.0F;
                    float g = (float) (colour >> 8 & 0xFF) / 255.0F;
                    float b = (float) (colour & 0xFF) / 255.0F;
                    GL11.glColor4f(r * brightness, g * brightness, b * brightness, 1.0f);
                    this.renderFlat(tessellator, POCKET_COMPUTER_COLOUR);
                } else {
                    this.renderFlat(tessellator, tex);
                }

                if (lightState != -1) {
                    float r = (float) (lightState >> 16 & 0xFF) / 255.0F;
                    float g = (float) (lightState >> 8 & 0xFF) / 255.0F;
                    float b = (float) (lightState & 0xFF) / 255.0F;
                    GL11.glColor4f(r * brightness, g * brightness, b * brightness, 1.0f);
                    this.renderFlat(tessellator, POCKET_COMPUTER_LIGHT);
                }
                GL11.glPopMatrix();
            }
        }
    }
}
