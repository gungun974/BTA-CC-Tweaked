/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.common;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.BlockPos;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.phys.HitResult;

import javax.annotation.Nonnull;

public abstract class TileGeneric extends TileEntity// implements BlockEntityClientSerializable
{
    /*
    public TileGeneric( BlockEntityType<? extends TileGeneric> type )
    {
        super( type );
    }
     */

    public void onChunkUnloaded()
    {
    }

    /*
    public final void updateBlock()
    {
        markDirty();
        BlockPos pos = new BlockPos(x, y, z);
        BlockState state = getCachedState();
        worldObj.updateListeners( pos, state, state, 3 );
    }

    @Nonnull
    public ActionResult onActivate(Player player, Hand hand, HitResult hit )
    {
        return ActionResult.PASS;
    }
     */

    protected void blockTick()
    {
    }

    public boolean isUsable( Player player, boolean ignoreRange )
    {
        if( player == null || !player.isAlive() || worldObj.getTileEntity( x, y ,z ) != this )
        {
            return false;
        }
        if( ignoreRange )
        {
            return true;
        }

        double range = getInteractRange( player );
        return player.world == worldObj && player.distanceToSqr( x + 0.5, y + 0.5, z + 0.5 ) <= range * range;
    }

    protected double getInteractRange( Player player )
    {
        return 8.0;
    }

    /*
    @Override
    public void fromClientTag( CompoundTag compoundTag )
    {
        readDescription( compoundTag );
    }

    protected void readDescription( @Nonnull CompoundTag nbt )
    {
    }

    @Override
    public CompoundTag toClientTag( CompoundTag compoundTag )
    {
        writeDescription( compoundTag );
        return compoundTag;
    }

    protected void writeDescription( @Nonnull CompoundTag nbt )
    {
    }
     */
}
