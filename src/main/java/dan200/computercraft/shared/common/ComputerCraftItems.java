package dan200.computercraft.shared.common;

import dan200.computercraft.shared.computer.items.ItemBlockComputer;
import dan200.computercraft.shared.media.items.ItemDisk;
import dan200.computercraft.shared.turtle.items.ItemTurtle;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.util.collection.NamespaceID;
import turniplabs.halplibe.helper.ItemBuilder;

import static dan200.computercraft.ComputerCraft.MOD_ID;

public class ComputerCraftItems {

    public static ItemBlockComputer COMPUTER_NORMAL;
    public static ItemBlockComputer COMPUTER_ADVANCED;

    public static ItemTurtle TURTLE_NORMAL;
    public static ItemTurtle TURTLE_ADVANCED;

    public static ItemBlock<?> WIRELESS_MODEM_NORMAL;
    public static ItemBlock<?> WIRELESS_MODEM_ADVANCED;

    public static ItemBlock<?> SPEAKER;

    public static final Item DISK;

    public ComputerCraftItems() {

    }

    static {
        DISK = new ItemBuilder(MOD_ID)
            .setKey("item.disk")
            .build((new ItemDisk(NamespaceID.getPermanent(MOD_ID, "disk"), 16539)));
    }
}
