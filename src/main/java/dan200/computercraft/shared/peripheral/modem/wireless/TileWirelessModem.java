/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.modem.wireless;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.shared.common.TileGeneric;
import dan200.computercraft.shared.peripheral.modem.ModemPeripheral;
import dan200.computercraft.shared.peripheral.modem.ModemState;
import dan200.computercraft.shared.util.TickScheduler;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;

import javax.annotation.Nonnull;

public class TileWirelessModem extends TileGeneric implements IPeripheralTile
{
    private final boolean advanced;
    private final ModemPeripheral modem;
    private boolean hasModemDirection = false;
    private Direction modemDirection = Direction.DOWN;
    private boolean destroyed = false;

    public TileWirelessModem() {
        this(false);
    }

    public TileWirelessModem( boolean advanced )
    {
        this.advanced = advanced;
        modem = new Peripheral( this );
    }

//    @Override
//    public void cancelRemoval()
//    {
//        super.cancelRemoval();
//        TickScheduler.schedule( this );
//    }
//
//    @Override
//    public void resetBlock()
//    {
//        super.resetBlock();
//        hasModemDirection = false;
//        world.getBlockTickScheduler()
//            .schedule( getPos(),
//                getCachedState().getBlock(), 0 );
//    }
//
//    @Override
//    public void destroy()
//    {
//        if( !destroyed )
//        {
//            modem.destroy();
//            destroyed = true;
//        }
//    }

    @Override
    public void tick()
    {
        Direction currentDirection = modemDirection;
        refreshDirection();
        // Invalidate the capability if the direction has changed. I'm not 100% happy with this implementation
        //  - ideally we'd do it within refreshDirection or updateContainingBlockInfo, but this seems the _safest_
        //  place.
        if( modem.getModemState()
            .pollChanged() )
        {
            updateBlockState();
        }
    }

    private void refreshDirection()
    {
        if( hasModemDirection )
        {
            return;
        }

        hasModemDirection = true;
        modemDirection = BlockWirelessModem.metaToDirection(worldObj.getBlockMetadata(x, y , z)).getOpposite();
    }

    private void updateBlockState()
    {
        final boolean on = modem.getModemState()
            .isOpen();

        final int currentMetadata = getBlockMeta();

        final boolean isOn = ((currentMetadata >> 3) & 0b1) == 1;

        if( isOn != on )
        {
            final int newMetadata = (currentMetadata & ~0b1000) | ((on ? 1 : 0) << 3);

            if (worldObj != null) {
                worldObj.setBlockMetadataWithNotify(this.x, this.y, this.z, newMetadata);
            }
        }
    }

    @Nonnull
    @Override
    public IPeripheral getPeripheral( Direction side )
    {
        refreshDirection();
        return side == modemDirection ? modem : null;
    }

    private static class Peripheral extends WirelessModemPeripheral
    {
        private final TileWirelessModem entity;

        Peripheral( TileWirelessModem entity )
        {
            super( new ModemState( () -> TickScheduler.schedule( entity ) ), entity.advanced );
            this.entity = entity;
        }

        @Nonnull
        @Override
        public World getWorld()
        {
            return entity.worldObj;
        }

        @Nonnull
        @Override
        public Vec3 getPosition()
        {
            return Vec3.getPermanentVec3(
                entity.x + entity.modemDirection.getOffsetX(),
                entity.y + entity.modemDirection.getOffsetY(),
                entity.z + entity.modemDirection.getOffsetZ()
            );
        }

        @Nonnull
        @Override
        public Object getTarget()
        {
            return entity;
        }

        @Override
        public boolean equals( IPeripheral other )
        {
            return this == other || (other instanceof Peripheral && entity == ((Peripheral) other).entity);
        }
    }
}
