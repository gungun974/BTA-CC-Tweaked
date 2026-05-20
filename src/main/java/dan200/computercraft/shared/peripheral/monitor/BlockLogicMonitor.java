/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.monitor;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.EnvironmentHelper;

public class BlockLogicMonitor extends BlockLogic {
    public BlockLogicMonitor(Block<?> block, boolean advanced) {
        super(block, Materials.STONE);
        block.withEntity(() -> new TileMonitor(advanced));
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

    @Override
    public void onPlacedOnSide(World world, TilePosc tilePos, @NotNull Side side, double xPlaced, double yPlaced) {
        world.setBlockDataNotify(tilePos, facingToMeta(side.direction()) + orientationToMeta(Direction.NORTH));
    }

    @Override
    public void onPlacedByMob(World world, TilePosc tilePos, @NotNull Side side, Mob mob, double xPlaced, double yPlaced) {
        float pitch = mob.xRot;
        Direction orientation;
        if (pitch > 66.5f) {
            // If the player is looking down, place it facing upwards
            orientation = Direction.UP;
        } else if (pitch < -66.5f) {
            // If they're looking up, place it down.
            orientation = Direction.DOWN;
        } else {
            orientation = Direction.NORTH;
        }

        Direction direction = mob.getHorizontalPlacementDirection(side).opposite();
        world.setBlockDataNotify(tilePos, facingToMeta(direction) + orientationToMeta(orientation));
    }

    @Override
    public void onPlacedByWorld(World world, TilePosc tilePos) {
        TileEntity entity = world.getTileEntity(tilePos);
        if (entity instanceof TileMonitor monitor && !EnvironmentHelper.isClientWorld()) {
            monitor.updateNeighbors();
        }
    }

    public boolean onInteracted(World world, TilePosc tilePos, Player player, Side side, double xPlaced, double yPlaced) {
        return ((TileMonitor) world.getTileEntity(tilePos)).onInteracted(player, side, xPlaced, yPlaced);
    }

    @Override
    public void onRemoved(World world, TilePosc tilePos, int data) {
        super.onRemoved(world, tilePos, data);
        TileEntity entity = world.getTileEntity(tilePos);
        if (entity instanceof TileMonitor monitor && !EnvironmentHelper.isClientWorld()) {
            world.setBlockTypeDataRaw(tilePos, this.block, data);
            monitor.markDestroyed();
            world.setBlockTypeRaw(tilePos, Blocks.AIR);
        }
    }
}
