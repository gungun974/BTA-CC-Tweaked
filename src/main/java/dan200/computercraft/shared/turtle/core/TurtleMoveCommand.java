/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.core;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleCommand;
import dan200.computercraft.api.turtle.TurtleAnimation;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.event.TurtleBlockEvent;
import dan200.computercraft.api.turtle.event.TurtleEvent;
import dan200.computercraft.shared.TurtlePermissions;
import dan200.computercraft.shared.util.WorldUtil;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.ChunkPos;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.joml.primitives.AABBd;

import java.util.List;

public class TurtleMoveCommand implements ITurtleCommand {
    private final MoveDirection direction;

    public TurtleMoveCommand(MoveDirection direction) {
        this.direction = direction;
    }

    private static TurtleCommandResult canEnter(World world, TilePosc position) {
        if (position.y() < 0 || position.y() >= World.HEIGHT_BLOCKS) {
            return TurtleCommandResult.failure(position.y() < 0 ? "Too low to move" : "Too high to move");
        }
        // Check spawn protection
        if (ComputerCraft.turtlesObeyBlockProtection && !TurtlePermissions.isBlockEnterable(world, position)) {
            return TurtleCommandResult.failure("Cannot enter protected area");
        }

        if (!world.isChunkLoaded(new ChunkPos(Math.floorDiv(position.x(), 16), Math.floorDiv(position.z(), 16)))) {
            return TurtleCommandResult.failure("Cannot leave loaded world");
        }
        return TurtleCommandResult.success();
    }

    @NotNull
    @Override
    public TurtleCommandResult execute(@NotNull ITurtleAccess turtle) {
        // Get world direction from direction
        Direction direction = this.direction.toWorldDir(turtle);

        // Check if we can move
        World oldWorld = turtle.getWorld();
        TilePosc oldPosition = turtle.getPosition();
        TilePosc newPosition = oldPosition.add(direction, new TilePos());

//        TurtlePlayer turtlePlayer = TurtlePlaceCommand.createPlayer( turtle, oldPosition, direction );
        TurtleCommandResult canEnterResult = canEnter(oldWorld, newPosition);
        if (!canEnterResult.isSuccess()) {
            return canEnterResult;
        }

        // Check existing block is air or replaceable
        if (!oldWorld.isAirBlock(newPosition) && !WorldUtil.isLiquidBlock(oldWorld, newPosition) && !oldWorld.getBlockMaterial(newPosition)
            .isReplaceable()) {
            return TurtleCommandResult.failure("Movement obstructed");
        }

        // Check there isn't anything in the way

        AABBd collision = new AABBd(newPosition.x(), newPosition.y(), newPosition.z(), newPosition.x() + 1.0, newPosition.y() + 1.0, newPosition.z() + 1.0);

        if (!oldWorld.getEntitiesWithinAABB(Entity.class, collision).isEmpty()) {
            if (!ComputerCraft.turtlesCanPush || this.direction == MoveDirection.UP || this.direction == MoveDirection.DOWN) {
                return TurtleCommandResult.failure("Movement obstructed");
            }

            // Check there is space for all the pushable entities to be pushed
            List<Entity> list = oldWorld.getEntitiesWithinAABB(Entity.class, (collision));
            for (Entity entity : list) {
                if (!entity.isAlive()) {
                    continue;
                }
                double dx = Math.abs(direction.offsetX()) * 0.5;
                double dy = Math.abs(direction.offsetY()) * 0.5;
                double dz = Math.abs(direction.offsetZ()) * 0.5;

                AABBd pushedBB = new AABBd(
                    entity.bb.minX() - dx,
                    entity.bb.minY() - dy,
                    entity.bb.minZ() - dz,
                    entity.bb.maxX() + dx,
                    entity.bb.maxY() + dy,
                    entity.bb.maxZ() + dz
                );

                if (!oldWorld.getEntitiesWithinAABB(Entity.class, pushedBB).isEmpty()) {
                    return TurtleCommandResult.failure("Movement obstructed");
                }
            }
        }

        TurtleBlockEvent.Move moveEvent = new TurtleBlockEvent.Move(turtle, oldWorld, newPosition);
        if (TurtleEvent.post(moveEvent)) {
            return TurtleCommandResult.failure(moveEvent.getFailureMessage());
        }

        // Check fuel level
        if (turtle.isFuelNeeded() && turtle.getFuelLevel() < 1) {
            return TurtleCommandResult.failure("Out of fuel");
        }

        // Move
        if (!turtle.teleportTo(oldWorld, newPosition)) {
            return TurtleCommandResult.failure("Movement failed");
        }

        // Consume fuel
        turtle.consumeFuel(1);

        // Animate
        switch (this.direction) {
            case FORWARD:
            default:
                turtle.playAnimation(TurtleAnimation.MOVE_FORWARD);
                break;
            case BACK:
                turtle.playAnimation(TurtleAnimation.MOVE_BACK);
                break;
            case UP:
                turtle.playAnimation(TurtleAnimation.MOVE_UP);
                break;
            case DOWN:
                turtle.playAnimation(TurtleAnimation.MOVE_DOWN);
                break;
        }
        return TurtleCommandResult.success();
    }
}
