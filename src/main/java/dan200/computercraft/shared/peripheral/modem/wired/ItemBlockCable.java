/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.modem.wired;

import net.minecraft.core.block.Block;
import net.minecraft.core.item.block.ItemBlock;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import static dan200.computercraft.shared.peripheral.modem.wired.BlockCable.*;

public abstract class ItemBlockCable extends ItemBlock
{
    private String translationKey;

    public ItemBlockCable(@NotNull Block block) {
        super(block);
    }


//    boolean placeAtCorrected( World world, BlockPos pos, BlockState state )
//    {
//        return placeAt( world, pos, correctConnections( world, pos, state ), null );
//    }

//    boolean placeAt( World world, BlockPos pos, BlockState state, PlayerEntity player )
//    {
//        // TODO: Check entity collision.
//        if( !state.canPlaceAt( world, pos ) )
//        {
//            return false;
//        }
//
//        world.setBlockState( pos, state, 3 );
//        BlockSoundGroup soundType = state.getBlock()
//            .getSoundGroup( state );
//        world.playSound( null, pos, soundType.getPlaceSound(), SoundCategory.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F );
//
//        BlockEntity tile = world.getBlockEntity( pos );
//        if( tile instanceof TileCable )
//        {
//            TileCable cable = (TileCable) tile;
//            cable.modemChanged();
//            cable.connectionsChanged();
//        }
//
//        return true;
//    }

//    @Nonnull
//    @Override
//    public String getTranslationKey()
//    {
//        if( translationKey == null )
//        {
//            translationKey = Util.createTranslationKey( "block", Registry.ITEM.getId( this ) );
//        }
//        return translationKey;
//    }

//    @Override
//    public void appendStacks( @Nonnull ItemGroup group, @Nonnull DefaultedList<ItemStack> list )
//    {
//        if( isIn( group ) )
//        {
//            list.add( new ItemStack( this ) );
//        }
//    }

    public static class WiredModem extends ItemBlockCable
    {
        public WiredModem(@NotNull Block block) {
            super(block);
        }
//
//        @Nonnull
//        @Override
//        public ActionResult place( ItemPlacementContext context )
//        {
//            ItemStack stack = context.getStack();
//            if( stack.isEmpty() )
//            {
//                return ActionResult.FAIL;
//            }
//
//            World world = context.getWorld();
//            BlockPos pos = context.getBlockPos();
//            BlockState existingState = world.getBlockState( pos );
//
//            // Try to add a modem to a cable
//            if( existingState.getBlock() == ComputerCraftRegistry.ModBlocks.CABLE && existingState.get( MODEM ) == CableModemVariant.None )
//            {
//                Direction side = context.getSide()
//                    .getOpposite();
//                BlockState newState = existingState.with( MODEM, CableModemVariant.from( side ) )
//                    .with( CONNECTIONS.get( side ), existingState.get( CABLE ) );
//                if( placeAt( world, pos, newState, context.getPlayer() ) )
//                {
//                    stack.decrement( 1 );
//                    return ActionResult.SUCCESS;
//                }
//            }
//
//            return super.place( context );
//        }
    }

    public static class Cable extends ItemBlockCable
    {

        public Cable(@NotNull Block block) {
            super(block);
        }

//        @Nonnull
//        @Override
//        public ActionResult place( ItemPlacementContext context )
//        {
//            ItemStack stack = context.getStack();
//            if( stack.isEmpty() )
//            {
//                return ActionResult.FAIL;
//            }
//
//            World world = context.getWorld();
//            BlockPos pos = context.getBlockPos();
//
//            // Try to add a cable to a modem inside the block we're clicking on.
//            BlockPos insidePos = pos.offset( context.getSide()
//                .getOpposite() );
//            BlockState insideState = world.getBlockState( insidePos );
//            if( insideState.getBlock() == ComputerCraftRegistry.ModBlocks.CABLE && !insideState.get( BlockCable.CABLE ) && placeAtCorrected( world,
//                insidePos,
//                insideState.with(
//                    BlockCable.CABLE,
//                    true ) ) )
//            {
//                stack.decrement( 1 );
//                return ActionResult.SUCCESS;
//            }
//
//            // Try to add a cable to a modem adjacent to this block
//            BlockState existingState = world.getBlockState( pos );
//            if( existingState.getBlock() == ComputerCraftRegistry.ModBlocks.CABLE && !existingState.get( BlockCable.CABLE ) && placeAtCorrected( world,
//                pos,
//                existingState.with(
//                    BlockCable.CABLE,
//                    true ) ) )
//            {
//                stack.decrement( 1 );
//                return ActionResult.SUCCESS;
//            }
//
//            return super.place( context );
//        }
    }
}
