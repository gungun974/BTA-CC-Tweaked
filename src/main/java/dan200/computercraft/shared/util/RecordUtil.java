/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.util;

import dan200.computercraft.shared.network.client.PlayRecordClientMessage;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import turniplabs.halplibe.helper.network.NetworkHandler;
import turniplabs.halplibe.helper.network.NetworkMessage;

public final class RecordUtil {
    private RecordUtil() {
    }

    public static void playRecord(String record, String recordInfo, World world, TilePosc pos) {
        NetworkMessage packet = record != null ? new PlayRecordClientMessage(pos, record, recordInfo) : new PlayRecordClientMessage(pos);
        NetworkHandler.sendToAllAround(pos.x(), pos.y(), pos.z(), 64, world.dimension.id, packet);
    }
}
