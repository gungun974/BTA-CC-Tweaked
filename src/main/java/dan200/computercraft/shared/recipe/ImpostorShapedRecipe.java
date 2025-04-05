/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.recipe;

import com.google.gson.*;
import dan200.computercraft.ComputerCraft;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.recipe.HasJsonAdapter;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.data.registry.recipe.adapter.RecipeCraftingShapedJsonAdapter;
import net.minecraft.core.data.registry.recipe.adapter.RecipeJsonAdapter;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShaped;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
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
            JsonObject obj = json.getAsJsonObject();
            RecipeEntryCraftingShaped recipeEntryCraftingShaped = (new RecipeCraftingShapedJsonAdapter()).deserialize(json, typeOfT, context);

            ItemStack result = context.deserialize(obj.get("result").getAsJsonObject(), ItemStack.class);

            convertUpgradeId(result, "LeftUpgrade");
            convertUpgradeId(result, "RightUpgrade");
            convertUpgradeId(result, "Upgrade");

            return new ImpostorShapedRecipe(
                recipeEntryCraftingShaped.recipeWidth,
                recipeEntryCraftingShaped.recipeHeight,
                recipeEntryCraftingShaped.getInput(),
                result,
                recipeEntryCraftingShaped.consumeContainerItem,
                recipeEntryCraftingShaped.allowMirrored
            );
        }

        private static void convertUpgradeId(ItemStack result, String key) {
            if (result.getData().containsKey(key)) {
                String currentKey = result.getData().getString(key);

                Integer itemId = Item.nameToIdMap.get(currentKey);

                if (itemId != null) {
                    result.getData().putInt(key, itemId);
                }
                else {
                    @Deprecated
                    Integer blockId = Blocks.keyToIdMap.get(currentKey);
                    if (blockId != null) {
                        result.getData().putInt(key, blockId);
                    }
                }
            }
        }

        public JsonElement serialize(ImpostorShapedRecipe src, Type typeOfSrc, JsonSerializationContext context) {
            return (new RecipeCraftingShapedJsonAdapter()).serialize( src, typeOfSrc, context);
        }
    }
}
