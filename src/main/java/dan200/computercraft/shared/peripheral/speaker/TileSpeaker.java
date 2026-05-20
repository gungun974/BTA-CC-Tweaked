/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.speaker;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.shared.common.TileGeneric;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class TileSpeaker extends TileGeneric implements IPeripheralTile {
    public static final int MIN_TICKS_BETWEEN_SOUNDS = 1;

    private final SpeakerPeripheral peripheral;

    public TileSpeaker() {
        peripheral = new Peripheral(this);
    }

    @Override
    public void tick() {
        peripheral.update();
    }

    @Override
    public void readAdditionalData(@NotNull CompoundTag var1) {

    }

    @Override
    public void writeAdditionalData(@NotNull CompoundTag var1) {

    }

    @NotNull
    @Override
    public IPeripheral getPeripheral(Direction side) {
        return peripheral;
    }

    private static final class Peripheral extends SpeakerPeripheral {
        private final TileSpeaker speaker;

        private Peripheral(TileSpeaker speaker) {
            this.speaker = speaker;
        }

        @Override
        public World getWorld() {
            return speaker.worldObj;
        }

        @Override
        public Vector3dc getPosition() {
            return new Vector3d(speaker.tilePos.x, speaker.tilePos.y, speaker.tilePos.z);
        }

        @Override
        public boolean equals(@Nullable IPeripheral other) {
            return this == other || (other instanceof Peripheral && speaker == ((Peripheral) other).speaker);
        }
    }
}
