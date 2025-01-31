/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.util;

import dan200.computercraft.core.computer.ComputerSide;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Direction;

public final class DirectionUtil
{
    public static final Direction[] FACINGS = {
        Direction.NORTH,
        Direction.EAST,
        Direction.SOUTH,
        Direction.WEST,
        Direction.UP,
        Direction.DOWN,
    };

    private DirectionUtil() {}

    public static ComputerSide toLocal( Direction front, Direction dir )
    {
       if (dir == Direction.NONE) {
           throw new RuntimeException("Direction should not be none in Computer World");
       }

        if( front.getAxis() == Axis.Y )
        {
            front = Direction.NORTH;
        }

        if( dir == front )
        {
            return ComputerSide.FRONT;
        }
        if( dir == front.getOpposite() )
        {
            return ComputerSide.BACK;
        }
        if( dir == rotateYCounterclockwise(front) )
        {
            return ComputerSide.LEFT;
        }
        if( dir == rotateYClockwise(front) )
        {
            return ComputerSide.RIGHT;
        }
        if( dir == Direction.UP )
        {
            return ComputerSide.TOP;
        }
        return ComputerSide.BOTTOM;
    }

    public static float toPitchAngle( Direction dir )
    {
        switch( dir )
        {
            case DOWN:
                return 90.0f;
            case UP:
                return 270.0f;
            default:
                return 0.0f;
        }
    }

    private static Direction rotateYClockwise(Direction direction) {
        if (direction == Direction.UP || direction == Direction.DOWN || direction == Direction.NONE) {
            return direction;
        }
        return Direction.horizontalDirections[(direction.getHorizontalIndex() + 1) % 4];
    }

    private static Direction rotateYCounterclockwise(Direction direction) {
        if (direction == Direction.UP || direction == Direction.DOWN || direction == Direction.NONE) {
            return direction;
        }
        return Direction.horizontalDirections[(direction.getHorizontalIndex() + 3) % 4];
    }
}
