/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.recipe;

import com.google.gson.*;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.*;
import net.minecraft.core.data.registry.recipe.adapter.RecipeJsonAdapter;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerCrafting;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class ImpostorShapelessRecipe extends RecipeEntryCrafting<List<RecipeSymbol>, ItemStack> implements HasJsonAdapter {
    public ImpostorShapelessRecipe(List<RecipeSymbol> input, ItemStack output) {
        super(input, output);
    }

    public ImpostorShapelessRecipe() {
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
    public ItemStack[] onCraftResult(ContainerCrafting containerCrafting) {
        ItemStack[] returnStack = new ItemStack[9];

        for (int i = 0; i < containerCrafting.getContainerSize(); i++) {
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
    public ItemStack getCraftingResult(ContainerCrafting containerCrafting) {
        return this.getOutput().copy();
    }

    @Override
    public int getRecipeSize() {
        return this.getInput().size();
    }

    @Override
    public RecipeJsonAdapter<?> getAdapter() {
        return new ImpostorShapelessRecipeJsonAdapter();
    }

    private static class ImpostorShapelessRecipeJsonAdapter implements RecipeJsonAdapter<ImpostorShapelessRecipe> {
        @Override
        public ImpostorShapelessRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            List<RecipeSymbol> symbols = obj.get("inputs").getAsJsonArray().asList().stream().map((E)->context.<RecipeSymbol>deserialize(E,RecipeSymbol.class)).collect(Collectors.toList());
            ItemStack result = context.deserialize(obj.get("result").getAsJsonObject(),ItemStack.class);
            return new ImpostorShapelessRecipe(symbols,result);
        }

        @Override
        public JsonElement serialize(ImpostorShapelessRecipe src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            obj.addProperty("name",src.toString());
            obj.addProperty("type", Registries.RECIPE_TYPES.getKey(src.getClass()));
            List<RecipeSymbol> symbols = src.getInput();
            obj.add("inputs",context.serialize(symbols));
            obj.add("result",context.serialize(src.getOutput()));
            return obj;
        }
    }
}
