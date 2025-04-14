/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.modem.wireless;

import dan200.computercraft.shared.peripheral.modem.ModemShapes;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicFullyRotatable;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Mob;
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

    @Override
    public void onBlockPlacedByWorld(World world, int x, int y, int z) {
        super.onBlockPlacedByWorld(world, x, y, z);
    }

    public void onBlockPlacedByMob(World world, int x, int y, int z, @NotNull Side side, Mob mob, double xPlaced, double yPlaced) {
        Direction direction = mob.getPlacementDirection(side, PlacementMode.SIDE);
        world.setBlockMetadataWithNotify(x, y, z, directionToMeta(direction));
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
