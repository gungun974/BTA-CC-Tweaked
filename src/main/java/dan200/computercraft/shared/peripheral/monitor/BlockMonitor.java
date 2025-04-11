/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.monitor;

import dan200.computercraft.fabric.Helper;
import dan200.computercraft.shared.computer.blocks.TileComputerBase;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;

public class BlockMonitor extends BlockLogic
{
    @Override
    public void onBlockPlacedOnSide(World world, int x, int y, int z, @NotNull Side side, double xPlaced, double yPlaced) {
        world.setBlockMetadataWithNotify(x, y, z, facingToMeta(side.getDirection()) + orientationToMeta(Direction.NORTH));
    }

    public static int facingToMeta(Direction direction) {
        switch (direction) {
            case EAST:
                return 0b0001;
            case SOUTH:
                return 0b0010;
            case WEST:
                return 0b0011;
            case NORTH:
            default:
                return 0b0000;
        }
    }

    public static Direction metaToFacing(int meta) {
        switch (meta & 0b11) {
            case 0b01:
                return Direction.EAST;
            case 0b10:
                return Direction.SOUTH;
            case 0b11:
                return Direction.WEST;
            case 0b00:
            default:
                return Direction.NORTH;
        }
    }

    public static int orientationToMeta(Direction direction) {
        switch (direction) {
            case UP:
                return 0b01 << 2;
            case DOWN:
                return 0b10 << 2;
            case NORTH:
            default:
                return 0b00;
        }
    }

    public static Direction metaToOrientation(int meta) {
        switch ((meta >> 2) & 0b11) {
            case 0b01:
                return Direction.UP;
            case 0b10:
                return Direction.DOWN;
            case 0b00:
            default:
                return Direction.NORTH;
        }
    }

    public static int stateToMeta(MonitorEdgeState state) {
        return state.ordinal() << 4;
    }

    public static MonitorEdgeState metaToState(int meta) {
        return MonitorEdgeState.values()[(meta >> 4) & 0b1111];
    }

    public BlockMonitor(Block<?> block, boolean advanced)
    {
        super(block, Material.stone);
        block.withEntity(() -> new TileMonitor(advanced));
    }

    @Override
    public void onBlockPlacedByMob(World world, int x, int y, int z, @NotNull Side side, Mob mob, double xPlaced, double yPlaced) {
        float pitch = mob.xRot;
        Direction orientation;
        if( pitch > 66.5f )
        {
            // If the player is looking down, place it facing upwards
            orientation = Direction.UP;
        }
        else if( pitch < -66.5f )
        {
            // If they're looking up, place it down.
            orientation = Direction.DOWN;
        }
        else
        {
            orientation = Direction.NORTH;
        }

        Direction direction = mob.getHorizontalPlacementDirection(side).getOpposite();
        world.setBlockMetadataWithNotify(x, y, z, facingToMeta(direction) + orientationToMeta(orientation));

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
