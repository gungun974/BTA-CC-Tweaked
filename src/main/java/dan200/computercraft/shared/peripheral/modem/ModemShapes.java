/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.modem;

import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.phys.AABB;

public final class ModemShapes
{
    public static AABB getBounds(Direction facing)
    {
        switch (facing) {
            case NORTH:
                return AABB.getPermanentBB( 0.125, 0.125, 0.0, 0.875, 0.875, 0.1875 );
            case EAST:
                return AABB.getPermanentBB( 0.8125, 0.125, 0.125, 1.0, 0.875, 0.875 );
            case SOUTH:
                return AABB.getPermanentBB( 0.125, 0.125, 0.8125, 0.875, 0.875, 1.0 );
            case WEST:
                return AABB.getPermanentBB( 0.0, 0.125, 0.125, 0.1875, 0.875, 0.875 );
            case UP:
                return AABB.getPermanentBB( 0.125, 0.8125, 0.125, 0.875, 1.0, 0.875 );
            case DOWN:
                return AABB.getPermanentBB(0.125, 0.0, 0.125, 0.875, 0.1875, 0.875);
        }
        return AABB.getPermanentBB(0, 0, 0, 1, 1, 1);
    }
}
