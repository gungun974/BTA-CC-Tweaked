/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.util;

import dan200.computercraft.BlockPos;
import dan200.computercraft.ComputerCraft;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class DropConsumer
{
    private static Function<ItemStack, ItemStack> dropConsumer;
    private static List<ItemStack> remainingDrops;
    private static WeakReference<World> dropWorld;
    private static BlockPos dropPos;
    private static AABB dropBounds;
    private static WeakReference<Entity> dropEntity;

    private DropConsumer()
    {
    }

    public static void set( Entity entity, Function<ItemStack, ItemStack> consumer )
    {
        dropConsumer = consumer;
        remainingDrops = new ArrayList<>();
        dropEntity = new WeakReference<>( entity );
        dropWorld = new WeakReference<>( entity.world );
        dropPos = null;
        dropBounds = AABB.getPermanentBB( entity.x, entity.y, entity.z, entity.x, entity.y, entity.z ).expand( 2, 2, 2 );
    }

    public static void set( World world, BlockPos pos, Function<ItemStack, ItemStack> consumer )
    {
        dropConsumer = consumer;
        remainingDrops = new ArrayList<>( 2 );
        dropEntity = null;
        dropWorld = new WeakReference<>( world );
        dropBounds = AABB.getPermanentBB( pos.x, pos.y, pos.z, pos.x, pos.y, pos.z ).expand( 2, 2, 2 );
    }

    public static List<ItemStack> clear()
    {
        List<ItemStack> remainingStacks = remainingDrops;

        dropConsumer = null;
        remainingDrops = null;
        dropEntity = null;
        dropWorld = null;
        dropBounds = null;

        return remainingStacks;
    }

    public static boolean onHarvestDrops( World world, BlockPos pos, ItemStack stack )
    {
        if( dropWorld != null && dropWorld.get() == world && dropPos != null && dropPos.equals( pos ) )
        {
            handleDrops( stack );
            return true;
        }
        return false;
    }

    private static void handleDrops( ItemStack stack )
    {
        ItemStack remaining = dropConsumer.apply( stack );
        if( remaining != null && remaining.stackSize != 0)
        {
            remainingDrops.add( remaining );
        }
    }

    public static boolean onEntitySpawn( Entity entity )
    {
        // Capture any nearby item spawns
        if( dropWorld != null && dropWorld.get() == entity.world && entity instanceof EntityItem && dropBounds.contains(  Vec3.getPermanentVec3(entity.x, entity.y, entity.z)))
        {
            handleDrops( ((EntityItem) entity).item );
            return true;
        }
        return false;
    }

    public static boolean onLivingDrops( Entity entity, ItemStack stack )
    {
        if( dropEntity != null && entity == dropEntity.get() )
        {
            handleDrops( stack );
            return true;
        }
        return false;
    }
}
