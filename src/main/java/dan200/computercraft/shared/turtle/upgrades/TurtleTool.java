/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.upgrades;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.ComputerCraft;
import dan200.computercraft.api.turtle.*;
import dan200.computercraft.api.turtle.event.TurtleAttackEvent;
import dan200.computercraft.api.turtle.event.TurtleBlockEvent;
import dan200.computercraft.api.turtle.event.TurtleEvent;
import dan200.computercraft.shared.TurtlePermissions;
import dan200.computercraft.shared.turtle.blocks.TileTurtle;
import dan200.computercraft.shared.turtle.core.TurtleBrain;
import dan200.computercraft.shared.util.DropConsumer;
import dan200.computercraft.shared.util.InventoryUtil;
import dan200.computercraft.shared.util.WorldUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3dc;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class TurtleTool extends AbstractTurtleUpgrade {
    private static final int TAG_LIST = 9;
    private static final int TAG_COMPOUND = 10;
    protected final ItemStack item;
    protected EnumDropCause dropCause = EnumDropCause.PROPER_TOOL;

    public TurtleTool(int id, Item item) {
        super(id, TurtleUpgradeType.TOOL, item);
        this.item = new ItemStack(item);
    }

    public TurtleTool(int id, ItemStack craftItem, ItemStack toolItem) {
        super(id, TurtleUpgradeType.TOOL, craftItem);
        item = toolItem;
    }

    private static Function<ItemStack, ItemStack> turtleDropConsumer(TileEntity turtleBlock, ITurtleAccess turtle) {
        return drop -> turtleBlock.isInvalid() ? drop : InventoryUtil.storeItems(drop, turtle.getItemHandler(), turtle.getSelectedSlot());
    }

    private static void stopConsuming(TileEntity turtleBlock, ITurtleAccess turtle) {
        Direction direction = !turtleBlock.isInvalid() ? null : turtle.getDirection().opposite();
        List<ItemStack> extra = DropConsumer.clear();
        for (ItemStack remainder : extra) {
            WorldUtil.dropItemStack(remainder,
                turtle.getWorld(),
                turtle.getPosition(),
                direction);
        }
    }

    @Override
    public @NotNull String getUnlocalisedAdjective() {
        return "upgrade.minecraft.diamond_pickaxe.adjective";
    }

    @Override
    public boolean isItemSuitable(@NotNull ItemStack stack) {
        CompoundTag tag = stack.getData();

        // Check we've not got anything vaguely interesting on the item. We allow other mods to add their
        // own NBT, with the understanding such details will be lost to the mist of time.
        if (stack.isItemDamaged() || stack.hasCustomName()) return false;
        return !tag.containsKey("AttributeModifiers") ||
            tag.getList("AttributeModifiers") != null;
    }

    @NotNull
    @Override
    public TurtleCommandResult useTool(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side, @NotNull TurtleVerb verb, @NotNull Direction direction) {
        switch (verb) {
            case ATTACK:
                return attack(turtle, direction, side);
            case DIG:
                return dig(turtle, direction, side);
            default:
                return TurtleCommandResult.failure("Unsupported action");
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void drawTileUpgrade(Tessellator tessellator, TextureManager textureManager, TileTurtle tileEntity, float angle, @NotNull TurtleSide side, float partialTick) {
        float xOffset = side == TurtleSide.LEFT ? -0.40625f : 0.40625f;

        byte lightIndex = tileEntity.worldObj.getLightIndex(tileEntity.tilePos, 0);

        ItemModel model = ItemModelDispatcher.getInstance().getDispatch(item);
        float tileWidth = 1f / model.getIcon(null, item).width;

        GLRenderer.pushFrame();

        GLRenderer.modelM4f().scale(-1, -1, -1);
        GLRenderer.modelM4f().rotateY((float) Math.toRadians(270));
        GLRenderer.modelM4f().translate(-1 + tileWidth * 9, -1 + tileWidth * 7, xOffset - tileWidth);
        GLRenderer.modelM4f().translate(0.5f, 0.5f, 0);
        GLRenderer.modelM4f().rotateZ((float) Math.toRadians(tileEntity.getToolRenderAngle(side, partialTick)));
        GLRenderer.modelM4f().rotateZ((float) Math.toRadians(90));
        GLRenderer.modelM4f().translate(tileWidth, tileWidth, tileWidth);

        GLRenderer.modelM4f().scale(-1, -1, -1);

        model.render((TessellatorGeneral) tessellator, null, item, "none", true, 1, lightIndex, partialTick, false);

        GLRenderer.popFrame();
    }

    @Override
    public void drawItemUpgrade(TessellatorGeneral tessellator, byte lightIndex, @NotNull TurtleSide side) {
        float xOffset = side == TurtleSide.LEFT ? -0.40625f : 0.40625f;
        ItemModel model = ItemModelDispatcher.getInstance().getDispatch(item);
        float tileWidth = 1f / model.getIcon(null, item).width;
        GLRenderer.pushFrame();
        GLRenderer.modelM4f().scale(-1, -1, -1);
        GLRenderer.modelM4f().rotateY((float) Math.toRadians(270));
        GLRenderer.modelM4f().translate(-1 + tileWidth * 9, -1 + tileWidth * 7, xOffset - tileWidth);
        GLRenderer.modelM4f().translate(0.5f, 0.5f, 0);
        GLRenderer.modelM4f().rotateZ((float) Math.toRadians(90));
        GLRenderer.modelM4f().translate(tileWidth, tileWidth, tileWidth);
        GLRenderer.modelM4f().scale(-1, -1, -1);
        model.render(tessellator, null, item, "none", true, 1, lightIndex, 0, false);
        GLRenderer.popFrame();
    }

    private TurtleCommandResult attack(ITurtleAccess turtle, Direction direction, TurtleSide side) {
        // Create a fake player, and orient it appropriately
        World world = turtle.getWorld();
        TilePosc position = turtle.getPosition();
        TileEntity turtleBlock = turtle instanceof TurtleBrain ? ((TurtleBrain) turtle).getOwner() : world.getTileEntity(position);
        if (turtleBlock == null) return TurtleCommandResult.failure("Turtle has vanished from existence.");

        // See if there is an entity present
        Vector3d turtlePos = new Vector3d(position.x(), position.y(), position.z());
        Vector3d rayDir = new Vector3d(direction.offsetX(), direction.offsetY(), direction.offsetZ());
        Pair<Entity, Vector3dc> hit = WorldUtil.rayTraceEntities(world, turtlePos, rayDir, 1.5);
        if (hit != null) {
            Entity hitEntity = hit.getKey();

            // Fire several events to ensure we have permissions.
            if (!hitEntity.isSelectable()) {
                return TurtleCommandResult.failure("Nothing to attack here");
            }

            TurtleAttackEvent attackEvent = new TurtleAttackEvent(turtle, hitEntity, this, side);
            if (TurtleEvent.post(attackEvent)) {
                return TurtleCommandResult.failure(attackEvent.getFailureMessage());
            }

            // Start claiming entity drops
            DropConsumer.set(hitEntity, turtleDropConsumer(turtleBlock, turtle));

            // Attack the entity
            boolean attacked = false;
//            if( !hitEntity.handleAttack( turtlePlayer ) )
//            {
            int damage = 1;
            damage *= getDamageMultiplier();
            if (damage > 0.0f) {
//                    if( hitEntity instanceof ArmorStandEntity )
//                    {
//                        // Special case for armor stands: attack twice to guarantee destroy
//                        hitEntity.damage( source, damage );
//                        if( hitEntity.isAlive() )
//                        {
//                            hitEntity.damage( source, damage );
//                        }
//                        attacked = true;
//                    }
//                    else
                {
                    if (hitEntity.hurt(null, damage, DamageType.GENERIC)) {
                        attacked = true;
                    }
                }
            }
//            }

            // Stop claiming drops
            stopConsuming(turtleBlock, turtle);

            // Put everything we collected into the turtles inventory, then return
            if (attacked) {
                return TurtleCommandResult.success();
            }
        }

        return TurtleCommandResult.failure("Nothing to attack here");
    }

    private TurtleCommandResult dig(ITurtleAccess turtle, Direction direction, TurtleSide side) {
        // Get ready to dig
        World world = turtle.getWorld();
        TilePosc turtlePosition = turtle.getPosition();
        TileEntity turtleBlock = turtle instanceof TurtleBrain ? ((TurtleBrain) turtle).getOwner() : world.getTileEntity(turtlePosition);
        if (turtleBlock == null) return TurtleCommandResult.failure("Turtle has vanished from existence.");


        TilePosc blockPosition = turtlePosition.add(direction, new TilePos());

        if (world.isAirBlock(blockPosition) || WorldUtil.isLiquidBlock(world, blockPosition)) {
            return TurtleCommandResult.failure("Nothing to dig here");
        }

        Block<?> block = Objects.requireNonNull(world.getBlockType(blockPosition));

        if (ComputerCraft.turtlesObeyBlockProtection) {
            if (!TurtlePermissions.isBlockEditable(world, blockPosition)) {
                return TurtleCommandResult.failure("Cannot break protected block");
            }
        }

        // Check if we can break the block
        if (!canBreakBlock(world, blockPosition)) {
            return TurtleCommandResult.failure("Unbreakable block detected");
        }

        // Fire the dig event, checking whether it was cancelled.
        TurtleBlockEvent.Dig digEvent = new TurtleBlockEvent.Dig(turtle, world, blockPosition, block.id(), this, side);
        if (TurtleEvent.post(digEvent)) {
            return TurtleCommandResult.failure(digEvent.getFailureMessage());
        }

        // Consume the items the block drops
        DropConsumer.set(world, blockPosition, turtleDropConsumer(turtleBlock, turtle));

        TileEntity tile = world.getTileEntity(blockPosition);

        // Destroy the block
        world.playBlockEvent(null, blockPosition, 2001, block.id());
        block.dropWithCause(world, dropCause, blockPosition, world.getBlockData(blockPosition), tile, null);
        world.setBlockTypeNotify(blockPosition, Blocks.AIR);

        stopConsuming(turtleBlock, turtle);

        return TurtleCommandResult.success();

    }

    protected int getDamageMultiplier() {
        return 3;
    }

    protected boolean canBreakBlock(World world, TilePosc pos) {
        Block<?> block = world.getBlockType(pos);
        return block != null && block != Blocks.BEDROCK;
    }
}
