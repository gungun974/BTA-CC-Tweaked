/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.shared.common.ComputerCraftTurtleUpgrades;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import net.minecraft.core.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;

public final class TurtleUpgrades
{
    private static class Wrapper
    {
        final ITurtleUpgrade upgrade;
        final int id;
        final String modId;
        boolean enabled;

        Wrapper( ITurtleUpgrade upgrade )
        {
            this.upgrade = upgrade;
            id = upgrade.getUpgradeID();
            // TODO This should be the mod id of the mod the peripheral comes from
            modId = ComputerCraft.MOD_ID;
            enabled = true;
        }
    }

    private static ITurtleUpgrade[] vanilla;

    private static final Map<Integer, ITurtleUpgrade> upgrades = new HashMap<>();
    private static final IdentityHashMap<ITurtleUpgrade, Wrapper> wrappers = new IdentityHashMap<>();
    private static boolean needsRebuild;

    private TurtleUpgrades() {}

    public static void register( @Nonnull ITurtleUpgrade upgrade )
    {
        Objects.requireNonNull( upgrade, "upgrade cannot be null" );
        rebuild();

        Wrapper wrapper = new Wrapper( upgrade );
        int id = wrapper.id;
        ITurtleUpgrade existing = upgrades.get( id );
        if( existing != null )
        {
            throw new IllegalStateException( "Error registering '" + upgrade.getUnlocalisedAdjective() + " Turtle'. Upgrade ID '" + id + "' is already registered by '" + existing.getUnlocalisedAdjective() + " Turtle'" );
        }

        upgrades.put( id, upgrade );
        wrappers.put( upgrade, wrapper );
    }

    @Nullable
    public static ITurtleUpgrade get( int id )
    {
        rebuild();
        return upgrades.get( id );
    }

    @Nullable
    public static String getOwner( @Nonnull ITurtleUpgrade upgrade )
    {
        Wrapper wrapper = wrappers.get( upgrade );
        return wrapper != null ? wrapper.modId : null;
    }

    public static ITurtleUpgrade get( ItemStack stack )
    {
        if( stack == null ) return null;

        for( Wrapper wrapper : wrappers.values() )
        {
            if( !wrapper.enabled ) continue;

            ItemStack craftingStack = wrapper.upgrade.getCraftingItem();
            if( craftingStack != null && craftingStack.getItem().equals(stack.getItem()) && wrapper.upgrade.isItemSuitable( stack ) )
            {
                return wrapper.upgrade;
            }
        }

        return null;
    }

    public static Stream<ITurtleUpgrade> getVanillaUpgrades()
    {
        if( vanilla == null )
        {
            //TODO: Upgrade the turtle !!!
            vanilla = new ITurtleUpgrade[] {
                // ComputerCraft upgrades
                ComputerCraftTurtleUpgrades.wirelessModemNormal,
                ComputerCraftTurtleUpgrades.wirelessModemAdvanced,
                ComputerCraftTurtleUpgrades.speaker,

                // Vanilla Minecraft upgrades
                ComputerCraftTurtleUpgrades.diamondPickaxe,
                ComputerCraftTurtleUpgrades.diamondAxe,
                ComputerCraftTurtleUpgrades.diamondSword,
                ComputerCraftTurtleUpgrades.diamondShovel,
                ComputerCraftTurtleUpgrades.diamondHoe,
                ComputerCraftTurtleUpgrades.craftingTable,

                ComputerCraftTurtleUpgrades.goldenPickaxe,
            };
        }

        return Arrays.stream( vanilla ).filter( x -> x != null && wrappers.get( x ).enabled );
    }

    public static Stream<ITurtleUpgrade> getUpgrades()
    {
        return wrappers.values().stream().filter( x -> x.enabled ).map( x -> x.upgrade );
    }

    public static boolean suitableForFamily( ComputerFamily family, ITurtleUpgrade upgrade )
    {
        return true;
    }

    /**
     * Rebuild the cache of turtle upgrades. This is done before querying the cache or registering new upgrades.
     */
    private static void rebuild()
    {
        if( !needsRebuild ) return;

        upgrades.clear();
        for( Wrapper wrapper : wrappers.values() )
        {
            if( !wrapper.enabled ) continue;

            ITurtleUpgrade existing = upgrades.get( wrapper.id );
            if( existing != null )
            {
                ComputerCraft.log.error( "Error registering '" + wrapper.upgrade.getUnlocalisedAdjective() + " Turtle'." +
                    " Upgrade ID '" + wrapper.id + "' is already registered by '" + existing.getUnlocalisedAdjective() + " Turtle'" );
                continue;
            }

            upgrades.put( wrapper.id, wrapper.upgrade );
        }

        needsRebuild = false;
    }

    public static void enable( ITurtleUpgrade upgrade )
    {
        Wrapper wrapper = wrappers.get( upgrade );
        if( wrapper.enabled ) return;

        wrapper.enabled = true;
        needsRebuild = true;
    }

    public static void disable( ITurtleUpgrade upgrade )
    {
        Wrapper wrapper = wrappers.get( upgrade );
        if( !wrapper.enabled ) return;

        wrapper.enabled = false;
        upgrades.remove( wrapper.id );
    }

    public static void remove( ITurtleUpgrade upgrade )
    {
        wrappers.remove( upgrade );
        needsRebuild = true;
    }
}
