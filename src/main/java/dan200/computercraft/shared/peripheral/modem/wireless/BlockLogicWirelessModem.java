/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.modem.wireless;

import dan200.computercraft.shared.peripheral.modem.ModemShapes;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicFullyRotatable;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.enums.PlacementMode;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.joml.primitives.AABBdc;

public class BlockLogicWirelessModem extends BlockLogicFullyRotatable {
    public BlockLogicWirelessModem(Block<?> block, boolean advanced) {
        super(block, Materials.STONE);
        block.withEntity(() -> new TileWirelessModem(advanced));
        this.setBlockBounds(0.125, 0.0, 0.125, 0.875, 0.1875, 0.875);
    }

    @Override
    public boolean canPlaceAt(World world, TilePosc tilePos) {
        for (Side side : Side.sides) {
            if (world.isBlockNormalCube(tilePos.add(side, new TilePos()))) {
                return true;
            }
        }
        return false;
    }

    private void dropIfCantStay(World world, TilePosc tilePos) {
        Direction direction = BlockLogicWirelessModem.metaToDirection(world.getBlockData(tilePos)).opposite();

        if (!(world.isBlockNormalCube(tilePos.add(direction, new TilePos())))) {
            this.dropWithCause(world, EnumDropCause.WORLD, tilePos, world.getBlockData(tilePos), null, null);
            world.setBlockTypeNotify(tilePos, Blocks.AIR);
        }
    }

    @Override
    public void onPlacedByMob(World world, TilePosc tilePos, @NotNull Side side, Mob mob, double xPlaced, double yPlaced) {
        Direction direction = mob.getPlacementDirection(side, PlacementMode.SIDE);
        world.setBlockDataNotify(tilePos, directionToMeta(direction));
        dropIfCantStay(world, tilePos);
    }

    @Override
    public void onPlacedOnSide(World world, TilePosc tilePos, @NotNull Side side, double xPlaced, double yPlaced) {
        Direction direction = side.direction().opposite();

        if (world.isBlockNormalCube(tilePos.sub(direction, new TilePos()))) {
            world.setBlockDataNotify(tilePos, directionToMeta(direction));
            return;
        }

        world.setBlockDataNotify(tilePos, directionToMeta(direction.opposite()));
        dropIfCantStay(world, tilePos);
    }

    public void onNeighborChanged(World world, TilePosc tilePos, Block<?> block) {
        dropIfCantStay(world, tilePos);
    }

    public boolean isCubeShaped() {
        return false;
    }

    public boolean isSolidRender() {
        return false;
    }

    @Override
    @NotNull
    public AABBdc getBoundsFromState(WorldSource world, TilePosc tilePos) {
        return ModemShapes.getBounds(BlockLogicWirelessModem.metaToDirection(world.getBlockData(tilePos)).opposite());
    }
}
