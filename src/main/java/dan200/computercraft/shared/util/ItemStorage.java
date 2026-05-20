/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.util;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;

/**
 * The most cutesy alternative of {@code IItemHandler} the world has ever seen.
 */
public interface ItemStorage {
    static ItemStorage wrap(Container inventory) {
        return new InventoryWrapper(inventory);
    }

//    static ItemStorage wrap(@NotNull SidedInventory inventory,@NotNull Direction facing )
//    {
//        return new SidedInventoryWrapper( inventory, facing );
//    }

//    static ItemStorage wrap(@NotNull Inventory inventory,@NotNull Direction facing )
//    {
//        return inventory instanceof SidedInventory ? new SidedInventoryWrapper( (SidedInventory) inventory, facing ) : new InventoryWrapper( inventory );
//    }

    static boolean areStackable(ItemStack a, ItemStack b) {
        return a == b || (a.getItem().equals(b.getItem()) && a.getData().equals(b.getData()));
    }

    int size();

    ItemStack getStack(int slot);

    ItemStack take(int slot, int limit, ItemStack filter, boolean simulate);

    ItemStack store(int slot, ItemStack stack, boolean simulate);

    default ItemStorage view(int start, int size) {
        return new View(this, start, size);
    }

    class InventoryWrapper implements ItemStorage {
        private final Container inventory;

        InventoryWrapper(Container inventory) {
            this.inventory = inventory;
        }

        @Override
        public int size() {
            return inventory.getContainerSize();
        }

        @Override
        public ItemStack getStack(int slot) {
            return inventory.getItem(slot);
        }

        @Override
        public ItemStack take(int slot, int limit, ItemStack filter, boolean simulate) {
            ItemStack existing = inventory.getItem(slot);
            if (existing == null || existing.stackSize == 0 || !canExtract(slot, existing) || (!(filter == null) && !areStackable(existing, filter))) {
                return null;
            }

            if (simulate) {
                existing = existing.copy();
                if (existing.stackSize > limit) {
                    existing.stackSize = limit;
                }
                return existing;
            } else if (existing.stackSize < limit) {
                setAndDirty(slot, null);
                return existing;
            } else {
                ItemStack result = existing.splitStack(limit);
                setAndDirty(slot, existing);
                return result;
            }
        }

        protected boolean canExtract(int slot, ItemStack stack) {
            return true;
        }

        private void setAndDirty(int slot, ItemStack stack) {

            if (stack == null || stack.stackSize == 0) {
                inventory.setItem(slot, null);
            } else {
                inventory.setItem(slot, stack);
            }
            inventory.setChanged();
        }

        @Override
        public ItemStack store(int slot, ItemStack stack, boolean simulate) {
            if (stack == null/*|| !inventory.isValid( slot, stack )*/) {
                return stack;
            }

            ItemStack existing = inventory.getItem(slot);
            if (existing == null) {
                int limit = Math.min(stack.getMaxStackSize(), inventory.getMaxStackSize());
                if (limit <= 0) {
                    return stack;
                }

                if (stack.stackSize < limit) {
                    if (!simulate) {
                        setAndDirty(slot, stack);
                    }
                    return null;
                } else {
                    stack = stack.copy();
                    ItemStack insert = stack.splitStack(limit);
                    if (!simulate) {
                        setAndDirty(slot, insert);
                    }
                    return stack;
                }
            } else if (areStackable(stack, existing)) {
                int limit = Math.min(existing.getMaxStackSize(), inventory.getMaxStackSize()) - existing.stackSize;
                if (limit <= 0) {
                    return stack;
                }

                if (stack.stackSize < limit) {
                    if (!simulate) {
                        existing.stackSize += stack.stackSize;
                        setAndDirty(slot, existing);
                    }
                    return null;
                } else {
                    stack = stack.copy();
                    stack.stackSize -= limit;
                    if (!simulate) {
                        existing.stackSize += limit;
                        setAndDirty(slot, existing);
                    }
                    return stack;
                }
            } else {
                return stack;
            }
        }
    }

//    class SidedInventoryWrapper extends InventoryWrapper
//    {
//        private final SidedInventory inventory;
//        private final Direction facing;
//
//        SidedInventoryWrapper( SidedInventory inventory, Direction facing )
//        {
//            super( inventory );
//            this.inventory = inventory;
//            this.facing = facing;
//        }
//
//        @Override
//        protected boolean canExtract( int slot, ItemStack stack )
//        {
//            return super.canExtract( slot, stack ) && inventory.canExtract( slot, stack, facing );
//        }
//
//        @Override
//        public int size()
//        {
//            return inventory.getAvailableSlots( facing ).length;
//        }
//
//       @NotNull
//        @Override
//        public ItemStack take( int slot, int limit,@NotNull ItemStack filter, boolean simulate )
//        {
//            int[] slots = inventory.getAvailableSlots( facing );
//            return slot >= 0 && slot < slots.length ? super.take( slots[slot], limit, filter, simulate ) : ItemStack.EMPTY;
//        }
//
//       @NotNull
//        @Override
//        public ItemStack store( int slot,@NotNull ItemStack stack, boolean simulate )
//        {
//            int[] slots = inventory.getAvailableSlots( facing );
//            if( slot < 0 || slot >= slots.length )
//            {
//                return stack;
//            }
//
//            int mappedSlot = slots[slot];
//            if( !inventory.canInsert( slot, stack, facing ) )
//            {
//                return stack;
//            }
//            return super.store( mappedSlot, stack, simulate );
//        }
//    }

    class View implements ItemStorage {
        private final ItemStorage parent;
        private final int start;
        private final int size;

        View(ItemStorage parent, int start, int size) {
            this.parent = parent;
            this.start = start;
            this.size = size;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public ItemStack getStack(int slot) {
            if (slot < start || slot >= start + size) {
                return null;
            }
            return parent.getStack(slot - start);
        }

        @Override
        public ItemStack take(int slot, int limit, ItemStack filter, boolean simulate) {
            if (slot < start || slot >= start + size) {
                return null;
            }
            return parent.take(slot - start, limit, filter, simulate);
        }

        @Override
        public ItemStack store(int slot, ItemStack stack, boolean simulate) {
            if (slot < start || slot >= start + size) {
                return stack;
            }
            return parent.store(slot - start, stack, simulate);
        }

        @Override
        public ItemStorage view(int start, int size) {
            return new View(parent, this.start + start, size);
        }
    }
}
