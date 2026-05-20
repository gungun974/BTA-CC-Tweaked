/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.common;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;

public abstract class TileGeneric extends TileEntity {
    public void onChunkUnloaded() {
    }

    public boolean isUsable(Player player, boolean ignoreRange) {
        if (player == null || !player.isAlive() || worldObj == null || worldObj.getTileEntity(tilePos) != this) {
            return false;
        }
        if (ignoreRange) {
            return true;
        }

        double range = getInteractRange(player);
        return player.world == worldObj && player.distanceToSqr(tilePos.x + 0.5, tilePos.y + 0.5, tilePos.z + 0.5) <= range * range;
    }

    protected double getInteractRange(Player player) {
        return 8.0;
    }
}
