package dan200.computercraft.shared.common;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.items.ItemBlockComputer;
import dan200.computercraft.shared.media.items.ItemDisk;
import dan200.computercraft.shared.media.items.ItemPrintout;
import dan200.computercraft.shared.media.items.ItemTreasureDisk;
import dan200.computercraft.shared.peripheral.modem.wired.ItemCable;
import dan200.computercraft.shared.pocket.items.ItemPocketComputer;
import dan200.computercraft.shared.turtle.items.ItemBlockTurtle;
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
    public static ItemBlockTurtle TURTLE_NORMAL;
    public static ItemBlockTurtle TURTLE_ADVANCED;
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

    public static ItemCable.Cable CABLE;
    public static ItemCable.WiredModem WIRED_MODEM;

    private static int currentGeneratedId;

    private static int generateNexId () {
        return currentGeneratedId++;
    }

    public static void RegisterItems() {
        currentGeneratedId = ComputerCraft.startItemID;

        POCKET_COMPUTER_NORMAL = new ItemBuilder(MOD_ID)
            .setKey("item.pocket_computer_normal")
            .build((new ItemPocketComputer(NamespaceID.getPermanent(MOD_ID, "pocket_computer_normal"), generateNexId(), ComputerFamily.NORMAL)));

        POCKET_COMPUTER_ADVANCED = new ItemBuilder(MOD_ID)
            .setKey("item.pocket_computer_advanced")
            .build((new ItemPocketComputer(NamespaceID.getPermanent(MOD_ID, "pocket_computer_advanced"), generateNexId(), ComputerFamily.ADVANCED)));

        DISK = new ItemBuilder(MOD_ID)
            .setKey("item.disk")
            .build((new ItemDisk(NamespaceID.getPermanent(MOD_ID, "disk"), generateNexId())));

        TREASURE_DISK = new ItemBuilder(MOD_ID)
            .setKey("item.treasure_disk")
            .build((new ItemTreasureDisk(NamespaceID.getPermanent(MOD_ID, "treasure_disk"), generateNexId())));

        PRINTED_PAGE = new ItemBuilder(MOD_ID)
            .setKey("item.printed_page")
            .build((new ItemPrintout(NamespaceID.getPermanent(MOD_ID, "printed_page"), generateNexId(), ItemPrintout.Type.PAGE)));

        PRINTED_PAGES = new ItemBuilder(MOD_ID)
            .setKey("item.printed_pages")
            .build((new ItemPrintout(NamespaceID.getPermanent(MOD_ID, "printed_pages"), generateNexId(), ItemPrintout.Type.PAGES)));

        PRINTED_BOOK = new ItemBuilder(MOD_ID)
            .setKey("item.printed_book")
            .build((new ItemPrintout(NamespaceID.getPermanent(MOD_ID, "printed_book"), generateNexId(), ItemPrintout.Type.BOOK)));

        CABLE = new ItemBuilder(MOD_ID)
            .setKey("item.cable")
            .build((new ItemCable.Cable(NamespaceID.getPermanent(MOD_ID, "cable"), generateNexId())));

        WIRED_MODEM = new ItemBuilder(MOD_ID)
            .setKey("item.wired_modem")
            .build((new ItemCable.WiredModem(NamespaceID.getPermanent(MOD_ID, "wired_modem"), generateNexId())));
    }
}
