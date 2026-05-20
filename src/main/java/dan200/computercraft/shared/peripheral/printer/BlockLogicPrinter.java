/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.printer;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicRotatable;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;

public class BlockLogicPrinter extends BlockLogicRotatable {
    public BlockLogicPrinter(Block<?> block) {
        super(block, Materials.STONE);
        block.withEntity(TilePrinter::new);
    }

    public boolean onInteracted(World world, TilePosc tilePos, Player player, Side side, double xPlaced, double yPlaced) {
        return ((TilePrinter) world.getTileEntity(tilePos)).onInteracted(player, side, xPlaced, yPlaced);
    }
}
