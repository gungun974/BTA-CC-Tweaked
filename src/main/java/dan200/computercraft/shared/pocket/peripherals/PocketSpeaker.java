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
import net.minecraft.core.entity.Entity;
import net.minecraft.core.util.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PocketSpeaker extends AbstractPocketUpgrade
{
    public PocketSpeaker(int id)
    {
        super( id, ComputerCraftItems.SPEAKER );
    }

    @Nullable
    @Override
    public IPeripheral createPeripheral( @Nonnull IPocketAccess access )
    {
        return new PocketSpeakerPeripheral();
    }

    @Override
    public @NotNull String getUnlocalisedAdjective() {
        return "upgrade.computercraft.speaker.adjective";
    }

    @Override
    public void update( @Nonnull IPocketAccess access, @Nullable IPeripheral peripheral )
    {
        if( !(peripheral instanceof PocketSpeakerPeripheral) )
        {
            return;
        }

        PocketSpeakerPeripheral speaker = (PocketSpeakerPeripheral) peripheral;

        Entity entity = access.getEntity();
        if( entity != null )
        {
            speaker.setLocation( entity.world, Vec3.getPermanentVec3(entity.x, entity.y + entity.getHeadHeight(), entity.z));
        }

        speaker.update();
        access.setLight( speaker.madeSound( 20 ) ? 0x3320fc : -1 );
    }
}
