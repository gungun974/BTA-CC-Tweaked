/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.printer;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.core.terminal.Terminal;
import dan200.computercraft.fabric.Helper;
import dan200.computercraft.shared.common.TileGeneric;
import dan200.computercraft.shared.media.items.ItemPrintout;
import dan200.computercraft.shared.network.client.OpenGuiPrinterClientMessage;
import dan200.computercraft.shared.util.*;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.Vec3;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TilePrinter extends TileGeneric implements IPeripheralTile, Container {
    static final int SLOTS = 13;
    private static final String NBT_NAME = "CustomName";
    private static final String NBT_PRINTING = "Printing";
    private static final String NBT_PAGE_TITLE = "PageTitle";
    private static final int[] BOTTOM_SLOTS = new int[]{
        7,
        8,
        9,
        10,
        11,
        12,
    };
    private static final int[] TOP_SLOTS = new int[]{
        1,
        2,
        3,
        4,
        5,
        6,
    };
    private static final int[] SIDE_SLOTS = new int[]{0};
    private final List<ItemStack> inventory = new ArrayList<>(Collections.nCopies(SLOTS, null));
    private final ItemStorage itemHandlerAll = ItemStorage.wrap(this);
    private final Terminal page = new Terminal(ItemPrintout.LINE_MAX_LENGTH, ItemPrintout.LINES_PER_PAGE);
    String customName;
    private String pageTitle = "";
    private boolean printing = false;

    public TilePrinter() {
    }

    @Override
    public void invalidate() {
        ejectContents();
        super.invalidate();
    }

    private static boolean isPaper(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        Item item = stack.getItem();
        return item.equals(Items.PAPER) || (item instanceof ItemPrintout && ((ItemPrintout) item).getType() == ItemPrintout.Type.PAGE);
    }

    static boolean isInk(@Nonnull ItemStack stack) {
        return ColourUtils.getStackColour(stack) != null;
    }

    public boolean onBlockRightClicked(Player player, Side side, double xPlaced, double yPlaced) {
        if (player.isSneaking()) {
            return true;
        }

        // Open the GUI
        if (!Helper.isClientWorld()) {
            new OpenGuiPrinterClientMessage(this).sendToPlayer(player);
        }
        return true;
    }

    private void ejectContents() {
        for (int i = 0; i < 13; i++) {
            ItemStack stack = inventory.get(i);
            if (stack != null) {
                // Remove the stack from the inventory
                setItem(i, null);

                // Spawn the item in the world
                WorldUtil.dropItemStack(stack, worldObj,
                    Vec3.getPermanentVec3(x, y, z)
                        .add(0.5, 0.75, 0.5));
            }
        }
    }

    public BlockPos getPos() {
        return new BlockPos(x, y, z);
    }

    private void updateBlockState() {
        boolean top = false, bottom = false;
        for (int i = 1; i < 7; i++) {
            ItemStack stack = inventory.get(i);
            if (isPaper(stack)) {
                top = true;
                break;
            }
        }
        for (int i = 7; i < 13; i++) {
            ItemStack stack = inventory.get(i);
            if (isPaper(stack)) {
                bottom = true;
                break;
            }
        }

        updateBlockState(top, bottom);
    }

    private void updateBlockState(boolean top, boolean bottom) {
        if (isInvalid()) {
            return;
        }

        final int currentMetadata = getBlockMeta();

        final boolean currentBottom = ((currentMetadata >> 3) & 0b1) == 1;
        final boolean currentTop = ((currentMetadata >> 4) & 0b1) == 1;

        if (currentBottom != bottom || currentTop != top) {
            final int newMetadata = (currentMetadata & ~0b11000) | ((bottom ? 1 << 3 : 0) + (top ? 1 << 4 : 0));

            if (worldObj != null) {
                worldObj.setBlockMetadataWithNotify(this.x, this.y, this.z, newMetadata);
            }
        }
    }

    @Override
    public void readFromNBT(CompoundTag nbt) {
        super.readFromNBT(nbt);

        customName = nbt.containsKey(NBT_NAME) ? nbt.getString(NBT_NAME) : null;

        // Read page
        synchronized (page) {
            printing = nbt.getBoolean(NBT_PRINTING);
            pageTitle = nbt.getString(NBT_PAGE_TITLE);
            page.readFromNBT(nbt);
        }

        // Read inventory
        ListTag nbttaglist = nbt.getList("Items");
        inventory.replaceAll(ignored -> null);
        for (int i = 0; i < nbttaglist.tagCount(); i++) {
            CompoundTag tag = (CompoundTag) nbttaglist.tagAt(i);
            int slot = tag.getByte("Slot") & 0xff;
            if (slot < inventory.size()) {
                inventory.set(slot, ItemStack.readItemStackFromNbt(tag));
                ItemStack item = inventory.get(slot);
            }
        }
    }

    @Override
    public void writeToNBT(CompoundTag nbt) {
        if (customName != null) {
            nbt.putString(NBT_NAME, customName);
        }

        // Write page
        synchronized (page) {
            nbt.putBoolean(NBT_PRINTING, printing);
            nbt.putString(NBT_PAGE_TITLE, pageTitle);
            page.writeToNBT(nbt);
        }

        // Write inventory
        ListTag nbttaglist = new ListTag();
        for (int i = 0; i < inventory.size(); i++) {
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

        super.writeToNBT(nbt);
    }

    boolean isPrinting() {
        return printing;
    }

    // IInventory implementation
    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public @Nullable ItemStack getItem(int slot) {
        return slot >= 0 && slot < inventory.size() ? inventory.get(slot) : null;
    }

    @Override
    public @Nullable ItemStack removeItem(int slot, int count) {
        ItemStack stack = inventory.get(slot);
        if (stack == null) {
            return null;
        }

        if (stack.stackSize <= count) {
            setItem(slot, null);
            updateBlockState();
            return stack;
        }

        ItemStack part = stack.splitStack(count);
        if (inventory.get(slot) == null) {
            inventory.set(slot, null);
            updateBlockState();
        }
        //markDirty();
        updateBlockState();
        return part;
    }


    @Override
    public void setItem(int i, @Nullable ItemStack stack) {
        if (i >= 0 && i < inventory.size() && !InventoryUtil.areItemsEqual(stack, inventory.get(i))) {
            inventory.set(i, stack);
        }
        updateBlockState();
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

    @Nonnull
    @Override
    public IPeripheral getPeripheral(Direction side) {
        return new PrinterPeripheral(this);
    }

    @Nullable
    Terminal getCurrentPage() {
        synchronized (page) {
            return printing ? page : null;
        }
    }

    boolean startNewPage() {
        synchronized (page) {
            if (!canInputPage()) {
                return false;
            }
            if (printing && !outputPage()) {
                return false;
            }
            return inputPage();
        }
    }

    boolean endCurrentPage() {
        synchronized (page) {
            return printing && outputPage();
        }
    }

    private boolean outputPage() {
        int height = page.getHeight();
        String[] lines = new String[height];
        String[] colours = new String[height];
        for (int i = 0; i < height; i++) {
            lines[i] = page.getLine(i)
                .toString();
            colours[i] = page.getTextColourLine(i)
                .toString();
        }

        ItemStack stack = ItemPrintout.createSingleFromTitleAndText(pageTitle, lines, colours);
        for (int slot : BOTTOM_SLOTS) {
            if (inventory.get(slot) == null) {
                setItem(slot, stack);
                printing = false;
                return true;
            }
        }
        return false;
    }

    int getInkLevel() {
        ItemStack inkStack = inventory.get(0);
        return isInk(inkStack) ? inkStack.stackSize : 0;
    }

    int getPaperLevel() {
        int count = 0;
        for (int i = 1; i < 7; i++) {
            ItemStack paperStack = inventory.get(i);
            if (isPaper(paperStack)) {
                count += paperStack.stackSize;
            }
        }
        return count;
    }

    void setPageTitle(String title) {
        synchronized (page) {
            if (printing) {
                pageTitle = title;
            }
        }
    }

    private boolean canInputPage() {
        ItemStack inkStack = inventory.get(0);
        return inkStack != null && isInk(inkStack) && getPaperLevel() > 0;
    }

    private boolean inputPage() {
        ItemStack inkStack = inventory.get(0);
        DyeColor dye = ColourUtils.getStackColour(inkStack);
        if (dye == null) return false;

        for (int i = 1; i < 7; i++) {
            ItemStack paperStack = inventory.get(i);
            if (!isPaper(paperStack)) {
                continue;
            }

            // Setup the new page
            page.setTextColour(dye.blockMeta);

            page.clear();
            if (paperStack.getItem() instanceof ItemPrintout) {
                pageTitle = ItemPrintout.getTitle(paperStack);
                String[] text = ItemPrintout.getText(paperStack);
                String[] textColour = ItemPrintout.getColours(paperStack);
                for (int y = 0; y < page.getHeight(); y++) {
                    page.setLine(y, text[y], textColour[y], "");
                }
            } else {
                pageTitle = "";
            }
            page.setCursorPos(0, 0);

            // Decrement ink
            inkStack.stackSize -= 1;
            if (inkStack.stackSize == 0) {
                inventory.set(0, null);
            }

            // Decrement paper
            paperStack.stackSize -= 1;
            if (paperStack.stackSize == 0) {
                inventory.set(i, null);
                updateBlockState();
            }

            //markDirty();
            printing = true;
            return true;
        }
        return false;
    }
}
