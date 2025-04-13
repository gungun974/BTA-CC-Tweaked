/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.util;

import com.google.common.base.Predicate;
import com.google.common.collect.MapMaker;
import dan200.computercraft.BlockPos;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.util.phys.HitResult;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

public final class WorldUtil {
    @SuppressWarnings("Guava")
    private static final Predicate<Entity> CAN_COLLIDE = x -> x != null && x.isAlive(); //TODO: && x.collides();

    private static final Map<World, Entity> entityCache = new MapMaker().weakKeys()
        .weakValues()
        .makeMap();

    public static boolean isLiquidBlock(World world, BlockPos pos) {
        if (World.HEIGHT_BLOCKS >= pos.y) {
            return false;
        }
        return world.getBlock(pos.x, pos.y, pos.z)
            .getMaterial()
            .isLiquid();
    }

    public static Pair<Entity, Vec3> rayTraceEntities(World world, Vec3 vecStart, Vec3 vecDir, double distance) {
        vecStart = vecStart.add(0.5, 0.5, 0.5);
        Vec3 vecEnd = vecStart.add(vecDir.x * distance, vecDir.y * distance, vecDir.z * distance);
        Vec3 vecStartBlock = vecStart.add(vecDir.x * 0.51, vecDir.y * 0.51, vecDir.z * 0.51);

        HitResult result = world.checkBlockCollisionBetweenPoints(vecStartBlock, vecEnd, true, true, true);
        if (result != null && result.hitType == HitResult.HitType.TILE) {
            distance = vecStart.distanceTo(result.location);
            vecEnd = vecStart.add(vecDir.x * distance, vecDir.y * distance, vecDir.z * distance);
        }

        // Check for entities
        float xStretch = Math.abs(vecDir.x) > 0.25f ? 0.0f : 1.0f;
        float yStretch = Math.abs(vecDir.y) > 0.25f ? 0.0f : 1.0f;
        float zStretch = Math.abs(vecDir.z) > 0.25f ? 0.0f : 1.0f;
        AABB bigBox = AABB.getPermanentBB(Math.min(vecStart.x, vecEnd.x) - 0.375f * xStretch,
            Math.min(vecStart.y, vecEnd.y) - 0.375f * yStretch,
            Math.min(vecStart.z, vecEnd.z) - 0.375f * zStretch,
            Math.max(vecStart.x, vecEnd.x) + 0.375f * xStretch,
            Math.max(vecStart.y, vecEnd.y) + 0.375f * yStretch,
            Math.max(vecStart.z, vecEnd.z) + 0.375f * zStretch);

        Entity closest = null;
        double closestDist = 99.0;
        List<Entity> list = world.getEntitiesWithinAABB(Entity.class, bigBox);
        for (Entity entity : list) {
            AABB littleBox = entity.bb;
            if (littleBox.contains(vecStart)) {
                closest = entity;
                closestDist = 0.0f;
                continue;
            }

            HitResult littleBoxResult = littleBox.clip(vecStart, vecEnd);
            if (littleBoxResult != null) {
                double dist = vecStart.distanceTo(littleBoxResult.location);
                if (closest == null || dist <= closestDist) {
                    closest = entity;
                    closestDist = dist;
                }
            } else if (littleBox.intersects(bigBox)) {
                if (closest == null) {
                    closest = entity;
                    closestDist = distance;
                }
            }
        }
        if (closest != null && closestDist <= distance) {
            Vec3 closestPos = vecStart.add(vecDir.x * closestDist, vecDir.y * closestDist, vecDir.z * closestDist);
            return Pair.of(closest, closestPos);
        }
        return null;
    }

    private static synchronized Entity getEntity(World world) {
        // TODO: It'd be nice if we could avoid this. Maybe always use the turtle player (if it's available).
        Entity entity = entityCache.get(world);
        if (entity != null) {
            return entity;
        }

        entity = new EntityItem(world);

        //entity.noClip = true;
        entityCache.put(world, entity);
        return entity;
    }

    public static Vec3 getRayEnd(Player player) {
        double reach = 5;
        Vec3 look = player.getLookAngle();
        return getRayStart(player).add(look.x * reach, look.y * reach, look.z * reach);
    }

    public static Vec3 getRayStart(Mob entity) {
        return entity.getViewVector(1);
    }

    public static void dropItemStack(@Nonnull ItemStack stack, World world, BlockPos pos) {
        dropItemStack(stack, world, pos, null);
    }

    public static void dropItemStack(@Nonnull ItemStack stack, World world, BlockPos pos, Direction direction) {
        double xDir;
        double yDir;
        double zDir;
        if (direction != null) {
            xDir = direction.getOffsetX();
            yDir = direction.getOffsetY();
            zDir = direction.getOffsetZ();
        } else {
            xDir = 0.0;
            yDir = 0.0;
            zDir = 0.0;
        }

        double xPos = pos.getX() + 0.5 + xDir * 0.4;
        double yPos = pos.getY() + 0.5 + yDir * 0.4;
        double zPos = pos.getZ() + 0.5 + zDir * 0.4;
        dropItemStack(stack, world, Vec3.getPermanentVec3(xPos, yPos, zPos), xDir, yDir, zDir);
    }

    public static void dropItemStack(@Nonnull ItemStack stack, World world, Vec3 pos, double xDir, double yDir, double zDir) {
        EntityItem item = new EntityItem(world, pos.x, pos.y, pos.z, stack.copy());
        item.xd = xDir * 0.7 + world.rand
            .nextFloat() * 0.2 - 0.1;
        item.yd = yDir * 0.7 + world.rand
            .nextFloat() * 0.2 - 0.1;
        item.zd = zDir * 0.7 + world.rand
            .nextFloat() * 0.2 - 0.1;
        item.pickupDelay = 40;
        world.entityJoinedWorld(item);
    }

    public static void dropItemStack(@Nonnull ItemStack stack, World world, Vec3 pos) {
        dropItemStack(stack, world, pos, 0.0, 0.0, 0.0);
    }
}
