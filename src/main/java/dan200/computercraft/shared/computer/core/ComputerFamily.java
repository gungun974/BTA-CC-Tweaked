/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.computer.core;

public enum ComputerFamily
{
    NORMAL, ADVANCED, COMMAND;

    public static ComputerFamily getFamily( String familyName )
    {
        for( ComputerFamily family : ComputerFamily.values() )
        {
            if( family.name()
                .equalsIgnoreCase( familyName ) )
            {
                return family;
            }
        }

        throw new RuntimeException( "Unknown computer family '" + familyName + '"');
    }
}
