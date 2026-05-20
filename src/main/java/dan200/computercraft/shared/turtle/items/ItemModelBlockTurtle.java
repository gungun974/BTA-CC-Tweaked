package dan200.computercraft.shared.turtle.items;

import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.shared.common.IColouredItem;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import net.minecraft.client.render.block.color.BlockColor;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.generic.BlockModelGeneric;
import net.minecraft.client.render.item.model.ItemModelBlock;
import net.minecraft.client.render.renderer.BlendFactor;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.Shaders;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;
import org.useless.dragonfly.DisplayPos;
import org.useless.dragonfly.models.block.StaticBlockModel;

public class ItemModelBlockTurtle extends ItemModelBlock {
    private static StaticBlockModel baseModel;
    private static StaticBlockModel advancedModel;
    private static StaticBlockModel colourModel;

    private final BlockModelGeneric standaloneModel;

    public ItemModelBlockTurtle(ItemBlock<?> itemBlock) {
        super(itemBlock);
        if (baseModel == null) {
            baseModel = BlockModelDispatcher.loadDataModel("computercraft:block/turtle_normal").asModel();
            advancedModel = BlockModelDispatcher.loadDataModel("computercraft:block/turtle_advanced").asModel();
            colourModel = BlockModelDispatcher.loadDataModel("computercraft:block/turtle_colour").asModel();
        }
        this.standaloneModel = new BlockModelGeneric(itemBlock.getBlock(), baseModel);
    }

    @Override
    public @NotNull DisplayPos getDisplayPos(@NotNull String id) {
        return baseModel.getItemDisplayPos(id);
    }

    @Override
    public void render(@NotNull TessellatorGeneral tessellator, @Nullable Entity holder, @NotNull ItemStack itemStack, @NotNull String displayPosId, boolean items3d, int clusterSize, byte lightIndex, float partialTick, boolean mirrorX) {
        random.setSeed(187L);
        GLRenderer.setShader(Shaders.ITEM);
        DisplayPos displayPos = this.getDisplayPos(displayPosId);
        GLRenderer.modelM4f().translate(displayPos.tx, displayPos.ty, displayPos.tz);
        GLRenderer.modelM4f().rotateX(Math.toRadians(displayPos.rx));
        GLRenderer.modelM4f().rotateY(Math.toRadians(displayPos.ry));
        GLRenderer.modelM4f().rotateZ(Math.toRadians(displayPos.rz));
        GLRenderer.modelM4f().scale(displayPos.sx, displayPos.sy, displayPos.sz);

        for (int i = 0; i < clusterSize; i++) {
            float rOffX = 0.0F;
            float rOffY = 0.0F;
            float rOffZ = 0.0F;
            if (i > 0) {
                rOffX = (random.nextFloat() * 2.0F - 1.0F) * 0.2F / displayPos.sx;
                rOffY = (random.nextFloat() * 2.0F - 1.0F) * 0.2F / displayPos.sy;
                rOffZ = (random.nextFloat() * 2.0F - 1.0F) * 0.2F / displayPos.sz;
            }
            GLRenderer.modelM4f().translate(rOffX, rOffY, rOffZ);
            this.renderSingle(tessellator, holder, itemStack, items3d, lightIndex, this.getColor(itemStack), partialTick, mirrorX);
            GLRenderer.modelM4f().translate(-rOffX, -rOffY, -rOffZ);
        }
    }

    @Override
    public void renderGui(@NotNull TessellatorGeneral tessellator, @Nullable Entity holder, @NotNull ItemStack itemStack, int x, int y, byte lightIndex, float partialTick) {
        GLRenderer.pushFrame();
        GLRenderer.setShader(Shaders.ITEM);
        GLRenderer.enableState(State.BLEND);
        GLRenderer.setBlendFunc(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);
        GLRenderer.enableState(State.CULL_FACE);
        GLRenderer.modelM4f().translate((float) (x + 8), (float) (y + 8), 8.0F);
        GLRenderer.modelM4f().scale(16.0F, -16.0F, 16.0F);
        this.render(tessellator, holder, itemStack, "gui", false, 1, lightIndex, partialTick, false);
        GLRenderer.popFrame();
    }

    @Override
    protected void renderSingle(@NotNull TessellatorGeneral tessellator, @Nullable Entity holder, @NotNull ItemStack itemStack, boolean items3d, byte lightIndex, int color, float partialTick, boolean mirrorX) {
        TextureRegistry.worldAtlas.bind();

        int turtleColour = IColouredItem.getColourBasic(itemStack);

        if (turtleColour != -1) {
            final int argbColour = (turtleColour & 0x00FFFFFF) | 0xFF000000;
            colourModel.renderStandalone(standaloneModel, tessellator, 0.0, 0.0, 0.0, 0, lightIndex, new BlockColor() {
                @Override
                public int getFallbackColor(int meta, int tintIndex) {
                    return tintIndex == 0 ? argbColour : -1;
                }

                @Override
                public int getWorldColor(@NotNull WorldSource source, @NotNull TilePosc pos, int tintIndex) {
                    return tintIndex == 0 ? argbColour : -1;
                }
            });
        } else if (itemStack.getItem() instanceof ITurtleItem turtleItem && turtleItem.getFamily() == ComputerFamily.ADVANCED) {
            advancedModel.renderStandalone(standaloneModel, tessellator, 0.0, 0.0, 0.0, 0, lightIndex, BlockColorDispatcher.getInstance().getDispatch(standaloneModel.block));
        } else {
            baseModel.renderStandalone(standaloneModel, tessellator, 0.0, 0.0, 0.0, 0, lightIndex, BlockColorDispatcher.getInstance().getDispatch(standaloneModel.block));
        }

        if (itemStack.getItem() instanceof ITurtleItem turtleItem) {
            ITurtleUpgrade leftUpgrade = turtleItem.getUpgrade(itemStack, TurtleSide.LEFT);
            if (leftUpgrade != null) {
                leftUpgrade.drawItemUpgrade(tessellator, lightIndex, TurtleSide.LEFT);
            }
            ITurtleUpgrade rightUpgrade = turtleItem.getUpgrade(itemStack, TurtleSide.RIGHT);
            if (rightUpgrade != null) {
                rightUpgrade.drawItemUpgrade(tessellator, lightIndex, TurtleSide.RIGHT);
            }
        }
    }
}
