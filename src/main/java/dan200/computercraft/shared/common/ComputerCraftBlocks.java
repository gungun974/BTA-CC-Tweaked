package dan200.computercraft.shared.common;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.shared.computer.blocks.BlockLogicComputer;
import dan200.computercraft.shared.computer.blocks.TileEntityComputer;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.items.ItemBlockComputer;
import dan200.computercraft.shared.peripheral.diskdrive.BlockDiskDrive;
import dan200.computercraft.shared.peripheral.diskdrive.TileDiskDrive;
import dan200.computercraft.shared.peripheral.modem.wired.BlockCable;
import dan200.computercraft.shared.peripheral.modem.wired.BlockWiredModemFull;
import dan200.computercraft.shared.peripheral.modem.wired.TileCable;
import dan200.computercraft.shared.peripheral.modem.wired.TileWiredModemFull;
import dan200.computercraft.shared.peripheral.modem.wireless.BlockWirelessModem;
import dan200.computercraft.shared.peripheral.modem.wireless.TileWirelessModem;
import dan200.computercraft.shared.peripheral.monitor.BlockMonitor;
import dan200.computercraft.shared.peripheral.monitor.TileMonitor;
import dan200.computercraft.shared.peripheral.printer.BlockPrinter;
import dan200.computercraft.shared.peripheral.printer.TilePrinter;
import dan200.computercraft.shared.peripheral.speaker.BlockSpeaker;
import dan200.computercraft.shared.peripheral.speaker.TileSpeaker;
import dan200.computercraft.shared.turtle.blocks.BlockTurtle;
import dan200.computercraft.shared.turtle.blocks.TileTurtle;
import dan200.computercraft.shared.turtle.items.ItemTurtle;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.util.collection.NamespaceID;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.EntityHelper;

import static dan200.computercraft.ComputerCraft.MOD_ID;

public class ComputerCraftBlocks {
    public static Block<?> COMPUTER_NORMAL;
    public static Block<?> COMPUTER_ADVANCED;

    public static Block<?> TURTLE_NORMAL;
    public static Block<?> TURTLE_ADVANCED;

    public static Block<?> SPEAKER;

    public static Block<?> DISK_DRIVE;

    public static Block<?> PRINTER;

    public static Block<?> MONITOR_NORMAL;
    public static Block<?> MONITOR_ADVANCED;

    public static Block<?> WIRELESS_MODEM_NORMAL;
    public static Block<?> WIRELESS_MODEM_ADVANCED;

    public static Block<?> WIRED_MODEM_FULL;

    public static Block<?> CABLE;

    private static int currentGeneratedId;

    private static int generateNexId () {
        return currentGeneratedId++;
    }

    public static void RegisterBlocks() {
       currentGeneratedId = ComputerCraft.startBlockID;

        final IconCoordinate a = TextureRegistry.getTexture("computercraft:block/computer_normal_front");

        try {
            TextureRegistry.initializeAllFiles("computercraft", a.parentAtlas, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        EntityHelper.createTileEntity(TileEntityComputer.class, NamespaceID.getPermanent(MOD_ID, "computer"));

        COMPUTER_NORMAL = new BlockBuilder(MOD_ID)
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(() -> new TileEntityComputer(ComputerFamily.NORMAL))
            .setBlockItem(block -> {
                ComputerCraftItems.COMPUTER_NORMAL = new ItemBlockComputer(block);
                return ComputerCraftItems.COMPUTER_NORMAL;
            })
            .build("computer_normal", generateNexId(), b -> new BlockLogicComputer(b, ComputerFamily.NORMAL));


        COMPUTER_ADVANCED = new BlockBuilder(MOD_ID)
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(() -> new TileEntityComputer(ComputerFamily.ADVANCED))
            .setBlockItem(block -> {
                ComputerCraftItems.COMPUTER_ADVANCED = new ItemBlockComputer(block);
                return ComputerCraftItems.COMPUTER_ADVANCED;
            })
            .build("computer_advanced", generateNexId(), b -> new BlockLogicComputer(b, ComputerFamily.ADVANCED));

        EntityHelper.createTileEntity(TileTurtle.class, NamespaceID.getPermanent(MOD_ID, "turtle"));

        TURTLE_NORMAL = new BlockBuilder(MOD_ID)
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.NOT_IN_CREATIVE_MENU, BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(() -> new TileTurtle(ComputerFamily.NORMAL))
            .setBlockItem(block -> {
                ComputerCraftItems.TURTLE_NORMAL = new ItemTurtle(block);
                return ComputerCraftItems.TURTLE_NORMAL;
            })
            .build("turtle_normal", generateNexId(), b -> new BlockTurtle(b, ComputerFamily.NORMAL));

        TURTLE_ADVANCED = new BlockBuilder(MOD_ID)
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.NOT_IN_CREATIVE_MENU, BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(() -> new TileTurtle(ComputerFamily.ADVANCED))
            .setBlockItem(block -> {
                ComputerCraftItems.TURTLE_ADVANCED = new ItemTurtle(block);
                return ComputerCraftItems.TURTLE_ADVANCED;
            })
            .build("turtle_advanced", generateNexId(), b -> new BlockTurtle(b, ComputerFamily.ADVANCED));

        EntityHelper.createTileEntity(TileWirelessModem.class, NamespaceID.getPermanent(MOD_ID, "wireless_modem"));

        WIRELESS_MODEM_NORMAL = new BlockBuilder(MOD_ID)
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(() -> new TileWirelessModem(false))
            .setBlockItem(block -> {
                ComputerCraftItems.WIRELESS_MODEM_NORMAL = new ItemBlock<>(block);
                return ComputerCraftItems.WIRELESS_MODEM_NORMAL;
            })
            .build("wireless_modem_normal", generateNexId(), b -> new BlockWirelessModem(b, false));

        WIRELESS_MODEM_ADVANCED = new BlockBuilder(MOD_ID)
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(() -> new TileWirelessModem(true))
            .setBlockItem(block -> {
                ComputerCraftItems.WIRELESS_MODEM_ADVANCED = new ItemBlock<>(block);
                return ComputerCraftItems.WIRELESS_MODEM_ADVANCED;
            })
            .build("wireless_modem_advanced", generateNexId(), b -> new BlockWirelessModem(b, true));

        EntityHelper.createTileEntity(TileWiredModemFull.class, NamespaceID.getPermanent(MOD_ID, "wired_modem_full"));

        WIRED_MODEM_FULL = new BlockBuilder(MOD_ID)
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(TileWiredModemFull::new)
            .build("wired_modem_full", generateNexId(), b -> new BlockWiredModemFull(b, Material.stone));

        EntityHelper.createTileEntity(TileCable.class, NamespaceID.getPermanent(MOD_ID, "cable"));

        CABLE = new BlockBuilder(MOD_ID)
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.NOT_IN_CREATIVE_MENU, BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(TileCable::new)
            .build("cable", generateNexId(), b -> new BlockCable(b, Material.stone));

        EntityHelper.createTileEntity(TileMonitor.class, NamespaceID.getPermanent(MOD_ID, "monitor"));

        MONITOR_NORMAL = new BlockBuilder(MOD_ID)
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(() -> new TileMonitor(false))
            .setBlockItem(block -> {
                ComputerCraftItems.MONITOR_NORMAL = new ItemBlock<>(block);
                return ComputerCraftItems.MONITOR_NORMAL;
            })
            .build("monitor_normal", generateNexId(), b -> new BlockMonitor(b, false));

        MONITOR_ADVANCED = new BlockBuilder(MOD_ID)
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(() -> new TileMonitor(true))
            .setBlockItem(block -> {
                ComputerCraftItems.MONITOR_ADVANCED = new ItemBlock<>(block);
                return ComputerCraftItems.MONITOR_ADVANCED;
            })
            .build("monitor_advanced", generateNexId(), b -> new BlockMonitor(b, true));

        EntityHelper.createTileEntity(TileDiskDrive.class, NamespaceID.getPermanent(MOD_ID, "disk_drive"));

        DISK_DRIVE = new BlockBuilder(MOD_ID)
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(TileDiskDrive::new)
            .setBlockItem(block -> {
                ComputerCraftItems.DISK_DRIVE = new ItemBlock<>(block);
                return ComputerCraftItems.DISK_DRIVE;
            })
            .build("disk_drive", generateNexId(), b -> new BlockDiskDrive(b));

        EntityHelper.createTileEntity(TileSpeaker.class, NamespaceID.getPermanent(MOD_ID, "speaker"));

        SPEAKER = new BlockBuilder(MOD_ID)
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(TileSpeaker::new)
            .setBlockItem(block -> {
                ComputerCraftItems.SPEAKER = new ItemBlock<>(block);
                return ComputerCraftItems.SPEAKER;
            })
            .build("speaker", generateNexId(), b -> new BlockSpeaker(b));

        EntityHelper.createTileEntity(TilePrinter.class, NamespaceID.getPermanent(MOD_ID, "printer"));

        PRINTER = new BlockBuilder(MOD_ID)
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(TileDiskDrive::new)
            .setBlockItem(block -> {
                ComputerCraftItems.PRINTER = new ItemBlock<>(block);
                return ComputerCraftItems.PRINTER;
            })
            .build("printer", generateNexId(), b -> new BlockPrinter(b));
    }
}
