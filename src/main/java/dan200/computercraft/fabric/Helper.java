package dan200.computercraft.fabric;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.world.World;
import turniplabs.halplibe.helper.EnvironmentHelper;

import javax.annotation.Nullable;

public class Helper {
    public static boolean isServerEnvironment() {
        return EnvironmentHelper.isServerEnvironment();
    }

    public static boolean isSinglePlayer() {
        return EnvironmentHelper.isSinglePlayer();
    }

    public static boolean isClientWorld() {
        return EnvironmentHelper.isClientWorld();
    }

    public static @Nullable BlockLogic getBlockLogic(final World world, final int x, final int y, final int z) {
        final Block<?> block = world.getBlock(x, y, z);
        if (block == null) return null;
        return block.getLogic();
    }
}
