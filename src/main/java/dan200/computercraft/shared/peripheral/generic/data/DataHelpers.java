/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.generic.data;

import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class DataHelpers {
    private DataHelpers() {
    }

//    @Nonnull
//    public static Map<String, Boolean> getTags( @Nonnull Collection<Identifier> tags )
//    {
//        Map<String, Boolean> result = new HashMap<>( tags.size() );
//        for( Identifier location : tags ) result.put( location.toString(), true );
//        return result;
//    }

    @Nullable
    public static int getId(@Nonnull Block block) {
        return block.id();
    }

    @Nullable
    public static int getId(@Nonnull Item item) {
        return item.id;
    }
}
