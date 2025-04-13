/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.computer.blocks;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.BlockPos;
import dan200.computercraft.ComputerCraft;
import dan200.computercraft.Peripherals;
import dan200.computercraft.PortableTickScheduler;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.client.gui.GuiComputer;
import dan200.computercraft.core.computer.ComputerSide;
import dan200.computercraft.fabric.Helper;
import dan200.computercraft.fabric.IComputerPlayer;
import dan200.computercraft.shared.BundledRedstone;
import dan200.computercraft.shared.common.TileGeneric;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.core.ComputerState;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.computer.inventory.ContainerComputer;
import dan200.computercraft.shared.util.DirectionUtil;
import dan200.computercraft.shared.util.RedstoneUtil;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;

import javax.annotation.Nonnull;
import java.util.Objects;

public abstract class TileComputerBase extends TileGeneric implements IComputerTile, IPeripheralTile {
    private static final String NBT_ID = "ComputerId";
    private static final String NBT_LABEL = "Label";
    private static final String NBT_ON = "On";
    private static final String NBT_FAMILY = "Family";
    protected String label = null;
    protected PortableTickScheduler portableTickScheduler = new PortableTickScheduler();
    boolean startOn = false;
    private ComputerFamily family;
    private int instanceID = -1;
    private int computerID = -1;
    private boolean on = false;
    private boolean fresh = false;

    public TileComputerBase(ComputerFamily family) {
        super();
        this.family = family;
    }

    public void destroy() {
        unload();
        for (Direction dir : DirectionUtil.FACINGS) {
            RedstoneUtil.propagateRedstoneOutput(worldObj, new BlockPos(x, y, z), dir);
        }
    }

    @Override
    public void onChunkUnloaded() {
        unload();
    }

    protected void unload() {
        if (instanceID >= 0) {
            if (!Helper.isServerEnvironment() && !Helper.isSinglePlayer()) {
                ComputerCraft.serverComputerRegistry.remove(instanceID);
            }
            instanceID = -1;
        }
    }

    public boolean onBlockRightClicked(Player player, Side side, double xPlaced, double yPlaced) {
        ItemStack currentItem = player.getHeldItem();
        if (currentItem != null && currentItem.getItem().equals(Items.LABEL) && canNameWithTag(player) && currentItem.hasCustomName()) {
            // Label to rename computer
            if (!Helper.isClientWorld()) {
                setLabel(currentItem.getCustomName());
                currentItem.consumeItem(player);
            }
            return true;
        } else if (!player.isSneaking()) {
            // Regular right click to activate computer
            if (!worldObj.isClientSide && isUsable(player, false)) {
                createServerComputer().turnOn();
                createServerComputer().sendTerminalState(player);
                ((IComputerPlayer) player).setCurrentContainerComputer(new ContainerComputer(this));
                createServerComputer().sendOpenComputerGui(player, GuiComputer.class);
            }

            return true;
        }
        return true;
    }

    protected boolean canNameWithTag(Player player) {
        return true;
    }


    public ServerComputer createServerComputer() {
        if (!Helper.isServerEnvironment() && !Helper.isSinglePlayer()) {
            return null;
        }

        boolean changed = false;
        if (instanceID < 0) {
            instanceID = ComputerCraft.serverComputerRegistry.getUnusedInstanceID();
            changed = true;
        }
        if (!ComputerCraft.serverComputerRegistry.contains(instanceID)) {
            ServerComputer computer = createComputer(instanceID, computerID);
            ComputerCraft.serverComputerRegistry.add(instanceID, computer);
            fresh = true;
            changed = true;
        }
        if (changed) {
            updateBlock();
            updateRedstoneInput();
        }
        return ComputerCraft.serverComputerRegistry.get(instanceID);
    }

    public ServerComputer getServerComputer() {
        return (!Helper.isServerEnvironment() && !Helper.isSinglePlayer()) ? null : ComputerCraft.serverComputerRegistry.get(instanceID);
    }

    protected abstract ServerComputer createComputer(int instanceID, int id);

    public void updateRedstoneInput() {
        if (worldObj == null || (!Helper.isServerEnvironment() && !Helper.isSinglePlayer())) {
            return;
        }

        // Update all sides
        ServerComputer computer = getServerComputer();
        if (computer == null) {
            return;
        }

        BlockPos pos = computer.getPosition();
        for (Direction dir : DirectionUtil.FACINGS) {
            updateRedstoneSideInput(computer, dir, pos.offset(dir));
        }
    }

    public void updatePeripheral() {
        if (worldObj == null || (!Helper.isServerEnvironment() && !Helper.isSinglePlayer())) {
            return;
        }

        // Update all sides
        ServerComputer computer = getServerComputer();
        if (computer == null) {
            return;
        }

        BlockPos pos = computer.getPosition();
        for (Direction dir : DirectionUtil.FACINGS) {
            updatePeripheralSide(computer, dir, pos.offset(dir));
        }
    }

    private void updateRedstoneSideInput(ServerComputer computer, Direction dir, BlockPos offset) {
        Direction offsetSide = dir.getOpposite();
        ComputerSide localDir = remapToLocalSide(dir);

        computer.setRedstoneInput(localDir, getRedstoneInput(offset, dir));
        computer.setBundledRedstoneInput(localDir, BundledRedstone.getOutput(worldObj, offset, offsetSide));
    }

    private void updatePeripheralSide(ServerComputer computer, Direction dir, BlockPos offset) {
        Direction offsetSide = dir.getOpposite();
        ComputerSide localDir = remapToLocalSide(dir);

        if (!isPeripheralBlockedOnSide(localDir)) {
            IPeripheral peripheral = Peripherals.getPeripheral(worldObj, offset, offsetSide);
            computer.setPeripheral(localDir, peripheral);
        }
    }

    protected ComputerSide remapToLocalSide(Direction globalSide) {
        return remapLocalSide(DirectionUtil.toLocal(getDirection(), globalSide));
    }

    /*
     * Gets the redstone input for an adjacent block.
     *
     * @param pos   The position of the neighbour
     * @param side  The side we are reading from
     * @return The effective redstone power
     */
    private int getRedstoneInput(BlockPos pos, Direction direction) {
        return worldObj.getSignal(pos.x, pos.y, pos.z, direction.getSide()) ? 15 : 0;
    }

    protected boolean isPeripheralBlockedOnSide(ComputerSide localSide) {
        return false;
    }

    protected ComputerSide remapLocalSide(ComputerSide localSide) {
        return localSide;
    }

    protected abstract Direction getDirection();

    public void onNeighbourChange(@Nonnull BlockPos neighbour) {
        updateRedstoneInput(neighbour);
        if (Helper.isServerEnvironment() || Helper.isSinglePlayer()) {
            portableTickScheduler.scheduleOnNextStartTick(() -> updatePeripheral(neighbour));
        }
    }

    public void readDescription(@Nonnull CompoundTag nbt) {
        label = nbt.containsKey(NBT_LABEL) ? nbt.getString(NBT_LABEL) : null;
        computerID = nbt.containsKey(NBT_ID) ? nbt.getInteger(NBT_ID) : -1;
    }

    public void writeDescription(@Nonnull CompoundTag nbt) {
        if (label != null) {
            nbt.putString(NBT_LABEL, label);
        }
        if (computerID >= 0) {
            nbt.putInt(NBT_ID, computerID);
        }
    }

    @Override
    public void tick() {
        portableTickScheduler.tickAtStart();
        if (Helper.isServerEnvironment() || Helper.isSinglePlayer()) {
            ServerComputer computer = createServerComputer();
            if (computer == null) {
                portableTickScheduler.tickAtEnd();
                return;
            }

            // If the computer isn't on and should be, then turn it on
            if (startOn || (fresh && on)) {
                computer.turnOn();
                startOn = false;
            }

            computer.keepAlive();

            fresh = false;
            computerID = computer.getID();
            label = computer.getLabel();
            on = computer.isOn();

            if (computer.hasOutputChanged()) {
                updateRedstoneOutput();
                portableTickScheduler.scheduleOnNextStartTick(this::updatePeripheral);
            }

            // Update the block state if needed. We don't fire a block update intentionally,
            // as this only really is needed on the client side.
            updateBlockState(computer.getState());

            if (computer.hasOutputChanged()) {
                updateRedstoneOutput();
                portableTickScheduler.scheduleOnNextStartTick(this::updatePeripheral);
            }
        }
        portableTickScheduler.tickAtEnd();
    }

    public void updateRedstoneOutput() {
        // Update redstone
        updateBlock();
        for (Direction dir : DirectionUtil.FACINGS) {
            RedstoneUtil.propagateRedstoneOutput(worldObj, new BlockPos(x, y, z), dir);
        }
    }

    public void updateBlock() {
        int blockId = Helper.getBlockLogic(worldObj, this.x, this.y, this.z, BlockLogicComputer.class).id();

        worldObj.notifyBlockChange(this.x, this.y, this.z, blockId);
    }

    protected abstract void updateBlockState(ComputerState newState);

    @Override
    public void readFromNBT(CompoundTag nbt) {
        super.readFromNBT(nbt);

        // Load ID, label and power state
        computerID = nbt.containsKey(NBT_ID) ? nbt.getInteger(NBT_ID) : -1;
        label = nbt.containsKey(NBT_LABEL) ? nbt.getString(NBT_LABEL) : null;
        on = startOn = nbt.getBoolean(NBT_ON);
        family = nbt.containsKey(NBT_FAMILY) ? ComputerFamily.values()[nbt.getInteger(NBT_FAMILY)] : ComputerFamily.NORMAL;
    }

    @Override
    public void writeToNBT(CompoundTag nbt) {
        // Save ID, label and power state
        if (computerID >= 0) {
            nbt.putInt(NBT_ID, computerID);
        }
        if (label != null) {
            nbt.putString(NBT_LABEL, label);
        }
        nbt.putBoolean(NBT_ON, on);
        nbt.putInt(NBT_FAMILY, family.ordinal());

        super.writeToNBT(nbt);
    }

    @Override
    public void invalidate() {
        unload();
        super.invalidate();
    }

    private void updateRedstoneInput(BlockPos neighbour) {
        if (worldObj == null || (!Helper.isServerEnvironment() && !Helper.isSinglePlayer())) {
            return;
        }

        ServerComputer computer = getServerComputer();
        if (computer == null) {
            return;
        }

        for (Direction dir : DirectionUtil.FACINGS) {
            BlockPos offset = new BlockPos(x, y, z).offset(dir);
            if (offset.equals(neighbour)) {
                updateRedstoneSideInput(computer, dir, offset);
                return;
            }
        }

        // If the position is not any adjacent one, update all inputs.
        updateRedstoneInput();
    }

    private void updatePeripheral(BlockPos neighbour) {
        if (worldObj == null || (!Helper.isServerEnvironment() && !Helper.isSinglePlayer())) {
            return;
        }

        ServerComputer computer = getServerComputer();
        if (computer == null) {
            return;
        }

        for (Direction dir : DirectionUtil.FACINGS) {
            BlockPos offset = new BlockPos(x, y, z).offset(dir);
            if (offset.equals(neighbour)) {
                updatePeripheralSide(computer, dir, offset);
                return;
            }
        }

        // If the position is not any adjacent one, update all inputs.
        updatePeripheral();
    }

    private void updateRedstoneInput(Direction dir) {
        if (worldObj == null || (!Helper.isServerEnvironment() && !Helper.isSinglePlayer())) {
            return;
        }

        ServerComputer computer = getServerComputer();
        if (computer == null) {
            return;
        }

        updateRedstoneSideInput(computer, dir, new BlockPos(x, y, z).offset(dir));
    }

    @Override
    public final int getComputerID() {
        return computerID;
    }

    @Override
    public final void setComputerID(int id) {
        if ((!Helper.isSinglePlayer() && !Helper.isServerEnvironment()) || computerID == id) {
            return;
        }

        computerID = id;
        ServerComputer computer = getServerComputer();
        if (computer != null) {
            computer.setID(computerID);
        }
        worldObj.notifyBlockChange(x, y, z, getBlockId());
    }

    @Override
    public final String getLabel() {
        return label;
    }

    // Networking stuff

    @Override
    public final void setLabel(String label) {
        if ((!Helper.isSinglePlayer() && !Helper.isServerEnvironment()) || Objects.equals(this.label, label)) {
            return;
        }

        this.label = label;
        ServerComputer computer = getServerComputer();
        if (computer != null) {
            computer.setLabel(label);
        }
        worldObj.notifyBlockChange(x, y, z, getBlockId());
    }

    @Override
    public ComputerFamily getFamily() {
        return family;
    }

    @Nonnull
    @Override
    public IPeripheral getPeripheral(Direction side) {
        return new ComputerPeripheral("computer", createProxy());
    }

    public abstract ComputerProxy createProxy();
}
