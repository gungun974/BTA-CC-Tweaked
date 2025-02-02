/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.speaker;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.ComputerCraft;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.shared.common.TileGeneric;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileSpeaker extends TileGeneric implements IPeripheralTile
{
    public static final int MIN_TICKS_BETWEEN_SOUNDS = 1;

    private final SpeakerPeripheral peripheral;

    public TileSpeaker()
    {
        peripheral = new Peripheral( this );
    }

    @Override
    public void tick()
    {
        peripheral.update();
    }

    @Nonnull
    @Override
    public IPeripheral getPeripheral( Direction side )
    {
        return peripheral;
    }

    private static final class Peripheral extends SpeakerPeripheral
    {
        private final TileSpeaker speaker;

        private Peripheral( TileSpeaker speaker )
        {
            this.speaker = speaker;
        }

        @Override
        public World getWorld()
        {
            return speaker.worldObj;
        }

        @Override
        public Vec3 getPosition()
        {
            return Vec3.getPermanentVec3( speaker.x, speaker.y, speaker.z );
        }

        @Override
        public boolean equals( @Nullable IPeripheral other )
        {
            return this == other || (other instanceof Peripheral && speaker == ((Peripheral) other).speaker);
        }
    }
}
