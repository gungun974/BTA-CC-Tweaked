/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.modem.wired;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.shared.common.ComputerCraftItems;
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
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;

import java.util.List;

public class BlockLogicCable extends BlockLogic {
    public BlockLogicCable(Block<?> block, Material material) {
        super(block, material);
        this.setBlockBounds(0.125, 0.0, 0.125, 0.875, 0.1875, 0.875);
    }

    public static boolean canConnectIn(TileCable state, Direction direction) {
        return state.blockStateCable && state.blockStateModem
            .getFacing() != direction;
    }

    public static boolean doesConnectVisually(TileCable state, World world, TilePosc pos, Direction direction) {
        if (!state.blockStateCable) {
            return false;
        }
        if (state.blockStateModem
            .getFacing() == direction) {
            return true;
        }
        return ComputerCraftAPI.getWiredElementAt(world, pos.add(direction, new TilePos()), direction.opposite()) != null;
    }

    public static void correctConnections(World world, TilePosc pos, TileCable state) {
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
    public boolean canPlaceAt(World world, @NotNull TilePosc tilePos) {
        TileCable state = (TileCable) world.getTileEntity(tilePos);

        if (state == null) {
            return super.canPlaceAt(world, tilePos);
        }

        Direction facing = state.blockStateModem
            .getFacing();
        if (facing == null) {
            return true;
        }

        return world.isBlockNormalCube(tilePos.add(facing, new TilePos()));
    }

    @Override
    public @NotNull AABBdc getBoundsFromState(@NotNull WorldSource world, @NotNull TilePosc tilePos) {
        updateBlockBoundsFromState(world, tilePos);
        return super.getBoundsFromState(world, tilePos);
    }

    public void updateBlockBoundsFromState(WorldSource world, TilePosc tilePos) {
        TileEntity tileEntity = world.getTileEntity(tilePos);

        if (!(tileEntity instanceof TileCable state)) {
            setBlockBounds(0, 0, 0, 1, 1, 1);
            return;
        }

        AABBdc shape = CableShapes.getShape(state);

        double size =
            ((shape.maxX() - shape.minX()) +
                (shape.maxY() - shape.minY()) +
                (shape.maxZ() - shape.minZ())) / 3.0;

        if (size == 0) {
            setBlockBounds(0, 0, 0, 1, 1, 1);
            return;
        }

        setBlockBounds(shape.minX(), shape.minY(), shape.minZ(), shape.maxX(), shape.maxY(), shape.maxZ());
    }

    public boolean isCubeShaped() {
        return false;
    }

    public boolean isSolidRender() {
        return false;
    }

    @Override
    public void getCollisionAABBs(@NotNull World world, @NotNull TilePosc tilePos, @NotNull AABBdc aabb, @NotNull List<AABBdc> aabbList) {
        TileEntity tileEntity = world.getTileEntity(tilePos);

        if (tileEntity instanceof TileCable state) {

            if (state.blockStateCable) {
                this.addIntersectingBoundingBox(aabb, CableShapes.SHAPE_CABLE_CORE.translate(tilePos.x(), tilePos.y(), tilePos.z(), new AABBd()), aabbList);

                if (state.blockStateNorth) {
                    this.addIntersectingBoundingBox(aabb, CableShapes.SHAPE_CABLE_ARM.get(Direction.NORTH).translate(tilePos.x(), tilePos.y(), tilePos.z(), new AABBd()), aabbList);
                }
                if (state.blockStateSouth) {
                    this.addIntersectingBoundingBox(aabb, CableShapes.SHAPE_CABLE_ARM.get(Direction.SOUTH).translate(tilePos.x(), tilePos.y(), tilePos.z(), new AABBd()), aabbList);
                }
                if (state.blockStateEast) {
                    this.addIntersectingBoundingBox(aabb, CableShapes.SHAPE_CABLE_ARM.get(Direction.EAST).translate(tilePos.x(), tilePos.y(), tilePos.z(), new AABBd()), aabbList);
                }
                if (state.blockStateWest) {
                    this.addIntersectingBoundingBox(aabb, CableShapes.SHAPE_CABLE_ARM.get(Direction.WEST).translate(tilePos.x(), tilePos.y(), tilePos.z(), new AABBd()), aabbList);
                }
                if (state.blockStateUp) {
                    this.addIntersectingBoundingBox(aabb, CableShapes.SHAPE_CABLE_ARM.get(Direction.UP).translate(tilePos.x(), tilePos.y(), tilePos.z(), new AABBd()), aabbList);
                }
                if (state.blockStateDown) {
                    this.addIntersectingBoundingBox(aabb, CableShapes.SHAPE_CABLE_ARM.get(Direction.DOWN).translate(tilePos.x(), tilePos.y(), tilePos.z(), new AABBd()), aabbList);
                }
            }

            if (state.blockStateModem.getFacing() != null) {
                this.addIntersectingBoundingBox(aabb, CableShapes.getModemShape(state).translate(tilePos.x(), tilePos.y(), tilePos.z(), new AABBd()), aabbList);
            }
        }
    }

    @Override
    public void onPlacedByWorld(World world, @NotNull TilePosc tilePos) {
        TileEntity tile = world.getTileEntity(tilePos);
        if (tile instanceof TileCable cable) {

            if (cable.hasCable()) {
                cable.connectionsChanged();
            }
        }
    }

    @Override
    public void onPlacedByMob(World world, @NotNull TilePosc tilePos, @NotNull Side side, Mob mob, double xPlaced, double yPlaced) {
        TileEntity tile = world.getTileEntity(tilePos);
        if (tile instanceof TileCable cable) {

            if (cable.hasCable()) {
                cable.connectionsChanged();
            }
        }
    }

    public ItemStack @Nullable [] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
        if (dropCause == EnumDropCause.IMPROPER_TOOL) {
            return null;
        }

        if (!(tileEntity instanceof TileCable cable)) {
            return null;
        }

        if (cable.blockStateCable && cable.blockStateModem != CableModemVariant.None) {
            return new ItemStack[]{new ItemStack(ComputerCraftItems.CABLE), new ItemStack(ComputerCraftItems.WIRED_MODEM)};
        }

        if (cable.blockStateModem != CableModemVariant.None) {
            return new ItemStack[]{new ItemStack(ComputerCraftItems.WIRED_MODEM)};
        }

        return new ItemStack[]{new ItemStack(ComputerCraftItems.CABLE)};


    }

    public boolean onInteracted(World world, TilePosc tilePos, Player player, Side side, double xPlaced, double yPlaced) {
        return ((TileCable) world.getTileEntity(tilePos)).onInteracted(player, side, xPlaced, yPlaced);
    }

    @Override
    public void onNeighborChanged(World world, TilePosc tilePos, Block<?> block) {
        TileEntity entity = (world.getTileEntity(tilePos));
        if (!(entity instanceof TileCable tileCable)) {
            return;
        }

        tileCable.onNeighbourChange(tilePos);
    }
}
