/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.recipes;

import com.google.gson.*;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.fabric.mixin.ContainerCraftingAccessor;
import dan200.computercraft.shared.TurtleUpgrades;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.turtle.items.ITurtleItem;
import dan200.computercraft.shared.turtle.items.TurtleItemFactory;
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

public final class TurtleUpgradeRecipe extends RecipeEntryCrafting<RecipeSymbol[], ItemStack> implements HasJsonAdapter
{
    public TurtleUpgradeRecipe() {
    }

    @Nonnull
    @Override
    public ItemStack getOutput()
    {
        return Objects.requireNonNull(TurtleItemFactory.create(-1, null, -1, ComputerFamily.NORMAL, null, null, 0, -1));
    }

    @Override
    public boolean matches(ContainerCrafting inventory) {
        return getCraftingResult( inventory ) != null;
    }

    @Override
    public boolean matchesQuery(SearchQuery searchQuery) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(ContainerCrafting inventory) {
        // Scan the grid for a row containing a turtle and 1 or 2 items
        ItemStack leftItem = null;
        ItemStack turtle = null;
        ItemStack rightItem = null;

        int width = ((ContainerCraftingAccessor) inventory).getWidth();
        int height = inventory.getContainerSize() / width;

        for( int y = 0; y < height; y++ )
        {
            if( turtle == null )
            {
                // Search this row for potential turtles
                boolean finishedRow = false;
                for( int x = 0; x < width; x++ )
                {
                    ItemStack item = inventory.getItem( x + y * width );
                    if( item != null )
                    {
                        if( finishedRow )
                        {
                            return null;
                        }

                        if( item.getItem() instanceof ITurtleItem )
                        {
                            // Item is a turtle
                            if( turtle == null )
                            {
                                turtle = item;
                            }
                            else
                            {
                                return null;
                            }
                        }
                        else
                        {
                            // Item is not a turtle
                            if( turtle == null && leftItem == null )
                            {
                                leftItem = item;
                            }
                            else if( turtle != null && rightItem == null )
                            {
                                rightItem = item;
                            }
                            else
                            {
                                return null;
                            }
                        }
                    }
                    else
                    {
                        // Item is empty
                        if( leftItem != null || turtle != null )
                        {
                            finishedRow = true;
                        }
                    }
                }

                // If we found anything, check we found a turtle too
                if( turtle == null && (leftItem != null || rightItem != null) )
                {
                    return null;
                }
            }
            else
            {
                // Turtle is already found, just check this row is empty
                for( int x = 0; x < width; x++ )
                {
                    ItemStack item = inventory.getItem( x + y * width );
                    if( item != null )
                    {
                        return null;
                    }
                }
            }
        }

        // See if we found a turtle + one or more items
        if( turtle == null || leftItem == null && rightItem == null )
        {
            return null;
        }

        // At this point we have a turtle + 1 or 2 items
        // Get the turtle we already have
        ITurtleItem itemTurtle = (ITurtleItem) turtle.getItem();
        ComputerFamily family = itemTurtle.getFamily();
        ITurtleUpgrade[] upgrades = new ITurtleUpgrade[] {
            itemTurtle.getUpgrade( turtle, TurtleSide.LEFT ),
            itemTurtle.getUpgrade( turtle, TurtleSide.RIGHT ),
        };

        // Get the upgrades for the new items
        ItemStack[] items = new ItemStack[] { rightItem, leftItem };
        for( int i = 0; i < 2; i++ )
        {
            if( items[i] != null )
            {
                ITurtleUpgrade itemUpgrade = TurtleUpgrades.get( items[i] );
                if( itemUpgrade == null )
                {
                    return null;
                }
                if( upgrades[i] != null )
                {
                    return null;
                }
                if( !TurtleUpgrades.suitableForFamily( family, itemUpgrade ) )
                {
                    return null;
                }
                upgrades[i] = itemUpgrade;
            }
        }

        // Construct the new stack
        int computerID = itemTurtle.getComputerID( turtle );
        String label = itemTurtle.getLabel( turtle );
        int fuelLevel = itemTurtle.getFuelLevel( turtle );
        int colour = itemTurtle.getColour( turtle );
        int overlay = itemTurtle.getOverlay( turtle );
        return TurtleItemFactory.create( computerID, label, colour, family, upgrades[0], upgrades[1], fuelLevel, overlay );
    }

    @Override
    public int getRecipeSize() {
        return 2;
    }

    @Override
    public ItemStack[] onCraftResult(ContainerCrafting containerCrafting) {
        ItemStack[] returnStack = new ItemStack[9];

        for(int i = 0; i < containerCrafting.getContainerSize(); ++i) {
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
        return new TurtleUpgradeRecipeJsonAdapter();
    }

    private static class TurtleUpgradeRecipeJsonAdapter implements RecipeJsonAdapter<TurtleUpgradeRecipe> {
        @Override
        public TurtleUpgradeRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new TurtleUpgradeRecipe();
        }

        public JsonElement serialize(TurtleUpgradeRecipe src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonObject();
        }
    }
}
