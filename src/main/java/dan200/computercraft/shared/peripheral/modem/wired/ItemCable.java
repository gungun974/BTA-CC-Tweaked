/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.modem.wired;

import dan200.computercraft.shared.common.ComputerCraftBlocks;
import dan200.computercraft.shared.util.BlockPos;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

import static dan200.computercraft.shared.peripheral.modem.wired.BlockLogicCable.correctConnections;

public abstract class ItemCable extends Item {
    public ItemCable(NamespaceID namespaceId, int id) {
        super(namespaceId, id);
    }


    boolean placeAtCorrected(World world, BlockPos pos, TileCable state, CableModemVariant blockStateModem, boolean blockStateCable) {
        boolean original = state.blockStateCable;
        state.blockStateCable = blockStateCable;
        correctConnections(world, pos, state);
        state.blockStateCable = original;


        return placeAt(world, pos, null, blockStateModem, blockStateCable);
    }

    boolean placeAt(World world, BlockPos pos, Player player, CableModemVariant blockStateModem, boolean blockStateCable) {
        if (pos.y >= 0 && pos.y < world.getHeightBlocks()) {
            int currentId = world.getBlockId(pos.x, pos.y, pos.z);

            if (currentId == ComputerCraftBlocks.CABLE.id()) {
                TileEntity tileEntity = world.getTileEntity(pos.x, pos.y, pos.z);

                if (tileEntity instanceof TileCable) {
                    TileCable cable = (TileCable) tileEntity;

                    if (cable.blockStateCable == blockStateCable && cable.blockStateModem == blockStateModem) {
                        return false;
                    }

                    world.notifyBlockChange(pos.x, pos.y, pos.z, ComputerCraftBlocks.CABLE.id());

                    world.playBlockSoundEffect(player, (float) pos.x + 0.5F, (float) pos.y + 0.5F, (float) pos.z + 0.5F, ComputerCraftBlocks.CABLE, EnumBlockSoundEffectType.PLACE);

                    cable.blockStateModem = blockStateModem;
                    cable.blockStateCable = blockStateCable;

                    cable.modemChanged();
                    cable.connectionsChanged();

                    cable.updatePlacementState();
                }

                return true;
            }


            if (world.canBlockBePlacedAt(ComputerCraftBlocks.CABLE.id(), pos.x, pos.y, pos.z, false, Side.NORTH)) {
                if (world.setBlockAndMetadataWithNotify(pos.x, pos.y, pos.z, ComputerCraftBlocks.CABLE.id(), 0)) {
                    if (player == null) {
                        ComputerCraftBlocks.CABLE.onBlockPlacedOnSide(world, pos.x, pos.y, pos.z, Side.NORTH, 0, 0);
                    } else {
                        ComputerCraftBlocks.CABLE.onBlockPlacedByMob(world, pos.x, pos.y, pos.z, Side.NORTH, player, 0, 0);
                    }

                    world.playBlockSoundEffect(player, (float) pos.x + 0.5F, (float) pos.y + 0.5F, (float) pos.z + 0.5F, ComputerCraftBlocks.CABLE, EnumBlockSoundEffectType.PLACE);

                    TileEntity tileEntity = world.getTileEntity(pos.x, pos.y, pos.z);

                    if (tileEntity instanceof TileCable) {
                        TileCable cable = (TileCable) tileEntity;

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
        public WiredModem(NamespaceID namespaceId, int id) {
            super(namespaceId, id);
        }

        @Override
        public boolean onUseItemOnBlock(ItemStack stack, Player player, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
            if (stack.stackSize <= 0) {
                return false;
            }

            BlockPos pos = new BlockPos(blockX, blockY, blockZ);

            BlockPos insidePos = pos.offset(side.getDirection());

            TileEntity tileEntity = world.getTileEntity(insidePos.x, insidePos.y, insidePos.z);

            Direction direction = side.getDirection().getOpposite();

            if (tileEntity instanceof TileCable) {
                TileCable state = (TileCable) tileEntity;

                // Try to add a modem to a cable
                if (state.blockStateModem == CableModemVariant.None) {

                    if (placeAt(world, insidePos, player, CableModemVariant.from(direction), state.blockStateCable)) {
                        stack.consumeItem(player);
                        return true;
                    }
                }

                return false;
            }

            if (placeAt(world, insidePos, player, CableModemVariant.from(direction), false)) {
                stack.consumeItem(player);
                return true;
            }

            return false;
        }
    }

    public static class Cable extends ItemCable {
        public Cable(NamespaceID namespaceId, int id) {
            super(namespaceId, id);
        }

        @Override
        public boolean onUseItemOnBlock(ItemStack stack, Player player, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
            if (stack.stackSize <= 0) {
                return false;
            }

            BlockPos pos = new BlockPos(blockX, blockY, blockZ);

            // Try to add a cable to a modem inside the block we're clicking on.
            BlockPos insidePos = pos.offset(side.getDirection());

            TileEntity insideTileEntity = world.getTileEntity(insidePos.x, insidePos.y, insidePos.z);

            if (insideTileEntity instanceof TileCable) {
                TileCable state = (TileCable) insideTileEntity;

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

            TileEntity tileEntity = world.getTileEntity(pos.x, pos.y, pos.z);

            if (tileEntity instanceof TileCable) {
                TileCable state = (TileCable) tileEntity;

                if (!state.blockStateCable) {
                    return placeAtCorrected(world,
                        pos,
                        state,
                        state.blockStateModem,
                        true
                    );
                }
            }

            if (placeAt(world, insidePos, player, CableModemVariant.None, true)) {
                stack.consumeItem(player);
                return true;
            }

            return false;
        }
    }
}
