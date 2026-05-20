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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

public class PocketModem extends AbstractPocketUpgrade {
    private final boolean advanced;

    public PocketModem(boolean advanced, int id) {
        super(id, advanced ? ComputerCraftItems.WIRELESS_MODEM_ADVANCED : ComputerCraftItems.WIRELESS_MODEM_NORMAL);
        this.advanced = advanced;
    }

    @Nullable
    @Override
    public IPeripheral createPeripheral(@NotNull IPocketAccess access) {
        return new PocketModemPeripheral(advanced);
    }

    @Override
    public @NotNull String getUnlocalisedAdjective() {
        if (advanced) {
            return "upgrade.computercraft.wireless_modem_advanced.adjective";
        }
        return "upgrade.computercraft.wireless_modem_normal.adjective";
    }

    @Override
    public void update(@NotNull IPocketAccess access, @Nullable IPeripheral peripheral) {
        if (!(peripheral instanceof PocketModemPeripheral modem)) {
            return;
        }

        Entity entity = access.getEntity();

        if (entity != null) {
            modem.setLocation(entity.world, new Vector3d(entity.x, entity.y + entity.getHeadHeight(), entity.z));
        }

        ModemState state = modem.getModemState();
        if (state.pollChanged()) {
            access.setLight(state.isOpen() ? 0xBA0000 : -1);
        }
    }
}
