/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.blocks;

import dan200.computercraft.shared.computer.blocks.BlockLogicComputer;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.turtle.items.TurtleItemFactory;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import org.jetbrains.annotations.NotNull;

public class BlockTurtle extends BlockLogicComputer
{
    public BlockTurtle(Block<?> block, ComputerFamily family) {
        super(block, family);
        block.withEntity(() -> new TileTurtle(family));
        this.setBlockBounds( 0.125, 0.125, 0.125, 0.875, 0.875, 0.875);
    }

    public boolean isCubeShaped() {
        return false;
    }

    public boolean isSolidRender() {
        return false;
    }

    @Override
    public AABB getBlockBoundsFromState(WorldSource world, int x, int y, int z) {
        return AABB.getPermanentBB(0.125, 0.125, 0.125, 0.875, 0.875, 0.875);
    }

    @Override
    public void onBlockPlacedByMob(World world, int x, int y, int z, @NotNull Side side, Mob mob, double xPlaced, double yPlaced) {
        world.setBlockMetadataWithNotify(x, y, z, mob.getHorizontalPlacementDirection(side).getId());
    }

    @Override
    public float getBlastResistance(Entity entity) {
        return 2000;
    }

    @Override
    protected ItemStack getItemStack(TileEntity entity) {
        if( !(entity instanceof TileTurtle) )
        {
            return null;
        }

        return TurtleItemFactory.create( (TileTurtle) entity );
    }
}
