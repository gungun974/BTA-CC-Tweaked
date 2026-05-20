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
import dan200.computercraft.shared.computer.items.ItemBlockComputerBase;
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
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static dan200.computercraft.shared.turtle.core.TurtleBrain.*;

public class ItemBlockTurtle extends ItemBlockComputerBase implements ITurtleItem {
    public ItemBlockTurtle(@NotNull Block block) {
        super(block);
    }

    public void addToCreativeMenu(@NotNull List<ItemStack> list) {
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

    @Override
    public ITurtleUpgrade getUpgrade(@NotNull ItemStack stack, @NotNull TurtleSide side) {
        CompoundTag tag = stack.getData();

        String key = side == TurtleSide.LEFT ? NBT_LEFT_UPGRADE : NBT_RIGHT_UPGRADE;
        return tag.containsKey(key) ? TurtleUpgrades.get(tag.getInteger(key)) : null;
    }

    @Override
    public int getFuelLevel(@NotNull ItemStack stack) {
        CompoundTag tag = stack.getData();
        return tag.containsKey(NBT_FUEL) ? tag.getInteger(NBT_FUEL) : 0;
    }

    @Override
    public int getOverlay(@NotNull ItemStack stack) {
        CompoundTag tag = stack.getData();
        return tag.containsKey(NBT_OVERLAY) ? tag.getInteger(NBT_OVERLAY) : -1;
    }

    @Override
    public ItemStack withFamily(@NotNull ItemStack stack, @NotNull ComputerFamily family) {
        return TurtleItemFactory.create(getComputerID(stack), getLabel(stack), getColour(stack),
            family, getUpgrade(stack, TurtleSide.LEFT),
            getUpgrade(stack, TurtleSide.RIGHT), getFuelLevel(stack), getOverlay(stack));
    }

    @Override
    public boolean onUseOnBlock(@NotNull ItemStack selfStack, @NotNull World world, @Nullable Player player, @NotNull TilePosc blockPos, @NotNull Side side, double xHit, double yHit) {
        TilePos tilePos = new TilePos(blockPos);

        if (selfStack.stackSize <= 0) {
            return false;
        } else {
            if (!world.canPlaceInsideBlock(tilePos)) {
                tilePos.x += side.offsetX();
                tilePos.y += side.offsetY();
                tilePos.z += side.offsetZ();
            }

            if (tilePos.y >= 0 && tilePos.y < world.getHeightBlocks()) {
                if (world.canBlockIdBePlacedAt(this.block.id(), tilePos, false, side) && selfStack.consumeItem(player)) {
                    int meta = this.getPlacedData(selfStack, world, player, tilePos, side, xHit, yHit);
                    if (world.setBlockTypeDataNotify(tilePos, this.block, meta)) {
                        if (player == null) {
                            this.block.onPlacedOnSide(world, tilePos, side, xHit, yHit);
                        } else {
                            this.block.onPlacedByMob(world, tilePos, side, player, xHit, yHit);
                        }

                        world.playBlockSoundEffect(
                            player, (float) tilePos.x + 0.5F, (float) tilePos.y + 0.5F, (float) tilePos.z + 0.5F, this.block, EnumBlockSoundEffectType.PLACE
                        );

                        TileEntity entity = (world.getTileEntity(tilePos));
                        if (!(entity instanceof TileTurtle turtle)) {
                            return false;
                        }

                        turtle.readDescription(selfStack.getData());

                        // Set label
                        String label = selfStack.getCustomName();
                        if (label != null) {
                            turtle.setLabel(label);
                        }

                        if (player != null) {
                            turtle.setOwningPlayer(player.uuid);
                        }

                        if (selfStack.getItem() instanceof ITurtleItem item) {

                            // Set Upgrades
                            for (TurtleSide turtleSide : TurtleSide.values()) {
                                turtle.getAccess()
                                    .setUpgrade(turtleSide, item.getUpgrade(selfStack, turtleSide));
                            }

                            turtle.getAccess()
                                .setFuelLevel(item.getFuelLevel(selfStack));

                            // Set colour
                            int colour = item.getColour(selfStack);
                            if (colour != -1) {
                                turtle.getAccess()
                                    .setColour(colour);
                            }

                            // Set overlay
                            int overlay = item.getOverlay(selfStack);
                            if (overlay != -1) {
                                ((TurtleBrain) turtle.getAccess()).setOverlay(overlay);
                            }
                        }

                        return true;
                    }

                    if (player == null || player.getGamemode().hasBlockConsumption()) {
                        selfStack.stackSize++;
                    }
                }

                return false;
            } else {
                return false;
            }
        }
    }
}
