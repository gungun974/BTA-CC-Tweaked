package dan200.computercraft.shared.common;

import dan200.computercraft.shared.computer.core.ComputerState;
import net.minecraft.client.render.Font;
import net.minecraft.client.render.ItemRenderer;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelBlock;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.Global;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicFullyRotatable;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.util.helper.Direction;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.Random;

public class ItemModelRotatedBlock extends ItemModelBlock {
    private Block<?> block;
    private BlockModel<?> blockModel;

    public ItemModelRotatedBlock(ItemBlock<?> itemBlock) {
        super(itemBlock);
        this.block = itemBlock.getBlock();
        this.blockModel = (BlockModel)BlockModelDispatcher.getInstance().getDispatch(this.block);
    }

    int appliedCustomMeta(int meta) {
        meta = (meta & ~0b111) | Direction.EAST.getId();

        if (this.block == ComputerCraftBlocks.COMPUTER_NORMAL || this.block == ComputerCraftBlocks.COMPUTER_ADVANCED) {
            meta = (meta & ~0b11000) | (ComputerState.BLINKING.ordinal() << 3);
        }

        if (this.block == ComputerCraftBlocks.WIRELESS_MODEM_NORMAL || this.block == ComputerCraftBlocks.WIRELESS_MODEM_ADVANCED) {
            meta = BlockLogicFullyRotatable.directionToMeta(Direction.EAST);
        }

        return meta;
    }

    public void renderItemIntoGui(Tessellator tessellator, Font font, TextureManager textureManager, ItemStack itemStack, int x, int y, float brightness, float alpha) {
        if (itemStack != null) {
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(2884);
            if (((BlockModel) BlockModelDispatcher.getInstance().getDispatch(Blocks.blocksList[itemStack.itemID])).shouldItemRender3d()) {
                GL11.glBlendFunc(770, 771);
                TextureRegistry.blockAtlas.bind();
                GL11.glPushMatrix();
                GL11.glTranslatef((float)(x - 2), (float)(y + 3), -3.0F);
                GL11.glScalef(10.0F, 10.0F, 10.0F);
                GL11.glTranslatef(1.0F, 0.5F, 1.0F);
                GL11.glScalef(1.0F, 1.0F, -1.0F);
                GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                if (this.useColor) {
                    int color = this.getColor(itemStack);
                    float r = (float)(color >> 16 & 255) / 255.0F;
                    float g = (float)(color >> 8 & 255) / 255.0F;
                    float b = (float)(color & 255) / 255.0F;
                    GL11.glColor4f(r * brightness, g * brightness, b * brightness, alpha);
                } else {
                    GL11.glColor4f(brightness, brightness, brightness, alpha);
                }

                GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);

                if (itemStack.getItem().equals(ComputerCraftItems.WIRELESS_MODEM_NORMAL) || itemStack.getItem().equals(ComputerCraftItems.WIRELESS_MODEM_ADVANCED)) {
                    GL11.glScalef(1.20f, 1.20f, 1.20f);
                    GL11.glTranslatef(0.4f, -0.05f, 0);
                }

                BlockModel.renderBlocks.useInventoryTint = this.useColor;
                ((BlockModel)BlockModelDispatcher.getInstance().getDispatch(Blocks.blocksList[itemStack.itemID])).renderBlockOnInventory(tessellator, appliedCustomMeta(itemStack.getMetadata()), brightness, alpha, (Integer)null);
                BlockModel.renderBlocks.useInventoryTint = true;
                GL11.glPopMatrix();
            } else {
                super.renderItemIntoGui(tessellator, font, textureManager, itemStack, x, y, brightness, alpha);
            }

            GL11.glEnable(2884);
            GL11.glDisable(3042);
        }
    }

    @Override
    public void renderAsItemEntity(Tessellator tessellator, Entity entity, Random random, ItemStack itemstack, int renderCount, float yaw, float brightness, float partialTick) {
        if (Blocks.blocksList[itemstack.itemID] != null && ((BlockModel)BlockModelDispatcher.getInstance().getDispatch(Blocks.blocksList[itemstack.itemID])).shouldItemRender3d()) {
            GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
            TextureRegistry.blockAtlas.bind();
            float itemSize = this.blockModel.getItemRenderScale();
            GL11.glScalef(itemSize, itemSize, itemSize);

            for(int i = 0; i < renderCount; ++i) {
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

                if (itemstack.getItem().equals(ComputerCraftItems.WIRELESS_MODEM_NORMAL) || itemstack.getItem().equals(ComputerCraftItems.WIRELESS_MODEM_ADVANCED)) {
                    GL11.glTranslatef(0.5f, 0, 0);
                }

                this.blockModel.renderBlockOnInventory(tessellator, appliedCustomMeta(itemstack.getMetadata()), brightness, (Integer)null);
                GL11.glPopMatrix();
            }
        } else {
            super.renderAsItemEntity(tessellator, entity, random, itemstack, renderCount, yaw, brightness, partialTick);
        }
    }

    @Override
    public void heldTransformThirdPerson(ItemRenderer renderer, Entity entity, ItemStack itemStack) {
        if (itemStack.itemID < Blocks.blocksList.length && ((BlockModel)BlockModelDispatcher.getInstance().getDispatch(Blocks.blocksList[itemStack.itemID])).shouldItemRender3d()) {
            float scale = 0.375F;
            GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
            GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(0.375F, -0.375F, -0.375F);
        } else {
            super.heldTransformThirdPerson(renderer, entity, itemStack);
        }

    }

    public void renderItem(Tessellator tessellator, ItemRenderer renderer, ItemStack itemstack, @Nullable Entity entity, float brightness, boolean handheldTransform) {
        if (((BlockModel)BlockModelDispatcher.getInstance().getDispatch(Blocks.blocksList[itemstack.itemID])).shouldItemRender3d()) {
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            TextureRegistry.blockAtlas.bind();
            ((BlockModel)BlockModelDispatcher.getInstance().getDispatch(Blocks.blocksList[itemstack.itemID])).renderBlockOnInventory(tessellator, appliedCustomMeta(itemstack.getMetadata()), brightness, (Integer)null);
            GL11.glDisable(3042);
        } else {
            super.renderItem(tessellator, renderer, itemstack, entity, brightness, handheldTransform);
        }
    }

    public void renderItemInWorld(Tessellator tessellator, Entity entity, ItemStack itemStack, float brightness, float alpha, boolean worldTransform) {
        if (itemStack != null) {
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(2884);
            GL11.glEnable(3042);
            if (((BlockModel)BlockModelDispatcher.getInstance().getDispatch(Blocks.blocksList[itemStack.itemID])).shouldItemRender3d()) {
                GL11.glBlendFunc(770, 771);
                TextureRegistry.blockAtlas.bind();
                if (this.useColor) {
                    int color = this.getColor(itemStack);
                    float r = (float)(color >> 16 & 255) / 255.0F;
                    float g = (float)(color >> 8 & 255) / 255.0F;
                    float b = (float)(color & 255) / 255.0F;
                    GL11.glColor4f(r * brightness, g * brightness, b * brightness, alpha);
                } else {
                    GL11.glColor4f(brightness, brightness, brightness, alpha);
                }

                GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                BlockModel.renderBlocks.useInventoryTint = this.useColor;
                ((BlockModel)BlockModelDispatcher.getInstance().getDispatch(Blocks.blocksList[itemStack.itemID])).renderBlockOnInventory(tessellator, appliedCustomMeta(itemStack.getMetadata()), brightness, alpha, (Integer)null);
                BlockModel.renderBlocks.useInventoryTint = true;
            } else {
                super.renderItemInWorld(tessellator, entity, itemStack, brightness, alpha, worldTransform);
            }

            GL11.glDisable(2884);
            GL11.glDisable(3042);
        }
    }
}
