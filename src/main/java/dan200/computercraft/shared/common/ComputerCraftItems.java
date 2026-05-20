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
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.item.tag.ItemTags;
import net.minecraft.core.util.collection.NamespaceID;
import turniplabs.halplibe.helper.ItemBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryCategory;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;

import java.util.LinkedList;
import java.util.List;

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

    private static int generateNexId() {
        return currentGeneratedId++;
    }

    public static void RegisterItems() {
        currentGeneratedId = ComputerCraft.startItemID;

        POCKET_COMPUTER_NORMAL = new ItemBuilder(MOD_ID)
            .setKey("item.pocket_computer_normal")
            .addTags(ItemTags.NOT_IN_CREATIVE_MENU)
            .setCreativeInventoryPlacement(new CreativeInventoryPlacement.Category(CreativeInventoryCategory.MISCELLANEOUS).setCustomSupplier(() -> {
                List<ItemStack> creativeItems = new LinkedList<>();

                POCKET_COMPUTER_NORMAL.addToCreativeMenu(creativeItems);

                return creativeItems;
            }))
            .build((new ItemPocketComputer(new NamespaceID(MOD_ID, "pocket_computer_normal"), "computercraft.pocket_computer_normal", generateNexId(), ComputerFamily.NORMAL)));

        POCKET_COMPUTER_ADVANCED = new ItemBuilder(MOD_ID)
            .setKey("item.pocket_computer_advanced")
            .addTags(ItemTags.NOT_IN_CREATIVE_MENU)
            .setCreativeInventoryPlacement(new CreativeInventoryPlacement.Category(CreativeInventoryCategory.MISCELLANEOUS).setCustomSupplier(() -> {
                List<ItemStack> creativeItems = new LinkedList<>();

                POCKET_COMPUTER_ADVANCED.addToCreativeMenu(creativeItems);

                return creativeItems;
            }))
            .build((new ItemPocketComputer(new NamespaceID(MOD_ID, "pocket_computer_advanced"), "computercraft.pocket_computer_advanced", generateNexId(), ComputerFamily.ADVANCED)));

        DISK = new ItemBuilder(MOD_ID)
            .setKey("item.disk")
            .addTags(ItemTags.NOT_IN_CREATIVE_MENU)
            .setCreativeInventoryPlacement(new CreativeInventoryPlacement.Category(CreativeInventoryCategory.MISCELLANEOUS).setCustomSupplier(() -> {
                List<ItemStack> creativeItems = new LinkedList<>();

                DISK.addToCreativeMenu(creativeItems);

                return creativeItems;
            }))
            .build((new ItemDisk(new NamespaceID(MOD_ID, "disk"), "computercraft.disk", generateNexId())));

        TREASURE_DISK = new ItemBuilder(MOD_ID)
            .setKey("item.treasure_disk")
            .addTags(ItemTags.NOT_IN_CREATIVE_MENU)
            .build((new ItemTreasureDisk(new NamespaceID(MOD_ID, "treasure_disk"), "computercraft.treasure_disk", generateNexId())));

        PRINTED_PAGE = new ItemBuilder(MOD_ID)
            .setKey("item.printed_page")
            .setCreativeInventoryPlacement(new CreativeInventoryPlacement.Category(CreativeInventoryCategory.MISCELLANEOUS))
            .build((new ItemPrintout(new NamespaceID(MOD_ID, "printed_page"), "computercraft.printed_page", generateNexId(), ItemPrintout.Type.PAGE)));

        PRINTED_PAGES = new ItemBuilder(MOD_ID)
            .setKey("item.printed_pages")
            .setCreativeInventoryPlacement(new CreativeInventoryPlacement.Category(CreativeInventoryCategory.MISCELLANEOUS))
            .build((new ItemPrintout(new NamespaceID(MOD_ID, "printed_pages"), "computercraft.printed_page", generateNexId(), ItemPrintout.Type.PAGES)));

        PRINTED_BOOK = new ItemBuilder(MOD_ID)
            .setKey("item.printed_book")
            .setCreativeInventoryPlacement(new CreativeInventoryPlacement.Category(CreativeInventoryCategory.MISCELLANEOUS))
            .build((new ItemPrintout(new NamespaceID(MOD_ID, "printed_book"), "computercraft.printed_page", generateNexId(), ItemPrintout.Type.BOOK)));

        CABLE = new ItemBuilder(MOD_ID)
            .setKey("item.cable")
            .setCreativeInventoryPlacement(new CreativeInventoryPlacement.Category(CreativeInventoryCategory.MISCELLANEOUS))
            .build((new ItemCable.Cable(new NamespaceID(MOD_ID, "cable"), "computercraft.cable", generateNexId())));

        WIRED_MODEM = new ItemBuilder(MOD_ID)
            .setKey("item.wired_modem")
            .setCreativeInventoryPlacement(new CreativeInventoryPlacement.Category(CreativeInventoryCategory.MISCELLANEOUS))
            .build((new ItemCable.WiredModem(new NamespaceID(MOD_ID, "wired_modem"), "computercraft.wired_modem", generateNexId())));
    }
}
