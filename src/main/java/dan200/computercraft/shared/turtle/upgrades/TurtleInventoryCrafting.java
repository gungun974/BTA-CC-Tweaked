/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.turtle.upgrades;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.fabric.Helper;
import dan200.computercraft.shared.turtle.blocks.TileTurtle;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerCrafting;
import net.minecraft.core.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TurtleInventoryCrafting extends ContainerCrafting
{
    private final ITurtleAccess turtle;
    private int xStart;
    private int yStart;

    @SuppressWarnings( "ConstantConditions" )
    public TurtleInventoryCrafting( ITurtleAccess turtle )
    {
        // Passing null in here is evil, but we don't have a container present. We override most methods in order to
        // avoid throwing any NPEs.
        super( null, 0, 0 );
        this.turtle = turtle;
        xStart = 0;
        yStart = 0;
    }

    @Nullable
    private ItemStack tryCrafting( int xStart, int yStart )
    {
        this.xStart = xStart;
        this.yStart = yStart;

        // Check the non-relevant parts of the inventory are empty
        for( int x = 0; x < TileTurtle.INVENTORY_WIDTH; x++ )
        {
            for( int y = 0; y < TileTurtle.INVENTORY_HEIGHT; y++ )
            {
                if( x < this.xStart || x >= this.xStart + 3 || y < this.yStart || y >= this.yStart + 3 )
                {
                    if( turtle.getInventory()
                        .getItem( x + y * TileTurtle.INVENTORY_WIDTH )
                        != null)
                    {
                        return null;
                    }
                }
            }
        }

        // Check the actual crafting
        return Registries.RECIPES.findMatchingRecipe(this);
    }

    @Nullable
    public List<ItemStack> doCrafting(World world, int maxCount )
    {
        if( Helper.isClientWorld() )
        {
            return null;
        }

        // Find out what we can craft
        ItemStack recipe = tryCrafting( 0, 0 );
        if( recipe == null )
        {
            recipe = tryCrafting( 0, 1 );
        }
        if( recipe == null )
        {
            recipe = tryCrafting( 1, 0 );
        }
        if( recipe == null )
        {
            recipe = tryCrafting( 1, 1 );
        }
        if( recipe == null )
        {
            return null;
        }

        // Special case: craft(0) just returns an empty list if crafting was possible
        if( maxCount == 0 )
        {
            return Collections.emptyList();
        }

        ArrayList<ItemStack> results = new ArrayList<>();
        int i = 0;
        for (ItemStack result = Registries.RECIPES.findMatchingRecipe(this);
             i < maxCount && result != null && result.itemID == recipe.itemID;
             i++, result = Registries.RECIPES.findMatchingRecipe(this)) {
            Registries.RECIPES.onCraftResult(this);
            results.add(result);
        }


        return results;
    }

    @Override
    public boolean stillValid(Player entityplayer) {
        return turtle.getInventory()
            .stillValid(entityplayer);
    }

    private int modifyIndex( int index )
    {
        int x = xStart + index % getWidth();
        int y = yStart + index / getHeight();
        return x >= 0 && x < TileTurtle.INVENTORY_WIDTH && y >= 0 && y < TileTurtle.INVENTORY_HEIGHT ? x + y * TileTurtle.INVENTORY_WIDTH : -1;
    }

    private int getHeight() {
        return 3;
    }

    private int getWidth() {
        return 3;
    }

    // IInventory implementation

    @Override
    public int getContainerSize() {
        return getWidth() * getHeight();
    }

    @Nullable
    @Override
    public ItemStack getItem(int i) {
        i = modifyIndex( i );
        return turtle.getInventory()
            .getItem( i );
    }

    public ItemStack getItemStackAt(int i, int j) {
        if (i >= 0 && i < this.getHeight()) {
            int k = i + j * this.getHeight();
            return this.getItem(k);
        } else {
            return null;
        }
    }


    @Nullable
    @Override
    public ItemStack removeItem(int i, int j) {
        i = modifyIndex( i );
        return turtle.getInventory()
            .removeItem( i, j );
    }

    @Override
    public void setItem(int i, @Nullable ItemStack stack) {
        i = modifyIndex( i );
        turtle.getInventory()
            .setItem( i, stack );
    }

    public void setSlotContentsAt(int i, int j, ItemStack itemStack) {
        if (i >= 0 && i < this.getWidth()) {
            int k = i + j * this.getHeight();
            this.setItem(k, itemStack);
        }
    }

    @Override
    public void setChanged() {
        turtle.getInventory().setChanged();
    }
}
