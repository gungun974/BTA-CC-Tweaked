/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.modem.wired;

import dan200.computercraft.shared.common.ComputerCraftBlocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityActivator;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

import static dan200.computercraft.shared.peripheral.modem.wired.BlockLogicCable.correctConnections;

public abstract class ItemCable extends Item {
    public ItemCable(@NotNull NamespaceID namespaceId, @NotNull String translationKey, int id) {
        super(namespaceId, translationKey, id);
    }


    boolean placeAtCorrected(World world, TilePosc pos, TileCable state, CableModemVariant blockStateModem, boolean blockStateCable) {
        boolean original = state.blockStateCable;
        state.blockStateCable = blockStateCable;
        correctConnections(world, pos, state);
        state.blockStateCable = original;


        return placeAt(world, pos, null, blockStateModem, blockStateCable);
    }

    boolean placeAt(World world, TilePosc pos, Player player, CableModemVariant blockStateModem, boolean blockStateCable) {
        if (pos.y() >= 0 && pos.y() < world.getHeightBlocks()) {
            int currentId = world.getBlockType(pos).id();

            if (currentId == ComputerCraftBlocks.CABLE.id()) {
                TileEntity tileEntity = world.getTileEntity(pos);

                if (tileEntity instanceof TileCable cable) {

                    if (cable.blockStateCable == blockStateCable && cable.blockStateModem == blockStateModem) {
                        return false;
                    }

                    world.notifyBlockChange(pos, ComputerCraftBlocks.CABLE);

                    world.playBlockSoundEffect(player, (float) pos.x() + 0.5F, (float) pos.y() + 0.5F, (float) pos.z() + 0.5F, ComputerCraftBlocks.CABLE, EnumBlockSoundEffectType.PLACE);

                    cable.blockStateModem = blockStateModem;
                    cable.blockStateCable = blockStateCable;

                    cable.modemChanged();
                    cable.connectionsChanged();

                    cable.updatePlacementState();
                }

                return true;
            }


            if (world.canBlockIdBePlacedAt(ComputerCraftBlocks.CABLE.id(), pos, false, Side.NORTH)) {
                if (world.setBlockTypeDataNotify(pos, ComputerCraftBlocks.CABLE, 0)) {
                    if (player == null) {
                        ComputerCraftBlocks.CABLE.onPlacedOnSide(world, pos, Side.NORTH, 0, 0);
                    } else {
                        ComputerCraftBlocks.CABLE.onPlacedByMob(world, pos, Side.NORTH, player, 0, 0);
                    }

                    world.playBlockSoundEffect(player, (float) pos.x() + 0.5F, (float) pos.y() + 0.5F, (float) pos.z() + 0.5F, ComputerCraftBlocks.CABLE, EnumBlockSoundEffectType.PLACE);

                    TileEntity tileEntity = world.getTileEntity(pos);

                    if (tileEntity instanceof TileCable cable) {

                        cable.blockStateModem = blockStateModem;
                        cable.blockStateCable = blockStateCable;


                        cable.modemChanged();
                        cable.connectionsChanged();

                        cable.updatePlacementState();
                    }

                    return true;
                }
            }

        }
        return false;
    }

    public static class WiredModem extends ItemCable {
        public WiredModem(@NotNull NamespaceID namespaceId, @NotNull String translationKey, int id) {
            super(namespaceId, translationKey, id);
        }

        @Override
        public boolean onUseOnBlock(@NotNull ItemStack selfStack, @NotNull World world, @Nullable Player player, @NotNull TilePosc blockPos, @NotNull Side side, double xHit, double yHit) {
            if (selfStack.stackSize <= 0) {
                return false;
            }

            TilePosc insidePos = blockPos.add(side.direction(), new TilePos());

            TileEntity tileEntity = world.getTileEntity(insidePos);

            Direction direction = side.direction().opposite();

            if (tileEntity instanceof TileCable state) {

                // Try to add a modem to a cable
                if (state.blockStateModem == CableModemVariant.None) {

                    if (placeAt(world, insidePos, player, CableModemVariant.from(direction), state.blockStateCable)) {
                        selfStack.consumeItem(player);
                        return true;
                    }
                }

                return false;
            }

            if (placeAt(world, insidePos, player, CableModemVariant.from(direction), false)) {
                selfStack.consumeItem(player);
                return true;
            }

            return false;
        }
    }

    public static class Cable extends ItemCable {
        public Cable(@NotNull NamespaceID namespaceId, @NotNull String translationKey, int id) {
            super(namespaceId, translationKey, id);
        }

        @Override
        public boolean onUseOnBlock(@NotNull ItemStack selfStack, @NotNull World world, @Nullable Player player, @NotNull TilePosc blockPos, @NotNull Side side, double xHit, double yHit) {
            if (selfStack.stackSize <= 0) {
                return false;
            }

            // Try to add a cable to a modem inside the block we're clicking on.
            TilePosc insidePos = blockPos.add(side.direction(), new TilePos());

            TileEntity insideTileEntity = world.getTileEntity(insidePos);

            if (insideTileEntity instanceof TileCable state) {

                if (!state.blockStateCable) {
                    return placeAtCorrected(world,
                        insidePos,
                        state,
                        state.blockStateModem,
                        true
                    );
                }

                return false;
            }

            TileEntity tileEntity = world.getTileEntity(blockPos);

            if (tileEntity instanceof TileCable state) {

                if (!state.blockStateCable) {
                    return placeAtCorrected(world,
                        blockPos,
                        state,
                        state.blockStateModem,
                        true
                    );
                }
            }

            if (placeAt(world, insidePos, player, CableModemVariant.None, true)) {
                selfStack.consumeItem(player);
                return true;
            }

            return false;
        }

        @Override
        public void onUseByActivator(@NotNull ItemStack selfStack, @NotNull World world, @NotNull TileEntityActivator activator, @NotNull Random random, @NotNull TilePosc blockPos, @NotNull Direction direction, double offX, double offY, double offZ) {
            if (selfStack.stackSize <= 0) {
                return;
            }

            // Try to add a cable to a modem inside the block we're clicking on.
            TilePosc insidePos = blockPos.add(direction, new TilePos());

            TileEntity insideTileEntity = world.getTileEntity(insidePos);

            if (insideTileEntity instanceof TileCable state) {

                if (!state.blockStateCable) {
                    placeAtCorrected(world,
                        insidePos,
                        state,
                        state.blockStateModem,
                        true
                    );
                    world.notifyBlockChange(blockPos, world.getBlockType(blockPos));
                }
                return;
            }

            TileEntity tileEntity = world.getTileEntity(blockPos);

            if (tileEntity instanceof TileCable state) {

                if (!state.blockStateCable) {
                    placeAtCorrected(world,
                        blockPos,
                        state,
                        state.blockStateModem,
                        true
                    );
                    world.notifyBlockChange(blockPos, world.getBlockType(blockPos));
                }
            }

            if (placeAt(world, insidePos, null, CableModemVariant.None, true)) {
                --selfStack.stackSize;
            }

            world.notifyBlockChange(blockPos, world.getBlockType(blockPos));
        }
    }
}
