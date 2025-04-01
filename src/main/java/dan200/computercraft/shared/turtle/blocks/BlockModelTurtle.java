package dan200.computercraft.shared.turtle.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

public class BlockModelTurtle<T extends BlockLogic> extends BlockModelStandard<T> {
    public BlockModelTurtle(Block<T> block) {
        super(block);
    }

    @Override
    public boolean render(Tessellator tessellator, int x, int y, int z) {
        return false;
    }
}
