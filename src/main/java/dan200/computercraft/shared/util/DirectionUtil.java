/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.util;

import dan200.computercraft.core.computer.ComputerSide;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Direction;

public final class DirectionUtil {
    public static final Direction[] FACINGS = {
        Direction.NORTH,
        Direction.EAST,
        Direction.SOUTH,
        Direction.WEST,
        Direction.UP,
        Direction.DOWN,
    };

    private DirectionUtil() {
    }

    public static ComputerSide toLocal(Direction front, Direction dir) {
        if (front.axis() == Axis.Y) {
            front = Direction.NORTH;
        }

        if (dir == front) {
            return ComputerSide.FRONT;
        }
        if (dir == front.opposite()) {
            return ComputerSide.BACK;
        }
        if (dir == rotateYCounterclockwise(front)) {
            return ComputerSide.LEFT;
        }
        if (dir == rotateYClockwise(front)) {
            return ComputerSide.RIGHT;
        }
        if (dir == Direction.UP) {
            return ComputerSide.TOP;
        }
        return ComputerSide.BOTTOM;
    }

    public static float toPitchAngle(Direction dir) {
        return switch (dir) {
            case DOWN -> 90.0f;
            case UP -> 270.0f;
            default -> 0.0f;
        };
    }

    public static Direction rotateYClockwise(Direction direction) {
        return switch (direction) {
            case NORTH -> Direction.EAST;
            case EAST -> Direction.SOUTH;
            case SOUTH -> Direction.WEST;
            case WEST -> Direction.NORTH;
            default -> direction;
        };
    }

    public static Direction rotateYCounterclockwise(Direction direction) {
        return switch (direction) {
            case NORTH -> Direction.WEST;
            case WEST -> Direction.SOUTH;
            case SOUTH -> Direction.EAST;
            case EAST -> Direction.NORTH;
            default -> direction;
        };
    }

    public static float getRotationYaw(Direction direction) {
        return switch (direction) {
            case NORTH -> 180.0f;
            case EAST -> 270.0f;
            case SOUTH -> 0.0f;
            case WEST -> 90.0f;
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        };
    }
}
