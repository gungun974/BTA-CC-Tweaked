/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.printer;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicRotatable;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

public class BlockPrinter extends BlockLogicRotatable {
    public BlockPrinter(Block<?> block) {
        super(block, Material.stone);
        block.withEntity(TilePrinter::new);
    }

    public boolean onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xPlaced, double yPlaced) {
        return ((TilePrinter) world.getTileEntity(x, y, z)).onBlockRightClicked(player, side, xPlaced, yPlaced);
    }
}
