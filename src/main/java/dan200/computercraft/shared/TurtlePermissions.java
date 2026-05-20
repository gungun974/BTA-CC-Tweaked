/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared;

import com.google.common.eventbus.Subscribe;
import dan200.computercraft.ComputerCraft;
import dan200.computercraft.api.turtle.event.TurtleActionEvent;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import net.minecraft.server.MinecraftServer;
import turniplabs.halplibe.helper.EnvironmentHelper;

public final class TurtlePermissions {
    public static boolean isBlockEditable(World world, TilePosc pos) {
        return isBlockEnterable(world, pos);
    }

    public static boolean isBlockEnterable(World world, TilePosc pos) {
        if (!EnvironmentHelper.isServerEnvironment()) {
            return true;
        }

        MinecraftServer mcServer = MinecraftServer.getInstance();

        if (mcServer.spawnProtectionRange > 0) {
            int dx = (int) MathHelper.abs((float) (pos.x() - world.getLevelData().getSpawnPos().x()));
            int dz = (int) MathHelper.abs((float) (pos.z() - world.getLevelData().getSpawnPos().z()));
            return Math.max(dx, dz) > mcServer.spawnProtectionRange;
        }

        return true;
    }

    @Subscribe
    public void onTurtleAction(TurtleActionEvent event) {
        if (ComputerCraft.turtleDisabledActions.contains(event.getAction())) {
            event.setCanceled(true, "Action has been disabled");
        }
    }
}
