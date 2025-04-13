/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.modem.wired;

import com.google.common.collect.ImmutableMap;
import dan200.computercraft.shared.peripheral.modem.ModemShapes;
import dan200.computercraft.shared.util.DirectionUtil;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.phys.AABB;

import java.util.EnumMap;

public final class CableShapes
{
    private static final double MIN = 0.375;
    private static final double MAX = 1 - MIN;

    public static final AABB SHAPE_CABLE_CORE = AABB.getPermanentBB( MIN, MIN, MIN, MAX, MAX, MAX );
    public static final EnumMap<Direction, AABB> SHAPE_CABLE_ARM =
        new EnumMap<>( new ImmutableMap.Builder<Direction, AABB>().put( Direction.DOWN,
            AABB.getPermanentBB(
                MIN,
                0,
                MIN,
                MAX,
                MIN,
                MAX ) )
            .put( Direction.UP,
                AABB.getPermanentBB(
                    MIN,
                    MAX,
                    MIN,
                    MAX,
                    1,
                    MAX ) )
            .put( Direction.NORTH,
                AABB.getPermanentBB(
                    MIN,
                    MIN,
                    0,
                    MAX,
                    MAX,
                    MIN ) )
            .put( Direction.SOUTH,
                AABB.getPermanentBB(
                    MIN,
                    MIN,
                    MAX,
                    MAX,
                    MAX,
                    1 ) )
            .put( Direction.WEST,
                AABB.getPermanentBB(
                    0,
                    MIN,
                    MIN,
                    MIN,
                    MAX,
                    MAX ) )
            .put( Direction.EAST,
                AABB.getPermanentBB(
                    MAX,
                    MIN,
                    MIN,
                    1,
                    MAX,
                    MAX ) )
            .build() );

    private static final AABB[] SHAPES = new AABB[(1 << 6) * 7];
    private static final AABB[] CABLE_SHAPES = new AABB[1 << 6];

    private CableShapes()
    {
    }

    public static AABB getCableShape( TileCable state )
    {
        if( !state.blockStateCable )
        {
            return AABB.getPermanentBB(0, 0, 0, 0, 0, 0);
        }
        return getCableShape( getCableIndex( state ) );
    }

    private static AABB getCableShape( int index )
    {
        AABB shape = CABLE_SHAPES[index];
        if( shape != null )
        {
            return shape;
        }

        shape = SHAPE_CABLE_CORE;
        for( Direction facing : DirectionUtil.FACINGS )
        {
            if( (index & (1 << facing.ordinal())) != 0 )
            {
                shape = union( shape, SHAPE_CABLE_ARM.get( facing ) );
            }
        }

        return CABLE_SHAPES[index] = shape;
    }

    private static int getCableIndex( TileCable state )
    {
        int index = 0;
        for( Direction facing : DirectionUtil.FACINGS )
        {
            boolean hasConnection = false;
            switch (facing) {
                case NORTH:
                    hasConnection = state.blockStateNorth;
                    break;
                case EAST:
                    hasConnection = state.blockStateEast;
                    break;
                case SOUTH:
                    hasConnection = state.blockStateSouth;
                    break;
                case WEST:
                    hasConnection = state.blockStateWest;
                    break;
                case UP:
                    hasConnection = state.blockStateUp;
                    break;
                case DOWN:
                    hasConnection = state.blockStateDown;
                    break;
                case NONE:
                    break;
            }
            if( hasConnection )
            {
                index |= 1 << facing.ordinal();
            }
        }

        return index;
    }

    public static AABB getShape( TileCable state )
    {
        Direction facing = state.blockStateModem
            .getFacing();
        if( !state.blockStateCable )
        {
            return getModemShape( state );
        }

        int cableIndex = getCableIndex( state );
        int index = cableIndex + ((facing == null ? 0 : facing.ordinal() + 1) << 6);

        AABB shape = SHAPES[index];
        if( shape != null )
        {
            return shape;
        }

        shape = getCableShape( cableIndex );
        if( facing != null )
        {
            shape = union( shape, ModemShapes.getBounds( facing ) );
        }
        return SHAPES[index] = shape;
    }

    public static AABB getModemShape( TileCable state )
    {
        Direction facing = state.blockStateModem
            .getFacing();
        return facing == null ? AABB.getPermanentBB(0, 0, 0, 0, 0, 0) : ModemShapes.getBounds( facing );
    }

    private static AABB union(AABB a, AABB b) {
        return AABB.getPermanentBB(
            Math.min(a.minX, b.minX),
            Math.min(a.minY, b.minY),
            Math.min(a.minZ, b.minZ),
            Math.max(a.maxX, b.maxX),
            Math.max(a.maxY, b.maxY),
            Math.max(a.maxZ, b.maxZ)
        );
    }

}
