/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.recipe;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import net.minecraft.core.data.registry.recipe.HasJsonAdapter;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.data.registry.recipe.adapter.RecipeCraftingShapedJsonAdapter;
import net.minecraft.core.data.registry.recipe.adapter.RecipeJsonAdapter;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShaped;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerCrafting;

import java.lang.reflect.Type;

public class ImpostorShapedRecipe extends RecipeEntryCraftingShaped implements HasJsonAdapter {
    public ImpostorShapedRecipe(int recipeWidth, int recipeHeight, RecipeSymbol[] input, ItemStack output, boolean consumeContainerItem, boolean allowMirrored) {
        super(recipeWidth, recipeHeight, input, output, consumeContainerItem, allowMirrored);
    }

    public ImpostorShapedRecipe() {
        super();
    }

    @Override
    public boolean matches(ContainerCrafting containerCrafting) {
        return false;
    }

    @Override
    public boolean matchesQuery(SearchQuery query) {
        return false;
    }

    @Override
    public RecipeJsonAdapter<?> getAdapter() {
        return new ImpostorShapedRecipeJsonAdapter();
    }

    private static class ImpostorShapedRecipeJsonAdapter implements RecipeJsonAdapter<ImpostorShapedRecipe> {
        public ImpostorShapedRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            RecipeEntryCraftingShaped recipeEntryCraftingShaped = (new RecipeCraftingShapedJsonAdapter()).deserialize(json, typeOfT, context);

            return new ImpostorShapedRecipe(
                recipeEntryCraftingShaped.recipeWidth,
                recipeEntryCraftingShaped.recipeHeight,
                recipeEntryCraftingShaped.getInput(),
                recipeEntryCraftingShaped.getOutput(),
                recipeEntryCraftingShaped.consumeContainerItem,
                recipeEntryCraftingShaped.allowMirrored
            );
        }

        public JsonElement serialize(ImpostorShapedRecipe src, Type typeOfSrc, JsonSerializationContext context) {
            return (new RecipeCraftingShapedJsonAdapter()).serialize( src, typeOfSrc, context);
        }
    }
}
