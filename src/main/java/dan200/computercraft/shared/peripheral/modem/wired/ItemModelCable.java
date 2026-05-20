package dan200.computercraft.shared.peripheral.modem.wired;

import dan200.computercraft.shared.common.ComputerCraftBlocks;
import net.minecraft.client.render.block.color.BlockColor;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.generic.BlockModelGeneric;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.renderer.BlendFactor;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.Shaders;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;
import org.useless.dragonfly.DisplayPos;
import org.useless.dragonfly.models.block.StaticBlockModel;

public class ItemModelCable extends ItemModelStandard {
    private static StaticBlockModel cableModel;
    private static StaticBlockModel modemModel;
    private static BlockModelGeneric cableModelGeneric;
    private static BlockModelGeneric modemModelGeneric;

    private final boolean isModem;

    public ItemModelCable(Item item) {
        super(item);
        this.isModem = item instanceof ItemCable.WiredModem;
        if (cableModel == null) {
            cableModel = BlockModelDispatcher.loadDataModel("computercraft:item/cable").asModel();
            modemModel = BlockModelDispatcher.loadDataModel("computercraft:item/wired_modem").asModel();
            cableModelGeneric = new BlockModelGeneric(ComputerCraftBlocks.CABLE, cableModel);
            modemModelGeneric = new BlockModelGeneric(ComputerCraftBlocks.CABLE, modemModel);
        }
        this.icon = isModem ? modemModel.getOverlay() : cableModel.getOverlay();
        if (this.icon == null) {
            this.icon = ITEM_TEXTURE_UNASSIGNED;
        }
    }

    private StaticBlockModel getModel() {
        return isModem ? modemModel : cableModel;
    }

    private BlockModelGeneric getModelGeneric() {
        return isModem ? modemModelGeneric : cableModelGeneric;
    }

    @Override
    public @NotNull DisplayPos getDisplayPos(@NotNull String id) {
        return getModel().getItemDisplayPos(id);
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
        getModel().renderStandalone(getModelGeneric(), tessellator, 0.0, 0.0, 0.0, 0, lightIndex, new BlockColor() {
            @Override
            public int getFallbackColor(int meta, int tintIndex) {
                return -1;
            }

            @Override
            public int getWorldColor(@NotNull WorldSource source, @NotNull TilePosc pos, int tintIndex) {
                return -1;
            }
        });
    }
}
