/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.pocket.peripherals;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.shared.peripheral.speaker.SpeakerPeripheral;
import net.minecraft.core.world.World;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class PocketSpeakerPeripheral extends SpeakerPeripheral {
    private World world = null;
    private Vector3dc position = new Vector3d(0, 0, 0);

    void setLocation(World world, Vector3dc position) {
        this.position = position;
        this.world = world;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public Vector3dc getPosition() {
        return world != null ? position : null;
    }

    @Override
    public boolean equals(IPeripheral other) {
        return other instanceof PocketSpeakerPeripheral;
    }
}
