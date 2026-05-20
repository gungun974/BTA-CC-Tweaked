package dan200.computercraft.shared.peripheral.diskdrive;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.slot.Slot;

public class MenuDiskDrive extends MenuAbstract {
    private final int diskDriveSlotsStart;
    private final int inventorySlotsStart;
    private final int hotbarSlotsStart;
    public TileDiskDrive diskDrive;

    public MenuDiskDrive(Container container, TileDiskDrive diskDrive) {
        this.diskDrive = diskDrive;

        addSlot(new Slot(this.diskDrive, 0, 8 + 4 * 18, 35));

        for (int j = 0; j < 3; j++) {
            for (int i1 = 0; i1 < 9; i1++) {
                this.addSlot(new Slot(container, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
            }
        }

        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(container, k, 8 + k * 18, 142));
        }

        this.diskDriveSlotsStart = 0;
        this.inventorySlotsStart = 1;
        this.hotbarSlotsStart = 28;
    }

    @Override
    public boolean stillValid(Player entityplayer) {
        return this.diskDrive.stillValid(entityplayer);
    }

    @Override
    public IntList getMoveSlots(InventoryAction action, Slot slot, int target, Player player) {
        if (slot.index >= this.diskDriveSlotsStart && slot.index < this.inventorySlotsStart) {
            return this.getSlots(this.diskDriveSlotsStart, 1, false);
        } else {
            if (action == InventoryAction.MOVE_ALL) {
                if (slot.index >= this.inventorySlotsStart && slot.index < this.hotbarSlotsStart) {
                    return this.getSlots(this.inventorySlotsStart, 27, false);
                }

                if (slot.index >= this.hotbarSlotsStart) {
                    return this.getSlots(this.hotbarSlotsStart, 9, false);
                }
            }

            return action == InventoryAction.MOVE_SIMILAR && slot.index >= this.inventorySlotsStart ? this.getSlots(this.inventorySlotsStart, 36, false) : null;
        }
    }

    @Override
    public IntList getTargetSlots(InventoryAction action, Slot slot, int target, Player player) {
        return slot.index >= this.diskDriveSlotsStart && slot.index < this.inventorySlotsStart
            ? this.getSlots(this.inventorySlotsStart, 36, false)
            : this.getSlots(this.diskDriveSlotsStart, 1, false);
    }
}
