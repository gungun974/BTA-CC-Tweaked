/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.modem;

import net.minecraft.core.util.helper.Direction;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;

public final class ModemShapes {
    public static AABBdc getBounds(Direction facing) {
        switch (facing) {
            case NORTH:
                return new AABBd(0.125, 0.125, 0.0, 0.875, 0.875, 0.1875);
            case EAST:
                return new AABBd(0.8125, 0.125, 0.125, 1.0, 0.875, 0.875);
            case SOUTH:
                return new AABBd(0.125, 0.125, 0.8125, 0.875, 0.875, 1.0);
            case WEST:
                return new AABBd(0.0, 0.125, 0.125, 0.1875, 0.875, 0.875);
            case UP:
                return new AABBd(0.125, 0.8125, 0.125, 0.875, 1.0, 0.875);
            case DOWN:
                return new AABBd(0.125, 0.0, 0.125, 0.875, 0.1875, 0.875);
        }
        return new AABBd(0, 0, 0, 1, 1, 1);
    }
}
