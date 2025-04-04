/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.computer.recipe;

import dan200.computercraft.shared.computer.core.ComputerFamily;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.item.ItemStack;

public abstract class ComputerFamilyRecipe extends ComputerConvertRecipe
{
    private final ComputerFamily family;

    public ComputerFamilyRecipe(int recipeWidth, int recipeHeight, RecipeSymbol[] input, ItemStack output, boolean consumeContainerItem, boolean allowMirrored, ComputerFamily family) {
        super(recipeWidth, recipeHeight, input, output, consumeContainerItem, allowMirrored);
        this.family = family;
    }

    public ComputerFamilyRecipe() {
        super();
        this.family = ComputerFamily.NORMAL;
    }

    public ComputerFamily getFamily()
    {
        return family;
    }
}
