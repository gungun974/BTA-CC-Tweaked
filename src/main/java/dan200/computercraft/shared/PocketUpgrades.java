/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared;

import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.shared.common.ComputerCraftPocketUpgrades;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenCustomHashMap;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.core.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public final class PocketUpgrades
{
    private static final Map<Integer, IPocketUpgrade> upgrades = new HashMap<>();
    //private static final Map<IPocketUpgrade, String> upgradeOwners = new Object2ObjectLinkedOpenCustomHashMap<>();

    private PocketUpgrades() {}

    public static synchronized void register( @Nonnull IPocketUpgrade upgrade )
    {
        Objects.requireNonNull( upgrade, "upgrade cannot be null" );

        int id = upgrade.getUpgradeID();
        IPocketUpgrade existing = upgrades.get( id );
        if( existing != null )
        {
            throw new IllegalStateException( "Error registering '" + upgrade.getUnlocalisedAdjective() + " pocket computer'. UpgradeID '" + id + "' is already registered by '" + existing.getUnlocalisedAdjective() + " pocket computer'" );
        }

        upgrades.put( id, upgrade );

//        // Infer the mod id by the identifier of the upgrade. This is not how the forge api works, so it may break peripheral mods using the api.
//        // TODO: get the mod id of the mod that is currently being loaded.
//        ModContainer mc = FabricLoader.getInstance().getModContainer( upgrade.getUpgradeID().getNamespace() ).orElseGet( null );
//        if( mc != null && mc.getMetadata().getId() != null ) upgradeOwners.put( upgrade, mc.getMetadata().getId() );
    }

    public static IPocketUpgrade get( int id )
    {
        return upgrades.get( id );
    }

    public static IPocketUpgrade get( ItemStack stack )
    {
        if( stack == null ) return null;

        for( IPocketUpgrade upgrade : upgrades.values() )
        {
            ItemStack craftingStack = upgrade.getCraftingItem();
            if( craftingStack != null && craftingStack.getItem().equals(stack.getItem()) && upgrade.isItemSuitable( stack ) )
            {
                return upgrade;
            }
        }

        return null;
    }

//    @Nullable
//    public static String getOwner( IPocketUpgrade upgrade )
//    {
//        return upgradeOwners.get( upgrade );
//    }

    public static Iterable<IPocketUpgrade> getVanillaUpgrades()
    {
        List<IPocketUpgrade> vanilla = new ArrayList<>();
        vanilla.add( ComputerCraftPocketUpgrades.wirelessModemNormal );
        vanilla.add( ComputerCraftPocketUpgrades.wirelessModemAdvanced );
        vanilla.add( ComputerCraftPocketUpgrades.speaker );
        return vanilla;
    }

    public static Iterable<IPocketUpgrade> getUpgrades()
    {
        return Collections.unmodifiableCollection( upgrades.values() );
    }
}
