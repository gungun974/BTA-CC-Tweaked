package dan200.computercraft.shared.turtle.blocks;

import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.color.BlockColor;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.generic.BlockModelGeneric;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.Block;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3dc;
import org.useless.dragonfly.models.block.StaticBlockModel;

@Environment(EnvType.CLIENT)
public class TileEntityRendererTurtle extends TileEntityRenderer<TileTurtle> {
    public final @NotNull StaticBlockModel base;
    public final @NotNull StaticBlockModel advanced;
    public final @NotNull StaticBlockModel colour;

    @Nullable
    private BlockModelGeneric baseGeneric = null;
    @Nullable
    private BlockModelGeneric advancedGeneric = null;
    @Nullable
    private BlockModelGeneric colourGeneric = null;

    public TileEntityRendererTurtle() {
        this.base = BlockModelDispatcher.loadDataModel("computercraft:block/turtle_normal").asModel();
        this.advanced = BlockModelDispatcher.loadDataModel("computercraft:block/turtle_advanced").asModel();
        this.colour = BlockModelDispatcher.loadDataModel("computercraft:block/turtle_colour").asModel();
    }

    private void ensureGenericModels(Block<?> block) {
        if (baseGeneric == null) {
            baseGeneric = new BlockModelGeneric(block, this.base);
            advancedGeneric = new BlockModelGeneric(block, this.advanced);
            colourGeneric = new BlockModelGeneric(block, this.colour);
        }
    }

    @Override
    public void doRender(TessellatorGeneral tessellator, TileTurtle tileEntity, double x, double y, double z, float partialTick) {
        if (tileEntity.carriedBlock == null && tileEntity.worldObj == null) {
            return;
        }

        Block<?> block = tileEntity.getBlock();
        if (block == null || !(block.getLogic() instanceof BlockLogicTurtle)) {
            return;
        }

        ensureGenericModels(block);

        Vector3dc offset = tileEntity.getRenderOffset(partialTick);
        float yaw = tileEntity.getRenderYaw(partialTick);
        byte lightIndex = tileEntity.worldObj.getLightIndex(tileEntity.tilePos, 0);

        int colour = tileEntity.getColour();
        ComputerFamily family = tileEntity.getFamily();

        TextureRegistry.worldAtlas.bind();

        GLRenderer.pushFrame();
        GLRenderer.modelM4f().translate((float) (x + offset.x() + 0.5), (float) (y + offset.y() + 0.5), (float) (z + offset.z() + 0.5));
        GLRenderer.modelM4f().rotateY((float) Math.toRadians(180.0f - yaw));

        if (colour != -1) {
            final int turtleColour = (colour & 0x00FFFFFF) | 0xFF000000;
            this.colour.renderStandalone(colourGeneric, tessellator, 0.0, 0.0, 0.0, 0, lightIndex, new BlockColor() {
                @Override
                public int getFallbackColor(int meta, int tintIndex) {
                    return tintIndex == 0 ? turtleColour : -1;
                }

                @Override
                public int getWorldColor(@NotNull WorldSource source, @NotNull TilePosc pos, int tintIndex) {
                    return tintIndex == 0 ? turtleColour : -1;
                }
            });
        } else if (family == ComputerFamily.ADVANCED) {
            advancedGeneric.renderStandalone(tessellator, 0, lightIndex);
        } else {
            baseGeneric.renderStandalone(tessellator, 0, lightIndex);
        }

        ITurtleUpgrade leftUpgrade = tileEntity.getAccess().getUpgrade(TurtleSide.LEFT);
        if (leftUpgrade != null) {
            leftUpgrade.drawTileUpgrade(tessellator, this.renderDispatcher.textureManager, tileEntity, yaw, TurtleSide.LEFT, partialTick);
        }

        ITurtleUpgrade rightUpgrade = tileEntity.getAccess().getUpgrade(TurtleSide.RIGHT);
        if (rightUpgrade != null) {
            rightUpgrade.drawTileUpgrade(tessellator, this.renderDispatcher.textureManager, tileEntity, yaw, TurtleSide.RIGHT, partialTick);
        }

        GLRenderer.popFrame();
    }
}
