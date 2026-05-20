/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.core;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleCommand;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.event.TurtleBlockEvent;
import dan200.computercraft.api.turtle.event.TurtleEvent;
import dan200.computercraft.shared.peripheral.generic.data.BlockData;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TurtleInspectCommand implements ITurtleCommand {
    private final InteractDirection direction;

    public TurtleInspectCommand(InteractDirection direction) {
        this.direction = direction;
    }

    @NotNull
    @Override
    public TurtleCommandResult execute(@NotNull ITurtleAccess turtle) {
        // Get world direction from direction
        Direction direction = this.direction.toWorldDir(turtle);

        // Check if thing in front is air or not
        World world = turtle.getWorld();
        TilePosc oldPosition = turtle.getPosition();
        TilePosc newPosition = oldPosition.add(direction, new TilePos());

        if (world.isAirBlock(newPosition)) {
            return TurtleCommandResult.failure("No block to inspect");
        }

        BlockLogic block = world.getBlockType(newPosition).getLogic();
        int metadata = world.getBlockData(newPosition);

        Map<String, Object> table = BlockData.fill(new HashMap<>(), block.id(), metadata, block.namespaceId());

        // Fire the event, exiting if it is cancelled
        TurtleBlockEvent.Inspect event = new TurtleBlockEvent.Inspect(turtle, world, newPosition, block.id(), metadata, table);
        if (TurtleEvent.post(event)) {
            return TurtleCommandResult.failure(event.getFailureMessage());
        }

        return TurtleCommandResult.success(new Object[]{table});
    }
}
