package dan200.computercraft.shared.turtle.blocks;

import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.useless.dragonfly.DisplayPos;

public class BlockModelTurtle<T extends BlockLogic> extends BlockModel<T> {
    public BlockModelTurtle(Block<T> block) {
        super(block);
    }

    @Override
    public boolean render(@NotNull TessellatorGeneral var1, @NotNull WorldSource var2, @NotNull TilePosc var3) {
        return false;
    }

    @Override
    public boolean renderNoCulling(@NotNull TessellatorGeneral var1, @NotNull WorldSource var2, @NotNull TilePosc var3) {
        return false;
    }

    @Override
    public boolean renderWithOverrideTexture(@NotNull TessellatorGeneral var1, @NotNull WorldSource var2, @NotNull TilePosc var3, IconCoordinate var4) {
        return false;
    }

    @Override
    public void renderStandalone(@NotNull TessellatorGeneral var1, int var2, byte var3) {
    }

    @Override
    public boolean shouldItemRender3d() {
        return false;
    }

    @Override
    public @NotNull DisplayPos getItemDisplayPos(@NotNull String var1) {
        return DisplayPos.DEFAULT_DISPLAY_POS;
    }

    @Override
    public @Nullable IconCoordinate getParticleTexture(@NotNull Side var1, int var2) {
        return null;
    }

    @Override
    public @Nullable IconCoordinate getOverlayTexture(int var1) {
        return null;
    }

    @Override
    public int particleColorIndex(@NotNull WorldSource var1, @NotNull TilePosc var2, @NotNull Side var3, int var4) {
        return 0;
    }
}
