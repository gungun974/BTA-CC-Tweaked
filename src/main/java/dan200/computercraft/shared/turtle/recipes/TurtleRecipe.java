/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.recipes;

import com.google.gson.*;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.items.IComputerItem;
import dan200.computercraft.shared.computer.recipe.ComputerFamilyRecipe;
import dan200.computercraft.shared.turtle.items.TurtleItemFactory;
import net.minecraft.core.data.registry.recipe.HasJsonAdapter;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.adapter.RecipeCraftingShapedJsonAdapter;
import net.minecraft.core.data.registry.recipe.adapter.RecipeJsonAdapter;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShaped;
import net.minecraft.core.item.ItemStack;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;
import java.util.Objects;

public final class TurtleRecipe extends ComputerFamilyRecipe implements HasJsonAdapter
{
    public TurtleRecipe(int recipeWidth, int recipeHeight, RecipeSymbol[] input, ItemStack output, boolean consumeContainerItem, boolean allowMirrored, ComputerFamily family) {
        super(recipeWidth, recipeHeight, input, output, consumeContainerItem, allowMirrored, family);
    }

    public TurtleRecipe() {
        super();
    }

    @Nonnull
    @Override
    protected ItemStack convert( @Nonnull IComputerItem item, @Nonnull ItemStack stack )
    {
        int computerID = item.getComputerID( stack );
        String label = item.getLabel( stack );

        return Objects.requireNonNull(TurtleItemFactory.create(computerID, label, -1, getFamily(), null, null, 0, -1));
    }

    @Override
    public RecipeJsonAdapter<?> getAdapter() {
        return new TurtleRecipeJsonAdapter();
    }

    private static class TurtleRecipeJsonAdapter implements RecipeJsonAdapter<TurtleRecipe> {
        @Override
        public TurtleRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            RecipeEntryCraftingShaped recipeEntryCraftingShaped = (new RecipeCraftingShapedJsonAdapter()).deserialize(json, typeOfT, context);
            JsonObject obj = json.getAsJsonObject();
            ComputerFamily family = ComputerFamily.getFamily(obj.get("family").getAsString());

            return new TurtleRecipe(
                recipeEntryCraftingShaped.recipeWidth,
                recipeEntryCraftingShaped.recipeHeight,
                recipeEntryCraftingShaped.getInput(),
                recipeEntryCraftingShaped.getOutput(),
                recipeEntryCraftingShaped.consumeContainerItem,
                recipeEntryCraftingShaped.allowMirrored,
                family
            );
        }

        public JsonElement serialize(TurtleRecipe src, Type typeOfSrc, JsonSerializationContext context) {
            JsonElement recipeEntryCraftingShaped = (new RecipeCraftingShapedJsonAdapter()).serialize( src, typeOfSrc, context);

            JsonObject obj = recipeEntryCraftingShaped.getAsJsonObject();

            obj.add("family", context.serialize(src.getFamily().name()));

            return obj;
        }
    }
}
