/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.speaker;

import dan200.computercraft.shared.computer.blocks.TileEntityComputer;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicRotatable;
import net.minecraft.core.block.material.Material;

public class BlockSpeaker extends BlockLogicRotatable
{
    public BlockSpeaker(Block<?> block) {
        super(block, Material.stone);
        block.withEntity(TileSpeaker::new);
    }
//    private static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
//
//    public BlockSpeaker( Settings settings )
//    {
//        super( settings, ComputerCraftRegistry.ModTiles.SPEAKER );
//        setDefaultState( getStateManager().getDefaultState()
//            .with( FACING, Direction.NORTH ) );
//    }

//    @Nullable
//    @Override
//    public BlockState getPlacementState( ItemPlacementContext placement )
//    {
//        return getDefaultState().with( FACING,
//            placement.getPlayerFacing()
//                .getOpposite() );
//    }
//
//    @Override
//    protected void appendProperties( StateManager.Builder<Block, BlockState> properties )
//    {
//        properties.add( FACING );
//    }
}
