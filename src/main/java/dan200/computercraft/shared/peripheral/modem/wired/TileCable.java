/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.modem.wired;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.shared.util.BlockPos;
import dan200.computercraft.shared.util.PortableTickScheduler;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.network.wired.IWiredElement;
import dan200.computercraft.api.network.wired.IWiredNode;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.fabric.Helper;
import dan200.computercraft.shared.common.ComputerCraftItems;
import dan200.computercraft.shared.common.TileGeneric;
import dan200.computercraft.shared.peripheral.modem.ModemState;
import dan200.computercraft.shared.util.DirectionUtil;
import dan200.computercraft.shared.util.TickScheduler;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class TileCable extends TileGeneric implements IPeripheralTile {
    private static final String NBT_PERIPHERAL_ENABLED = "PeirpheralAccess";
    private final WiredModemLocalPeripheral peripheral = new WiredModemLocalPeripheral();
    private final WiredModemElement cable = new CableElement();
    private final IWiredNode node = cable.getNode();
    public CableModemVariant blockStateModem = CableModemVariant.None;
    public boolean blockStateCable = false;
    public boolean blockStateNorth = false;
    public boolean blockStateSouth = false;
    public boolean blockStateEast = false;
    public boolean blockStateWest = false;
    public boolean blockStateUp = false;
    public boolean blockStateDown = false;
    protected PortableTickScheduler portableTickScheduler = new PortableTickScheduler();
    private boolean peripheralAccessAllowed;
    private final boolean destroyed = false;
    private Direction modemDirection = Direction.NORTH;
    private final WiredModemPeripheral modem = new WiredModemPeripheral(new ModemState(() -> TickScheduler.schedule(this)), cable) {
        @Nonnull
        @Override
        protected WiredModemLocalPeripheral getLocalPeripheral() {
            return peripheral;
        }

        @Nonnull
        @Override
        public Vec3 getPosition() {
            BlockPos pos = getPos().offset(modemDirection);
            return Vec3.getPermanentVec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        }

        @Nonnull
        @Override
        public Object getTarget() {
            return TileCable.this;
        }
    };
    private boolean hasModemDirection = false;
    private boolean connectionsFormed = false;

    public TileCable() {
    }

    private BlockPos getPos() {
        return new BlockPos(x, y, z);
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        onRemove();
    }

    private void onRemove() {
        if (worldObj == null || !Helper.isClientWorld()) {
            node.remove();
            connectionsFormed = false;
        }
    }

    public boolean onBlockRightClicked(Player player, Side side, double xPlaced, double yPlaced) {
        if (player.isSneaking()) {
            return true;
        }
        if (!canAttachPeripheral()) {
            return false;
        }

        if (Helper.isClientWorld()) {
            return true;
        }

        String oldName = peripheral.getConnectedName();
        togglePeripheralAccess();
        String newName = peripheral.getConnectedName();
        if (!Objects.equals(newName, oldName)) {
            I18n i18n = I18n.getInstance();
            if (oldName != null) {
                player.sendMessage(i18n.translateKeyAndFormat("chat.computercraft.wired_modem.peripheral_disconnected", oldName));
            }
            if (newName != null) {
                player.sendMessage(i18n.translateKeyAndFormat("chat.computercraft.wired_modem.peripheral_connected", newName));
            }
        }

        return true;
    }

    public void onNeighbourChange(@Nonnull BlockPos neighbour) {
        if (hasModem() && !this.getBlock().getLogic().canPlaceBlockAt(worldObj, x, y, z)) {
            if (hasCable()) {
                // Drop the modem and convert to cable
                worldObj.dropItem(x, y, z, new ItemStack(ComputerCraftItems.WIRED_MODEM, 1));
                blockStateModem = CableModemVariant.None;
                modemChanged();
                connectionsChanged();
            } else {
                // Drop everything and remove block
                worldObj.dropItem(x, y, z, new ItemStack(ComputerCraftItems.WIRED_MODEM, 1));
                worldObj.setBlockWithNotify(x, y, z, 0);
                // This'll call #destroy(), so we don't need to reset the network here.
            }

            return;
        }

        if (Helper.isServerEnvironment() || Helper.isSinglePlayer()) {
            portableTickScheduler.scheduleOnNextEndTick(() -> onNeighbourTileEntityChange(neighbour));
        }
    }

    @Nonnull
    private Direction getDirection() {
        refreshDirection();
        return modemDirection == null ? Direction.NORTH : modemDirection;
    }

    public boolean hasModem() {
        return blockStateModem != CableModemVariant.None;
    }

    boolean hasCable() {

        return blockStateCable;
    }

    void modemChanged() {
        // Tell anyone who cares that the connection state has changed
        if (Helper.isClientWorld()) {
            return;
        }

        // If we can no longer attach peripherals, then detach any
        // which may have existed
        if (!canAttachPeripheral() && peripheralAccessAllowed) {
            peripheralAccessAllowed = false;
            peripheral.detach();
            node.updatePeripherals(Collections.emptyMap());
            //markDirty();
            updateBlockState();
        }
    }

    void connectionsChanged() {
        if (Helper.isClientWorld()) {
            return;
        }

        World world = worldObj;
        BlockPos current = getPos();
        for (Direction facing : DirectionUtil.FACINGS) {
            BlockPos offset = current.offset(facing);
            if (!world.isChunkLoaded(Math.floorDiv(offset.x, 16), Math.floorDiv(offset.z, 16))) {
                continue;
            }

            IWiredElement element = ComputerCraftAPI.getWiredElementAt(world, offset, facing.getOpposite());
            if (element != null) {
                // TODO Figure out why this crashes.
                IWiredNode node = element.getNode();
                if (node != null && this.node != null) {
                    if (BlockLogicCable.canConnectIn(this, facing)) {
                        // If we can connect to it then do so
                        this.node.connectTo(node);
                    } else if (this.node.getNetwork() == node.getNetwork()) {
                        // Otherwise if we're on the same network then attempt to void it.
                        this.node.disconnectFrom(node);
                    }
                }
            }
        }

        this.updatePlacementState();
    }

    public void updatePlacementState() {
        BlockLogicCable.correctConnections(this.worldObj, getPos(), this);

        final BlockLogic logic = getBlock().getLogic();

        if (logic instanceof BlockLogicCable) {
            ((BlockLogicCable) logic).updateBlockBoundsFromState(this.worldObj, this.x, this.y, this.z);
        }
    }

    private boolean canAttachPeripheral() {
        return hasCable() && hasModem();
    }

    private void updateBlockState() {
        CableModemVariant oldVariant = this.blockStateModem;
        CableModemVariant newVariant = CableModemVariant.from(oldVariant.getFacing(), modem.getModemState()
            .isOpen(), peripheralAccessAllowed);

        if (oldVariant != newVariant) {
            this.blockStateModem = newVariant;
            worldObj.notifyBlockChange(x, y, z, getBlockId());
        }
    }

    private void refreshPeripheral() {
        if (worldObj != null && !isInvalid() && peripheral.attach(worldObj, getPos(), getDirection())) {
            updateConnectedPeripherals();
        }
    }

    private void updateConnectedPeripherals() {
        Map<String, IPeripheral> peripherals = peripheral.toMap();
        if (peripherals.isEmpty()) {
            // If there are no peripherals then disable access and update the display state.
            peripheralAccessAllowed = false;
            updateBlockState();
        }

        node.updatePeripherals(peripherals);
    }

    public void onNeighbourTileEntityChange(@Nonnull BlockPos neighbour) {
        if (!Helper.isClientWorld() && peripheralAccessAllowed) {
            Direction facing = getDirection();
            refreshPeripheral();
        }

        this.updatePlacementState();
    }

    @Override
    public void tick() {
        super.tick();
        portableTickScheduler.tickAtStart();

        if (Helper.isClientWorld()) {
            portableTickScheduler.tickAtEnd();
            return;
        }

        refreshDirection();

        if (modem.getModemState()
            .pollChanged()) {
            updateBlockState();
        }

        if (!connectionsFormed) {
            connectionsFormed = true;

            connectionsChanged();
            if (peripheralAccessAllowed) {
                peripheral.attach(worldObj, getPos(), modemDirection);
                updateConnectedPeripherals();
            }
        }
        portableTickScheduler.tickAtEnd();
    }

    private void togglePeripheralAccess() {
        if (!peripheralAccessAllowed) {
            peripheral.attach(worldObj, getPos(), getDirection());
            if (!peripheral.hasPeripheral()) {
                return;
            }

            peripheralAccessAllowed = true;
            node.updatePeripherals(peripheral.toMap());
        } else {
            peripheral.detach();

            peripheralAccessAllowed = false;
            node.updatePeripherals(Collections.emptyMap());
        }

        updateBlockState();
    }

    @Nullable
    private Direction getMaybeDirection() {
        refreshDirection();
        return modemDirection;
    }

    private void refreshDirection() {
        if (hasModemDirection) {
            return;
        }

        hasModemDirection = true;
        modemDirection = blockStateModem.getFacing();
    }

    @Override
    public void readFromNBT(CompoundTag nbt) {
        super.readFromNBT(nbt);
        peripheralAccessAllowed = nbt.getBoolean(NBT_PERIPHERAL_ENABLED);
        peripheral.read(nbt, "");

        blockStateModem = CableModemVariant.values()[nbt.getInteger("BlockStateModem")];
        blockStateCable = nbt.getBoolean("BlockStateCable");

        blockStateNorth = nbt.getBoolean("BlockStateNorth");
        blockStateSouth = nbt.getBoolean("BlockStateSouth");
        blockStateEast = nbt.getBoolean("BlockStateEast");
        blockStateWest = nbt.getBoolean("BlockStateWest");
        blockStateUp = nbt.getBoolean("BlockStateUp");
        blockStateDown = nbt.getBoolean("BlockStateDown");
    }

    @Override
    public void writeToNBT(CompoundTag nbt) {
        nbt.putBoolean(NBT_PERIPHERAL_ENABLED, peripheralAccessAllowed);
        peripheral.write(nbt, "");

        nbt.putInt("BlockStateModem", blockStateModem.ordinal());
        nbt.putBoolean("BlockStateCable", blockStateCable);

        nbt.putBoolean("BlockStateNorth", blockStateNorth);
        nbt.putBoolean("BlockStateSouth", blockStateSouth);
        nbt.putBoolean("BlockStateEast", blockStateEast);
        nbt.putBoolean("BlockStateWest", blockStateWest);
        nbt.putBoolean("BlockStateUp", blockStateUp);
        nbt.putBoolean("BlockStateDown", blockStateDown);

        super.writeToNBT(nbt);
    }

    @Override
    public void invalidate() {
        super.invalidate();

        onRemove();
    }

    public IWiredElement getElement(Direction facing) {
        return BlockLogicCable.canConnectIn(this, facing) ? cable : null;
    }

    @Nonnull
    @Override
    public IPeripheral getPeripheral(Direction side) {
        return !destroyed && hasModem() && side == getDirection() ? modem : null;
    }

    private class CableElement extends WiredModemElement {
        @Nonnull
        @Override
        public World getWorld() {
            return TileCable.this.worldObj;
        }

        @Nonnull
        @Override
        public Vec3 getPosition() {
            BlockPos pos = getPos();
            return Vec3.getPermanentVec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        }

        @Override
        protected void attachPeripheral(String name, IPeripheral peripheral) {
            modem.attachPeripheral(name, peripheral);
        }

        @Override
        protected void detachPeripheral(String name) {
            modem.detachPeripheral(name);
        }
    }
}
