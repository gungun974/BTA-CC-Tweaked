/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.blocks;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import dan200.computercraft.shared.util.BlockPos;
import dan200.computercraft.ComputerCraft;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.core.computer.ComputerSide;
import dan200.computercraft.fabric.Helper;
import dan200.computercraft.fabric.IComputerPlayer;
import dan200.computercraft.shared.computer.blocks.ComputerProxy;
import dan200.computercraft.shared.computer.blocks.TileComputerBase;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.core.ComputerState;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.computer.inventory.ContainerComputer;
import dan200.computercraft.shared.turtle.apis.TurtleAPI;
import dan200.computercraft.shared.turtle.core.TurtleBrain;
import dan200.computercraft.shared.turtle.inventory.MenuTurtle;
import dan200.computercraft.shared.turtle.inventory.ScreenTurtle;
import dan200.computercraft.shared.util.DirectionUtil;
import dan200.computercraft.shared.util.InventoryUtil;
import dan200.computercraft.shared.util.RedstoneUtil;
import dan200.computercraft.shared.util.WorldUtil;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemDye;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TileTurtle extends TileComputerBase implements ITurtleTile, Container {
    public static final int INVENTORY_SIZE = 16;
    public static final int INVENTORY_WIDTH = 4;
    public static final int INVENTORY_HEIGHT = 4;
    public final List<@Nullable ItemStack> inventory = new ArrayList<>(Collections.nCopies(INVENTORY_SIZE, null));
    private final List<ItemStack> previousInventory = new ArrayList<>(Collections.nCopies(INVENTORY_SIZE, null));
    private boolean inventoryChanged = false;
    private TurtleBrain brain = new TurtleBrain(this);
    private MoveState moveState = MoveState.NOT_MOVED;

    public TileTurtle() {
        super(ComputerFamily.NORMAL);
    }

    public TileTurtle(ComputerFamily family) {
        super(family);
    }

    @Override
    protected void unload() {
        if (!hasMoved()) {
            super.unload();
        }
    }

    @Override
    public void destroy() {
        if (!hasMoved()) {
            // Stop computer
            super.destroy();

            // Drop contents
            if (!Helper.isClientWorld()) {
                int size = inventory.size();
                for (int i = 0; i < size; i++) {
                    ItemStack stack = getItem(i);
                    if (stack != null) {
                        WorldUtil.dropItemStack(stack, worldObj, getPos());
                    }
                }
            }
        } else {
            // Just turn off any redstone we had on
            for (Direction dir : DirectionUtil.FACINGS) {
                RedstoneUtil.propagateRedstoneOutput(worldObj, getPos(), dir);
            }
        }
    }

    public BlockPos getPos() {
        return new BlockPos(x, y, z);
    }

    private boolean hasMoved() {
        return moveState == MoveState.MOVED;
    }

    @Override
    public int getContainerSize() {
        return INVENTORY_SIZE;
    }

    @Override
    public @Nullable ItemStack getItem(int slot) {
        return slot >= 0 && slot < INVENTORY_SIZE ? inventory.get(slot) : null;
    }

    @Override
    public @Nullable ItemStack removeItem(int slot, int count) {
        if (count == 0) {
            return null;
        }

        ItemStack stack = getItem(slot);
        if (stack == null) {
            return null;
        }

        if (stack.stackSize <= count) {
            setItem(slot, null);
            return stack;
        }

        ItemStack part = stack.splitStack(count);
        onInventoryDefinitelyChanged();
        return part;
    }

    @Override
    public void setItem(int i, @Nullable ItemStack stack) {
        if (i >= 0 && i < INVENTORY_SIZE && !InventoryUtil.areItemsEqual(stack, inventory.get(i))) {
            inventory.set(i, stack);
            onInventoryDefinitelyChanged();
        }
    }

    @Override
    public String getNameTranslationKey() {
        return "";
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean stillValid(Player player) {
        return isUsable(player, false);
    }

    @Override
    public void sortContainer() {

    }


    private void onInventoryDefinitelyChanged() {
        inventoryChanged = true;
    }

    public boolean onBlockRightClicked(Player player, Side side, double xPlaced, double yPlaced) {
        // Apply dye
        ItemStack currentItem = player.getHeldItem();
        if (currentItem != null) {
            if (currentItem.getItem() instanceof ItemDye) {
                // Dye to change turtle colour
                if (!Helper.isClientWorld()) {
                    DyeColor dye = DyeColor.colorFromItemMeta(currentItem.getMetadata());
                    if (brain.getDyeColour() != dye) {
                        brain.setDyeColour(dye);
                        if (player.gamemode != Gamemode.creative) {
                            currentItem.stackSize -= 1;
                            if (currentItem.stackSize == 0) {
                                player.setHeldObject(null);
                            }
                        }
                    }
                }
                return true;
            } else if (currentItem.getItem().equals(Items.BUCKET_WATER) && brain.getColour() != -1) {
                // Water to remove turtle colour
                if (!Helper.isClientWorld()) {
                    if (brain.getColour() != -1) {
                        brain.setColour(-1);
                        if (player.gamemode != Gamemode.creative) {
                            //player.setHeldObject(new ItemStack( Items.BUCKET ));
                            //player.inventory.markDirty();
                        }
                    }
                }
                return true;
            }
        }

        // Open GUI or whatever
        //return super.onBlockRightClicked( player, side, xPlaced, yPlaced );
        // Open the GUI
        if (!Helper.isClientWorld() && isUsable(player, false)) {
            createServerComputer().turnOn();
            createServerComputer().sendTerminalState(player);
            ((IComputerPlayer) player).setCurrentContainerComputer(new ContainerComputer(this));
            createServerComputer().sendOpenContainerComputerGui(player, this, ScreenTurtle.class, MenuTurtle::new);
        }
        return true;
    }

    @Override
    public void onNeighbourChange(@Nonnull BlockPos neighbour) {
        if (moveState == MoveState.NOT_MOVED) {
            super.onNeighbourChange(neighbour);
        }
    }

    @Override
    public void tick() {
        super.tick();
        brain.update();
        if (!Helper.isClientWorld() && inventoryChanged) {
            ServerComputer computer = getServerComputer();
            if (computer != null) {
                computer.queueEvent("turtle_inventory");
            }

            inventoryChanged = false;
            for (int n = 0; n < inventory.size(); n++) {
                ItemStack item = getItem(n);
                previousInventory.set(n,
                    item != null ? item.copy() : null);
            }
        }
    }

    @Override
    protected void updateBlockState(ComputerState newState) {
    }

    @Override
    public void writeToNBT(CompoundTag nbt) {
        // Write inventory
        ListTag nbttaglist = new ListTag();
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            ItemStack item = inventory.get(i);
            if (item != null) {
                CompoundTag tag = new CompoundTag();
                tag.putByte("Slot", (byte) i);
                item.writeToNBT(tag);
                nbttaglist.addTag(tag);
            } else {
                inventory.set(i, null);
            }
        }
        nbt.put("Items", nbttaglist);

        // Write brain
        nbt = brain.writeToNBT(nbt);

        super.writeToNBT(nbt);
    }

    // IDirectionalTile


    @Override
    public void readFromNBT(CompoundTag nbt) {
        super.readFromNBT(nbt);

        // Read inventory
        ListTag nbttaglist = nbt.getList("Items");
        inventory.replaceAll(ignored -> null);
        previousInventory.replaceAll(ignored -> null);
        for (int i = 0; i < nbttaglist.tagCount(); i++) {
            CompoundTag tag = (CompoundTag) nbttaglist.tagAt(i);
            int slot = tag.getByte("Slot") & 0xff;
            if (slot < inventory.size()) {
                inventory.set(slot, ItemStack.readItemStackFromNbt(tag));
                ItemStack item = inventory.get(slot);
                previousInventory.set(slot, item != null ? item.copy() : null);
            }
        }

        // Read state
        brain.readFromNBT(nbt);
    }

    @Override
    protected boolean isPeripheralBlockedOnSide(ComputerSide localSide) {
        return hasPeripheralUpgradeOnSide(localSide);
    }

    // ITurtleTile

    @Override
    public Direction getDirection() {
        return BlockLogicTurtle.getDirectionFromMeta(getBlockMeta());
    }

    public void setDirection(Direction dir) {
        if (dir.getAxis() == Axis.Y) {
            dir = Direction.NORTH;
        }

        final int currentMetadata = getBlockMeta();

        worldObj.setBlockMetadata(x, y, z, BlockLogicTurtle.setDirection(currentMetadata, dir));

        updateRedstoneOutput();
        updateRedstoneInput();
        onTileEntityChange();
    }

    @Override
    protected ServerComputer createComputer(int instanceID, int id) {
        ServerComputer computer = new ServerComputer(worldObj,
            id, label,
            instanceID, getFamily(),
            ComputerCraft.turtleTermWidth,
            ComputerCraft.turtleTermHeight);
        computer.setPosition(getPos());
        computer.addAPI(new TurtleAPI(computer.getAPIEnvironment(), getAccess()));
        brain.setupComputer(computer);
        return computer;
    }

    @Override
    public void writeDescription(@Nonnull CompoundTag nbt) {
        super.writeDescription(nbt);
        brain.writeDescription(nbt);
    }

    @Override
    public void readDescription(@Nonnull CompoundTag nbt) {
        super.readDescription(nbt);
        brain.readDescription(nbt);
    }

    @Override
    public ComputerProxy createProxy() {
        return brain.getProxy();
    }

    public void onTileEntityChange() {
        worldObj.notifyBlockChange(x, y, z, getBlockId());
    }

    private boolean hasPeripheralUpgradeOnSide(ComputerSide side) {
        ITurtleUpgrade upgrade;
        switch (side) {
            case RIGHT:
                upgrade = getUpgrade(TurtleSide.RIGHT);
                break;
            case LEFT:
                upgrade = getUpgrade(TurtleSide.LEFT);
                break;
            default:
                return false;
        }
        return upgrade != null && upgrade.getType()
            .isPeripheral();
    }

    // IInventory

    @Override
    protected double getInteractRange(Player player) {
        return 12.0;
    }

    public void notifyMoveStart() {
        if (moveState == MoveState.NOT_MOVED) {
            moveState = MoveState.IN_PROGRESS;
        }
    }

    public void notifyMoveEnd() {
        // MoveState.MOVED is final
        if (moveState == MoveState.IN_PROGRESS) {
            moveState = MoveState.NOT_MOVED;
        }
    }

    @Override
    public int getColour() {
        return brain.getColour();
    }

    @Override
    public int getOverlay() {
        return brain.getOverlay();
    }

    @Override
    public ITurtleUpgrade getUpgrade(TurtleSide side) {
        return brain.getUpgrade(side);
    }

    @Override
    public ITurtleAccess getAccess() {
        return brain;
    }

    @Override
    public Vec3 getRenderOffset(float f) {
        return brain.getRenderOffset(f);
    }

    @Override
    public float getRenderYaw(float f) {
        return brain.getVisualYaw(f);
    }

    @Override
    public float getToolRenderAngle(TurtleSide side, float f) {
        return brain.getToolRenderAngle(side, f);
    }

    public void setOwningPlayer(UUID player) {
        brain.setOwningPlayer(player);
        markDirty();
    }

    // Networking stuff

    public void markDirty() {
        //super.markDirty();
        if (!inventoryChanged) {
            for (int n = 0; n < inventory.size(); n++) {
                if (!ItemStack.areItemStacksEqual(getItem(n), previousInventory.get(n))) {
                    inventoryChanged = true;
                    break;
                }
            }
        }
    }

    public void clear() {
        boolean changed = false;
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            if (inventory.get(i) != null) {
                inventory.set(i, null);
                changed = true;
            }
        }

        if (changed) {
            onInventoryDefinitelyChanged();
        }
    }

    // Privates

    public void transferStateFrom(TileTurtle copy) {
        Collections.copy(inventory, copy.inventory);
        Collections.copy(previousInventory, copy.previousInventory);
        inventoryChanged = copy.inventoryChanged;
        brain = copy.brain;
        brain.setOwner(this);
    }
}
