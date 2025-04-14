/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.modem.wired;

import dan200.computercraft.shared.util.BlockPos;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

public class BlockLogicWiredModemFull extends BlockLogic {
    public BlockLogicWiredModemFull(Block<?> block, Material material) {
        super(block, material);
    }

    public boolean onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xPlaced, double yPlaced) {
        return ((TileWiredModemFull) world.getTileEntity(x, y, z)).onBlockRightClicked(player, side, xPlaced, yPlaced);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
        TileEntity entity = (world.getTileEntity(x, y, z));
        if (!(entity instanceof TileWiredModemFull)) {
            return;
        }

        TileWiredModemFull computerEntity = (TileWiredModemFull) entity;

        computerEntity.onNeighbourTileEntityChange(new BlockPos(x, y, z));
    }
}
