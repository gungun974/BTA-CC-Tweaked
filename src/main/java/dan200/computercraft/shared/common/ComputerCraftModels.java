package dan200.computercraft.shared.common;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.client.items.ItemModelRotatedBlock;
import dan200.computercraft.shared.computer.blocks.BlockModelComputer;
import dan200.computercraft.shared.peripheral.diskdrive.BlockModelDiskDrive;
import dan200.computercraft.shared.peripheral.diskdrive.ItemModelDisk;
import dan200.computercraft.shared.peripheral.modem.wired.BlockModelCable;
import dan200.computercraft.shared.peripheral.modem.wired.BlockModelModemFull;
import dan200.computercraft.shared.peripheral.modem.wired.ItemModelCable;
import dan200.computercraft.shared.peripheral.modem.wireless.BlockModelWirelessModem;
import dan200.computercraft.shared.peripheral.monitor.BlockModelMonitor;
import dan200.computercraft.shared.peripheral.monitor.TileEntityMonitorRenderer;
import dan200.computercraft.shared.peripheral.monitor.TileMonitor;
import dan200.computercraft.shared.peripheral.printer.BlockModelPrinter;
import dan200.computercraft.shared.pocket.items.ItemModelPocketComputer;
import dan200.computercraft.shared.turtle.blocks.BlockModelTurtle;
import dan200.computercraft.shared.turtle.blocks.TileEntityRendererTurtle;
import dan200.computercraft.shared.turtle.blocks.TileTurtle;
import dan200.computercraft.shared.turtle.items.ItemModelBlockTurtle;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelHorizontalRotation;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.util.helper.Side;
import turniplabs.halplibe.helper.ModelHelper;
import turniplabs.halplibe.util.ModelEntrypoint;

import static dan200.computercraft.ComputerCraft.MOD_ID;

public class ComputerCraftModels implements ModelEntrypoint {

    @Override
    public void initBlockModels(BlockModelDispatcher dispatcher) {

        ModelHelper.setBlockModel(ComputerCraftBlocks.COMPUTER_NORMAL, () -> new BlockModelComputer<>(ComputerCraftBlocks.COMPUTER_NORMAL)
            .setTex(0, "computercraft:block/computer_normal_side", Side.SOUTH, Side.EAST, Side.WEST)
            .setTex(0, "computercraft:block/computer_normal_top", Side.TOP, Side.BOTTOM)
            .setTex(0, "computercraft:block/computer_normal_front", Side.NORTH)
        );

        ModelHelper.setBlockModel(ComputerCraftBlocks.COMPUTER_ADVANCED, () -> new BlockModelComputer<>(ComputerCraftBlocks.COMPUTER_ADVANCED)
            .setTex(0, "computercraft:block/computer_advanced_side", Side.SOUTH, Side.EAST, Side.WEST)
            .setTex(0, "computercraft:block/computer_advanced_top", Side.TOP, Side.BOTTOM)
            .setTex(0, "computercraft:block/computer_advanced_front", Side.NORTH)
        );

        ModelHelper.setBlockModel(ComputerCraftBlocks.SPEAKER, () -> new BlockModelHorizontalRotation<>(ComputerCraftBlocks.SPEAKER)
            .setTex(0, "computercraft:block/speaker_side", Side.SOUTH, Side.EAST, Side.WEST)
            .setTex(0, "computercraft:block/speaker_top", Side.TOP, Side.BOTTOM)
            .setTex(0, "computercraft:block/speaker_front", Side.NORTH)
        );

        ModelHelper.setBlockModel(ComputerCraftBlocks.WIRELESS_MODEM_NORMAL, () -> new BlockModelWirelessModem<>(ComputerCraftBlocks.WIRELESS_MODEM_NORMAL)
            .setTex(0, "computercraft:block/wireless_modem_normal_face", Side.TOP, Side.NORTH, Side.SOUTH, Side.EAST, Side.WEST)
            .setTex(0, "computercraft:block/modem_back", Side.BOTTOM)
        );

        ModelHelper.setBlockModel(ComputerCraftBlocks.WIRELESS_MODEM_ADVANCED, () -> new BlockModelWirelessModem<>(ComputerCraftBlocks.WIRELESS_MODEM_ADVANCED)
            .setTex(0, "computercraft:block/wireless_modem_advanced_face", Side.TOP, Side.NORTH, Side.SOUTH, Side.EAST, Side.WEST)
            .setTex(0, "computercraft:block/modem_back", Side.BOTTOM)
        );

        ModelHelper.setBlockModel(ComputerCraftBlocks.WIRED_MODEM_FULL, () -> new BlockModelModemFull<>(ComputerCraftBlocks.WIRED_MODEM_FULL)
            .setTex(0, "computercraft:block/wired_modem_face", Side.sides)
        );

        ModelHelper.setBlockModel(ComputerCraftBlocks.CABLE, () -> new BlockModelCable<>(ComputerCraftBlocks.CABLE));

        ModelHelper.setBlockModel(ComputerCraftBlocks.DISK_DRIVE, () -> new BlockModelDiskDrive<>(ComputerCraftBlocks.DISK_DRIVE)
            .setTex(0, "computercraft:block/disk_drive_side", Side.SOUTH, Side.EAST, Side.WEST)
            .setTex(0, "computercraft:block/disk_drive_top", Side.TOP, Side.BOTTOM)
            .setTex(0, "computercraft:block/disk_drive_front", Side.NORTH)
        );

        ModelHelper.setBlockModel(ComputerCraftBlocks.PRINTER, () -> new BlockModelPrinter<>(ComputerCraftBlocks.PRINTER)
            .setTex(0, "computercraft:block/printer_side", Side.SOUTH, Side.EAST, Side.WEST)
            .setTex(0, "computercraft:block/printer_top", Side.TOP, Side.BOTTOM)
            .setTex(0, "computercraft:block/printer_front_empty", Side.NORTH)
        );

        ModelHelper.setBlockModel(ComputerCraftBlocks.TURTLE_NORMAL, () -> new BlockModelTurtle<>(ComputerCraftBlocks.TURTLE_NORMAL)
            .setTex(0, "computercraft:block/computer_normal_side", Side.SOUTH, Side.EAST, Side.WEST)
            .setTex(0, "computercraft:block/computer_normal_top", Side.TOP, Side.BOTTOM)
            .setTex(0, "computercraft:block/computer_normal_front", Side.NORTH)
        );

        ModelHelper.setBlockModel(ComputerCraftBlocks.TURTLE_ADVANCED, () -> new BlockModelTurtle<>(ComputerCraftBlocks.TURTLE_ADVANCED)
            .setTex(0, "computercraft:block/computer_advanced_side", Side.SOUTH, Side.EAST, Side.WEST)
            .setTex(0, "computercraft:block/computer_advanced_top", Side.TOP, Side.BOTTOM)
            .setTex(0, "computercraft:block/computer_advanced_front", Side.NORTH)
        );

        ModelHelper.setBlockModel(ComputerCraftBlocks.MONITOR_NORMAL, () -> new BlockModelMonitor<>(ComputerCraftBlocks.MONITOR_NORMAL, "computercraft:block/monitor_normal")
            .setTex(0, "computercraft:block/monitor_normal_4", Side.SOUTH, Side.EAST, Side.WEST)
            .setTex(0, "computercraft:block/monitor_normal_0", Side.TOP, Side.BOTTOM)
            .setTex(0, "computercraft:block/monitor_normal_15", Side.NORTH)
        );

        ModelHelper.setBlockModel(ComputerCraftBlocks.MONITOR_ADVANCED, () -> new BlockModelMonitor<>(ComputerCraftBlocks.MONITOR_ADVANCED, "computercraft:block/monitor_advanced")
            .setTex(0, "computercraft:block/monitor_advanced_4", Side.SOUTH, Side.EAST, Side.WEST)
            .setTex(0, "computercraft:block/monitor_advanced_0", Side.TOP, Side.BOTTOM)
            .setTex(0, "computercraft:block/monitor_advanced_15", Side.NORTH)
        );

        ComputerCraft.log.info("Block Models initialized.");
    }

    @Override
    public void initItemModels(ItemModelDispatcher dispatcher) {
        ModelHelper.setItemModel(ComputerCraftItems.COMPUTER_NORMAL, () -> new ItemModelRotatedBlock(ComputerCraftItems.COMPUTER_NORMAL));
        ModelHelper.setItemModel(ComputerCraftItems.COMPUTER_ADVANCED, () -> new ItemModelRotatedBlock(ComputerCraftItems.COMPUTER_ADVANCED));

        ModelHelper.setItemModel(ComputerCraftItems.POCKET_COMPUTER_NORMAL, () -> {
            ItemModelStandard itemModelStandard = new ItemModelPocketComputer(ComputerCraftItems.POCKET_COMPUTER_NORMAL, MOD_ID);
            itemModelStandard.icon = TextureRegistry.getTexture(NamespaceID.getPermanent(MOD_ID, "item/pocket_computer_normal"));
            return itemModelStandard;
        });

        ModelHelper.setItemModel(ComputerCraftItems.POCKET_COMPUTER_ADVANCED, () -> {
            ItemModelStandard itemModelStandard = new ItemModelPocketComputer(ComputerCraftItems.POCKET_COMPUTER_ADVANCED, MOD_ID);
            itemModelStandard.icon = TextureRegistry.getTexture(NamespaceID.getPermanent(MOD_ID, "item/pocket_computer_advanced"));
            return itemModelStandard;
        });

        ModelHelper.setItemModel(ComputerCraftItems.SPEAKER, () -> new ItemModelRotatedBlock(ComputerCraftItems.SPEAKER));
        ModelHelper.setItemModel(ComputerCraftItems.DISK_DRIVE, () -> new ItemModelRotatedBlock(ComputerCraftItems.DISK_DRIVE));
        ModelHelper.setItemModel(ComputerCraftItems.PRINTER, () -> new ItemModelRotatedBlock(ComputerCraftItems.PRINTER));

        ModelHelper.setItemModel(ComputerCraftItems.MONITOR_NORMAL, () -> new ItemModelRotatedBlock(ComputerCraftItems.MONITOR_NORMAL));
        ModelHelper.setItemModel(ComputerCraftItems.MONITOR_ADVANCED, () -> new ItemModelRotatedBlock(ComputerCraftItems.MONITOR_ADVANCED));

        ModelHelper.setItemModel(ComputerCraftItems.TURTLE_NORMAL, () -> new ItemModelBlockTurtle(ComputerCraftItems.TURTLE_NORMAL));
        ModelHelper.setItemModel(ComputerCraftItems.TURTLE_ADVANCED, () -> new ItemModelBlockTurtle(ComputerCraftItems.TURTLE_ADVANCED));

        ModelHelper.setItemModel(ComputerCraftItems.WIRELESS_MODEM_NORMAL, () -> new ItemModelRotatedBlock(ComputerCraftItems.WIRELESS_MODEM_NORMAL));
        ModelHelper.setItemModel(ComputerCraftItems.WIRELESS_MODEM_ADVANCED, () -> new ItemModelRotatedBlock(ComputerCraftItems.WIRELESS_MODEM_ADVANCED));

        ModelHelper.setItemModel(ComputerCraftItems.DISK, () -> {
            ItemModelStandard itemModelStandard = new ItemModelDisk(ComputerCraftItems.DISK, MOD_ID);
            itemModelStandard.icon = TextureRegistry.getTexture(NamespaceID.getPermanent(MOD_ID, "item/disk_frame"));
            return itemModelStandard;
        });

        ModelHelper.setItemModel(ComputerCraftItems.TREASURE_DISK, () -> {
            ItemModelStandard itemModelStandard = new ItemModelDisk(ComputerCraftItems.TREASURE_DISK, MOD_ID);
            itemModelStandard.icon = TextureRegistry.getTexture(NamespaceID.getPermanent(MOD_ID, "item/disk_frame"));
            return itemModelStandard;
        });

        ModelHelper.setItemModel(ComputerCraftItems.PRINTED_PAGE, () -> {
            ItemModelStandard itemModelStandard = new ItemModelStandard(ComputerCraftItems.PRINTED_PAGE, MOD_ID);
            itemModelStandard.icon = TextureRegistry.getTexture(NamespaceID.getPermanent(MOD_ID, "item/printed_page"));
            return itemModelStandard;
        });

        ModelHelper.setItemModel(ComputerCraftItems.PRINTED_PAGES, () -> {
            ItemModelStandard itemModelStandard = new ItemModelStandard(ComputerCraftItems.PRINTED_PAGES, MOD_ID);
            itemModelStandard.icon = TextureRegistry.getTexture(NamespaceID.getPermanent(MOD_ID, "item/printed_pages"));
            return itemModelStandard;
        });

        ModelHelper.setItemModel(ComputerCraftItems.PRINTED_BOOK, () -> {
            ItemModelStandard itemModelStandard = new ItemModelStandard(ComputerCraftItems.PRINTED_BOOK, MOD_ID);
            itemModelStandard.icon = TextureRegistry.getTexture(NamespaceID.getPermanent(MOD_ID, "item/printed_book"));
            return itemModelStandard;
        });

        ModelHelper.setItemModel(ComputerCraftItems.CABLE, () -> new ItemModelCable(ComputerCraftItems.CABLE));
        ModelHelper.setItemModel(ComputerCraftItems.WIRED_MODEM, () -> new ItemModelCable(ComputerCraftItems.WIRED_MODEM));

        ComputerCraft.log.info("Item Models initialized.");
    }

    @Override
    public void initEntityModels(EntityRenderDispatcher dispatcher) {
    }

    @Override
    public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {
        ModelHelper.setTileEntityModel(TileMonitor.class, TileEntityMonitorRenderer::new);

        ModelHelper.setTileEntityModel(TileTurtle.class, TileEntityRendererTurtle::new);

        ComputerCraft.log.info("Tile Entity Models initialized.");
    }

    @Override
    public void initBlockColors(BlockColorDispatcher dispatcher) {
    }
}
