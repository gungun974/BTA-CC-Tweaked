/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.computer.recipe;

import dan200.computercraft.shared.computer.items.IComputerItem;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShaped;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerCrafting;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a recipe which converts a computer from one form into another.
 */
public abstract class ComputerConvertRecipe extends RecipeEntryCraftingShaped {
    public ComputerConvertRecipe(int recipeWidth, int recipeHeight, RecipeSymbol[] input, ItemStack output, boolean consumeContainerItem, boolean allowMirrored) {
        super(recipeWidth, recipeHeight, input, output, consumeContainerItem, allowMirrored);
    }

    public ComputerConvertRecipe() {
        super();
    }

    @Override
    public boolean matches(ContainerCrafting inventory) {
        if (!super.matches(inventory)) {
            return false;
        }

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);

            if (stack != null && stack.getItem() instanceof IComputerItem) {
                return true;
            }
        }

        return false;
    }

    @Override
    public ItemStack getCraftingResult(ContainerCrafting inventory) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack != null && stack.getItem() instanceof IComputerItem) {
                return convert((IComputerItem) stack.getItem(), stack);
            }
        }

        return null;
    }

    @NotNull
    protected abstract ItemStack convert(@NotNull IComputerItem item, @NotNull ItemStack stack);
}
