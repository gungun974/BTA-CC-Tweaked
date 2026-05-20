/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.modem.wired;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.network.wired.IWiredElement;
import dan200.computercraft.api.network.wired.IWiredNode;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.shared.common.ComputerCraftItems;
import dan200.computercraft.shared.common.TileGeneric;
import dan200.computercraft.shared.peripheral.modem.ModemState;
import dan200.computercraft.shared.util.DirectionUtil;
import dan200.computercraft.shared.util.PortableTickScheduler;
import dan200.computercraft.shared.util.TickScheduler;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.PacketTileEntityData;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.ChunkPos;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import turniplabs.halplibe.helper.EnvironmentHelper;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class TileCable extends TileGeneric implements IPeripheralTile {
    private static final String NBT_PERIPHERAL_ENABLED = "PeripheralAccess";
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
        @NotNull
        @Override
        protected WiredModemLocalPeripheral getLocalPeripheral() {
            return peripheral;
        }

        @NotNull
        @Override
        public Vector3dc getPosition() {
            TilePosc pos = getPos().add(modemDirection, new TilePos());
            return new Vector3d(pos.x() + 0.5, pos.y() + 0.5, pos.z() + 0.5);
        }

        @NotNull
        @Override
        public Object getTarget() {
            return TileCable.this;
        }
    };
    private boolean hasModemDirection = false;
    private boolean connectionsFormed = false;

    public TileCable() {
    }

    private TilePosc getPos() {
        return tilePos;
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        onRemove();
    }

    private void onRemove() {
        if (worldObj == null || !EnvironmentHelper.isClientWorld()) {
            node.remove();
            connectionsFormed = false;
        }
    }

    public boolean onInteracted(Player player, Side side, double xPlaced, double yPlaced) {
        if (player.isSneaking()) {
            return true;
        }
        if (!canAttachPeripheral()) {
            return false;
        }

        if (EnvironmentHelper.isClientWorld()) {
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

    public void onNeighbourChange(@NotNull TilePosc neighbour) {
        if (hasModem() && !this.getBlock().getLogic().canPlaceAt(worldObj, tilePos)) {
            if (hasCable()) {
                // Drop the modem and convert to cable
                worldObj.dropItem(tilePos, new ItemStack(ComputerCraftItems.WIRED_MODEM, 1));
                blockStateModem = CableModemVariant.None;
                modemChanged();
                connectionsChanged();
            } else {
                // Drop everything and remove block
                worldObj.dropItem(tilePos, new ItemStack(ComputerCraftItems.WIRED_MODEM, 1));
                worldObj.setBlockTypeNotify(tilePos, Blocks.AIR);
                // This'll call #destroy(), so we don't need to reset the network here.
            }

            return;
        }

        if (EnvironmentHelper.isServerEnvironment() || EnvironmentHelper.isSinglePlayer()) {
            portableTickScheduler.scheduleOnNextEndTick(() -> onNeighbourTileEntityChange(neighbour));
        }
    }

    @NotNull
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
        if (EnvironmentHelper.isClientWorld()) {
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
        if (EnvironmentHelper.isClientWorld()) {
            return;
        }

        World world = worldObj;
        TilePosc current = getPos();
        for (Direction facing : DirectionUtil.FACINGS) {
            TilePosc offset = current.add(facing, new TilePos());
            if (!world.isChunkLoaded(new ChunkPos(Math.floorDiv(offset.x(), 16), Math.floorDiv(offset.z(), 16)))) {
                continue;
            }

            IWiredElement element = ComputerCraftAPI.getWiredElementAt(world, offset, facing.opposite());
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

        final BlockLogic logic = worldObj.getBlockType(tilePos).getLogic();

        if (logic instanceof BlockLogicCable) {
            ((BlockLogicCable) logic).updateBlockBoundsFromState(this.worldObj, tilePos);

            worldObj.markBlockNeedsUpdate(tilePos);
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
            worldObj.notifyBlockChange(tilePos, getBlock());
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

    public void onNeighbourTileEntityChange(@NotNull TilePosc neighbour) {
        if (!EnvironmentHelper.isClientWorld() && peripheralAccessAllowed) {
            Direction facing = getDirection();
            refreshPeripheral();
        }

        this.updatePlacementState();
    }

    @Override
    public void tick() {
        super.tick();
        portableTickScheduler.tickAtStart();

        if (EnvironmentHelper.isClientWorld()) {
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
    public Packet getDescriptionPacket() {
        return new PacketTileEntityData(this);
    }


    @Override
    public void readAdditionalData(CompoundTag nbt) {
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
    public void writeAdditionalData(CompoundTag nbt) {
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
    }

    @Override
    public void invalidate() {
        super.invalidate();

        onRemove();
    }

    public IWiredElement getElement(Direction facing) {
        return BlockLogicCable.canConnectIn(this, facing) ? cable : null;
    }

    @NotNull
    @Override
    public IPeripheral getPeripheral(Direction side) {
        return !destroyed && hasModem() && side == getDirection() ? modem : null;
    }

    private class CableElement extends WiredModemElement {
        @NotNull
        @Override
        public World getWorld() {
            return TileCable.this.worldObj;
        }

        @NotNull
        @Override
        public Vector3dc getPosition() {
            TilePosc pos = getPos();
            return new Vector3d(pos.x() + 0.5, pos.y() + 0.5, pos.z() + 0.5);
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
