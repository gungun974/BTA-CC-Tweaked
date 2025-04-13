/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.util;

import com.google.common.collect.MapMaker;
import dan200.computercraft.shared.common.TileGeneric;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.world.World;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

/**
 * We use this when modems and other peripherals change a block in a different thread.
 */
public final class TickScheduler {
    private static final Set<TileEntity> toTick = Collections.newSetFromMap(new MapMaker().weakKeys()
        .makeMap());

    private TickScheduler() {
    }

    public static void schedule(TileGeneric tile) {
        World world = tile.worldObj;
        if (world != null && !world.isClientSide) {
            toTick.add(tile);
        }
    }

    public static void tick() {
    }
}
