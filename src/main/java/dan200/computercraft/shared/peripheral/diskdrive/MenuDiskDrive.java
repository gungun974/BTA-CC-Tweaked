package dan200.computercraft.shared.peripheral.diskdrive;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.block.entity.TileEntityDispenser;
import net.minecraft.core.crafting.ContainerListener;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.menu.MenuContainer;
import net.minecraft.core.player.inventory.slot.Slot;

import java.util.List;

public class MenuDiskDrive extends MenuAbstract {
    public TileDiskDrive trap;
    private int dispenserSlotsStart;
    private int inventorySlotsStart;
    private int hotbarSlotsStart;

    public MenuDiskDrive(Container container, TileDiskDrive trap) {
        this.trap = trap;

        for (int i = 0; i < 1; i++) {
            for (int l = 0; l < 1; l++) {
                this.addSlot(new Slot(trap, l + i * 3, 62 + l * 18, 17 + i * 18));
            }
        }

        for (int j = 0; j < 3; j++) {
            for (int i1 = 0; i1 < 9; i1++) {
                this.addSlot(new Slot(container, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
            }
        }

        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(container, k, 8 + k * 18, 142));
        }

        this.dispenserSlotsStart = 0;
        this.inventorySlotsStart = 9;
        this.hotbarSlotsStart = 36;
    }

    @Override
    public boolean stillValid(Player entityplayer) {
        return this.trap.stillValid(entityplayer);
    }

    @Override
    public List<Integer> getMoveSlots(InventoryAction action, Slot slot, int target, Player player) {
        if (slot.index >= this.dispenserSlotsStart && slot.index < this.inventorySlotsStart) {
            return this.getSlots(this.dispenserSlotsStart, 1, false);
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
    public List<Integer> getTargetSlots(InventoryAction action, Slot slot, int target, Player player) {
        return slot.index >= this.dispenserSlotsStart && slot.index < this.inventorySlotsStart
            ? this.getSlots(this.inventorySlotsStart, 36, false)
            : this.getSlots(this.dispenserSlotsStart, 1, false);
    }
}
