/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.core;

import dan200.computercraft.BlockPos;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleCommand;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.event.TurtleBlockEvent;
import dan200.computercraft.api.turtle.event.TurtleEvent;
import dan200.computercraft.shared.peripheral.generic.data.BlockData;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class TurtleInspectCommand implements ITurtleCommand
{
    private final InteractDirection direction;

    public TurtleInspectCommand( InteractDirection direction )
    {
        this.direction = direction;
    }

    @Nonnull
    @Override
    public TurtleCommandResult execute( @Nonnull ITurtleAccess turtle )
    {
        // Get world direction from direction
        Direction direction = this.direction.toWorldDir( turtle );

        // Check if thing in front is air or not
        World world = turtle.getWorld();
        BlockPos oldPosition = turtle.getPosition();
        BlockPos newPosition = oldPosition.offset( direction );

        if( world.isAirBlock( newPosition.x, newPosition.y, newPosition.z ) )
        {
            return TurtleCommandResult.failure( "No block to inspect" );
        }

        Block block = world.getBlock( newPosition.x, newPosition.y, newPosition.z );
        int metadata = world.getBlockMetadata( newPosition.x, newPosition.y, newPosition.z );

        Map<String, Object> table = BlockData.fill( new HashMap<>(), block.id(), metadata );

        // Fire the event, exiting if it is cancelled
        TurtleBlockEvent.Inspect event = new TurtleBlockEvent.Inspect( turtle, world, newPosition, block.id(), metadata, table );
        if( TurtleEvent.post( event ) )
        {
            return TurtleCommandResult.failure( event.getFailureMessage() );
        }

        return TurtleCommandResult.success( new Object[] { table } );
    }
}
