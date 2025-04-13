package dan200.computercraft.shared.peripheral.printer;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.slot.Slot;

import java.util.List;

public class MenuPrinter extends MenuAbstract {
    private final int printerSlotsStart;
    private final int inventorySlotsStart;
    private final int hotbarSlotsStart;
    public TilePrinter printer;

    public MenuPrinter(Container container, TilePrinter printer) {
        this.printer = printer;

        // Ink slot
        addSlot(new Slot(this.printer, 0, 13, 35));

        // In-tray
        for (int x = 0; x < 6; x++) {
            addSlot(new Slot(this.printer, x + 1, 61 + x * 18, 22));
        }

        // Out-tray
        for (int x = 0; x < 6; x++) {
            addSlot(new Slot(this.printer, x + 7, 61 + x * 18, 49));
        }

        for (int j = 0; j < 3; j++) {
            for (int i1 = 0; i1 < 9; i1++) {
                this.addSlot(new Slot(container, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
            }
        }

        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(container, k, 8 + k * 18, 142));
        }

        this.printerSlotsStart = 0;
        this.inventorySlotsStart = 13;
        this.hotbarSlotsStart = 40;
    }

    @Override
    public boolean stillValid(Player entityplayer) {
        return this.printer.stillValid(entityplayer);
    }

    @Override
    public List<Integer> getMoveSlots(InventoryAction action, Slot slot, int target, Player player) {
        if (slot.index >= this.printerSlotsStart && slot.index < this.inventorySlotsStart) {
            return this.getSlots(this.printerSlotsStart, 1, false);
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
        return slot.index >= this.printerSlotsStart && slot.index < this.inventorySlotsStart
            ? this.getSlots(this.inventorySlotsStart, 36, false)
            : this.getSlots(this.printerSlotsStart, 1, false);
    }
}
