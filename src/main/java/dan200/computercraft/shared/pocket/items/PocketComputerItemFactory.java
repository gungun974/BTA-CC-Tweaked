/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.pocket.items;

import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.shared.common.ComputerCraftItems;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import net.minecraft.core.item.ItemStack;

public final class PocketComputerItemFactory {
    private PocketComputerItemFactory() {
    }

    public static ItemStack create(int id, String label, int colour, ComputerFamily family, IPocketUpgrade upgrade) {
        switch (family) {
            case NORMAL:
                return ComputerCraftItems.POCKET_COMPUTER_NORMAL.create(id, label, colour, upgrade);
            case ADVANCED:
                return ComputerCraftItems.POCKET_COMPUTER_ADVANCED.create(id, label, colour, upgrade);
            default:
                return null;
        }
    }
}
