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
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;

import java.util.EnumMap;

public final class CableShapes {
    private static final double MIN = 0.375;
    private static final double MAX = 1 - MIN;

    public static final AABBdc SHAPE_CABLE_CORE = new AABBd(MIN, MIN, MIN, MAX, MAX, MAX);
    public static final EnumMap<Direction, AABBdc> SHAPE_CABLE_ARM =
        new EnumMap<>(new ImmutableMap.Builder<Direction, AABBdc>().put(Direction.DOWN,
                new AABBd(
                    MIN,
                    0,
                    MIN,
                    MAX,
                    MIN,
                    MAX))
            .put(Direction.UP,
                new AABBd(
                    MIN,
                    MAX,
                    MIN,
                    MAX,
                    1,
                    MAX))
            .put(Direction.NORTH,
                new AABBd(
                    MIN,
                    MIN,
                    0,
                    MAX,
                    MAX,
                    MIN))
            .put(Direction.SOUTH,
                new AABBd(
                    MIN,
                    MIN,
                    MAX,
                    MAX,
                    MAX,
                    1))
            .put(Direction.WEST,
                new AABBd(
                    0,
                    MIN,
                    MIN,
                    MIN,
                    MAX,
                    MAX))
            .put(Direction.EAST,
                new AABBd(
                    MAX,
                    MIN,
                    MIN,
                    1,
                    MAX,
                    MAX))
            .build());

    private static final AABBdc[] SHAPES = new AABBd[(1 << 6) * 7];
    private static final AABBdc[] CABLE_SHAPES = new AABBd[1 << 6];

    private CableShapes() {
    }

    public static AABBdc getCableShape(TileCable state) {
        if (!state.blockStateCable) {
            return new AABBd(0, 0, 0, 0, 0, 0);
        }
        return getCableShape(getCableIndex(state));
    }

    private static AABBdc getCableShape(int index) {
        AABBdc shape = CABLE_SHAPES[index];
        if (shape != null) {
            return shape;
        }

        shape = SHAPE_CABLE_CORE;
        for (Direction facing : DirectionUtil.FACINGS) {
            if ((index & (1 << facing.ordinal())) != 0) {
                shape = union(shape, SHAPE_CABLE_ARM.get(facing));
            }
        }

        return CABLE_SHAPES[index] = shape;
    }

    private static int getCableIndex(TileCable state) {
        int index = 0;
        for (Direction facing : DirectionUtil.FACINGS) {
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
                default:
                    break;
            }
            if (hasConnection) {
                index |= 1 << facing.ordinal();
            }
        }

        return index;
    }

    public static AABBdc getShape(TileCable state) {
        Direction facing = state.blockStateModem
            .getFacing();
        if (!state.blockStateCable) {
            return getModemShape(state);
        }

        int cableIndex = getCableIndex(state);
        int index = cableIndex + ((facing == null ? 0 : facing.ordinal() + 1) << 6);

        AABBdc shape = SHAPES[index];
        if (shape != null) {
            return shape;
        }

        shape = getCableShape(cableIndex);
        if (facing != null) {
            shape = union(shape, ModemShapes.getBounds(facing));
        }
        return SHAPES[index] = shape;
    }

    public static AABBdc getModemShape(TileCable state) {
        Direction facing = state.blockStateModem
            .getFacing();
        return facing == null ? new AABBd(0, 0, 0, 0, 0, 0) : ModemShapes.getBounds(facing);
    }

    private static AABBdc union(AABBdc a, AABBdc b) {
        return new AABBd(
            Math.min(a.minX(), b.minX()),
            Math.min(a.minY(), b.minY()),
            Math.min(a.minZ(), b.minZ()),
            Math.max(a.maxX(), b.maxX()),
            Math.max(a.maxY(), b.maxY()),
            Math.max(a.maxZ(), b.maxZ())
        );
    }

}
