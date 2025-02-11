package dan200.computercraft.shared.common;

import dan200.computercraft.shared.computer.blocks.BlockLogicComputer;
import dan200.computercraft.shared.computer.blocks.BlockModelComputer;
import dan200.computercraft.shared.computer.blocks.TileEntityComputer;
import dan200.computercraft.shared.computer.items.ItemBlockComputer;
import dan200.computercraft.shared.media.items.ItemDisk;
import dan200.computercraft.shared.peripheral.diskdrive.BlockDiskDrive;
import dan200.computercraft.shared.peripheral.diskdrive.TileDiskDrive;
import dan200.computercraft.shared.peripheral.modem.wired.BlockCable;
import dan200.computercraft.shared.peripheral.modem.wired.BlockWiredModemFull;
import dan200.computercraft.shared.peripheral.modem.wired.TileCable;
import dan200.computercraft.shared.peripheral.modem.wired.TileWiredModemFull;
import dan200.computercraft.shared.peripheral.modem.wireless.BlockModelWirelessModem;
import dan200.computercraft.shared.peripheral.modem.wireless.BlockWirelessModem;
import dan200.computercraft.shared.peripheral.modem.wireless.TileWirelessModem;
import dan200.computercraft.shared.peripheral.speaker.BlockSpeaker;
import dan200.computercraft.shared.peripheral.speaker.TileSpeaker;
import net.minecraft.client.render.block.model.BlockModelRotatable;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.collection.NamespaceID;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.EntityHelper;
import turniplabs.halplibe.helper.ItemBuilder;

import static dan200.computercraft.ComputerCraft.MOD_ID;

public class ComputerCraftItems {

    static final Item DISK;

    public ComputerCraftItems() {

    }

    static {
       DISK = new ItemBuilder(MOD_ID)
           .setKey("item.computercraft.disk")
            .build((new ItemDisk(NamespaceID.getPermanent(MOD_ID, "disk"), 16539)));
    }
}
