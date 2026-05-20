/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.blocks;

import dan200.computercraft.shared.computer.blocks.BlockLogicComputer;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.turtle.items.TurtleItemFactory;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;
import turniplabs.halplibe.helper.EnvironmentHelper;

public class BlockLogicTurtle extends BlockLogicComputer {
    public BlockLogicTurtle(Block<?> block, ComputerFamily family) {
        super(block, family);
        block.withEntity(() -> new TileTurtle(family));
        this.setBlockBounds(0.125, 0.125, 0.125, 0.875, 0.875, 0.875);
    }

    public boolean isCubeShaped() {
        return false;
    }

    public boolean isSolidRender() {
        return false;
    }

    @Override
    public @NotNull AABBdc getBoundsFromState(@NotNull WorldSource world, @NotNull TilePosc tilePos) {
        return new AABBd(0.125, 0.125, 0.125, 0.875, 0.875, 0.875);
    }

    @Override
    public void onPlacedByMob(World world, TilePosc tilePos, @NotNull Side side, Mob mob, double xPlaced, double yPlaced) {
        Direction direction = mob.getHorizontalPlacementDirection(side);

        world.setBlockDataNotify(tilePos, direction.id);

        TileEntity entity = (world.getTileEntity(tilePos));
        if (!(entity instanceof TileTurtle turtle)) {
            return;
        }

        turtle.setDirection(direction);
        turtle.carriedBlock = null;

        if (!EnvironmentHelper.isClientWorld()) {
            ServerComputer computer = turtle.getServerComputer();

            if (computer != null) {
                computer.setWorld(world);
            }
        }
    }

    @Override
    public float getBlastResistance(Entity entity) {
        return 2000;
    }

    @Override
    protected ItemStack getItemStack(TileEntity entity) {
        if (!(entity instanceof TileTurtle)) {
            return null;
        }

        return TurtleItemFactory.create((TileTurtle) entity);
    }
}
