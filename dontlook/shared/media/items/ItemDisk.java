/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.media.items;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.ComputerCraft;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.filesystem.IMount;
import dan200.computercraft.api.media.IMedia;
import dan200.computercraft.shared.ComputerCraftRegistry;
import dan200.computercraft.shared.common.IColouredItem;
import dan200.computercraft.shared.util.Colour;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.world.World;

import javax.annotation.Nonnull;

public class ItemDisk extends Item implements IMedia, IColouredItem
{
    private static final String NBT_ID = "DiskId";

    public ItemDisk(NamespaceID namespaceId, int id )
    {
        super(namespaceId, id);
    }

//    @Override
//    public void appendTooltip( @Nonnull ItemStack stack, @Nullable World world, @Nonnull List<Text> list, TooltipContext options )
//    {
//        if( options.isAdvanced() )
//        {
//            int id = getDiskID( stack );
//            if( id >= 0 )
//            {
//                list.add( new TranslatableText( "gui.computercraft.tooltip.disk_id", id ).formatted( Formatting.GRAY ) );
//            }
//        }
//    }
//
//    @Override
//    public void appendStacks( @Nonnull ItemGroup tabs, @Nonnull DefaultedList<ItemStack> list )
//    {
//        if( !isIn( tabs ) )
//        {
//            return;
//        }
//        for( int colour = 0; colour < 16; colour++ )
//        {
//            list.add( createFromIDAndColour( -1, null, Colour.VALUES[colour].getHex() ) );
//        }
//    }

    @Nonnull
    public static ItemStack createFromIDAndColour( int id, String label, int colour )
    {
        ItemStack stack = new ItemStack( ComputerCraftRegistry.ModItems.DISK );
        setDiskID( stack, id );
        ComputerCraftRegistry.ModItems.DISK.setLabel( stack, label );
        IColouredItem.setColourBasic( stack, colour );
        return stack;
    }

    private static void setDiskID( @Nonnull ItemStack stack, int id )
    {
        if( id >= 0 )
        {
            stack.getData()
                .putInt( NBT_ID, id );
        }
    }

    public static int getDiskID( @Nonnull ItemStack stack )
    {
        CompoundTag nbt = stack.getData();
        return nbt.containsKey( NBT_ID ) ? nbt.getInteger( NBT_ID ) : -1;
    }

    @Override
    public String getLabel( @Nonnull ItemStack stack )
    {
        return stack.hasCustomName() ? stack.getCustomName() : null;
    }

    @Override
    public boolean setLabel( @Nonnull ItemStack stack, String label )
    {
        if( label != null )
        {
            stack.setCustomName( label );
        }
        else
        {
            stack.removeCustomName();
        }
        return true;
    }

    @Override
    public IMount createDataMount( @Nonnull ItemStack stack, @Nonnull World world )
    {
        int diskID = getDiskID( stack );
        if( diskID < 0 )
        {
            diskID = ComputerCraftAPI.createUniqueNumberedSaveDir( world, "disk" );
            setDiskID( stack, diskID );
        }
        return ComputerCraftAPI.createSaveDirMount( world, "disk/" + diskID, ComputerCraft.floppySpaceLimit );
    }

    @Override
    public int getColour( @Nonnull ItemStack stack )
    {
        int colour = IColouredItem.getColourBasic( stack );
        return colour == -1 ? Colour.WHITE.getHex() : colour;
    }
}
