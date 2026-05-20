/*
 * This file is part of the public ComputerCraft API - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. This API may be redistributed unmodified and in full only.
 * For help using the API, and posting your mods, visit the forums at computercraft.info.
 */
package dan200.computercraft.api.pocket;

import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * A base class for {@link IPocketUpgrade}s.
 * <p>
 * One does not have to use this, but it does provide a convenient template.
 */
public abstract class AbstractPocketUpgrade implements IPocketUpgrade {
    private final int id;
    private final ItemStack stack;

    protected AbstractPocketUpgrade(int id, IItemConvertible item) {
        this.id = id;
        stack = new ItemStack(item);
    }

    protected AbstractPocketUpgrade(int id, ItemStack stack) {
        this.id = id;
        this.stack = stack;
    }


    @NotNull
    @Override
    public final int getUpgradeID() {
        return id;
    }

    @NotNull
    @Override
    public final ItemStack getCraftingItem() {
        return stack;
    }
}
