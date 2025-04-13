package dan200.computercraft.shared.common;

import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.items.ItemBlockComputer;
import dan200.computercraft.shared.media.items.ItemDisk;
import dan200.computercraft.shared.media.items.ItemPrintout;
import dan200.computercraft.shared.media.items.ItemTreasureDisk;
import dan200.computercraft.shared.peripheral.modem.wired.ItemBlockCable;
import dan200.computercraft.shared.pocket.items.ItemPocketComputer;
import dan200.computercraft.shared.turtle.items.ItemTurtle;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.util.collection.NamespaceID;
import turniplabs.halplibe.helper.ItemBuilder;

import static dan200.computercraft.ComputerCraft.MOD_ID;

public class ComputerCraftItems {

    public static ItemDisk DISK;
    public static ItemTreasureDisk TREASURE_DISK;

    public static ItemBlockComputer COMPUTER_NORMAL;
    public static ItemBlockComputer COMPUTER_ADVANCED;
    public static ItemPocketComputer POCKET_COMPUTER_NORMAL;
    public static ItemPocketComputer POCKET_COMPUTER_ADVANCED;
    public static ItemTurtle TURTLE_NORMAL;
    public static ItemTurtle TURTLE_ADVANCED;
    public static ItemBlock<?> DISK_DRIVE;
    public static ItemBlock<?> PRINTER;
    public static ItemBlock<?> MONITOR_NORMAL;
    public static ItemBlock<?> MONITOR_ADVANCED;
    public static ItemBlock<?> WIRELESS_MODEM_NORMAL;
    public static ItemBlock<?> WIRELESS_MODEM_ADVANCED;
    public static ItemBlock<?> SPEAKER;
    public static ItemPrintout PRINTED_PAGE;
    public static ItemPrintout PRINTED_PAGES;
    public static ItemPrintout PRINTED_BOOK;

    public static ItemBlockCable.Cable CABLE;
    public static ItemBlockCable.WiredModem WIRED_MODEM;

    public static void RegisterItems() {
        POCKET_COMPUTER_NORMAL = new ItemBuilder(MOD_ID)
            .setKey("item.pocket_computer_normal")
            .build((new ItemPocketComputer(NamespaceID.getPermanent(MOD_ID, "pocket_computer_normal"), 16540, ComputerFamily.NORMAL)));

        POCKET_COMPUTER_ADVANCED = new ItemBuilder(MOD_ID)
            .setKey("item.pocket_computer_advanced")
            .build((new ItemPocketComputer(NamespaceID.getPermanent(MOD_ID, "pocket_computer_advanced"), 16541, ComputerFamily.ADVANCED)));

        DISK = new ItemBuilder(MOD_ID)
            .setKey("item.disk")
            .build((new ItemDisk(NamespaceID.getPermanent(MOD_ID, "disk"), 16539)));

        TREASURE_DISK = new ItemBuilder(MOD_ID)
            .setKey("item.treasure_disk")
            .build((new ItemTreasureDisk(NamespaceID.getPermanent(MOD_ID, "treasure_disk"), 16547)));

        PRINTED_PAGE = new ItemBuilder(MOD_ID)
            .setKey("item.printed_page")
            .build((new ItemPrintout(NamespaceID.getPermanent(MOD_ID, "printed_page"), 16542, ItemPrintout.Type.PAGE)));

        PRINTED_PAGES = new ItemBuilder(MOD_ID)
            .setKey("item.printed_pages")
            .build((new ItemPrintout(NamespaceID.getPermanent(MOD_ID, "printed_pages"), 16543, ItemPrintout.Type.PAGES)));

        PRINTED_BOOK = new ItemBuilder(MOD_ID)
            .setKey("item.printed_book")
            .build((new ItemPrintout(NamespaceID.getPermanent(MOD_ID, "printed_book"), 16544, ItemPrintout.Type.BOOK)));

        CABLE = new ItemBuilder(MOD_ID)
            .setKey("item.cable")
            .build((new ItemBlockCable.Cable(NamespaceID.getPermanent(MOD_ID, "cable"), 16545)));

        WIRED_MODEM = new ItemBuilder(MOD_ID)
            .setKey("item.wired_modem")
            .build((new ItemBlockCable.WiredModem(NamespaceID.getPermanent(MOD_ID, "wired_modem"), 16546)));
    }
}
