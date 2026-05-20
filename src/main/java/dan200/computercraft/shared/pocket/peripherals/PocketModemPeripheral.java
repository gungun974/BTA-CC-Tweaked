/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.pocket.peripherals;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.shared.peripheral.modem.ModemState;
import dan200.computercraft.shared.peripheral.modem.wireless.WirelessModemPeripheral;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class PocketModemPeripheral extends WirelessModemPeripheral {
    private World world = null;
    private Vector3dc position = new Vector3d(0, 0, 0);

    public PocketModemPeripheral(boolean advanced) {
        super(new ModemState(), advanced);
    }

    void setLocation(World world, Vector3dc position) {
        this.position = position;
        this.world = world;
    }

    @NotNull
    @Override
    public World getWorld() {
        return world;
    }

    @NotNull
    @Override
    public Vector3dc getPosition() {
        return position;
    }

    @Override
    public boolean equals(IPeripheral other) {
        return other instanceof PocketModemPeripheral;
    }
}
