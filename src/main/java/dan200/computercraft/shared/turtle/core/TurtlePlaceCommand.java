/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.core;

import dan200.computercraft.BlockPos;
import dan200.computercraft.ComputerCraft;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleCommand;
import dan200.computercraft.api.turtle.TurtleAnimation;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.event.TurtleBlockEvent;
import dan200.computercraft.api.turtle.event.TurtleEvent;
import dan200.computercraft.shared.TurtlePermissions;
import dan200.computercraft.shared.util.DirectionUtil;
import dan200.computercraft.shared.util.DropConsumer;
import dan200.computercraft.shared.util.InventoryUtil;
import dan200.computercraft.shared.util.WorldUtil;
import net.minecraft.core.block.BlockLogicActivator;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityActivator;
import net.minecraft.core.block.entity.TileEntitySign;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.item.*;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.List;

public class TurtlePlaceCommand implements ITurtleCommand
{
    private final InteractDirection direction;
    private final Object[] extraArguments;

    public TurtlePlaceCommand( InteractDirection direction, Object[] arguments )
    {
        this.direction = direction;
        extraArguments = arguments;
    }

    @Nonnull
    @Override
    public TurtleCommandResult execute( @Nonnull ITurtleAccess turtle )
    {
        // Get thing to place
        ItemStack stack = turtle.getInventory()
            .getItem( turtle.getSelectedSlot() );
        if( stack == null )
        {
            return TurtleCommandResult.failure( "No items to place" );
        }

        // Remember old block
        Direction direction = this.direction.toWorldDir( turtle );
        BlockPos coordinates = turtle.getPosition()
            .offset( direction );

        TurtleBlockEvent.Place place = new TurtleBlockEvent.Place( turtle, turtle.getWorld(), coordinates, stack );
        if( TurtleEvent.post( place ) )
        {
            return TurtleCommandResult.failure( place.getFailureMessage() );
        }

        // Do the deploying
        String[] errorMessage = new String[1];
        ItemStack remainder = deploy( stack, turtle, direction, extraArguments, errorMessage );
        if( remainder != stack )
        {
            // Put the remaining items back
            turtle.getInventory()
                .setItem( turtle.getSelectedSlot(), remainder );
            turtle.getInventory()
                .setChanged();

            // Animate and return success
            turtle.playAnimation( TurtleAnimation.WAIT );
            return TurtleCommandResult.success();
        }
        else
        {
            if( errorMessage[0] != null )
            {
                return TurtleCommandResult.failure( errorMessage[0] );
            }
            else if( stack.getItem() instanceof ItemBlock)
            {
                return TurtleCommandResult.failure( "Cannot place block here" );
            }
            else
            {
                return TurtleCommandResult.failure( "Cannot place item here" );
            }
        }
    }

    public static ItemStack deploy( @Nonnull ItemStack stack, ITurtleAccess turtle, Direction direction,
                                    Object[] extraArguments, String[] outErrorMessage )
    {
        BlockPos turtlePosition = turtle.getPosition();
        BlockPos position = turtlePosition.offset( direction );

        // Calculate where the turtle would hit the block
        float hitX = 0.5f + direction.getOffsetX() * 0.5f;
        float hitY = 0.5f + direction.getOffsetY() * 0.5f;
        float hitZ = 0.5f + direction.getOffsetZ() * 0.5f;
        if( Math.abs( hitY - 0.5f ) < 0.01f )
        {
            hitY = 0.45f;
        }

        // Load up the turtle's inventory
        Item item = stack.getItem();

        // Check if there's something suitable to place onto
        if (!(
            item instanceof ItemBucket ||
                item instanceof ItemBoat ||
                item instanceof ItemBucketEmpty ||
                item instanceof ItemEgg ||
                item instanceof ItemSnowball
        ) && !canDeployOnBlock(stack, turtle, position, direction, true, outErrorMessage)) {
            return stack;
        }

        // Do the deploying (put everything in the players inventory)
        boolean placed = false;
        TileEntity existingTile = turtle.getWorld()
            .getTileEntity( position.x, position.y, position.z );

        TileEntityActivator tileEntityActivator = new TileEntityActivator();

        tileEntityActivator.x = turtlePosition.x;
        tileEntityActivator.y = turtlePosition.y;
        tileEntityActivator.z = turtlePosition.z;

        tileEntityActivator.worldObj = turtle.getWorld();

        int oldStackSize = stack.stackSize;

        item.onUseByActivator(stack, tileEntityActivator, turtle.getWorld(), turtle.getWorld().rand, turtlePosition.x, turtlePosition.y, turtlePosition.z, hitX, hitY, hitZ, direction);

        if( oldStackSize != stack.stackSize )
        {
            placed = true;
        }

        if (!placed && item instanceof ItemSign) {
            placeSign(stack, turtle.getWorld(), turtlePosition.x, turtlePosition.y, turtlePosition.z, direction.getSide(), turtle.getDirection());
        }

        if( oldStackSize != stack.stackSize )
        {
            placed = true;
        }

        // Set text on signs
        if( placed && item instanceof ItemSign)
        {
            if( extraArguments != null && extraArguments.length >= 1 && extraArguments[0] instanceof String )
            {
                World world = turtle.getWorld();
                TileEntity tile = world.getTileEntity( position.x, position.y, position.z );
                if( tile == null || tile == existingTile )
                {
                    tile = world.getTileEntity( position.x, position.y, position.z );
                }
                if( tile instanceof TileEntitySign)
                {
                    TileEntitySign signTile = (TileEntitySign) tile;
                    String s = (String) extraArguments[0];
                    String[] split = s.split( "\n" );
                    int firstLine = split.length <= 2 ? 1 : 0;
                    for( int i = 0; i < 4; i++ )
                    {
                        if( i >= firstLine && i < firstLine + split.length )
                        {
                            if( split[i - firstLine].length() > 15 )
                            {
                                signTile.signText[i] = split[i - firstLine].substring( 0, 15 );
                            }
                            else
                            {
                                signTile.signText[i] = split[i - firstLine];
                            }
                        }
                        else
                        {
                            signTile.signText[i] = "" ;
                        }
                    }
                    signTile.setChanged();
                }
            }
        }

        // Put everything we collected into the turtles inventory, then return
        if (stack.stackSize == 0) {
            return null;
        }

        return stack.copy();
    }

    static private boolean placeSign(
        ItemStack itemstack, World world, int blockX, int blockY, int blockZ, Side side, Direction direction
    ) {
        int sideHit = side.getOpposite().getId();
        if (!world.getBlockMaterial(blockX, blockY, blockZ).isSolid()) {
            return false;
        } else {
            if (!world.canPlaceInsideBlock(blockX, blockY, blockZ)) {
                blockX += side.getOffsetX();
                blockY += side.getOffsetY();
                blockZ += side.getOffsetZ();
            }

            if (!world.getBlockMaterial(
                blockX + side.getOffsetX(),
                blockY + side.getOffsetY(),
                blockZ + side.getOffsetZ()
            ).isSolid()) {
                sideHit = side.getId();
            }

            if (blockY < 0 || blockY >= world.getHeightBlocks()) {
                return false;
            } else if (!Blocks.SIGN_POST_PLANKS_OAK.canPlaceBlockAt(world, blockX, blockY, blockZ)) {
                return false;
            } else {
                if (sideHit == Side.TOP.getId() || sideHit == Side.BOTTOM.getId()) {
                    world.playBlockSoundEffect(
                        null,
                        (double)((float)blockX + 0.5F),
                        (double)((float)blockY + 0.5F),
                        (double)((float)blockZ + 0.5F),
                        Blocks.SIGN_POST_PLANKS_OAK,
                        EnumBlockSoundEffectType.PLACE
                    );
                    world.setBlockAndMetadataWithNotify(
                        blockX, blockY, blockZ, Blocks.SIGN_POST_PLANKS_OAK.id(), MathHelper.floor((double)((DirectionUtil.getRotationYaw(direction) + 180.0F) * 16.0F / 360.0F) + 0.5) & 15
                    );
                } else {
                    world.playBlockSoundEffect(
                        null,
                        (double)((float)blockX + 0.5F),
                        (double)((float)blockY + 0.5F),
                        (double)((float)blockZ + 0.5F),
                        Blocks.SIGN_WALL_PLANKS_OAK,
                        EnumBlockSoundEffectType.PLACE
                    );
                    world.setBlockAndMetadataWithNotify(blockX, blockY, blockZ, Blocks.SIGN_WALL_PLANKS_OAK.id(), sideHit);
                }

                itemstack.consumeItem(null);

                return true;
            }
        }
    }

    private static boolean canDeployOnBlock(ItemStack item, ITurtleAccess turtle, BlockPos position,
                                            Direction side, boolean allowReplaceable, String[] outErrorMessage) {
        if (position.getY() < 0 || position.getY() >= World.HEIGHT_BLOCKS) {
            return false;
        }

        World world = turtle.getWorld();

        if (!(item.getItem() instanceof ItemBlock || item.getItem() instanceof ItemSign)) {
            return false;
        }

        if (world.isAirBlock(position.x, position.y, position.z)) {
            return true;
        }

        return world.getBlockMaterial(position.x, position.y, position.z).isReplaceable();

//        if (ComputerCraft.turtlesObeyBlockProtection) {
//            // Check spawn protection
//            boolean editable = replaceable ? TurtlePermissions.isBlockEditable(world, position) : TurtlePermissions.isBlockEditable(world,
//                position.offset(
//                    side));
//            if (!editable) {
//                if (outErrorMessage != null) {
//                    outErrorMessage[0] = "Cannot place in protected area";
//                }
//                return false;
//            }
//        }
    }
}
