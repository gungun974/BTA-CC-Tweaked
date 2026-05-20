/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.util;

import com.google.common.base.Predicate;
import com.google.common.collect.MapMaker;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.phys.HitResult;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;

import java.util.List;
import java.util.Map;

public final class WorldUtil {
    @SuppressWarnings("Guava")
    private static final Predicate<Entity> CAN_COLLIDE = x -> x != null && x.isAlive(); //TODO: && x.collides();

    private static final Map<World, Entity> entityCache = new MapMaker().weakKeys()
        .weakValues()
        .makeMap();

    public static boolean isLiquidBlock(World world, TilePosc pos) {
        if (World.HEIGHT_BLOCKS >= pos.y()) {
            return false;
        }
        return world.getBlockType(pos)
            .getMaterial()
            .isLiquid();
    }

    public static Pair<Entity, Vector3dc> rayTraceEntities(World world, Vector3dc vecStart, Vector3dc vecDir, double distance) {
        vecStart = vecStart.add(0.5, 0.5, 0.5, new Vector3d());
        Vector3dc vecEnd = vecStart.add(vecDir.x() * distance, vecDir.y() * distance, vecDir.z() * distance, new Vector3d());
        Vector3dc vecStartBlock = vecStart.add(vecDir.x() * 0.51, vecDir.y() * 0.51, vecDir.z() * 0.51, new Vector3d());

        HitResult result = world.checkBlockCollisionBetweenPoints(vecStartBlock, vecEnd, true, true, true);
        if (result instanceof HitResult.Tile) {
            distance = vecStart.distance(result.location);
            vecEnd = vecStart.add(vecDir.x() * distance, vecDir.y() * distance, vecDir.z() * distance, new Vector3d());
        }

        // Check for entities
        float xStretch = Math.abs(vecDir.x()) > 0.25f ? 0.0f : 1.0f;
        float yStretch = Math.abs(vecDir.y()) > 0.25f ? 0.0f : 1.0f;
        float zStretch = Math.abs(vecDir.z()) > 0.25f ? 0.0f : 1.0f;
        AABBd bigBox = new AABBd(Math.min(vecStart.x(), vecEnd.x()) - 0.375f * xStretch,
            Math.min(vecStart.y(), vecEnd.y()) - 0.375f * yStretch,
            Math.min(vecStart.z(), vecEnd.z()) - 0.375f * zStretch,
            Math.max(vecStart.x(), vecEnd.x()) + 0.375f * xStretch,
            Math.max(vecStart.y(), vecEnd.y()) + 0.375f * yStretch,
            Math.max(vecStart.z(), vecEnd.z()) + 0.375f * zStretch);

        Entity closest = null;
        double closestDist = 99.0;
        List<Entity> list = world.getEntitiesWithinAABB(Entity.class, bigBox);
        for (Entity entity : list) {
            AABBdc littleBox = entity.bb;
            if (littleBox.containsPoint(vecStart)) {
                closest = entity;
                closestDist = 0.0f;
                continue;
            }

            HitResult littleBoxResult = clip(littleBox, vecStart, vecEnd);
            if (littleBoxResult != null) {
                double dist = vecStart.distance(littleBoxResult.location);
                if (closest == null || dist <= closestDist) {
                    closest = entity;
                    closestDist = dist;
                }
            } else if (littleBox.intersectsAABB(bigBox)) {
                if (closest == null) {
                    closest = entity;
                    closestDist = distance;
                }
            }
        }
        if (closest != null && closestDist <= distance) {
            Vector3dc closestPos = vecStart.add(vecDir.x() * closestDist, vecDir.y() * closestDist, vecDir.z() * closestDist, new Vector3d());
            return Pair.of(closest, closestPos);
        }
        return null;
    }

    private static HitResult clip(AABBdc bb, Vector3dc start, Vector3dc end) {
        Vector3d vec32 = clipX(start, end, bb.minX());
        Vector3d vec33 = clipX(start, end, bb.maxX());
        Vector3d vec34 = clipY(start, end, bb.minY());
        Vector3d vec35 = clipY(start, end, bb.maxY());
        Vector3d vec36 = clipZ(start, end, bb.minZ());
        Vector3d vec37 = clipZ(start, end, bb.maxZ());

        if (!containsX(bb, vec32)) {
            vec32 = null;
        }

        if (!containsX(bb, vec33)) {
            vec33 = null;
        }

        if (!containsY(bb, vec34)) {
            vec34 = null;
        }

        if (!containsY(bb, vec35)) {
            vec35 = null;
        }

        if (!containsZ(bb, vec36)) {
            vec36 = null;
        }

        if (!containsZ(bb, vec37)) {
            vec37 = null;
        }

        Vector3d vec38 = null;

        if (vec32 != null && (vec38 == null || start.distanceSquared(vec32) < start.distanceSquared(vec38))) {
            vec38 = vec32;
        }

        if (vec33 != null && (vec38 == null || start.distanceSquared(vec33) < start.distanceSquared(vec38))) {
            vec38 = vec33;
        }

        if (vec34 != null && (vec38 == null || start.distanceSquared(vec34) < start.distanceSquared(vec38))) {
            vec38 = vec34;
        }

        if (vec35 != null && (vec38 == null || start.distanceSquared(vec35) < start.distanceSquared(vec38))) {
            vec38 = vec35;
        }

        if (vec36 != null && (vec38 == null || start.distanceSquared(vec36) < start.distanceSquared(vec38))) {
            vec38 = vec36;
        }

        if (vec37 != null && (vec38 == null || start.distanceSquared(vec37) < start.distanceSquared(vec38))) {
            vec38 = vec37;
        }

        if (vec38 == null) {
            return null;
        }

        return new HitResult(vec38);
    }

    private static boolean containsX(AABBdc bb, Vector3dc vec3) {
        return vec3 != null
            && vec3.y() >= bb.minY()
            && vec3.y() <= bb.maxY()
            && vec3.z() >= bb.minZ()
            && vec3.z() <= bb.maxZ();
    }

    private static boolean containsY(AABBdc bb, Vector3dc vec3) {
        return vec3 != null
            && vec3.x() >= bb.minX()
            && vec3.x() <= bb.maxX()
            && vec3.z() >= bb.minZ()
            && vec3.z() <= bb.maxZ();
    }

    private static boolean containsZ(AABBdc bb, Vector3dc vec3) {
        return vec3 != null
            && vec3.x() >= bb.minX()
            && vec3.x() <= bb.maxX()
            && vec3.y() >= bb.minY()
            && vec3.y() <= bb.maxY();
    }

    private static Vector3d clipX(Vector3dc from, Vector3dc to, double x) {
        double diffX = to.x() - from.x();
        double diffY = to.y() - from.y();
        double diffZ = to.z() - from.z();

        if (diffX * diffX < 1.0E-7) {
            return null;
        }

        double t = (x - from.x()) / diffX;

        return t >= 0.0 && t <= 1.0
            ? new Vector3d(from.x() + diffX * t, from.y() + diffY * t, from.z() + diffZ * t)
            : null;
    }

    private static Vector3d clipY(Vector3dc from, Vector3dc to, double y) {
        double diffX = to.x() - from.x();
        double diffY = to.y() - from.y();
        double diffZ = to.z() - from.z();

        if (diffY * diffY < 1.0E-7) {
            return null;
        }

        double t = (y - from.y()) / diffY;

        return t >= 0.0 && t <= 1.0
            ? new Vector3d(from.x() + diffX * t, from.y() + diffY * t, from.z() + diffZ * t)
            : null;
    }

    private static Vector3d clipZ(Vector3dc from, Vector3dc to, double z) {
        double diffX = to.x() - from.x();
        double diffY = to.y() - from.y();
        double diffZ = to.z() - from.z();

        if (diffZ * diffZ < 1.0E-7) {
            return null;
        }

        double t = (z - from.z()) / diffZ;

        return t >= 0.0 && t <= 1.0
            ? new Vector3d(from.x() + diffX * t, from.y() + diffY * t, from.z() + diffZ * t)
            : null;
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

    public static Vector3dc getRayEnd(Player player) {
        double reach = 5;
        Vector3dc look = player.getViewVector(1.0f);
        return getRayStart(player).add(look.x() * reach, look.y() * reach, look.z() * reach, new Vector3d());
    }

    public static Vector3dc getRayStart(Mob entity) {
        return entity.getViewVector(1);
    }

    public static void dropItemStack(@NotNull ItemStack stack, World world, TilePosc pos) {
        dropItemStack(stack, world, pos, null);
    }

    public static void dropItemStack(@NotNull ItemStack stack, World world, TilePosc pos, Direction direction) {
        double xDir;
        double yDir;
        double zDir;
        if (direction != null) {
            xDir = direction.offsetX();
            yDir = direction.offsetY();
            zDir = direction.offsetZ();
        } else {
            xDir = 0.0;
            yDir = 0.0;
            zDir = 0.0;
        }

        double xPos = pos.x() + 0.5 + xDir * 0.4;
        double yPos = pos.y() + 0.5 + yDir * 0.4;
        double zPos = pos.z() + 0.5 + zDir * 0.4;
        dropItemStack(stack, world, new Vector3d(xPos, yPos, zPos), xDir, yDir, zDir);
    }

    public static void dropItemStack(@NotNull ItemStack stack, World world, Vector3dc pos, double xDir, double yDir, double zDir) {
        EntityItem item = new EntityItem(world, pos.x(), pos.y(), pos.z(), stack.copy());
        item.xd = xDir * 0.7 + world.rand
            .nextFloat() * 0.2 - 0.1;
        item.yd = yDir * 0.7 + world.rand
            .nextFloat() * 0.2 - 0.1;
        item.zd = zDir * 0.7 + world.rand
            .nextFloat() * 0.2 - 0.1;
        item.pickupDelay = 40;
        world.entityJoinedWorld(item);
    }

    public static void dropItemStack(@NotNull ItemStack stack, World world, Vector3dc pos) {
        dropItemStack(stack, world, pos, 0.0, 0.0, 0.0);
    }
}
