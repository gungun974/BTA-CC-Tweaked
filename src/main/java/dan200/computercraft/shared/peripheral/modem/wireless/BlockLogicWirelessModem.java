/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.modem.wireless;

import dan200.computercraft.shared.peripheral.modem.ModemShapes;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicFullyRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.enums.PlacementMode;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import org.jetbrains.annotations.NotNull;

public class BlockLogicWirelessModem extends BlockLogicFullyRotatable {
    public BlockLogicWirelessModem(Block<?> block, boolean advanced) {
        super(block, Material.stone);
        block.withEntity(() -> new TileWirelessModem(advanced));
        this.setBlockBounds(0.125, 0.0, 0.125, 0.875, 0.1875, 0.875);
    }

    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        for (Side side : Side.sides) {
            if (world.isBlockNormalCube(x + side.getOffsetX(), y + side.getOffsetY(), z + side.getOffsetZ())) {
               return true;
            }
        }
        return false;
    }

    private void dropIfCantStay(World world, int x, int y, int z) {
        Direction direction = BlockLogicWirelessModem.metaToDirection(world.getBlockMetadata(x, y, z)).getOpposite();

        if (!(world.isBlockNormalCube(x + direction.getOffsetX(), y + direction.getOffsetY(), z + direction.getOffsetZ()))) {
            this.dropBlockWithCause(world, EnumDropCause.WORLD, x, y, z, world.getBlockMetadata(x, y, z), (TileEntity)null, (Player)null);
            world.setBlockWithNotify(x, y, z, 0);
        }
    }

    public void onBlockPlacedByMob(World world, int x, int y, int z, @NotNull Side side, Mob mob, double xPlaced, double yPlaced) {
        Direction direction = mob.getPlacementDirection(side, PlacementMode.SIDE);
        world.setBlockMetadataWithNotify(x, y, z, directionToMeta(direction));
        dropIfCantStay(world, x, y, z);
    }

    @Override
    public void onBlockPlacedOnSide(World world, int x, int y, int z, @NotNull Side side, double xPlaced, double yPlaced) {
       Direction direction = side.getDirection().getOpposite();

        if (world.isBlockNormalCube(x - direction.getOffsetX(), y - direction.getOffsetY(), z - direction.getOffsetZ())) {
            world.setBlockMetadataWithNotify(x, y, z, directionToMeta(direction));
            return;
        }

        world.setBlockMetadataWithNotify(x, y, z, directionToMeta(direction.getOpposite()));
        dropIfCantStay(world, x, y, z);
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
        dropIfCantStay(world, x, y, z);
    }

    public boolean isCubeShaped() {
        return false;
    }

    public boolean isSolidRender() {
        return false;
    }

    @Override
    public AABB getBlockBoundsFromState(WorldSource world, int x, int y, int z) {
        return ModemShapes.getBounds(BlockLogicWirelessModem.metaToDirection(world.getBlockMetadata(x, y, z)).getOpposite());
    }
}
