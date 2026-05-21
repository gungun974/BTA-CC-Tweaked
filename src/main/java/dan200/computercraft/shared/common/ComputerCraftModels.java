package dan200.computercraft.shared.common;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.client.BlockModelCorrectRotable;
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
import net.minecraft.client.render.EntityRendererDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.util.collection.NamespaceID;
import turniplabs.halplibe.helper.ModelHelper;
import turniplabs.halplibe.util.ModelEntrypoint;

import static dan200.computercraft.ComputerCraft.MOD_ID;

public class ComputerCraftModels implements ModelEntrypoint {

    @Override
    public void initBlockModels(BlockModelDispatcher dispatcher) {

        final IconCoordinate a = TextureRegistry.getTexture("computercraft:block/computer_normal_front");

        try {
            TextureRegistry.initializeAllFiles("computercraft", a.parentAtlas, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ModelHelper.setBlockModel(ComputerCraftBlocks.COMPUTER_NORMAL, () -> new BlockModelComputer<>(ComputerCraftBlocks.COMPUTER_NORMAL, "computercraft:block/computer_normal"));

        ModelHelper.setBlockModel(ComputerCraftBlocks.COMPUTER_ADVANCED, () -> new BlockModelComputer<>(ComputerCraftBlocks.COMPUTER_ADVANCED, "computercraft:block/computer_advanced"));

        ModelHelper.setBlockModel(ComputerCraftBlocks.SPEAKER, () -> new BlockModelCorrectRotable<>(ComputerCraftBlocks.SPEAKER, BlockModelDispatcher.loadDataModel("computercraft:block/speaker").asModel()));

        ModelHelper.setBlockModel(ComputerCraftBlocks.WIRELESS_MODEM_NORMAL, () -> new BlockModelWirelessModem<>(ComputerCraftBlocks.WIRELESS_MODEM_NORMAL, "computercraft:block/wireless_modem_normal"));

        ModelHelper.setBlockModel(ComputerCraftBlocks.WIRELESS_MODEM_ADVANCED, () -> new BlockModelWirelessModem<>(ComputerCraftBlocks.WIRELESS_MODEM_ADVANCED, "computercraft:block/wireless_modem_advanced"));

        ModelHelper.setBlockModel(ComputerCraftBlocks.WIRED_MODEM_FULL, () -> new BlockModelModemFull<>(ComputerCraftBlocks.WIRED_MODEM_FULL));

        ModelHelper.setBlockModel(ComputerCraftBlocks.CABLE, () -> new BlockModelCable<>(ComputerCraftBlocks.CABLE));

        ModelHelper.setBlockModel(ComputerCraftBlocks.DISK_DRIVE, () -> new BlockModelDiskDrive<>(ComputerCraftBlocks.DISK_DRIVE));

        ModelHelper.setBlockModel(ComputerCraftBlocks.PRINTER, () -> new BlockModelPrinter<>(ComputerCraftBlocks.PRINTER));

        ModelHelper.setBlockModel(ComputerCraftBlocks.TURTLE_NORMAL, () -> new BlockModelTurtle<>(ComputerCraftBlocks.TURTLE_NORMAL));

        ModelHelper.setBlockModel(ComputerCraftBlocks.TURTLE_ADVANCED, () -> new BlockModelTurtle<>(ComputerCraftBlocks.TURTLE_ADVANCED));

        ModelHelper.setBlockModel(ComputerCraftBlocks.MONITOR_NORMAL, () -> new BlockModelMonitor<>(ComputerCraftBlocks.MONITOR_NORMAL, "computercraft:block/monitor_normal"));

        ModelHelper.setBlockModel(ComputerCraftBlocks.MONITOR_ADVANCED, () -> new BlockModelMonitor<>(ComputerCraftBlocks.MONITOR_ADVANCED, "computercraft:block/monitor_advanced"));

        ComputerCraft.log.info("Block Models initialized.");
    }

    @Override
    public void initItemModels(ItemModelDispatcher dispatcher) {
        ModelHelper.setItemModel(ComputerCraftItems.COMPUTER_NORMAL, () -> new ItemModelRotatedBlock(ComputerCraftItems.COMPUTER_NORMAL));
        ModelHelper.setItemModel(ComputerCraftItems.COMPUTER_ADVANCED, () -> new ItemModelRotatedBlock(ComputerCraftItems.COMPUTER_ADVANCED));

        ModelHelper.setItemModel(ComputerCraftItems.POCKET_COMPUTER_NORMAL, () -> {
            ItemModelStandard itemModelStandard = new ItemModelPocketComputer(ComputerCraftItems.POCKET_COMPUTER_NORMAL);
            itemModelStandard.icon = TextureRegistry.getTexture(new NamespaceID(MOD_ID, "item/pocket_computer_normal"));
            return itemModelStandard;
        });

        ModelHelper.setItemModel(ComputerCraftItems.POCKET_COMPUTER_ADVANCED, () -> {
            ItemModelStandard itemModelStandard = new ItemModelPocketComputer(ComputerCraftItems.POCKET_COMPUTER_ADVANCED);
            itemModelStandard.icon = TextureRegistry.getTexture(new NamespaceID(MOD_ID, "item/pocket_computer_advanced"));
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
            ItemModelStandard itemModelStandard = new ItemModelDisk(ComputerCraftItems.DISK);
            itemModelStandard.icon = TextureRegistry.getTexture(new NamespaceID(MOD_ID, "item/disk_frame"));
            return itemModelStandard;
        });

        ModelHelper.setItemModel(ComputerCraftItems.TREASURE_DISK, () -> {
            ItemModelStandard itemModelStandard = new ItemModelDisk(ComputerCraftItems.TREASURE_DISK);
            itemModelStandard.icon = TextureRegistry.getTexture(new NamespaceID(MOD_ID, "item/disk_frame"));
            return itemModelStandard;
        });

        ModelHelper.setItemModel(ComputerCraftItems.PRINTED_PAGE, () -> {
            ItemModelStandard itemModelStandard = new ItemModelStandard(ComputerCraftItems.PRINTED_PAGE, false);
            itemModelStandard.icon = TextureRegistry.getTexture(new NamespaceID(MOD_ID, "item/printed_page"));
            return itemModelStandard;
        });

        ModelHelper.setItemModel(ComputerCraftItems.PRINTED_PAGES, () -> {
            ItemModelStandard itemModelStandard = new ItemModelStandard(ComputerCraftItems.PRINTED_PAGES, false);
            itemModelStandard.icon = TextureRegistry.getTexture(new NamespaceID(MOD_ID, "item/printed_pages"));
            return itemModelStandard;
        });

        ModelHelper.setItemModel(ComputerCraftItems.PRINTED_BOOK, () -> {
            ItemModelStandard itemModelStandard = new ItemModelStandard(ComputerCraftItems.PRINTED_BOOK, false);
            itemModelStandard.icon = TextureRegistry.getTexture(new NamespaceID(MOD_ID, "item/printed_book"));
            return itemModelStandard;
        });

        ModelHelper.setItemModel(ComputerCraftItems.CABLE, () -> new ItemModelCable(ComputerCraftItems.CABLE));
        ModelHelper.setItemModel(ComputerCraftItems.WIRED_MODEM, () -> new ItemModelCable(ComputerCraftItems.WIRED_MODEM));

        ComputerCraft.log.info("Item Models initialized.");
    }

    @Override
    public void initEntityModels(EntityRendererDispatcher dispatcher) {

    }

    @Override
    public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {
        ModelHelper.setTileEntityModel(TileMonitor.class, new TileEntityMonitorRenderer());

        ModelHelper.setTileEntityModel(TileTurtle.class, new TileEntityRendererTurtle());

        ComputerCraft.log.info("Tile Entity Models initialized.");
    }

    @Override
    public void initBlockColors(BlockColorDispatcher dispatcher) {
    }
}
