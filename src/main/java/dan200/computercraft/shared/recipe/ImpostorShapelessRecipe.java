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
import net.minecraft.core.data.registry.recipe.adapter.RecipeCraftingShapelessJsonAdapter;
import net.minecraft.core.data.registry.recipe.adapter.RecipeJsonAdapter;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShapeless;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerCrafting;

import java.lang.reflect.Type;
import java.util.List;

public class ImpostorShapelessRecipe extends RecipeEntryCraftingShapeless implements HasJsonAdapter {
    public ImpostorShapelessRecipe(List<RecipeSymbol> input, ItemStack output) {
        super(input, output);
    }

    public ImpostorShapelessRecipe() {
        super();
    }

    @Override
    public boolean matches(ContainerCrafting containerCrafting) {
        return false;
    }

    @Override
    public RecipeJsonAdapter<?> getAdapter() {
        return new ImpostorShapelessRecipeJsonAdapter();
    }

    private static class ImpostorShapelessRecipeJsonAdapter implements RecipeJsonAdapter<ImpostorShapelessRecipe> {
        public ImpostorShapelessRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            RecipeEntryCraftingShapeless recipeEntryCraftingShapeless = (new RecipeCraftingShapelessJsonAdapter()).deserialize(json, typeOfT, context);

            return new ImpostorShapelessRecipe(
                recipeEntryCraftingShapeless.getInput(),
                recipeEntryCraftingShapeless.getOutput()
            );
        }

        public JsonElement serialize(ImpostorShapelessRecipe src, Type typeOfSrc, JsonSerializationContext context) {
            return (new RecipeCraftingShapelessJsonAdapter()).serialize(src, typeOfSrc, context);
        }
    }
}
