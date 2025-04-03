/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.printer;

import dan200.computercraft.shared.peripheral.diskdrive.TileDiskDrive;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicRotatable;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockPrinter extends BlockLogicRotatable
{
//    static final BooleanProperty TOP = BooleanProperty.of( "top" );
//    static final BooleanProperty BOTTOM = BooleanProperty.of( "bottom" );

    public BlockPrinter( Block<?> block )
    {
        super(block, Material.stone);
        block.withEntity(TilePrinter::new);
    }

//    @Override
//    public BlockState getPlacementState( ItemPlacementContext placement )
//    {
//        return getDefaultState().with( FACING,
//            placement.getPlayerFacing()
//                .getOpposite() );
//    }

//    @Override
//    public void afterBreak( @Nonnull World world, @Nonnull PlayerEntity player, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable BlockEntity te, @Nonnull ItemStack stack )
//    {
//        if( te instanceof Nameable && ((Nameable) te).hasCustomName() )
//        {
//            player.incrementStat( Stats.MINED.getOrCreateStat( this ) );
//            player.addExhaustion( 0.005F );
//
//            ItemStack result = new ItemStack( this );
//            result.setCustomName( ((Nameable) te).getCustomName() );
//            dropStack( world, pos, result );
//        }
//        else
//        {
//            super.afterBreak( world, player, pos, state, te, stack );
//        }
//    }

//    @Override
//    public void onPlaced( @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState state, LivingEntity placer, ItemStack stack )
//    {
//        if( stack.hasCustomName() )
//        {
//            BlockEntity tileentity = world.getBlockEntity( pos );
//            if( tileentity instanceof TilePrinter )
//            {
//                ((TilePrinter) tileentity).customName = stack.getName();
//            }
//        }
//    }

//    public boolean onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xPlaced, double yPlaced) {
//        return ((TilePrinter)world.getTileEntity(x, y, z)).onBlockRightClicked(player, side, xPlaced, yPlaced);
//    }
}
