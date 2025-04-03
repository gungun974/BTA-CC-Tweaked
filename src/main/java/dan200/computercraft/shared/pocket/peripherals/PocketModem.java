/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.pocket.peripherals;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.pocket.AbstractPocketUpgrade;
import dan200.computercraft.api.pocket.IPocketAccess;
import dan200.computercraft.shared.common.ComputerCraftItems;
import dan200.computercraft.shared.peripheral.modem.ModemState;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.util.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PocketModem extends AbstractPocketUpgrade
{
    private final boolean advanced;

    public PocketModem( boolean advanced, int id )
    {
        super( id, advanced ? ComputerCraftItems.WIRELESS_MODEM_ADVANCED : ComputerCraftItems.WIRELESS_MODEM_NORMAL );
        this.advanced = advanced;
    }

    @Nullable
    @Override
    public IPeripheral createPeripheral( @Nonnull IPocketAccess access )
    {
        return new PocketModemPeripheral( advanced );
    }

    @Override
    public @NotNull String getUnlocalisedAdjective() {
        if (advanced) {
            return "upgrade.computercraft.wireless_modem_advanced.adjective";
        }
        return "upgrade.computercraft.wireless_modem_normal.adjective";
    }

    @Override
    public void update( @Nonnull IPocketAccess access, @Nullable IPeripheral peripheral )
    {
        if( !(peripheral instanceof PocketModemPeripheral) )
        {
            return;
        }

        Entity entity = access.getEntity();

        PocketModemPeripheral modem = (PocketModemPeripheral) peripheral;

        if( entity != null )
        {
            modem.setLocation( entity.world, Vec3.getPermanentVec3(entity.x, entity.y + entity.getHeadHeight(), entity.z));
        }

        ModemState state = modem.getModemState();
        if( state.pollChanged() )
        {
            access.setLight( state.isOpen() ? 0xBA0000 : -1 );
        }
    }
}
