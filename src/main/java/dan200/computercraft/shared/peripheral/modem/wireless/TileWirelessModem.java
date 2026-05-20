/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.modem.wireless;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.shared.common.TileGeneric;
import dan200.computercraft.shared.peripheral.modem.ModemPeripheral;
import dan200.computercraft.shared.peripheral.modem.ModemState;
import dan200.computercraft.shared.util.TickScheduler;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class TileWirelessModem extends TileGeneric implements IPeripheralTile {
    private static final String NBT_ADVANCED = "Advanced";
    private final ModemPeripheral modem;
    private boolean advanced;
    private boolean hasModemDirection = false;
    private Direction modemDirection = Direction.DOWN;
    private final boolean destroyed = false;

    public TileWirelessModem() {
        this(false);
    }

    public TileWirelessModem(boolean advanced) {
        this.advanced = advanced;
        modem = new Peripheral(this);
    }

    @Override
    public void invalidate() {
        modem.destroy();
        super.invalidate();
    }

    @Override
    public void tick() {
        Direction currentDirection = modemDirection;
        refreshDirection();
        // Invalidate the capability if the direction has changed. I'm not 100% happy with this implementation
        //  - ideally we'd do it within refreshDirection or updateContainingBlockInfo, but this seems the _safest_
        //  place.
        if (modem.getModemState()
            .pollChanged()) {
            updateBlockState();
        }
    }

    private void refreshDirection() {
        if (hasModemDirection) {
            return;
        }

        hasModemDirection = true;
        modemDirection = BlockLogicWirelessModem.metaToDirection(worldObj.getBlockData(tilePos)).opposite();
    }

    private void updateBlockState() {
        final boolean on = modem.getModemState()
            .isOpen();

        final int currentMetadata = getBlockMeta();

        final boolean isOn = ((currentMetadata >> 3) & 0b1) == 1;

        if (isOn != on) {
            final int newMetadata = (currentMetadata & ~0b1000) | ((on ? 1 : 0) << 3);

            if (worldObj != null) {
                worldObj.setBlockDataNotify(this.tilePos, newMetadata);
            }
        }
    }

    @NotNull
    @Override
    public IPeripheral getPeripheral(Direction side) {
        refreshDirection();
        return side == modemDirection ? modem : null;
    }

    @Override
    public void readAdditionalData(CompoundTag nbt) {
        advanced = nbt.getBoolean(NBT_ADVANCED);
    }

    @Override
    public void writeAdditionalData(CompoundTag tag) {
        tag.putBoolean(NBT_ADVANCED, advanced);
    }

    private static class Peripheral extends WirelessModemPeripheral {
        private final TileWirelessModem entity;

        Peripheral(TileWirelessModem entity) {
            super(new ModemState(() -> TickScheduler.schedule(entity)), entity.advanced);
            this.entity = entity;
        }

        @NotNull
        @Override
        public World getWorld() {
            return entity.worldObj;
        }

        @NotNull
        @Override
        public Vector3dc getPosition() {
            return new Vector3d(
                entity.tilePos.x + entity.modemDirection.offsetX(),
                entity.tilePos.y + entity.modemDirection.offsetY(),
                entity.tilePos.z + entity.modemDirection.offsetZ()
            );
        }

        @NotNull
        @Override
        public Object getTarget() {
            return entity;
        }

        @Override
        public boolean equals(IPeripheral other) {
            return this == other || (other instanceof Peripheral && entity == ((Peripheral) other).entity);
        }
    }
}
