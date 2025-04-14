/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.monitor;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.shared.util.BlockPos;
import dan200.computercraft.ComputerCraft;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.core.terminal.Terminal;
import dan200.computercraft.fabric.Helper;
import dan200.computercraft.shared.common.ServerTerminal;
import dan200.computercraft.shared.common.TileGeneric;
import dan200.computercraft.shared.network.client.TerminalState;
import dan200.computercraft.shared.util.DirectionUtil;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.PacketTileEntityData;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.HitResult;
import net.minecraft.core.world.World;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class TileMonitor extends TileGeneric implements IPeripheralTile {
    public static final double RENDER_BORDER = 2.0 / 16.0;
    public static final double RENDER_MARGIN = 0.5 / 16.0;
    public static final double RENDER_PIXEL_SCALE = 1.0 / 64.0;

    private static final String NBT_X = "XIndex";
    private static final String NBT_Y = "YIndex";
    private static final String NBT_WIDTH = "Width";
    private static final String NBT_HEIGHT = "Height";
    private static final String NBT_ADVANCED = "Advanced";
    private final Set<IComputerAccess> computers = new HashSet<>();
    // MonitorWatcher state.
    private boolean advanced;
    private ServerMonitor serverMonitor;
    private ClientMonitor clientMonitor;
    private MonitorPeripheral peripheral;
    private boolean needsUpdate = false;
    private boolean destroyed = false;
    private boolean visiting = false;
    private int width = 1;
    private int height = 1;
    private int xIndex = 0;
    private int yIndex = 0;

    public TileMonitor() {
    }

    public TileMonitor(boolean advanced) {
        this.advanced = advanced;
    }

    @Override
    public void invalidate() {
        super.invalidate();

        if (clientMonitor != null && xIndex == 0 && yIndex == 0) {
            clientMonitor.destroy();
        }
    }

    public void markDestroyed() {
        if (destroyed) {
            return;
        }
        destroyed = true;
        if (!Helper.isClientWorld()) {
            contractNeighbours();
        }
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        if (clientMonitor != null && xIndex == 0 && yIndex == 0) {
            clientMonitor.destroy();
        }
        clientMonitor = null;
    }

    public boolean onBlockRightClicked(Player player, Side side, double xPlaced, double yPlaced) {
        if (!player.isSneaking() && getFront() == side.getDirection()) {
            if (!Helper.isClientWorld()) {
                HitResult hit = player.rayTrace(player.distanceTo(x, y, z), 0, false, false);
                if (hit != null) {
                    monitorTouched((float) (hit.location.x - hit.x), (float) (hit.location.y - hit.y), (float) (hit.location.z - hit.z));
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public void tick() {
        if (needsUpdate) {
            needsUpdate = false;
            updateNeighbors();
        }

        if (xIndex != 0 || yIndex != 0 || serverMonitor == null) {
            return;
        }

        serverMonitor.clearChanged();

        if (serverMonitor.pollResized()) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    TileMonitor monitor = getNeighbour(x, y);
                    if (monitor == null) {
                        continue;
                    }

                    for (IComputerAccess computer : monitor.computers) {
                        computer.queueEvent("monitor_resize", computer.getAttachmentName());
                    }
                }
            }
        }

        if (serverMonitor.pollTerminalChanged()) {
            updateBlock();
        }

        if (Helper.isSinglePlayer()) {
            if (clientMonitor == null) {
                clientMonitor = new ClientMonitor(advanced, this);
            }
            clientMonitor.read(serverMonitor.write());
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        return new PacketTileEntityData(this);
    }

    private TileMonitor getNeighbour(int x, int y) {
        BlockPos pos = getPos();
        Direction right = getRight();
        Direction down = getDown();
        int xOffset = -xIndex + x;
        int yOffset = -yIndex + y;
        return getSimilarMonitorAt(pos.offset(right, xOffset)
            .offset(down, yOffset));
    }

    public BlockPos getPos() {
        return new BlockPos(x, y, z);
    }

    public Direction getRight() {
        return DirectionUtil.rotateYCounterclockwise(getDirection());
    }

    public Direction getDown() {
        Direction orientation = getOrientation();
        if (orientation == Direction.NORTH) {
            return Direction.UP;
        }
        return orientation == Direction.DOWN ? getDirection() : getDirection().getOpposite();
    }

    private TileMonitor getSimilarMonitorAt(BlockPos pos) {
        if (pos.equals(getPos())) {
            return this;
        }

        int y = pos.getY();
        World world = worldObj;

        if (world == null || !world.isChunkLoaded(Math.floorDiv(pos.x, 16), Math.floorDiv(pos.z, 16))) {
            return null;
        }

        TileEntity tile = world.getTileEntity(pos.x, pos.y, pos.z);
        if (!(tile instanceof TileMonitor)) {
            return null;
        }

        TileMonitor monitor = (TileMonitor) tile;
        return !monitor.visiting && !monitor.destroyed && advanced == monitor.advanced && getDirection() == monitor.getDirection() && getOrientation() == monitor.getOrientation() ? monitor : null;
    }

    // region Sizing and placement stuff
    public Direction getDirection() {
        return BlockLogicMonitor.metaToFacing(getBlockMeta());
    }

    public Direction getOrientation() {
        return BlockLogicMonitor.metaToOrientation(getBlockMeta());
    }

    @Override
    public void readFromNBT(CompoundTag nbt) {
        super.readFromNBT(nbt);

        int oldXIndex = xIndex;
        int oldYIndex = yIndex;
        int oldWidth = width;
        int oldHeight = height;

        xIndex = nbt.getInteger(NBT_X);
        yIndex = nbt.getInteger(NBT_Y);
        width = nbt.getInteger(NBT_WIDTH);
        height = nbt.getInteger(NBT_HEIGHT);
        advanced = nbt.getBoolean(NBT_ADVANCED);

        if (!Helper.isServerEnvironment()) {
            if (oldXIndex != xIndex || oldYIndex != yIndex) {
                // If our index has changed then it's possible the origin monitor has changed. Thus
                // we'll clear our cache. If we're the origin then we'll need to remove the glList as well.
                if (oldXIndex == 0 && oldYIndex == 0 && clientMonitor != null) {
                    clientMonitor.destroy();
                }
                clientMonitor = null;
            }

            if (xIndex == 0 && yIndex == 0) {
                // If we're the origin terminal then create it.
                if (clientMonitor == null) {
                    clientMonitor = new ClientMonitor(advanced, this);
                }
                clientMonitor.readDescription(nbt);
            }
        }

        if (oldXIndex != xIndex || oldYIndex != yIndex || oldWidth != width || oldHeight != height) {
            // One of our properties has changed, so ensure we redraw the block
            updateBlock();
        }
    }

    // Networking stuff

    @Override
    public void writeToNBT(CompoundTag tag) {
        tag.putInt(NBT_X, xIndex);
        tag.putInt(NBT_Y, yIndex);
        tag.putInt(NBT_WIDTH, width);
        tag.putInt(NBT_HEIGHT, height);
        tag.putBoolean(NBT_ADVANCED, advanced);

        if (xIndex == 0 && yIndex == 0 && serverMonitor != null) {
            serverMonitor.writeDescription(tag);
        }

        super.writeToNBT(tag);
    }

    // Sizing and placement stuff


    @Nonnull
    @Override
    public IPeripheral getPeripheral(Direction side) {
        createServerMonitor(); // Ensure the monitor is created before doing anything else.
        if (peripheral == null) {
            peripheral = new MonitorPeripheral(this);
        }
        return peripheral;
    }

    public ServerMonitor getCachedServerMonitor() {
        return serverMonitor;
    }

    private ServerMonitor getServerMonitor() {
        if (serverMonitor != null) {
            return serverMonitor;
        }

        TileMonitor origin = getOrigin();
        if (origin == null) {
            return null;
        }

        return serverMonitor = origin.serverMonitor;
    }

    private ServerMonitor createServerMonitor() {
        if (serverMonitor != null) {
            return serverMonitor;
        }

        if (xIndex == 0 && yIndex == 0) {
            // If we're the origin, set up the new monitor
            serverMonitor = new ServerMonitor(advanced, this);
            clientMonitor = null;
            serverMonitor.rebuild();

            // And propagate it to child monitormonitors
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    TileMonitor monitor = getNeighbour(x, y);
                    if (monitor != null) {
                        monitor.serverMonitor = serverMonitor;
                    }
                }
            }

            return serverMonitor;
        } else {
            // Otherwise fetch the origin and attempt to get its monitor
            // Note this may load chunks, but we don't really have a choice here.
            BlockPos pos = getPos().offset(getRight(), -xIndex)
                .offset(getDown(), -yIndex);
            TileEntity te = worldObj.getTileEntity(pos.x, pos.y, pos.z);
            if (!(te instanceof TileMonitor)) {
                return null;
            }

            return serverMonitor = ((TileMonitor) te).createServerMonitor();
        }
    }

    public ClientMonitor getClientMonitor() {
        if (clientMonitor != null) {
            return clientMonitor;
        }

        BlockPos pos = getPos().offset(getRight(), -xIndex)
            .offset(getDown(), -yIndex);
        TileEntity te = worldObj.getTileEntity(pos.x, pos.y, pos.z);
        if (!(te instanceof TileMonitor)) {
            return null;
        }

        return clientMonitor = ((TileMonitor) te).clientMonitor;
    }

    public final void read(TerminalState state) {
        if (xIndex != 0 || yIndex != 0) {
            ComputerCraft.log.warn("Receiving monitor state for non-origin terminal at {}", getPos());
            return;
        }

        if (clientMonitor == null) {
            clientMonitor = new ClientMonitor(advanced, this);
        }
        clientMonitor.read(state);
    }

    private void updateBlockState() {
        final int currentMetadata = getBlockMeta();

        final MonitorEdgeState edgeState = MonitorEdgeState.fromConnections(yIndex < height - 1,
            yIndex > 0, xIndex > 0, xIndex < width - 1);

        if (worldObj != null) {
            final int newMetadata = (currentMetadata & ~0b11110000) | (BlockLogicMonitor.stateToMeta(edgeState));

            worldObj.setBlockMetadataWithNotify(this.x, this.y, this.z, newMetadata);
        }
    }

    public Direction getFront() {
        Direction orientation = getOrientation();
        return orientation == Direction.NORTH ? getDirection() : orientation;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getXIndex() {
        return xIndex;
    }

    public int getYIndex() {
        return yIndex;
    }

    private TileMonitor getOrigin() {
        return getNeighbour(0, 0);
    }

    private void resize(int width, int height) {
        // If we're not already the origin then we'll need to generate a new terminal.
        if (xIndex != 0 || yIndex != 0) {
            serverMonitor = null;
            clientMonitor = null;
        }

        xIndex = 0;
        yIndex = 0;
        this.width = width;
        this.height = height;

        // Determine if we actually need a monitor. In order to do this, simply check if
        // any component monitor been wrapped as a peripheral. Whilst this flag may be
        // out of date,
        boolean needsTerminal = false;
        terminalCheck:
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                TileMonitor monitor = getNeighbour(x, y);
                if (monitor != null && monitor.peripheral != null) {
                    needsTerminal = true;
                    break terminalCheck;
                }
            }
        }

        // Either delete the current monitor or sync a new one.
        if (needsTerminal) {
            if (serverMonitor == null) {
                serverMonitor = new ServerMonitor(advanced, this);
                clientMonitor = null;
            }
        } else {
            serverMonitor = null;
            clientMonitor = null;
        }

        // Update the terminal's width and height and rebuild it. This ensures the monitor
        // is consistent when syncing it to other monitors.
        if (serverMonitor != null) {
            serverMonitor.rebuild();
        }

        // Update the other monitors, setting coordinates, dimensions and the server terminal
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                TileMonitor monitor = getNeighbour(x, y);
                if (monitor == null) {
                    continue;
                }

                monitor.xIndex = x;
                monitor.yIndex = y;
                monitor.width = width;
                monitor.height = height;
                monitor.serverMonitor = serverMonitor;
                monitor.clientMonitor = null;
                monitor.updateBlockState();
                monitor.updateBlock();
            }
        }
    }

    private void updateBlock() {
        if (worldObj != null) {
            worldObj.notifyBlockChange(x, y, z, getBlockId());
        }
    }

    private boolean mergeLeft() {
        TileMonitor left = getNeighbour(-1, 0);
        if (left == null || left.yIndex != 0 || left.height != height) {
            return false;
        }

        int width = left.width + this.width;
        if (width > ComputerCraft.monitorWidth) {
            return false;
        }

        TileMonitor origin = left.getOrigin();
        if (origin != null) {
            origin.resize(width, height);
        }
        left.expand();
        return true;
    }

    private boolean mergeRight() {
        TileMonitor right = getNeighbour(width, 0);
        if (right == null || right.yIndex != 0 || right.height != height) {
            return false;
        }

        int width = this.width + right.width;
        if (width > ComputerCraft.monitorWidth) {
            return false;
        }

        TileMonitor origin = getOrigin();
        if (origin != null) {
            origin.resize(width, height);
        }
        expand();
        return true;
    }

    private boolean mergeUp() {
        TileMonitor above = getNeighbour(0, height);
        if (above == null || above.xIndex != 0 || above.width != width) {
            return false;
        }

        int height = above.height + this.height;
        if (height > ComputerCraft.monitorHeight) {
            return false;
        }

        TileMonitor origin = getOrigin();
        if (origin != null) {
            origin.resize(width, height);
        }
        expand();
        return true;
    }

    private boolean mergeDown() {
        TileMonitor below = getNeighbour(0, -1);
        if (below == null || below.xIndex != 0 || below.width != width) {
            return false;
        }

        int height = this.height + below.height;
        if (height > ComputerCraft.monitorHeight) {
            return false;
        }

        TileMonitor origin = below.getOrigin();
        if (origin != null) {
            origin.resize(width, height);
        }
        below.expand();
        return true;
    }

    void updateNeighborsDeferred() {
        needsUpdate = true;
    }

    void updateNeighbors() {
        contractNeighbours();
        contract();
        expand();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    void expand() {
        if (Helper.isSinglePlayer()) {
            createServerMonitor();
        }
        while (mergeLeft() || mergeRight() || mergeUp() || mergeDown()) ;
    }

    void contractNeighbours() {
        if (Helper.isSinglePlayer()) {
            createServerMonitor();
        }
        visiting = true;
        if (xIndex > 0) {
            TileMonitor left = getNeighbour(xIndex - 1, yIndex);
            if (left != null) {
                left.contract();
            }
        }
        if (xIndex + 1 < width) {
            TileMonitor right = getNeighbour(xIndex + 1, yIndex);
            if (right != null) {
                right.contract();
            }
        }
        if (yIndex > 0) {
            TileMonitor below = getNeighbour(xIndex, yIndex - 1);
            if (below != null) {
                below.contract();
            }
        }
        if (yIndex + 1 < height) {
            TileMonitor above = getNeighbour(xIndex, yIndex + 1);
            if (above != null) {
                above.contract();
            }
        }
        visiting = false;
    }

    void contract() {
        if (Helper.isSinglePlayer()) {
            createServerMonitor();
        }
        int height = this.height;
        int width = this.width;

        TileMonitor origin = getOrigin();
        if (origin == null) {
            TileMonitor right = width > 1 ? getNeighbour(1, 0) : null;
            TileMonitor below = height > 1 ? getNeighbour(0, 1) : null;

            if (right != null) {
                right.resize(width - 1, 1);
            }
            if (below != null) {
                below.resize(width, height - 1);
            }
            if (right != null) {
                right.expand();
            }
            if (below != null) {
                below.expand();
            }

            return;
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                TileMonitor monitor = origin.getNeighbour(x, y);
                if (monitor != null) {
                    continue;
                }

                // Decompose
                TileMonitor above = null;
                TileMonitor left = null;
                TileMonitor right = null;
                TileMonitor below = null;

                if (y > 0) {
                    above = origin;
                    above.resize(width, y);
                }
                if (x > 0) {
                    left = origin.getNeighbour(0, y);
                    left.resize(x, 1);
                }
                if (x + 1 < width) {
                    right = origin.getNeighbour(x + 1, y);
                    right.resize(width - (x + 1), 1);
                }
                if (y + 1 < height) {
                    below = origin.getNeighbour(0, y + 1);
                    below.resize(width, height - (y + 1));
                }

                // Re-expand
                if (above != null) {
                    above.expand();
                }
                if (left != null) {
                    left.expand();
                }
                if (right != null) {
                    right.expand();
                }
                if (below != null) {
                    below.expand();
                }
                return;
            }
        }
    }
    // endregion

    private void monitorTouched(float xPos, float yPos, float zPos) {
        XYPair pair = XYPair.of(xPos, yPos, zPos, getDirection(), getOrientation())
            .add(xIndex, height - yIndex - 1);

        if (pair.x > width - RENDER_BORDER || pair.y > height - RENDER_BORDER || pair.x < RENDER_BORDER || pair.y < RENDER_BORDER) {
            return;
        }

        ServerTerminal serverTerminal = getServerMonitor();
        if (serverTerminal == null || !serverTerminal.isColour()) {
            return;
        }

        Terminal originTerminal = serverTerminal.getTerminal();
        if (originTerminal == null) {
            return;
        }

        double xCharWidth = (width - (RENDER_BORDER + RENDER_MARGIN) * 2.0) / originTerminal.getWidth();
        double yCharHeight = (height - (RENDER_BORDER + RENDER_MARGIN) * 2.0) / originTerminal.getHeight();

        int xCharPos = (int) Math.min(originTerminal.getWidth(), Math.max((pair.x - RENDER_BORDER - RENDER_MARGIN) / xCharWidth + 1.0, 1.0));
        int yCharPos = (int) Math.min(originTerminal.getHeight(), Math.max((pair.y - RENDER_BORDER - RENDER_MARGIN) / yCharHeight + 1.0, 1.0));

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                TileMonitor monitor = getNeighbour(x, y);
                if (monitor == null) {
                    continue;
                }

                for (IComputerAccess computer : monitor.computers) {
                    computer.queueEvent("monitor_touch", computer.getAttachmentName(), xCharPos, yCharPos);
                }
            }
        }
    }

    void addComputer(IComputerAccess computer) {
        computers.add(computer);
    }

    void removeComputer(IComputerAccess computer) {
        computers.remove(computer);
    }
}
