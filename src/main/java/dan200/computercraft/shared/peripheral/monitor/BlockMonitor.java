/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.monitor;

import dan200.computercraft.fabric.Helper;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicFullyRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.enums.PlacementMode;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;

public class BlockMonitor extends BlockLogicFullyRotatable
{
    //public static final DirectionProperty ORIENTATION = DirectionProperty.of( "orientation", Direction.UP, Direction.DOWN, Direction.NORTH );

    //public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    //static final EnumProperty<MonitorEdgeState> STATE = EnumProperty.of( "state", MonitorEdgeState.class );

    public BlockMonitor(Block<?> block, boolean advanced)
    {
        super(block, Material.stone);
        block.withEntity(() -> new TileMonitor(advanced));
//        setDefaultState( getStateManager().getDefaultState()
//            .with( ORIENTATION, Direction.NORTH )
//            .with( FACING, Direction.NORTH )
//            .with( STATE, MonitorEdgeState.NONE ) );
    }

//    @Override
//    @Nullable
//    public BlockState getPlacementState( ItemPlacementContext context )
//    {
//        float pitch = context.getPlayer() == null ? 0 : context.getPlayer().pitch;
//        Direction orientation;
//        if( pitch > 66.5f )
//        {
//            // If the player is looking down, place it facing upwards
//            orientation = Direction.UP;
//        }
//        else if( pitch < -66.5f )
//        {
//            // If they're looking up, place it down.
//            orientation = Direction.DOWN;
//        }
//        else
//        {
//            orientation = Direction.NORTH;
//        }
//
//        return getDefaultState().with( FACING,
//            context.getPlayerFacing()
//                .getOpposite() )
//            .with( ORIENTATION, orientation );
//    }

    @Override
    public void onBlockPlacedByMob(World world, int x, int y, int z, @NotNull Side side, Mob mob, double xPlaced, double yPlaced) {
        Direction direction = mob.getPlacementDirection(side).getOpposite();
        world.setBlockMetadataWithNotify(x, y, z, directionToMeta(direction));

        TileEntity entity = world.getTileEntity( x, y, z );
        if( entity instanceof TileMonitor && !Helper.isClientWorld())
        {
            TileMonitor monitor = (TileMonitor) entity;
            // Defer the block update if we're being placed by another TE. See #691
//            if( livingEntity == null || livingEntity instanceof FakePlayer )
//            {
//                monitor.updateNeighborsDeferred();
//                return;
//            }

            monitor.updateNeighbors();
        }
    }

    @Override
    public void onBlockRemoved(World world, int x, int y, int z, int data) {
        super.onBlockRemoved(world, x, y, z, data);
        TileEntity entity = world.getTileEntity( x, y, z );
        if( entity instanceof TileMonitor && !Helper.isClientWorld())
        {
            world.setBlockAndMetadataRaw(x, y, z, this.id(), data);
            TileMonitor monitor = (TileMonitor) entity;
            monitor.markDestroyed();
            world.setBlockRaw(x, y, z, 0);
        }
    }
}
