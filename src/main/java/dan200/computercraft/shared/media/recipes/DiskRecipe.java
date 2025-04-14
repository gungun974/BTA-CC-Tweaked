/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.media.recipes;

import com.google.gson.*;
import dan200.computercraft.shared.media.items.ItemDisk;
import dan200.computercraft.shared.util.Colour;
import dan200.computercraft.shared.util.ColourTracker;
import dan200.computercraft.shared.util.ColourUtils;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.HasJsonAdapter;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.data.registry.recipe.adapter.RecipeJsonAdapter;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.player.inventory.container.ContainerCrafting;
import net.minecraft.core.util.helper.DyeColor;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;

public class DiskRecipe extends RecipeEntryCrafting<RecipeSymbol[], ItemStack> implements HasJsonAdapter {
    public DiskRecipe() {

    }

    @Override
    public boolean matches(ContainerCrafting inv) {
        boolean paperFound = false;
        boolean redstoneFound = false;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);

            if (stack != null) {
                if (stack.getItem().equals(Items.PAPER)) {
                    if (paperFound) {
                        return false;
                    }
                    paperFound = true;
                } else if (stack.getItem().equals(Items.DUST_REDSTONE)) {
                    if (redstoneFound) {
                        return false;
                    }
                    redstoneFound = true;
                } else if (ColourUtils.getStackColour(stack) == null) {
                    return false;
                }
            }
        }

        return redstoneFound && paperFound;
    }

    @Override
    public boolean matchesQuery(SearchQuery searchQuery) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(ContainerCrafting inv) {
        ColourTracker tracker = new ColourTracker();

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);

            if (stack == null) {
                continue;
            }

            if (!stack.getItem().equals(Items.PAPER) && !stack.getItem().equals(Items.DUST_REDSTONE)) {
                DyeColor dye = ColourUtils.getStackColour(stack);
                if (dye != null) tracker.addColour(dye);
            }
        }

        return ItemDisk.createFromIDAndColour(-1, null, tracker.hasColour() ? tracker.getColour() : Colour.BLUE.getHex());
    }

    @Override
    public int getRecipeSize() {
        return 0;
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

    @Nonnull
    @Override
    public ItemStack getOutput() {
        return ItemDisk.createFromIDAndColour(-1, null, Colour.BLUE.getHex());
    }


    @Override
    public RecipeJsonAdapter<?> getAdapter() {
        return new DiskRecipeJsonAdapter();
    }

    private static class DiskRecipeJsonAdapter implements RecipeJsonAdapter<DiskRecipe> {
        @Override
        public DiskRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new DiskRecipe();
        }

        public JsonElement serialize(DiskRecipe src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            obj.addProperty("name", src.toString().replaceFirst("/*$", ""));
            obj.addProperty("type", Registries.RECIPE_TYPES.getKey(src.getClass()));
            return obj;
        }
    }
}
