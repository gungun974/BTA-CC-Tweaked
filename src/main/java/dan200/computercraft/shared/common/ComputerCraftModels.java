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

public class ComputerCraftModels {
    public void initBlockModels(BlockModelDispatcher dispatcher) {

        final IconCoordinate a = TextureRegistry.getTexture("computercraft:block/computer_normal_front");

        try {
            TextureRegistry.initializeAllFiles("computercraft", a.parentAtlas, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        dispatcher.addDispatch(new BlockModelComputer<>(ComputerCraftBlocks.COMPUTER_NORMAL, "computercraft:block/computer_normal"));

        dispatcher.addDispatch(new BlockModelComputer<>(ComputerCraftBlocks.COMPUTER_ADVANCED, "computercraft:block/computer_advanced"));

        dispatcher.addDispatch(new BlockModelCorrectRotable<>(ComputerCraftBlocks.SPEAKER, BlockModelDispatcher.loadDataModel("computercraft:block/speaker").asModel()));

        dispatcher.addDispatch(new BlockModelWirelessModem<>(ComputerCraftBlocks.WIRELESS_MODEM_NORMAL, "computercraft:block/wireless_modem_normal"));

        dispatcher.addDispatch(new BlockModelWirelessModem<>(ComputerCraftBlocks.WIRELESS_MODEM_ADVANCED, "computercraft:block/wireless_modem_advanced"));

        dispatcher.addDispatch(new BlockModelModemFull<>(ComputerCraftBlocks.WIRED_MODEM_FULL));

        dispatcher.addDispatch(new BlockModelCable<>(ComputerCraftBlocks.CABLE));

        dispatcher.addDispatch(new BlockModelDiskDrive<>(ComputerCraftBlocks.DISK_DRIVE));

        dispatcher.addDispatch(new BlockModelPrinter<>(ComputerCraftBlocks.PRINTER));

        dispatcher.addDispatch(new BlockModelTurtle<>(ComputerCraftBlocks.TURTLE_NORMAL));

        dispatcher.addDispatch(new BlockModelTurtle<>(ComputerCraftBlocks.TURTLE_ADVANCED));

        dispatcher.addDispatch(new BlockModelMonitor<>(ComputerCraftBlocks.MONITOR_NORMAL, "computercraft:block/monitor_normal"));

        dispatcher.addDispatch(new BlockModelMonitor<>(ComputerCraftBlocks.MONITOR_ADVANCED, "computercraft:block/monitor_advanced"));

        ComputerCraft.log.info("Block Models initialized.");
    }

    public void initItemModels(ItemModelDispatcher dispatcher) {
        dispatcher.addDispatch(new ItemModelRotatedBlock(ComputerCraftItems.COMPUTER_NORMAL));
        dispatcher.addDispatch(new ItemModelRotatedBlock(ComputerCraftItems.COMPUTER_ADVANCED));

        dispatcher.addDispatch(new ItemModelPocketComputer(ComputerCraftItems.POCKET_COMPUTER_NORMAL)
            .setIcon("computercraft:item/pocket_computer_normal"));

        dispatcher.addDispatch(new ItemModelPocketComputer(ComputerCraftItems.POCKET_COMPUTER_ADVANCED)
            .setIcon("computercraft:item/pocket_computer_advanced"));

        dispatcher.addDispatch(new ItemModelRotatedBlock(ComputerCraftItems.SPEAKER));
        dispatcher.addDispatch(new ItemModelRotatedBlock(ComputerCraftItems.DISK_DRIVE));
        dispatcher.addDispatch(new ItemModelRotatedBlock(ComputerCraftItems.PRINTER));

        dispatcher.addDispatch(new ItemModelRotatedBlock(ComputerCraftItems.MONITOR_NORMAL));
        dispatcher.addDispatch(new ItemModelRotatedBlock(ComputerCraftItems.MONITOR_ADVANCED));

        dispatcher.addDispatch(new ItemModelBlockTurtle(ComputerCraftItems.TURTLE_NORMAL));
        dispatcher.addDispatch(new ItemModelBlockTurtle(ComputerCraftItems.TURTLE_ADVANCED));

        dispatcher.addDispatch(new ItemModelRotatedBlock(ComputerCraftItems.WIRELESS_MODEM_NORMAL));
        dispatcher.addDispatch(new ItemModelRotatedBlock(ComputerCraftItems.WIRELESS_MODEM_ADVANCED));

        dispatcher.addDispatch(new ItemModelDisk(ComputerCraftItems.DISK)
            .setIcon("computercraft:item/disk_frame"));

        dispatcher.addDispatch(new ItemModelStandard(ComputerCraftItems.TREASURE_DISK, false)
            .setIcon("computercraft:item/disk_frame"));

        dispatcher.addDispatch(new ItemModelStandard(ComputerCraftItems.PRINTED_PAGE, false)
            .setIcon("computercraft:item/printed_page"));

        dispatcher.addDispatch(new ItemModelStandard(ComputerCraftItems.PRINTED_PAGES, false)
            .setIcon("computercraft:item/printed_pages"));

        dispatcher.addDispatch(new ItemModelStandard(ComputerCraftItems.PRINTED_BOOK, false)
            .setIcon("computercraft:item/printed_book"));

        dispatcher.addDispatch(new ItemModelCable(ComputerCraftItems.CABLE));
        dispatcher.addDispatch(new ItemModelCable(ComputerCraftItems.WIRED_MODEM));

        ComputerCraft.log.info("Item Models initialized.");
    }

    public void initEntityModels(EntityRendererDispatcher dispatcher) {

    }

    public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {
        dispatcher.assignRenderer(TileMonitor.class, new TileEntityMonitorRenderer());

        dispatcher.assignRenderer(TileTurtle.class, new TileEntityRendererTurtle());

        ComputerCraft.log.info("Tile Entity Models initialized.");
    }

    public void initBlockColors(BlockColorDispatcher dispatcher) {
    }
}
