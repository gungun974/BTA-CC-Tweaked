package dan200.computercraft.shared.peripheral.diskdrive;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.crafting.ContainerListener;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.menu.MenuContainer;
import net.minecraft.core.player.inventory.slot.Slot;

import java.util.List;

public class MenuDiskDrive extends MenuAbstract {
	public TileDiskDrive furnace;

	public MenuDiskDrive(ContainerInventory inventory, TileDiskDrive tileEntity) {
		this.furnace = tileEntity;
		this.addSlot(new Slot(tileEntity, 0, 56, 17));

		for (int i = 0; i < 3; i++) {
			for (int k = 0; k < 9; k++) {
				this.addSlot(new Slot(inventory, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
			}
		}

		for (int j = 0; j < 9; j++) {
			this.addSlot(new Slot(inventory, j, 8 + j * 18, 142));
		}
	}

    @Override
    public boolean stillValid(Player entityplayer) {
        return this.furnace.stillValid(entityplayer);
    }

    @Override
    public List<Integer> getMoveSlots(InventoryAction action, Slot slot, int target, Player player) {
        int chestSize = 1;
        if (slot.index >= 0 && slot.index < chestSize) {
            return this.getSlots(0, chestSize, false);
        } else {
            if (action == InventoryAction.MOVE_ALL) {
                if (slot.index >= chestSize && slot.index < chestSize + 27) {
                    return this.getSlots(chestSize, 27, false);
                }

                if (slot.index >= chestSize + 27 && slot.index < chestSize + 36) {
                    return this.getSlots(chestSize + 27, 9, false);
                }
            } else if (slot.index >= chestSize && slot.index < chestSize + 36) {
                return this.getSlots(chestSize, 36, false);
            }

            return null;
        }
    }

    @Override
    public List<Integer> getTargetSlots(InventoryAction action, Slot slot, int target, Player player) {
        int chestSize = 1;
        return slot.index < chestSize ? this.getSlots(chestSize, 36, true) : this.getSlots(0, chestSize, false);
    }
}
