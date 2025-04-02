/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.upgrades;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.BlockPos;
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
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class TurtleTool extends AbstractTurtleUpgrade
{
    protected final ItemStack item;

    protected EnumDropCause dropCause = EnumDropCause.PROPER_TOOL;

    private static final int TAG_LIST = 9;
    private static final int TAG_COMPOUND = 10;

    public TurtleTool( int id, Item item )
    {
        super( id, TurtleUpgradeType.TOOL, item );
        this.item = new ItemStack( item );
    }

    public TurtleTool( int id, ItemStack craftItem, ItemStack toolItem )
    {
        super( id, TurtleUpgradeType.TOOL, craftItem );
        item = toolItem;
    }

    @Override
    public @NotNull String getUnlocalisedAdjective() {
        return "upgrade.minecraft.diamond_pickaxe.adjective";
    }

    @Override
    public boolean isItemSuitable( @Nonnull ItemStack stack )
    {
        CompoundTag tag = stack.getData();

        // Check we've not got anything vaguely interesting on the item. We allow other mods to add their
        // own NBT, with the understanding such details will be lost to the mist of time.
        if( stack.isItemDamaged() || stack.hasCustomName() ) return false;
        return !tag.containsKey( "AttributeModifiers" ) ||
            tag.getList( "AttributeModifiers" ) != null;
    }

    @Nonnull
    @Override
    public TurtleCommandResult useTool( @Nonnull ITurtleAccess turtle, @Nonnull TurtleSide side, @Nonnull TurtleVerb verb, @Nonnull Direction direction )
    {
        switch( verb )
        {
            case ATTACK:
                return attack( turtle, direction, side );
            case DIG:
                return dig( turtle, direction, side );
            default:
                return TurtleCommandResult.failure( "Unsupported action" );
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void drawTileUpgrade(Tessellator tessellator, TextureManager textureManager, TileTurtle tileEntity, float angle, @Nonnull TurtleSide side, float partialTick) {
        float xOffset = side == TurtleSide.LEFT ? -0.40625f : 0.40625f;

        GL11.glPushMatrix();

        GL11.glScalef(-1, -1, -1);

        GL11.glRotatef(270, 0, 1f, 0);

        ItemModel model = ItemModelDispatcher.getInstance().getDispatch(item);

        float tileWidth = 1f / model.getIcon(null, item).width;

        GL11.glTranslatef(-1 + tileWidth, -1 + tileWidth, 0.5f + tileWidth/2f - xOffset);

        GL11.glTranslatef(0.5f, 0.5f, 0);

        GL11.glRotatef(tileEntity.getToolRenderAngle(side, partialTick), 0f, 0f, 1f);

        GL11.glRotatef(180, 0, 1f, 0);
        GL11.glRotatef(-90, 0, 0f, 1);

        GL11.glTranslatef(-0.5f, -0.5f, 0);

        GL11.glTranslatef(tileWidth, tileWidth, tileWidth);

        model.renderItemInWorld(
            tessellator, null, item, 1f, 1.0F, false
        );

        GL11.glPopMatrix();
    }

    @Override
    public void drawItemUpgrade(Tessellator tessellator, TextureManager textureManager, @NotNull TurtleSide side) {
        float xOffset = side == TurtleSide.LEFT ? -0.40625f : 0.40625f;

        GL11.glPushMatrix();

        GL11.glScalef(-1, -1, -1);

        GL11.glRotatef(270, 0, 1f, 0);

        ItemModel model = ItemModelDispatcher.getInstance().getDispatch(item);

        float tileWidth = 1f / model.getIcon(null, item).width;

        GL11.glTranslatef(-1 + tileWidth, -1 + tileWidth, 0.5f + tileWidth/2f - xOffset);

        model.renderItemInWorld(
            tessellator, null, item, 1f, 1.0F, false
        );

        GL11.glPopMatrix();

    }

    private TurtleCommandResult attack(ITurtleAccess turtle, Direction direction, TurtleSide side )
    {
        // Create a fake player, and orient it appropriately
        World world = turtle.getWorld();
        BlockPos position = turtle.getPosition();
        TileEntity turtleBlock = turtle instanceof TurtleBrain ? ((TurtleBrain) turtle).getOwner() : world.getTileEntity( position.x, position.y, position.z );
        if( turtleBlock == null ) return TurtleCommandResult.failure( "Turtle has vanished from existence." );

        // See if there is an entity present
        Vec3 turtlePos = Vec3.getPermanentVec3(position.x, position.y, position.z);
        Vec3 rayDir = Vec3.getPermanentVec3(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ());
        Pair<Entity, Vec3> hit = WorldUtil.rayTraceEntities( world, turtlePos, rayDir, 1.5 );
        if( hit != null )
        {
            Entity hitEntity = hit.getKey();

            // Fire several events to ensure we have permissions.
            if( !hitEntity.isSelectable() )
            {
                return TurtleCommandResult.failure( "Nothing to attack here" );
            }

            TurtleAttackEvent attackEvent = new TurtleAttackEvent( turtle, hitEntity, this, side );
            if( TurtleEvent.post( attackEvent ) )
            {
                return TurtleCommandResult.failure( attackEvent.getFailureMessage() );
            }

            // Start claiming entity drops
            DropConsumer.set( hitEntity, turtleDropConsumer( turtleBlock, turtle ) );

            // Attack the entity
            boolean attacked = false;
//            if( !hitEntity.handleAttack( turtlePlayer ) )
//            {
                int damage = 1;
                damage *= getDamageMultiplier();
                if( damage > 0.0f )
                {
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
                        if( hitEntity.hurt( null, damage, DamageType.GENERIC) )
                        {
                            attacked = true;
                        }
                    }
                }
//            }

            // Stop claiming drops
            stopConsuming( turtleBlock, turtle );

            // Put everything we collected into the turtles inventory, then return
            if( attacked )
            {
                return TurtleCommandResult.success();
            }
        }

        return TurtleCommandResult.failure( "Nothing to attack here" );
    }

    private TurtleCommandResult dig( ITurtleAccess turtle, Direction direction, TurtleSide side )
    {
        // Get ready to dig
        World world = turtle.getWorld();
        BlockPos turtlePosition = turtle.getPosition();
        TileEntity turtleBlock = turtle instanceof TurtleBrain ? ((TurtleBrain) turtle).getOwner() : world.getTileEntity( turtlePosition.x, turtlePosition.y, turtlePosition.z );
        if( turtleBlock == null ) return TurtleCommandResult.failure( "Turtle has vanished from existence." );


        BlockPos blockPosition = turtlePosition.offset( direction );

        if( world.isAirBlock( blockPosition.x, blockPosition.y, blockPosition.z ) || WorldUtil.isLiquidBlock( world, blockPosition ) )
        {
            return TurtleCommandResult.failure( "Nothing to dig here" );
        }

        Block<?> block = Objects.requireNonNull(world.getBlock(blockPosition.x, blockPosition.y, blockPosition.z));

        if( ComputerCraft.turtlesObeyBlockProtection )
        {
            if( !TurtlePermissions.isBlockEditable( world, blockPosition ) )
            {
                return TurtleCommandResult.failure( "Cannot break protected block" );
            }
        }

        // Check if we can break the block
        if( !canBreakBlock( world, blockPosition ) )
        {
            return TurtleCommandResult.failure( "Unbreakable block detected" );
        }

        // Fire the dig event, checking whether it was cancelled.
        TurtleBlockEvent.Dig digEvent = new TurtleBlockEvent.Dig( turtle, world, blockPosition, block.id(), this, side );
        if( TurtleEvent.post( digEvent ) )
        {
            return TurtleCommandResult.failure( digEvent.getFailureMessage() );
        }

        // Consume the items the block drops
        DropConsumer.set( world, blockPosition, turtleDropConsumer( turtleBlock, turtle ) );

        TileEntity tile = world.getTileEntity( blockPosition.x, blockPosition.y, blockPosition.z );

        // Destroy the block
        world.playBlockEvent(null, 2001, blockPosition.x, blockPosition.y, blockPosition.z, block.id());
        block.dropBlockWithCause(world, dropCause, blockPosition.x, blockPosition.y, blockPosition.z, world.getBlockMetadata(blockPosition.x, blockPosition.y, blockPosition.z), tile, null);
        world.setBlockWithNotify(blockPosition.x, blockPosition.y, blockPosition.z, 0);

        stopConsuming( turtleBlock, turtle );

        return TurtleCommandResult.success();

    }

    private static Function<ItemStack, ItemStack> turtleDropConsumer( TileEntity turtleBlock, ITurtleAccess turtle )
    {
        return drop -> turtleBlock.isInvalid() ? drop : InventoryUtil.storeItems( drop, turtle.getItemHandler(), turtle.getSelectedSlot() );
    }

    protected int getDamageMultiplier()
    {
        return 3;
    }

    private static void stopConsuming( TileEntity turtleBlock, ITurtleAccess turtle )
    {
        Direction direction = !turtleBlock.isInvalid() ? null : turtle.getDirection().getOpposite();
        List<ItemStack> extra = DropConsumer.clear();
        for( ItemStack remainder : extra )
        {
            WorldUtil.dropItemStack( remainder,
                turtle.getWorld(),
                turtle.getPosition(),
                direction );
        }
    }

    protected boolean canBreakBlock( World world, BlockPos pos )
    {
        Block<?> block = world.getBlock(pos.x, pos.y, pos.z);
        return block != null && block != Blocks.BEDROCK && block.blockHardness > 0;
    }
}
