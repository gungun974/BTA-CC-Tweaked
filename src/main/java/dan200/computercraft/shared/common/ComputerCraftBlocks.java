package dan200.computercraft.shared.common;

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
import dan200.computercraft.shared.peripheral.speaker.BlockSpeaker;
import dan200.computercraft.shared.peripheral.speaker.TileSpeaker;
import dan200.computercraft.shared.turtle.blocks.BlockTurtle;
import dan200.computercraft.shared.turtle.blocks.TileTurtle;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.util.collection.NamespaceID;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.EntityHelper;

import static dan200.computercraft.ComputerCraft.MOD_ID;

public class ComputerCraftBlocks {
    public static final Block<?> COMPUTER_NORMAL;
    public static final Block<?> COMPUTER_ADVANCED;
    // public static final BlockComputer COMPUTER_COMMAND // "computer_command"

    public static final Block<?> TURTLE_NORMAL;
    public static final Block<?> TURTLE_ADVANCED;

    public static final Block<?> SPEAKER;

    public static final Block<?> DISK_DRIVE;

    // public static final BlockPrinter PRINTER // "printer"

    public static final Block<?> MONITOR_NORMAL;
    public static final Block<?> MONITOR_ADVANCED;

    public static final Block<?> WIRELESS_MODEM_NORMAL;
    public static final Block<?> WIRELESS_MODEM_ADVANCED;

    public static final Block<?> WIRED_MODEM_FULL;

    public static final Block<?> CABLE;

    public ComputerCraftBlocks() {}

    static {
        final IconCoordinate a = TextureRegistry.getTexture("computercraft:block/computer_normal_front");

        try {
            TextureRegistry.initializeAllFiles("computercraft", a.parentAtlas, false);
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        EntityHelper.createTileEntity(TileEntityComputer.class, NamespaceID.getPermanent(MOD_ID, "computer"));

        COMPUTER_NORMAL = new BlockBuilder(MOD_ID)
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(() -> new TileEntityComputer(ComputerFamily.NORMAL))
            .setBlockItem(ItemBlockComputer::new)
            .build("computer_normal", 10000, b -> new BlockLogicComputer(b, ComputerFamily.NORMAL));


        COMPUTER_ADVANCED = new BlockBuilder(MOD_ID)
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(() -> new TileEntityComputer(ComputerFamily.ADVANCED))
            .setBlockItem(ItemBlockComputer::new)
            .build("computer_advanced", 10008, b -> new BlockLogicComputer(b, ComputerFamily.ADVANCED));

        EntityHelper.createTileEntity(TileSpeaker.class, NamespaceID.getPermanent(MOD_ID, "speaker"));

        SPEAKER = new BlockBuilder(MOD_ID)
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(TileSpeaker::new)
            .build("speaker", 10001, b -> new BlockSpeaker(b));

        EntityHelper.createTileEntity(TileWirelessModem.class, NamespaceID.getPermanent(MOD_ID, "wireless_modem"));

        WIRELESS_MODEM_NORMAL = new BlockBuilder(MOD_ID)
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(() -> new TileWirelessModem(false))
            .build("wireless_modem_normal", 10002, b -> new BlockWirelessModem(b, false));

        WIRELESS_MODEM_ADVANCED = new BlockBuilder(MOD_ID)
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(() -> new TileWirelessModem(true))
            .build("wireless_modem_advanced", 10011, b -> new BlockWirelessModem(b, true));

        EntityHelper.createTileEntity(TileWiredModemFull.class, NamespaceID.getPermanent(MOD_ID, "wired_modem_full"));

        WIRED_MODEM_FULL = new BlockBuilder(MOD_ID)
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(TileWiredModemFull::new)
            .build("wired_modem_full", 10003, b -> new BlockWiredModemFull(b, Material.stone));

        EntityHelper.createTileEntity(TileCable.class, NamespaceID.getPermanent(MOD_ID, "cable"));

        CABLE = new BlockBuilder(MOD_ID)
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(TileCable::new)
            .build("cable", 10004, b -> new BlockCable(b, Material.stone));

        EntityHelper.createTileEntity(TileDiskDrive.class, NamespaceID.getPermanent(MOD_ID, "disk_drive"));

        DISK_DRIVE = new BlockBuilder(MOD_ID)
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(TileDiskDrive::new)
            .build("disk_drive", 10005, b -> new BlockDiskDrive(b));

        EntityHelper.createTileEntity(TileTurtle.class, NamespaceID.getPermanent(MOD_ID, "turtle"));

        TURTLE_NORMAL = new BlockBuilder(MOD_ID)
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(() -> new TileTurtle(ComputerFamily.NORMAL))
            .build("turtle_normal", 10006, b -> new BlockTurtle(b, ComputerFamily.NORMAL));

        TURTLE_ADVANCED = new BlockBuilder(MOD_ID)
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(() -> new TileTurtle(ComputerFamily.ADVANCED))
            .build("turtle_advanced", 10009, b -> new BlockTurtle(b, ComputerFamily.ADVANCED));

        EntityHelper.createTileEntity(TileMonitor.class, NamespaceID.getPermanent(MOD_ID, "monitor"));

        MONITOR_NORMAL = new BlockBuilder(MOD_ID)
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(() -> new TileMonitor(false))
            .build("monitor_normal", 10010, b -> new BlockMonitor(b, false));

        MONITOR_ADVANCED = new BlockBuilder(MOD_ID)
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(() -> new TileMonitor(true))
            .build("monitor_advanced", 10007, b -> new BlockMonitor(b, true));
    }
}
