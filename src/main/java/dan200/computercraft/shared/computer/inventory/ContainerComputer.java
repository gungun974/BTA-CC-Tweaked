/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.computer.inventory;

import dan200.computercraft.shared.computer.blocks.TileComputerBase;
import dan200.computercraft.shared.pocket.items.ItemPocketComputer;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;

public class ContainerComputer extends ContainerComputerBase {
    public ContainerComputer(TileComputerBase tile) {
        super(tile.createServerComputer().getInstanceID(), tile.getFamily());
    }

    public ContainerComputer(ItemPocketComputer item, final World world, ContainerInventory inventory, Entity entity, @NotNull ItemStack stack) {
        super(item.createServerComputer(world, inventory, entity, stack).getInstanceID(), item.getFamily());
    }
}
