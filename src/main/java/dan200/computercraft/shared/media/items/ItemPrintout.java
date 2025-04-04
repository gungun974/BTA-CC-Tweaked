/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.media.items;

import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.fabric.Helper;
import dan200.computercraft.shared.common.ComputerCraftItems;
import dan200.computercraft.shared.network.client.OpenGuiPrintoutMessage;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.NetworkManager;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.world.World;
import turniplabs.halplibe.helper.NetworkHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

import javax.annotation.Nonnull;

public class ItemPrintout extends Item
{
    public static final int LINES_PER_PAGE = 21;
    public static final int LINE_MAX_LENGTH = 25;
    public static final int MAX_PAGES = 16;
    private static final String NBT_TITLE = "Title";
    private static final String NBT_PAGES = "Pages";
    private static final String NBT_LINE_TEXT = "Text";
    private static final String NBT_LINE_COLOUR = "Color";
    private final Type type;

    public ItemPrintout(NamespaceID namespaceId, int id, Type type) {
        super(namespaceId, id);
        this.type = type;
    }

    @Nonnull
    public static ItemStack createSingleFromTitleAndText(String title, String[] text, String[] colours )
    {
        return ComputerCraftItems.PRINTED_PAGE.createFromTitleAndText( title, text, colours );
    }

    @Nonnull
    private ItemStack createFromTitleAndText( String title, String[] text, String[] colours )
    {
        ItemStack stack = new ItemStack( this );

        CompoundTag tag = stack.getData();

        // Build NBT
        if( title != null )
        {
            tag .putString( NBT_TITLE, title );
        }
        if( text != null )
        {
            tag.putInt( NBT_PAGES, text.length / LINES_PER_PAGE );
            for( int i = 0; i < text.length; i++ )
            {
                if( text[i] != null )
                {
                    tag.putString( NBT_LINE_TEXT + i, text[i] );
                }
            }
        }
        if( colours != null )
        {
            for( int i = 0; i < colours.length; i++ )
            {
                if( colours[i] != null )
                {
                    tag.putString( NBT_LINE_COLOUR + i, colours[i] );
                }
            }
        }

        stack.setData(tag);

        return stack;
    }

    @Nonnull
    public static ItemStack createMultipleFromTitleAndText( String title, String[] text, String[] colours )
    {
        return ComputerCraftItems.PRINTED_PAGES.createFromTitleAndText( title, text, colours );
    }

    @Nonnull
    public static ItemStack createBookFromTitleAndText( String title, String[] text, String[] colours )
    {
        return ComputerCraftItems.PRINTED_BOOK.createFromTitleAndText( title, text, colours );
    }

    public static String[] getText( @Nonnull ItemStack stack )
    {
        return getLines( stack, NBT_LINE_TEXT );
    }

    private static String[] getLines( @Nonnull ItemStack stack, String prefix )
    {
        CompoundTag nbt = stack.getData();
        int numLines = getPageCount( stack ) * LINES_PER_PAGE;
        String[] lines = new String[numLines];
        for( int i = 0; i < lines.length; i++ )
        {
            lines[i] = nbt.getString( prefix + i );
        }
        return lines;
    }

    public static int getPageCount( @Nonnull ItemStack stack )
    {
        CompoundTag nbt = stack.getData();
        return nbt.containsKey( NBT_PAGES ) ? nbt.getInteger( NBT_PAGES ) : 1;
    }

    public static String[] getColours( @Nonnull ItemStack stack )
    {
        return getLines( stack, NBT_LINE_COLOUR );
    }

    @Override
    public ItemStack onUseItem(ItemStack stack, World world, Player player) {
        if (Helper.isClientWorld()) {
            return stack;
        }

        NetworkHandler.sendToPlayer(player, new OpenGuiPrintoutMessage(stack));

        return stack;
    }


//    @Override
//    public void appendTooltip( @Nonnull ItemStack stack, World world, @Nonnull List<Text> list, @Nonnull TooltipContext options )
//    {
//        String title = getTitle( stack );
//        if( title != null && !title.isEmpty() )
//        {
//            list.add( new LiteralText( title ) );
//        }
//    }

    public static String getTitle( @Nonnull ItemStack stack )
    {
        CompoundTag nbt = stack.getData();
        return nbt.containsKey( NBT_TITLE ) ? nbt.getString( NBT_TITLE ) : null;
    }

    public Type getType()
    {
        return type;
    }

    public enum Type
    {
        PAGE, PAGES, BOOK
    }
}
