/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.computer.recipe;

import com.google.gson.*;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.items.IComputerItem;
import net.minecraft.core.data.registry.recipe.HasJsonAdapter;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.adapter.RecipeCraftingShapedJsonAdapter;
import net.minecraft.core.data.registry.recipe.adapter.RecipeJsonAdapter;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShaped;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class ComputerUpgradeRecipe extends ComputerFamilyRecipe implements HasJsonAdapter {
    public ComputerUpgradeRecipe(int recipeWidth, int recipeHeight, RecipeSymbol[] input, ItemStack output, boolean consumeContainerItem, boolean allowMirrored, ComputerFamily family) {
        super(recipeWidth, recipeHeight, input, output, consumeContainerItem, allowMirrored, family);
    }

    public ComputerUpgradeRecipe() {
        super();
    }

    @NotNull
    @Override
    protected ItemStack convert(@NotNull IComputerItem item, @NotNull ItemStack stack) {
        return item.withFamily(stack, getFamily());
    }

    @Override
    public RecipeJsonAdapter<?> getAdapter() {
        return new ComputerUpgradeRecipeJsonAdapter();
    }

    private static class ComputerUpgradeRecipeJsonAdapter implements RecipeJsonAdapter<ComputerUpgradeRecipe> {
        @Override
        public ComputerUpgradeRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            RecipeEntryCraftingShaped recipeEntryCraftingShaped = (new RecipeCraftingShapedJsonAdapter()).deserialize(json, typeOfT, context);
            JsonObject obj = json.getAsJsonObject();
            ComputerFamily family = ComputerFamily.getFamily(obj.get("family").getAsString());

            return new ComputerUpgradeRecipe(
                recipeEntryCraftingShaped.recipeWidth,
                recipeEntryCraftingShaped.recipeHeight,
                recipeEntryCraftingShaped.getInput(),
                recipeEntryCraftingShaped.getOutput(),
                recipeEntryCraftingShaped.consumeContainerItem,
                recipeEntryCraftingShaped.allowMirrored,
                family
            );
        }

        public JsonElement serialize(ComputerUpgradeRecipe src, Type typeOfSrc, JsonSerializationContext context) {
            JsonElement recipeEntryCraftingShaped = (new RecipeCraftingShapedJsonAdapter()).serialize(src, typeOfSrc, context);

            JsonObject obj = recipeEntryCraftingShaped.getAsJsonObject();

            obj.add("family", context.serialize(src.getFamily().name()));

            return obj;
        }
    }
}
