/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.recipe;

import com.google.gson.*;
import dan200.computercraft.shared.common.IColouredItem;
import dan200.computercraft.shared.util.ColourTracker;
import dan200.computercraft.shared.util.ColourUtils;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.HasJsonAdapter;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.data.registry.recipe.adapter.RecipeJsonAdapter;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerCrafting;
import net.minecraft.core.util.helper.DyeColor;

import java.lang.reflect.Type;

public final class ColourableRecipe extends RecipeEntryCrafting<RecipeSymbol[], ItemStack> implements HasJsonAdapter {
    public ColourableRecipe() {
    }

    @Override
    public boolean matches(ContainerCrafting inv) {
        boolean hasColourable = false;
        boolean hasDye = false;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack == null) {
                continue;
            }

            if (stack.getItem() instanceof IColouredItem) {
                if (hasColourable) {
                    return false;
                }
                hasColourable = true;
            } else if (ColourUtils.getStackColour(stack) != null) {
                hasDye = true;
            } else {
                return false;
            }
        }

        return hasColourable && hasDye;
    }

    @Override
    public boolean matchesQuery(SearchQuery searchQuery) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(ContainerCrafting inv) {
        ItemStack colourable = null;

        ColourTracker tracker = new ColourTracker();

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);

            if (stack == null) {
                continue;
            }

            if (stack.getItem() instanceof IColouredItem) {
                colourable = stack;
                continue;
            }

            DyeColor dye = ColourUtils.getStackColour(stack);
            if (dye != null) tracker.addColour(dye);
        }

        if (colourable == null) return null;

        ItemStack stack = ((IColouredItem) colourable.getItem()).withColour(colourable, tracker.getColour());
        stack.stackSize = 1;
        return stack;
    }

    @Override
    public int getRecipeSize() {
        return 2;
    }

    @Override
    public ItemStack[] onCraftResult(ContainerCrafting containerCrafting) {
        ItemStack[] returnStack = new ItemStack[9];

        for (int i = 0; i < containerCrafting.getContainerSize(); ++i) {
            ItemStack itemStack = containerCrafting.getItem(i);
            if (itemStack != null) {
                containerCrafting.removeItem(i, 1);
                if (itemStack.getItem().hasContainerItem()) {
                    containerCrafting.setItem(i, new ItemStack(itemStack.getItem().getContainerItem()));
                }
            }
        }

        return returnStack;
    }

    @Override
    public RecipeJsonAdapter<?> getAdapter() {
        return new ColourableRecipeJsonAdapter();
    }

    private static class ColourableRecipeJsonAdapter implements RecipeJsonAdapter<ColourableRecipe> {
        @Override
        public ColourableRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new ColourableRecipe();
        }

        public JsonElement serialize(ColourableRecipe src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            obj.addProperty("name", src.toString().replaceFirst("/*$", ""));
            obj.addProperty("type", Registries.RECIPE_TYPES.getKey(src.getClass()));
            return obj;
        }
    }
}
