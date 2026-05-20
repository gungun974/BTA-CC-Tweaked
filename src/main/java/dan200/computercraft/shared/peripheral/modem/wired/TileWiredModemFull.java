/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.modem.wired;

import com.google.common.base.Objects;
import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.network.wired.IWiredElement;
import dan200.computercraft.api.network.wired.IWiredNode;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.shared.common.TileGeneric;
import dan200.computercraft.shared.peripheral.modem.ModemState;
import dan200.computercraft.shared.util.DirectionUtil;
import dan200.computercraft.shared.util.TickScheduler;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.ChunkPos;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import turniplabs.halplibe.helper.EnvironmentHelper;

import java.util.*;

public class TileWiredModemFull extends TileGeneric implements IPeripheralTile {
    private static final String NBT_PERIPHERAL_ENABLED = "PeripheralAccess";
    private final WiredModemPeripheral[] modems = new WiredModemPeripheral[6];
    private final WiredModemLocalPeripheral[] peripherals = new WiredModemLocalPeripheral[6];
    private final ModemState modemState = new ModemState(() -> TickScheduler.schedule(this));
    private final WiredModemElement element = new FullElement(this);
    private final IWiredNode node = element.getNode();
    private boolean peripheralAccessAllowed = false;
    private final boolean destroyed = false;
    private boolean connectionsFormed = false;

    public TileWiredModemFull() {
        for (int i = 0; i < peripherals.length; i++) {
            Direction facing = Direction.fromId(i);
            peripherals[i] = new WiredModemLocalPeripheral();
        }
    }

    private static void sendPeripheralChanges(Player player, String kind, Collection<String> peripherals) {
        if (peripherals.isEmpty()) {
            return;
        }

        List<String> names = new ArrayList<>(peripherals);
        names.sort(Comparator.naturalOrder());

        StringBuilder base = new StringBuilder();
        for (int i = 0; i < names.size(); i++) {
            if (i > 0) {
                base.append(", ");
            }
            base.append(names.get(i));
        }

        I18n i18n = I18n.getInstance();
        player.sendMessage(i18n.translateKeyAndFormat(kind, base));
    }

    @Override
    public void invalidate() {
        super.invalidate();
        doRemove();
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        doRemove();
    }

    private void doRemove() {
        if (worldObj == null || !EnvironmentHelper.isClientWorld()) {
            node.remove();
            connectionsFormed = false;
        }
    }

    public boolean onInteracted(Player player, Side side, double xPlaced, double yPlaced) {
        if (EnvironmentHelper.isClientWorld()) {
            return true;
        }

        // On server, we interacted if a peripheral was found
        Set<String> oldPeriphNames = getConnectedPeripheralNames();
        togglePeripheralAccess();
        Set<String> periphNames = getConnectedPeripheralNames();

        if (!Objects.equal(periphNames, oldPeriphNames)) {
            sendPeripheralChanges(player, "chat.computercraft.wired_modem.peripheral_disconnected", oldPeriphNames);
            sendPeripheralChanges(player, "chat.computercraft.wired_modem.peripheral_connected", periphNames);
        }

        return true;
    }

    public void onNeighbourTileEntityChange(@NotNull TilePosc neighbour) {
        if (!EnvironmentHelper.isClientWorld() && peripheralAccessAllowed) {
            for (Direction facing : DirectionUtil.FACINGS) {
                refreshPeripheral(facing);
            }
        }
    }

    @Override
    public void tick() {
        if (EnvironmentHelper.isClientWorld()) {
            return;
        }

        if (modemState.pollChanged()) {
            updateBlockState();
        }

        if (!connectionsFormed) {
            connectionsFormed = true;

            connectionsChanged();
            if (peripheralAccessAllowed) {
                for (Direction facing : DirectionUtil.FACINGS) {
                    peripherals[facing.ordinal()].attach(worldObj, getPos(), facing);
                }
                updateConnectedPeripherals();
            }
        }
    }

    private TilePosc getPos() {
        return tilePos;
    }

    private void connectionsChanged() {
        if (EnvironmentHelper.isClientWorld()) {
            return;
        }

        TilePosc current = getPos();
        for (Direction facing : DirectionUtil.FACINGS) {
            TilePosc offset = current.add(facing, new TilePos());
            if (!worldObj.isChunkLoaded(new ChunkPos(Math.floorDiv(offset.x(), 16), Math.floorDiv(offset.z(), 16)))) {
                continue;
            }

            IWiredElement element = ComputerCraftAPI.getWiredElementAt(worldObj, offset, facing.opposite());
            if (element == null) {
                continue;
            }

            node.connectTo(element.getNode());
        }
    }

    private void refreshPeripheral(@NotNull Direction facing) {
        WiredModemLocalPeripheral peripheral = peripherals[facing.ordinal()];
        if (worldObj != null && !isInvalid() && peripheral.attach(worldObj, getPos(), facing)) {
            updateConnectedPeripherals();
        }
    }

    private void updateConnectedPeripherals() {
        Map<String, IPeripheral> peripherals = getConnectedPeripherals();
        if (peripherals.isEmpty()) {
            // If there are no peripherals then disable access and update the display state.
            peripheralAccessAllowed = false;
            updateBlockState();
        }

        node.updatePeripherals(peripherals);
    }

    private Map<String, IPeripheral> getConnectedPeripherals() {
        if (!peripheralAccessAllowed) {
            return Collections.emptyMap();
        }

        Map<String, IPeripheral> peripherals = new HashMap<>(6);
        for (WiredModemLocalPeripheral peripheral : this.peripherals) {
            peripheral.extendMap(peripherals);
        }
        return peripherals;
    }

    private void updateBlockState() {
        final int metaId = getBlockMeta();

        final boolean isModemOn = (metaId & 0b1) == 1;
        final boolean isPeripheralOn = (metaId & 0b10) == 2;

        boolean modemOn = modemState.isOpen(), peripheralOn = peripheralAccessAllowed;
        if (isModemOn == modemOn && isPeripheralOn == peripheralOn) {
            return;
        }

        worldObj.setBlockData(tilePos, (modemOn ? 1 : 0) + (peripheralOn ? 2 : 0));
        worldObj.notifyBlockChange(tilePos, getBlock());
    }

    private Set<String> getConnectedPeripheralNames() {
        if (!peripheralAccessAllowed) {
            return Collections.emptySet();
        }

        Set<String> peripherals = new HashSet<>(6);
        for (WiredModemLocalPeripheral peripheral : this.peripherals) {
            String name = peripheral.getConnectedName();
            if (name != null) {
                peripherals.add(name);
            }
        }
        return peripherals;
    }

    private void togglePeripheralAccess() {
        if (!peripheralAccessAllowed) {
            boolean hasAny = false;
            for (Direction facing : DirectionUtil.FACINGS) {
                WiredModemLocalPeripheral peripheral = peripherals[facing.ordinal()];
                peripheral.attach(worldObj, getPos(), facing);
                hasAny |= peripheral.hasPeripheral();
            }

            if (!hasAny) {
                return;
            }

            peripheralAccessAllowed = true;
            node.updatePeripherals(getConnectedPeripherals());
        } else {
            peripheralAccessAllowed = false;

            for (WiredModemLocalPeripheral peripheral : peripherals) {
                peripheral.detach();
            }
            node.updatePeripherals(Collections.emptyMap());
        }

        updateBlockState();
    }

    @Override
    public void readAdditionalData(CompoundTag nbt) {
        peripheralAccessAllowed = nbt.getBoolean(NBT_PERIPHERAL_ENABLED);
        for (int i = 0; i < peripherals.length; i++) {
            peripherals[i].read(nbt, Integer.toString(i));
        }
    }

    @Override
    public void writeAdditionalData(CompoundTag nbt) {
        nbt.putBoolean(NBT_PERIPHERAL_ENABLED, peripheralAccessAllowed);
        for (int i = 0; i < peripherals.length; i++) {
            peripherals[i].write(nbt, Integer.toString(i));
        }
    }

    public IWiredElement getElement() {
        return element;
    }

    @NotNull
    @Override
    public IPeripheral getPeripheral(Direction side) {
        WiredModemPeripheral peripheral = modems[side.ordinal()];
        if (peripheral != null) {
            return peripheral;
        }

        WiredModemLocalPeripheral localPeripheral = peripherals[side.ordinal()];
        return modems[side.ordinal()] = new WiredModemPeripheral(modemState, element) {
            @NotNull
            @Override
            protected WiredModemLocalPeripheral getLocalPeripheral() {
                return localPeripheral;
            }

            @NotNull
            @Override
            public Vector3dc getPosition() {
                TilePosc pos = getPos().add(side, new TilePos());
                return new Vector3d(pos.x() + 0.5, pos.y() + 0.5, pos.z() + 0.5);
            }

            @NotNull
            @Override
            public Object getTarget() {
                return TileWiredModemFull.this;
            }
        };
    }

    private static final class FullElement extends WiredModemElement {
        private final TileWiredModemFull entity;

        private FullElement(TileWiredModemFull entity) {
            this.entity = entity;
        }

        @Override
        protected void detachPeripheral(String name) {
            for (int i = 0; i < 6; i++) {
                WiredModemPeripheral modem = entity.modems[i];
                if (modem != null) {
                    modem.detachPeripheral(name);
                }
            }
        }

        @Override
        protected void attachPeripheral(String name, IPeripheral peripheral) {
            for (int i = 0; i < 6; i++) {
                WiredModemPeripheral modem = entity.modems[i];
                if (modem != null) {
                    modem.attachPeripheral(name, peripheral);
                }
            }
        }

        @NotNull
        @Override
        public World getWorld() {
            return entity.worldObj;
        }

        @NotNull
        @Override
        public Vector3dc getPosition() {
            TilePosc pos = entity.getPos();
            return new Vector3d(pos.x() + 0.5, pos.y() + 0.5, pos.z() + 0.5);
        }
    }
}
