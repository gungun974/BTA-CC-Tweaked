/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.modem.wired;

import dan200.computercraft.BlockPos;
import dan200.computercraft.ComputerCraft;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.shared.peripheral.modem.ModemShapes;
import dan200.computercraft.shared.peripheral.modem.wireless.BlockWirelessModem;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import org.jetbrains.annotations.NotNull;

public class BlockCable extends BlockLogic
{
    public BlockCable( Block<?> block, Material material )
    {
        super( block, material );
        this.setBlockBounds(0.125, 0.0, 0.125, 0.875, 0.1875, 0.875);
    }

    public static boolean canConnectIn( TileCable state, Direction direction )
    {
        return state.blockStateCable && state.blockStateModem
            .getFacing() != direction;
    }

//    @Nonnull
//    @Override
//    @Deprecated
//    public BlockState getStateForNeighborUpdate( @Nonnull BlockState state, @Nonnull Direction side, @Nonnull BlockState otherState,
//                                                 @Nonnull WorldAccess world, @Nonnull BlockPos pos, @Nonnull BlockPos otherPos )
//    {
//        updateWaterloggedPostPlacement( state, world, pos );
//        // Should never happen, but handle the case where we've no modem or cable.
//        if( !state.get( CABLE ) && state.get( MODEM ) == CableModemVariant.None )
//        {
//            return getFluidState( state ).getBlockState();
//        }
//
//        return state.with( CONNECTIONS.get( side ), doesConnectVisually( state, world, pos, side ) );
//    }

    public static boolean doesConnectVisually(TileCable state, World world, BlockPos pos, Direction direction )
    {
        if( !state.blockStateCable )
        {
            return false;
        }
        if( state.blockStateModem
            .getFacing() == direction )
        {
            return true;
        }
        return ComputerCraftAPI.getWiredElementAt( world, pos.offset( direction ), direction.getOpposite() ) != null;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
       TileCable state = (TileCable) world.getTileEntity(x, y, z);

       if (state == null) {
           return super.canPlaceBlockAt(world, x, y, z);
       }

        Direction facing = state.blockStateModem
            .getFacing();
        if( facing == null )
        {
            return true;
        }

        return world.isBlockNormalCube(x + facing.getOffsetX(), y+ facing.getOffsetY(), z+ facing.getOffsetZ());
    }

    @Override
    public AABB getBlockBoundsFromState(WorldSource world, int x, int y, int z) {
        updateBlockBoundsFromState(world, x, y, z);
        return super.getBlockBoundsFromState(world, x, y, z);
    }

    public void updateBlockBoundsFromState(WorldSource world, int x, int y, int z) {
        TileCable state = (TileCable) world.getTileEntity(x, y, z);

        if (state == null) {
            setBlockBounds(0, 0, 0, 1, 1, 1);
            return;
        }

        AABB shape = CableShapes.getShape( state );

        if (shape.getSize() == 0) {
            setBlockBounds(0, 0, 0, 1, 1, 1);
            return;
        }

        setBlockBounds(shape.minX, shape.minY, shape.minZ, shape.maxX, shape.maxY, shape.maxZ);
    }

    public boolean isCubeShaped() {
        return false;
    }

    public boolean isSolidRender() {
        return false;
    }

    @Override
    public void onBlockPlacedByWorld(World world, int x, int y, int z) {
        super.onBlockPlacedByWorld(world, x, y, z);
    }

    @Override
    public void onBlockPlacedByMob(World world, int x, int y, int z, @NotNull Side side, Mob mob, double xPlaced, double yPlaced) {
        TileEntity tile = world.getTileEntity( x, y, z );
        if( tile instanceof TileCable )
        {
            TileCable cable = (TileCable) tile;

            if( cable.hasCable() )
            {
                cable.connectionsChanged();
            }
        }
    }

    public static void correctConnections( World world, BlockPos pos, TileCable state )
    {
        if( state.blockStateCable )
        {
            state.blockStateNorth = doesConnectVisually( state, world, pos, Direction.NORTH );
            state.blockStateSouth = doesConnectVisually( state, world, pos, Direction.SOUTH );
            state.blockStateEast = doesConnectVisually( state, world, pos, Direction.EAST );
            state.blockStateWest = doesConnectVisually( state, world, pos, Direction.WEST );
            state.blockStateUp = doesConnectVisually( state, world, pos, Direction.UP );
            state.blockStateDown = doesConnectVisually( state, world, pos, Direction.DOWN);
        }
        else
        {
            state.blockStateNorth = false;
            state.blockStateSouth = false;
            state.blockStateEast = false;
            state.blockStateWest = false;
            state.blockStateUp = false;
            state.blockStateDown = false;
        }
    }

    @Override
    public boolean onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xHit, double yHit) {
        TileEntity entity = (world.getTileEntity(x, y, z));
        if( !(entity instanceof TileCable) )
        {
            return true;
        }

        TileCable tileCable = (TileCable) entity;

        ComputerCraft.log.info("---------------");
        ComputerCraft.log.info("Modem : {}", tileCable.blockStateModem);
        ComputerCraft.log.info("Cable : {}", tileCable.blockStateCable);
        ComputerCraft.log.info("North : {}", tileCable.blockStateNorth);
        ComputerCraft.log.info("East : {}", tileCable.blockStateEast);
        ComputerCraft.log.info("South : {}", tileCable.blockStateSouth);
        ComputerCraft.log.info("West : {}", tileCable.blockStateWest);
        ComputerCraft.log.info("Up : {}", tileCable.blockStateUp);
        ComputerCraft.log.info("Down : {}", tileCable.blockStateDown);

        return true;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
        TileEntity entity = (world.getTileEntity(x, y, z));
        if( !(entity instanceof TileCable) )
        {
            return;
        }

        TileCable tileCable = (TileCable) entity;

        tileCable.onNeighbourChange(new BlockPos(x, y, z));
    }
}
