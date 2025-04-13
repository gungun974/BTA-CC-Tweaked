package dan200.computercraft.fabric;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Global;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.world.World;

import javax.annotation.Nullable;

public class Helper {
    public static boolean isServerEnvironment() {
        return Global.isServer;
    }

    public static boolean isSinglePlayer() {
        if (Global.isServer) {
            return false;
        }

        return !Minecraft.getMinecraft().isMultiplayerWorld();
    }

    public static boolean isClientWorld() {
        return !isSinglePlayer() && !isServerEnvironment();
    }

    public static @Nullable BlockLogic getBlockLogic(final World world, final int x, final int y, final int z) {
        final Block<?> block = world.getBlock(x, y, z);
        if (block == null) return null;
        return block.getLogic();
    }

    public static <T> @Nullable T getBlockLogic(final World world, final int x, final int y, final int z, final Class<T> logicClass) {
        final Block<?> block = world.getBlock(x, y, z);
        if (block == null) return null;
        final BlockLogic logic = block.getLogic();
        if (logicClass.isAssignableFrom(logic.getClass())) {
            return logicClass.cast(logic);
        }
        return null;
    }
}
