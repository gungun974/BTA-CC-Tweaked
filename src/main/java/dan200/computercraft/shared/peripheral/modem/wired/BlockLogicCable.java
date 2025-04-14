/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.modem.wired;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.shared.common.ComputerCraftItems;
import dan200.computercraft.shared.util.BlockPos;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockLogicCable extends BlockLogic {
    public BlockLogicCable(Block<?> block, Material material) {
        super(block, material);
        this.setBlockBounds(0.125, 0.0, 0.125, 0.875, 0.1875, 0.875);
    }

    public static boolean canConnectIn(TileCable state, Direction direction) {
        return state.blockStateCable && state.blockStateModem
            .getFacing() != direction;
    }

    public static boolean doesConnectVisually(TileCable state, World world, BlockPos pos, Direction direction) {
        if (!state.blockStateCable) {
            return false;
        }
        if (state.blockStateModem
            .getFacing() == direction) {
            return true;
        }
        return ComputerCraftAPI.getWiredElementAt(world, pos.offset(direction), direction.getOpposite()) != null;
    }

    public static void correctConnections(World world, BlockPos pos, TileCable state) {
        if (state.blockStateCable) {
            state.blockStateNorth = doesConnectVisually(state, world, pos, Direction.NORTH);
            state.blockStateSouth = doesConnectVisually(state, world, pos, Direction.SOUTH);
            state.blockStateEast = doesConnectVisually(state, world, pos, Direction.EAST);
            state.blockStateWest = doesConnectVisually(state, world, pos, Direction.WEST);
            state.blockStateUp = doesConnectVisually(state, world, pos, Direction.UP);
            state.blockStateDown = doesConnectVisually(state, world, pos, Direction.DOWN);
        } else {
            state.blockStateNorth = false;
            state.blockStateSouth = false;
            state.blockStateEast = false;
            state.blockStateWest = false;
            state.blockStateUp = false;
            state.blockStateDown = false;
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        TileCable state = (TileCable) world.getTileEntity(x, y, z);

        if (state == null) {
            return super.canPlaceBlockAt(world, x, y, z);
        }

        Direction facing = state.blockStateModem
            .getFacing();
        if (facing == null) {
            return true;
        }

        return world.isBlockNormalCube(x + facing.getOffsetX(), y + facing.getOffsetY(), z + facing.getOffsetZ());
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

        AABB shape = CableShapes.getShape(state);

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
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileCable) {
            TileCable cable = (TileCable) tile;

            if (cable.hasCable()) {
                cable.connectionsChanged();
            }
        }
    }

    @Override
    public void onBlockPlacedByMob(World world, int x, int y, int z, @NotNull Side side, Mob mob, double xPlaced, double yPlaced) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileCable) {
            TileCable cable = (TileCable) tile;

            if (cable.hasCable()) {
                cable.connectionsChanged();
            }
        }
    }

    public ItemStack @Nullable [] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
        if (dropCause == EnumDropCause.IMPROPER_TOOL) {
            return null;
        }

        if (!(tileEntity instanceof TileCable)) {
            return null;
        }

        TileCable cable = (TileCable) tileEntity;

        if (cable.blockStateCable && cable.blockStateModem != CableModemVariant.None) {
            return new ItemStack[]{new ItemStack(ComputerCraftItems.CABLE), new ItemStack(ComputerCraftItems.WIRED_MODEM)};
        }

        if (cable.blockStateModem != CableModemVariant.None) {
            return new ItemStack[]{new ItemStack(ComputerCraftItems.WIRED_MODEM)};
        }

        return new ItemStack[]{new ItemStack(ComputerCraftItems.CABLE)};


    }

    public boolean onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xPlaced, double yPlaced) {
        return ((TileCable) world.getTileEntity(x, y, z)).onBlockRightClicked(player, side, xPlaced, yPlaced);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
        TileEntity entity = (world.getTileEntity(x, y, z));
        if (!(entity instanceof TileCable)) {
            return;
        }

        TileCable tileCable = (TileCable) entity;

        tileCable.onNeighbourChange(new BlockPos(x, y, z));
    }
}
