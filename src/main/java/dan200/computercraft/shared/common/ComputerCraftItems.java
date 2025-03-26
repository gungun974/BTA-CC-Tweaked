package dan200.computercraft.shared.common;

import dan200.computercraft.shared.media.items.ItemDisk;
import net.minecraft.core.item.Item;
import net.minecraft.core.util.collection.NamespaceID;
import turniplabs.halplibe.helper.ItemBuilder;

import static dan200.computercraft.ComputerCraft.MOD_ID;

public class ComputerCraftItems {

    static final Item DISK;

    public ComputerCraftItems() {

    }

    static {
        DISK = new ItemBuilder(MOD_ID)
            .setKey("item.disk")
            .build((new ItemDisk(NamespaceID.getPermanent(MOD_ID, "disk"), 16539)));
    }
}
