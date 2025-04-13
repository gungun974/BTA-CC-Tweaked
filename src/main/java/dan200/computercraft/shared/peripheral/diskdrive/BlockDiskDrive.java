/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.diskdrive;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicRotatable;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

public class BlockDiskDrive extends BlockLogicRotatable {
    public BlockDiskDrive(Block<?> block) {
        super(block, Material.stone);
        block.withEntity(TileDiskDrive::new);
    }

    public boolean onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xPlaced, double yPlaced) {
        return ((TileDiskDrive) world.getTileEntity(x, y, z)).onBlockRightClicked(player, side, xPlaced, yPlaced);
    }
}
