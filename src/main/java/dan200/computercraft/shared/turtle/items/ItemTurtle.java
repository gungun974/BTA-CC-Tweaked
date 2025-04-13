/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.items;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.shared.TurtleUpgrades;
import dan200.computercraft.shared.common.IColouredItem;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.items.ItemComputerBase;
import dan200.computercraft.shared.turtle.blocks.TileTurtle;
import dan200.computercraft.shared.turtle.core.TurtleBrain;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

import static dan200.computercraft.shared.turtle.core.TurtleBrain.*;

public class ItemTurtle extends ItemComputerBase implements ITurtleItem {
    public ItemTurtle(@NotNull Block block) {
        super(block);
    }

    public void addToCreativeMenu(@Nonnull List<ItemStack> list) {
        ComputerFamily family = getFamily();

        list.add(create(-1, null, -1, null, null, 0, -1));
        TurtleUpgrades.getVanillaUpgrades()
            .filter(x -> TurtleUpgrades.suitableForFamily(family, x))
            .map(x -> create(-1, null, -1, null, x, 0, -1))
            .forEach(list::add);
    }

    public ItemStack create(int id, String label, int colour, ITurtleUpgrade leftUpgrade, ITurtleUpgrade rightUpgrade, int fuelLevel, int overlay) {
        // Build the stack
        ItemStack stack = new ItemStack(this);
        if (label != null) {
            stack.setCustomName(label);
        }

        CompoundTag tag = stack.getData();

        if (id >= 0) {
            tag
                .putInt(NBT_ID, id);
        }
        IColouredItem.setColourBasic(stack, colour);
        if (fuelLevel > 0) {
            tag
                .putInt(NBT_FUEL, fuelLevel);
        }
        if (overlay != -1) {
            tag
                .putInt(NBT_OVERLAY, overlay);
        }

        if (leftUpgrade != null) {
            tag
                .putInt(NBT_LEFT_UPGRADE,
                    leftUpgrade.getUpgradeID()
                );
        }

        if (rightUpgrade != null) {
            tag
                .putInt(NBT_RIGHT_UPGRADE,
                    rightUpgrade.getUpgradeID()
                );
        }

        stack.setData(tag);

        return stack;
    }

    public String getTranslatedName(ItemStack itemStack) {
        I18n i18n = I18n.getInstance();
        String baseString = itemStack.getItemKey();
        ITurtleUpgrade left = getUpgrade(itemStack, TurtleSide.LEFT);
        ITurtleUpgrade right = getUpgrade(itemStack, TurtleSide.RIGHT);
        if (left != null && right != null) {
            return i18n.translateKeyAndFormat(baseString + ".upgraded_twice.name",
                i18n.translateKey(right.getUnlocalisedAdjective()),
                i18n.translateKey(left.getUnlocalisedAdjective()));
        } else if (left != null) {
            return i18n.translateKeyAndFormat(baseString + ".upgraded.name", i18n.translateKey(left.getUnlocalisedAdjective()));
        } else if (right != null) {
            return i18n.translateKeyAndFormat(baseString + ".upgraded.name", i18n.translateKey(right.getUnlocalisedAdjective()));
        } else {
            return i18n.translateKey(baseString + ".name");
        }
    }

    //
//    //    @Nullable
//    //    @Override
//    //    public String getCreatorModId( ItemStack stack )
//    //    {
//    //        // Determine our "creator mod" from the upgrades. We attempt to find the first non-vanilla/non-CC
//    //        // upgrade (starting from the left).
//    //
//    //        ITurtleUpgrade left = getUpgrade( stack, TurtleSide.LEFT );
//    //        if( left != null )
//    //        {
//    //            String mod = TurtleUpgrades.getOwner( left );
//    //            if( mod != null && !mod.equals( ComputerCraft.MOD_ID ) ) return mod;
//    //        }
//    //
//    //        ITurtleUpgrade right = getUpgrade( stack, TurtleSide.RIGHT );
//    //        if( right != null )
//    //        {
//    //            String mod = TurtleUpgrades.getOwner( right );
//    //            if( mod != null && !mod.equals( ComputerCraft.MOD_ID ) ) return mod;
//    //        }
//    //
//    //        return super.getCreatorModId( stack );
//    //    }
//
    @Override
    public ITurtleUpgrade getUpgrade(@Nonnull ItemStack stack, @Nonnull TurtleSide side) {
        CompoundTag tag = stack.getData();

        String key = side == TurtleSide.LEFT ? NBT_LEFT_UPGRADE : NBT_RIGHT_UPGRADE;
        return tag.containsKey(key) ? TurtleUpgrades.get(tag.getInteger(key)) : null;
    }

    @Override
    public int getFuelLevel(@Nonnull ItemStack stack) {
        CompoundTag tag = stack.getData();
        return tag.containsKey(NBT_FUEL) ? tag.getInteger(NBT_FUEL) : 0;
    }

    @Override
    public int getOverlay(@Nonnull ItemStack stack) {
        CompoundTag tag = stack.getData();
        return tag.containsKey(NBT_OVERLAY) ? tag.getInteger(NBT_OVERLAY) : -1;
    }

    @Override
    public ItemStack withFamily(@Nonnull ItemStack stack, @Nonnull ComputerFamily family) {
        return TurtleItemFactory.create(getComputerID(stack), getLabel(stack), getColour(stack),
            family, getUpgrade(stack, TurtleSide.LEFT),
            getUpgrade(stack, TurtleSide.RIGHT), getFuelLevel(stack), getOverlay(stack));
    }

    @Override
    public boolean onUseItemOnBlock(ItemStack stack, @Nullable Player player, World world, int x, int y, int z, Side side, double xPlaced, double yPlaced) {
        if (stack.stackSize <= 0) {
            return false;
        } else {
            if (!world.canPlaceInsideBlock(x, y, z)) {
                x += side.getOffsetX();
                y += side.getOffsetY();
                z += side.getOffsetZ();
            }

            if (y >= 0 && y < world.getHeightBlocks()) {
                if (world.canBlockBePlacedAt(this.block.id(), x, y, z, false, side) && stack.consumeItem(player)) {
                    int meta = this.getPlacedBlockMetadata(player, stack, world, x, y, z, side, xPlaced, yPlaced);
                    if (world.setBlockAndMetadataWithNotify(x, y, z, this.block.id(), meta)) {
                        if (player == null) {
                            this.block.onBlockPlacedOnSide(world, x, y, z, side, xPlaced, yPlaced);
                        } else {
                            this.block.onBlockPlacedByMob(world, x, y, z, side, player, xPlaced, yPlaced);
                        }

                        world.playBlockSoundEffect(
                            player, (float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F, this.block, EnumBlockSoundEffectType.PLACE
                        );

                        TileEntity entity = (world.getTileEntity(x, y, z));
                        if (!(entity instanceof TileTurtle)) {
                            return false;
                        }

                        TileTurtle turtle = (TileTurtle) entity;

                        turtle.readDescription(stack.getData());

                        if (player != null) {
                            turtle.setOwningPlayer(player.uuid);
                        }

                        if (stack.getItem() instanceof ITurtleItem) {
                            ITurtleItem item = (ITurtleItem) stack.getItem();

                            // Set Upgrades
                            for (TurtleSide turtleSide : TurtleSide.values()) {
                                turtle.getAccess()
                                    .setUpgrade(turtleSide, item.getUpgrade(stack, turtleSide));
                            }

                            turtle.getAccess()
                                .setFuelLevel(item.getFuelLevel(stack));

                            // Set colour
                            int colour = item.getColour(stack);
                            if (colour != -1) {
                                turtle.getAccess()
                                    .setColour(colour);
                            }

                            // Set overlay
                            int overlay = item.getOverlay(stack);
                            if (overlay != -1) {
                                ((TurtleBrain) turtle.getAccess()).setOverlay(overlay);
                            }
                        }

                        return true;
                    }

                    if (player == null || player.getGamemode().consumeBlocks()) {
                        stack.stackSize++;
                    }
                }

                return false;
            } else {
                return false;
            }
        }
    }
}
