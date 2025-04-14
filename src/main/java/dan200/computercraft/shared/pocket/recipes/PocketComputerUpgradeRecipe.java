/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.pocket.recipes;

import com.google.gson.*;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.fabric.mixin.ContainerCraftingAccessor;
import dan200.computercraft.shared.PocketUpgrades;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.pocket.items.ItemPocketComputer;
import dan200.computercraft.shared.pocket.items.PocketComputerItemFactory;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.HasJsonAdapter;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.data.registry.recipe.adapter.RecipeJsonAdapter;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerCrafting;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;
import java.util.Objects;

public final class PocketComputerUpgradeRecipe extends RecipeEntryCrafting<RecipeSymbol[], ItemStack> implements HasJsonAdapter {
    public PocketComputerUpgradeRecipe() {
    }

    @Nonnull
    @Override
    public ItemStack getOutput() {
        return Objects.requireNonNull(PocketComputerItemFactory.create(-1, null, -1, ComputerFamily.NORMAL, null));
    }

    @Override
    public boolean matches(ContainerCrafting inventory) {
        return getCraftingResult(inventory) != null;
    }

    @Override
    public boolean matchesQuery(SearchQuery searchQuery) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(ContainerCrafting inventory) {
        // Scan the grid for a pocket computer
        ItemStack computer = null;
        int computerX = -1;
        int computerY = -1;

        int width = ((ContainerCraftingAccessor) inventory).getWidth();
        int height = inventory.getContainerSize() / width;

        computer:
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                ItemStack item = inventory.getItem(x + y * width);
                if (item != null && item.getItem() instanceof ItemPocketComputer) {
                    computer = item;
                    computerX = x;
                    computerY = y;
                    break computer;
                }
            }
        }

        if (computer == null) {
            return null;
        }

        ItemPocketComputer itemComputer = (ItemPocketComputer) computer.getItem();
        if (ItemPocketComputer.getUpgrade(computer) != null) {
            return null;
        }

        // Check for upgrades around the item
        IPocketUpgrade upgrade = null;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                ItemStack item = inventory.getItem(x + y * width);
                if (x == computerX && y == computerY) {
                    continue;
                }

                if (x == computerX && y == computerY - 1) {
                    upgrade = PocketUpgrades.get(item);
                    if (upgrade == null) {
                        return null;
                    }
                } else if (item != null) {
                    return null;
                }
            }
        }

        if (upgrade == null) {
            return null;
        }

        // Construct the new stack
        ComputerFamily family = itemComputer.getFamily();
        int computerID = itemComputer.getComputerID(computer);
        String label = itemComputer.getLabel(computer);
        int colour = itemComputer.getColour(computer);
        return Objects.requireNonNull(PocketComputerItemFactory.create(computerID, label, colour, family, upgrade));
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
        return new PocketComputerUpgradeRecipeJsonAdapter();
    }

    private static class PocketComputerUpgradeRecipeJsonAdapter implements RecipeJsonAdapter<PocketComputerUpgradeRecipe> {
        @Override
        public PocketComputerUpgradeRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new PocketComputerUpgradeRecipe();
        }

        public JsonElement serialize(PocketComputerUpgradeRecipe src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            obj.addProperty("name", src.toString().replaceFirst("/*$", ""));
            obj.addProperty("type", Registries.RECIPE_TYPES.getKey(src.getClass()));
            return obj;
        }
    }
}
