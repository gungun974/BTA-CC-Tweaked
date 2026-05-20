/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.modem.wired;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;

public class BlockLogicWiredModemFull extends BlockLogic {
    public BlockLogicWiredModemFull(Block<?> block, Material material) {
        super(block, material);
    }

    public boolean onInteracted(World world, TilePosc tilePos, Player player, Side side, double xPlaced, double yPlaced) {
        return ((TileWiredModemFull) world.getTileEntity(tilePos)).onInteracted(player, side, xPlaced, yPlaced);
    }

    @Override
    public void onNeighborChanged(World world, TilePosc tilePos, Block<?> block) {
        TileEntity entity = (world.getTileEntity(tilePos));
        if (!(entity instanceof TileWiredModemFull computerEntity)) {
            return;
        }

        computerEntity.onNeighbourTileEntityChange(tilePos);
    }
}
