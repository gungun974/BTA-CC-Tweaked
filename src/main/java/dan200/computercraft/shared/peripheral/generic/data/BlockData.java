/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.generic.data;

import javax.annotation.Nonnull;
import java.util.Map;

public class BlockData {
    @Nonnull
    public static <T extends Map<? super String, Object>> T fill(@Nonnull T data, int id, int metadata) {
        data.put("id", id);
        data.put("metadata", metadata);

//        Map<Object, Object> stateTable = new HashMap<>();
//        for( ImmutableMap.Entry<Property<?>, ? extends Comparable<?>> entry : state.getEntries().entrySet() )
//        {
//            Property<?> property = entry.getKey();
//            stateTable.put( property.getName(), getPropertyValue( property, entry.getValue() ) );
//        }
//        data.put( "state", stateTable );

        return data;
    }
}
