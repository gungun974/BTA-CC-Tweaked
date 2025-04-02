/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.pocket.peripherals;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.shared.peripheral.speaker.SpeakerPeripheral;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;

public class PocketSpeakerPeripheral extends SpeakerPeripheral
{
    private World world = null;
    private Vec3 position = Vec3.getPermanentVec3(0, 0, 0);

    void setLocation( World world, Vec3 position )
    {
        this.position = position;
        this.world = world;
    }

    @Override
    public World getWorld()
    {
        return world;
    }

    @Override
    public Vec3 getPosition()
    {
        return world != null ? position : null;
    }

    @Override
    public boolean equals( IPeripheral other )
    {
        return other instanceof PocketSpeakerPeripheral;
    }
}
