package dan200.computercraft.shared.computer.items;

import dan200.computercraft.shared.common.ComputerCraftItems;
import dan200.computercraft.shared.computer.blocks.TileComputerBase;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import net.minecraft.core.item.ItemStack;

public final class ComputerItemFactory
{
    private ComputerItemFactory() {}

    public static ItemStack create(TileComputerBase tile )
    {
        return create( tile.getComputerID(), tile.getLabel(), tile.getFamily() );
    }

    public static ItemStack create( int id, String label, ComputerFamily family )
    {
        switch( family )
        {
            case NORMAL:
                return ComputerCraftItems.COMPUTER_NORMAL.create( id, label );
            case ADVANCED:
                return ComputerCraftItems.COMPUTER_ADVANCED.create( id, label );
//            case COMMAND:
//                return ComputerCraftRegistry.ModItems.COMPUTER_COMMAND.create( id, label );
            default:
                return null;
        }
    }
}
