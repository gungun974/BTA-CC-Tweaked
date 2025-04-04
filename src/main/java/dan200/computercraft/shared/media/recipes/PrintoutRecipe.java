/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.media.recipes;

import com.google.gson.*;
import dan200.computercraft.fabric.mixin.ContainerCraftingAccessor;
import dan200.computercraft.shared.media.items.ItemPrintout;
import net.minecraft.core.data.registry.recipe.HasJsonAdapter;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.data.registry.recipe.adapter.RecipeJsonAdapter;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.player.inventory.container.ContainerCrafting;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;

public final class PrintoutRecipe extends RecipeEntryCrafting<RecipeSymbol[], ItemStack> implements HasJsonAdapter
{
    public PrintoutRecipe()
    {
    }

    @Nonnull
    @Override
    public ItemStack getOutput()
    {
        return ItemPrintout.createMultipleFromTitleAndText( null, null, null );
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
        // See if we match the recipe, and extract the input disk ID and dye colour
        int numPages = 0;
        int numPrintouts = 0;
        ItemStack[] printouts = null;
        boolean stringFound = false;
        boolean leatherFound = false;
        boolean printoutFound = false;

        int width = ((ContainerCraftingAccessor) inventory).getWidth();
        int height = inventory.getContainerSize() / width;

        for( int y = 0; y < height; y++ )
        {
            for( int x = 0; x < width; x++ )
            {
                ItemStack stack = inventory.getItem( x + y * width );
                if( stack != null )
                {
                    if( stack.getItem() instanceof ItemPrintout && ((ItemPrintout) stack.getItem()).getType() != ItemPrintout.Type.BOOK )
                    {
                        if( printouts == null )
                        {
                            printouts = new ItemStack[9];
                        }
                        printouts[numPrintouts] = stack;
                        numPages += ItemPrintout.getPageCount( stack );
                        numPrintouts++;
                        printoutFound = true;
                    }
                    else if (stack.getItem().equals(Items.PAPER))
                    {
                        if( printouts == null )
                        {
                            printouts = new ItemStack[9];
                        }
                        printouts[numPrintouts] = stack;
                        numPages++;
                        numPrintouts++;
                    }
                    else if( stack.getItem().equals(Items.STRING) && !stringFound )
                    {
                        stringFound = true;
                    }
                    else if( stack.getItem().equals(Items.LEATHER) && !leatherFound )
                    {
                        leatherFound = true;
                    }
                    else
                    {
                        return null;
                    }
                }
            }
        }

        // Build some pages with what was passed in
        if( numPages <= ItemPrintout.MAX_PAGES && stringFound && printoutFound && numPrintouts >= (leatherFound ? 1 : 2) )
        {
            String[] text = new String[numPages * ItemPrintout.LINES_PER_PAGE];
            String[] colours = new String[numPages * ItemPrintout.LINES_PER_PAGE];
            int line = 0;

            for( int printout = 0; printout < numPrintouts; printout++ )
            {
                ItemStack stack = printouts[printout];
                if( stack.getItem() instanceof ItemPrintout )
                {
                    // Add a printout
                    String[] pageText = ItemPrintout.getText( printouts[printout] );
                    String[] pageColours = ItemPrintout.getColours( printouts[printout] );
                    for( int pageLine = 0; pageLine < pageText.length; pageLine++ )
                    {
                        text[line] = pageText[pageLine];
                        colours[line] = pageColours[pageLine];
                        line++;
                    }
                }
                else
                {
                    // Add a blank page
                    for( int pageLine = 0; pageLine < ItemPrintout.LINES_PER_PAGE; pageLine++ )
                    {
                        text[line] = "";
                        colours[line] = "";
                        line++;
                    }
                }
            }

            String title = null;
            if( printouts[0].getItem() instanceof ItemPrintout )
            {
                title = ItemPrintout.getTitle( printouts[0] );
            }

            if( leatherFound )
            {
                return ItemPrintout.createBookFromTitleAndText( title, text, colours );
            }
            else
            {
                return ItemPrintout.createMultipleFromTitleAndText( title, text, colours );
            }
        }

        return null;
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
        return new PrintoutRecipeJsonAdapter();
    }

    private static class PrintoutRecipeJsonAdapter implements RecipeJsonAdapter<PrintoutRecipe> {
        @Override
        public PrintoutRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new PrintoutRecipe();
        }

        public JsonElement serialize(PrintoutRecipe src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonObject();
        }
    }
}
