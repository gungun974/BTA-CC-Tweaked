/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.blocks;

import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.shared.computer.blocks.BlockLogicComputer;
import dan200.computercraft.shared.peripheral.modem.ModemShapes;
import dan200.computercraft.shared.peripheral.modem.wireless.BlockWirelessModem;
import dan200.computercraft.shared.turtle.core.TurtleBrain;
import dan200.computercraft.shared.turtle.items.ITurtleItem;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.WorldSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockTurtle extends BlockLogicComputer
{
    public BlockTurtle(Block<?> block) {
        super(block);
        block.withEntity(TileTurtle::new);
        this.setBlockBounds( 0.125, 0.125, 0.125, 0.875, 0.875, 0.875);
    }

    public boolean isCubeShaped() {
        return false;
    }

    public boolean isSolidRender() {
        return false;
    }

    @Override
    public AABB getBlockBoundsFromState(WorldSource world, int x, int y, int z) {
        return AABB.getPermanentBB(0.125, 0.125, 0.125, 0.875, 0.875, 0.875);
    }


//    @Nonnull
//    @Override
//    @Deprecated
//    public BlockRenderType getRenderType( @Nonnull BlockState state )
//    {
//        return BlockRenderType.ENTITYBLOCK_ANIMATED;
//    }

//    @Nonnull
//    @Override
//    @Deprecated
//    public VoxelShape getOutlineShape( @Nonnull BlockState state, BlockView world, @Nonnull BlockPos pos, @Nonnull ShapeContext context )
//    {
//        BlockEntity tile = world.getBlockEntity( pos );
//        Vec3d offset = tile instanceof TileTurtle ? ((TileTurtle) tile).getRenderOffset( 1.0f ) : Vec3d.ZERO;
//        return offset.equals( Vec3d.ZERO ) ? DEFAULT_SHAPE : DEFAULT_SHAPE.offset( offset.x, offset.y, offset.z );
//    }

//    @Override
//    public float getBlastResistance()
//    {
//        // TODO Implement below functionality
//        return 2000;
//    }


    @Override
    public float getBlastResistance(Entity entity) {
        return 2000;
    }

//    @Nonnull
//    @Override
//    protected ItemStack getItem( TileComputerBase tile )
//    {
//        return tile instanceof TileTurtle ? TurtleItemFactory.create( (TileTurtle) tile ) : ItemStack.EMPTY;
//    }

    //    @Override
    //    public float getBlastResistance( BlockState state, BlockView world, BlockPos pos, Explosion explosion )
    //    {
    //        Entity exploder = explosion.getExploder();
    //        if( getFamily() == ComputerFamily.ADVANCED || exploder instanceof LivingEntity || exploder instanceof ExplosiveProjectileEntity )
    //        {
    //            return 2000;
    //        }
    //
    //        return super.getExplosionResistance( state, world, pos, explosion );
    //    }

//    @Override
//    public void onPlaced( @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable LivingEntity player, @Nonnull ItemStack stack )
//    {
//        super.onPlaced( world, pos, state, player, stack );
//
//        BlockEntity tile = world.getBlockEntity( pos );
//        if( !world.isClient && tile instanceof TileTurtle )
//        {
//            TileTurtle turtle = (TileTurtle) tile;
//
//            if( player instanceof PlayerEntity )
//            {
//                ((TileTurtle) tile).setOwningPlayer( ((PlayerEntity) player).getGameProfile() );
//            }
//
//            if( stack.getItem() instanceof ITurtleItem )
//            {
//                ITurtleItem item = (ITurtleItem) stack.getItem();
//
//                // Set Upgrades
//                for( TurtleSide side : TurtleSide.values() )
//                {
//                    turtle.getAccess()
//                        .setUpgrade( side, item.getUpgrade( stack, side ) );
//                }
//
//                turtle.getAccess()
//                    .setFuelLevel( item.getFuelLevel( stack ) );
//
//                // Set colour
//                int colour = item.getColour( stack );
//                if( colour != -1 )
//                {
//                    turtle.getAccess()
//                        .setColour( colour );
//                }
//
//                // Set overlay
//                Identifier overlay = item.getOverlay( stack );
//                if( overlay != null )
//                {
//                    ((TurtleBrain) turtle.getAccess()).setOverlay( overlay );
//                }
//            }
//        }
//    }
}
