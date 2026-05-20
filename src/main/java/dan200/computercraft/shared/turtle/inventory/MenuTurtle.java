package dan200.computercraft.shared.turtle.inventory;

import dan200.computercraft.shared.turtle.blocks.TileTurtle;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.slot.Slot;

public class MenuTurtle extends MenuAbstract {
    public static final int PLAYER_START_Y = 134;
    public static final int TURTLE_START_X = 175;
    private final int turtleSlotsStart;
    private final int inventorySlotsStart;
    private final int hotbarSlotsStart;
    public TileTurtle turtle;

    public MenuTurtle(Container container, TileTurtle diskDrive) {
        this.turtle = diskDrive;

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                addSlot(new Slot(turtle, x + y * 4, TURTLE_START_X + 1 + x * 18, PLAYER_START_Y + 1 + y * 18));
            }
        }

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                addSlot(new Slot(container, x + y * 9 + 9, 8 + x * 18, PLAYER_START_Y + 1 + y * 18));
            }
        }

        for (int x = 0; x < 9; x++) {
            addSlot(new Slot(container, x, 8 + x * 18, PLAYER_START_Y + 3 * 18 + 5));
        }

        this.turtleSlotsStart = 0;
        this.inventorySlotsStart = 16;
        this.hotbarSlotsStart = 43;
    }

    @Override
    public boolean stillValid(Player entityplayer) {
        return this.turtle.stillValid(entityplayer);
    }

    @Override
    public IntList getMoveSlots(InventoryAction action, Slot slot, int target, Player player) {
        if (slot.index >= this.turtleSlotsStart && slot.index < this.inventorySlotsStart) {
            return this.getSlots(this.turtleSlotsStart, 16, false);
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
        return slot.index >= this.turtleSlotsStart && slot.index < this.inventorySlotsStart
            ? this.getSlots(this.inventorySlotsStart, 36, false)
            : this.getSlots(this.turtleSlotsStart, 16, false);
    }
}
