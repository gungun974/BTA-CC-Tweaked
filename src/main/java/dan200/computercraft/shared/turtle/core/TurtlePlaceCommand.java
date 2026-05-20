/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.core;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleCommand;
import dan200.computercraft.api.turtle.TurtleAnimation;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.event.TurtleBlockEvent;
import dan200.computercraft.api.turtle.event.TurtleEvent;
import dan200.computercraft.shared.TurtlePermissions;
import dan200.computercraft.shared.peripheral.modem.wired.ItemCable;
import dan200.computercraft.shared.util.DirectionUtil;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityActivator;
import net.minecraft.core.block.entity.TileEntitySign;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.item.*;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

public class TurtlePlaceCommand implements ITurtleCommand {
    private final InteractDirection direction;
    private final Object[] extraArguments;

    public TurtlePlaceCommand(InteractDirection direction, Object[] arguments) {
        this.direction = direction;
        extraArguments = arguments;
    }

    public static ItemStack deploy(@NotNull ItemStack stack, ITurtleAccess turtle, Direction direction,
                                   Object[] extraArguments, String[] outErrorMessage) {
        TilePosc turtlePosition = turtle.getPosition();
        TilePosc position = turtlePosition.add(direction, new TilePos());

        // Calculate where the turtle would hit the block
        float hitX = 0.5f + direction.offsetX() * 0.5f;
        float hitY = 0.5f + direction.offsetY() * 0.5f;
        float hitZ = 0.5f + direction.offsetZ() * 0.5f;
        if (Math.abs(hitY - 0.5f) < 0.01f) {
            hitY = 0.45f;
        }

        // Load up the turtle's inventory
        Item item = stack.getItem();

        // Check if there's something suitable to place onto
        if (!(
            item instanceof ItemBucket ||
                item instanceof ItemBoat ||
                item instanceof ItemEgg ||
                item instanceof ItemSnowball ||
                item instanceof ItemCable ||
                item instanceof ItemSeeds ||
                item instanceof ItemDye
        ) && !canDeployOnBlock(stack, turtle, position, direction, true, outErrorMessage)) {
            return stack;
        }

        // Do the deploying (put everything in the players inventory)
        boolean placed = false;
        TileEntity existingTile = turtle.getWorld()
            .getTileEntity(position);

        TileEntityActivator tileEntityActivator = new TileEntityActivator();

        tileEntityActivator.tilePos.x = turtlePosition.x();
        tileEntityActivator.tilePos.y = turtlePosition.y();
        tileEntityActivator.tilePos.z = turtlePosition.z();

        tileEntityActivator.worldObj = turtle.getWorld();

        int oldStackSize = stack.stackSize;

        item.onUseByActivator(stack, turtle.getWorld(), tileEntityActivator, turtle.getWorld().rand, turtlePosition, direction, hitX, hitY, hitZ);

        if (oldStackSize != stack.stackSize) {
            placed = true;
        }

        if (!placed && item instanceof ItemSign) {
            placeSign(stack, turtle.getWorld(), turtlePosition.x(), turtlePosition.y(), turtlePosition.z(), direction.side(), turtle.getDirection());
        }

        if (oldStackSize != stack.stackSize) {
            placed = true;
        }

        // Set text on signs
        if (placed && item instanceof ItemSign) {
            if (extraArguments != null && extraArguments.length >= 1 && extraArguments[0] instanceof String s) {
                World world = turtle.getWorld();
                TileEntity tile = world.getTileEntity(position);
                if (tile == null || tile == existingTile) {
                    tile = world.getTileEntity(position);
                }
                if (tile instanceof TileEntitySign signTile) {
                    String[] split = s.split("\n");
                    int firstLine = split.length <= 2 ? 1 : 0;
                    for (int i = 0; i < 4; i++) {
                        if (i >= firstLine && i < firstLine + split.length) {
                            if (split[i - firstLine].length() > 15) {
                                signTile.signText[i] = split[i - firstLine].substring(0, 15);
                            } else {
                                signTile.signText[i] = split[i - firstLine];
                            }
                        } else {
                            signTile.signText[i] = "";
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

    static private void placeSign(
        ItemStack itemstack, World world, int blockX, int blockY, int blockZ, Side side, Direction direction
    ) {
        int sideHit = side.opposite().id;
        if (world.getBlockMaterial(new TilePos(blockX, blockY, blockZ)).isSolid()) {
            if (!world.canPlaceInsideBlock(new TilePos(blockX, blockY, blockZ))) {
                blockX += side.offsetX();
                blockY += side.offsetY();
                blockZ += side.offsetZ();
            }

            if (!world.getBlockMaterial(new TilePos(
                    blockX + side.offsetX(),
                    blockY + side.offsetY(),
                    blockZ + side.offsetZ()
                )
            ).isSolid()) {
                sideHit = side.id;
            }

            if (blockY < 0 || blockY >= world.getHeightBlocks()) {
            } else if (!Blocks.SIGN_POST_PLANKS_OAK.canPlaceAt(world, new TilePos(blockX, blockY, blockZ))) {
            } else {
                if (sideHit == Side.TOP.id || sideHit == Side.BOTTOM.id) {
                    world.playBlockSoundEffect(
                        null,
                        (float) blockX + 0.5F,
                        (float) blockY + 0.5F,
                        (float) blockZ + 0.5F,
                        Blocks.SIGN_POST_PLANKS_OAK,
                        EnumBlockSoundEffectType.PLACE
                    );
                    world.setBlockTypeDataNotify(
                        new TilePos(blockX, blockY, blockZ), Blocks.SIGN_POST_PLANKS_OAK, MathHelper.floor((double) ((DirectionUtil.getRotationYaw(direction) + 180.0F) * 16.0F / 360.0F) + 0.5) & 15
                    );
                } else {
                    world.playBlockSoundEffect(
                        null,
                        (float) blockX + 0.5F,
                        (float) blockY + 0.5F,
                        (float) blockZ + 0.5F,
                        Blocks.SIGN_WALL_PLANKS_OAK,
                        EnumBlockSoundEffectType.PLACE
                    );
                    world.setBlockTypeDataNotify(new TilePos(blockX, blockY, blockZ), Blocks.SIGN_WALL_PLANKS_OAK, sideHit);
                }

                itemstack.consumeItem(null);

            }
        }
    }

    private static boolean canDeployOnBlock(ItemStack item, ITurtleAccess turtle, TilePosc position,
                                            Direction side, boolean allowReplaceable, String[] outErrorMessage) {
        if (position.y() < 0 || position.y() >= World.HEIGHT_BLOCKS) {
            return false;
        }

        World world = turtle.getWorld();

        if (!(item.getItem() instanceof ItemBlock || item.getItem() instanceof ItemSign)) {
            return false;
        }

        if (world.isAirBlock(position)) {
            return true;
        }

        if (ComputerCraft.turtlesObeyBlockProtection) {
            // Check spawn protection
            boolean editable = allowReplaceable ? TurtlePermissions.isBlockEditable(world, position) : TurtlePermissions.isBlockEditable(world,
                position.add(side, new TilePos()));
            if (!editable) {
                if (outErrorMessage != null) {
                    outErrorMessage[0] = "Cannot place in protected area";
                }
                return false;
            }
        }

        return world.getBlockMaterial(position).isReplaceable();
    }

    @NotNull
    @Override
    public TurtleCommandResult execute(@NotNull ITurtleAccess turtle) {
        // Get thing to place
        ItemStack stack = turtle.getInventory()
            .getItem(turtle.getSelectedSlot());
        if (stack == null) {
            return TurtleCommandResult.failure("No items to place");
        }

        // Remember old block
        Direction direction = this.direction.toWorldDir(turtle);
        TilePosc coordinates = turtle.getPosition()
            .add(direction, new TilePos());

        TurtleBlockEvent.Place place = new TurtleBlockEvent.Place(turtle, turtle.getWorld(), coordinates, stack);
        if (TurtleEvent.post(place)) {
            return TurtleCommandResult.failure(place.getFailureMessage());
        }

        // Do the deploying
        String[] errorMessage = new String[1];
        ItemStack remainder = deploy(stack, turtle, direction, extraArguments, errorMessage);
        if (remainder != stack) {
            // Put the remaining items back
            turtle.getInventory()
                .setItem(turtle.getSelectedSlot(), remainder);
            turtle.getInventory()
                .setChanged();

            // Animate and return success
            turtle.playAnimation(TurtleAnimation.WAIT);
            return TurtleCommandResult.success();
        } else {
            if (errorMessage[0] != null) {
                return TurtleCommandResult.failure(errorMessage[0]);
            } else if (stack.getItem() instanceof ItemBlock) {
                return TurtleCommandResult.failure("Cannot place block here");
            } else {
                return TurtleCommandResult.failure("Cannot place item here");
            }
        }
    }
}
