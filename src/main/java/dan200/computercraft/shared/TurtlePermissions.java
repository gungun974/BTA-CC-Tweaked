/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared;

import com.google.common.eventbus.Subscribe;
import dan200.computercraft.BlockPos;
import dan200.computercraft.ComputerCraft;
import dan200.computercraft.api.turtle.event.TurtleActionEvent;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.World;
import net.minecraft.server.MinecraftServer;

public final class TurtlePermissions
{
    public static boolean isBlockEditable(World world, BlockPos pos, Player player )
    {
        return isBlockEnterable( world, pos, player );
    }

    public static boolean isBlockEnterable( World world, BlockPos pos, Player player )
    {
        //TODO: Make sure turtle respect spawn protection
        return true;
//        MinecraftServer server = MinecraftServer.getInstance();
//        return server == null || world.isClient || (world instanceof ServerWorld && !server.isSpawnProtected( (ServerWorld) world, pos, player ));
    }

    @Subscribe
    public void onTurtleAction( TurtleActionEvent event )
    {
        if( ComputerCraft.turtleDisabledActions.contains( event.getAction() ) )
        {
            event.setCanceled( true, "Action has been disabled" );
        }
    }
}
