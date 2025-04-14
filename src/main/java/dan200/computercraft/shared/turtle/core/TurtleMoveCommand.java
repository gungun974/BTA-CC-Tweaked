/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.core;

import dan200.computercraft.shared.util.BlockPos;
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
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class TurtleMoveCommand implements ITurtleCommand {
    private final MoveDirection direction;

    public TurtleMoveCommand(MoveDirection direction) {
        this.direction = direction;
    }

    private static TurtleCommandResult canEnter(World world, BlockPos position) {
        if (position.getY() < 0 || position.getY() >= World.HEIGHT_BLOCKS) {
            return TurtleCommandResult.failure(position.getY() < 0 ? "Too low to move" : "Too high to move");
        }
        // Check spawn protection
        if (ComputerCraft.turtlesObeyBlockProtection && !TurtlePermissions.isBlockEnterable(world, position)) {
            return TurtleCommandResult.failure("Cannot enter protected area");
        }

        if (!world.isChunkLoaded(Math.floorDiv(position.x, 16), Math.floorDiv(position.z, 16))) {
            return TurtleCommandResult.failure("Cannot leave loaded world");
        }
        return TurtleCommandResult.success();
    }

    @Nonnull
    @Override
    public TurtleCommandResult execute(@Nonnull ITurtleAccess turtle) {
        // Get world direction from direction
        Direction direction = this.direction.toWorldDir(turtle);

        // Check if we can move
        World oldWorld = turtle.getWorld();
        BlockPos oldPosition = turtle.getPosition();
        BlockPos newPosition = oldPosition.offset(direction);

//        TurtlePlayer turtlePlayer = TurtlePlaceCommand.createPlayer( turtle, oldPosition, direction );
        TurtleCommandResult canEnterResult = canEnter(oldWorld, newPosition);
        if (!canEnterResult.isSuccess()) {
            return canEnterResult;
        }

        // Check existing block is air or replaceable
        if (!oldWorld.isAirBlock(newPosition.x, newPosition.y, newPosition.z) && !WorldUtil.isLiquidBlock(oldWorld, newPosition) && !oldWorld.getBlockMaterial(newPosition.x, newPosition.y, newPosition.z)
            .isReplaceable()) {
            return TurtleCommandResult.failure("Movement obstructed");
        }

        // Check there isn't anything in the way

        AABB collision = AABB.getPermanentBB(newPosition.getX(), newPosition.getY(), newPosition.getZ(), newPosition.getX() + 1.0, newPosition.getY() + 1.0, newPosition.getZ() + 1.0);

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
                AABB pushedBB = entity.bb.copy()
                    .move(-direction.getOffsetX() / 2.0, -direction.getOffsetY() / 2.0, -direction.getOffsetZ() / 2.0)
                    .expand(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ());
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
