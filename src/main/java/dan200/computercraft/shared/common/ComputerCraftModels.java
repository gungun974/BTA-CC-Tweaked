package dan200.computercraft.shared.common;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.shared.computer.blocks.BlockModelComputer;
import dan200.computercraft.shared.peripheral.diskdrive.BlockModelDiskDrive;
import dan200.computercraft.shared.peripheral.modem.wireless.BlockModelWirelessModem;
import dan200.computercraft.shared.peripheral.monitor.BlockModelMonitor;
import dan200.computercraft.shared.peripheral.monitor.TileEntityMonitorRenderer;
import dan200.computercraft.shared.peripheral.monitor.TileMonitor;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelRotatable;
import net.minecraft.client.render.block.model.BlockModelStandard;
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

        ModelHelper.setBlockModel(ComputerCraftBlocks.SPEAKER, () -> new BlockModelRotatable<>(ComputerCraftBlocks.SPEAKER)
            .setTex(0, "computercraft:block/speaker_side", Side.SOUTH, Side.EAST, Side.WEST)
            .setTex(0, "computercraft:block/speaker_top", Side.TOP, Side.BOTTOM)
            .setTex(0, "computercraft:block/speaker_front", Side.NORTH)
        );

        ModelHelper.setBlockModel(ComputerCraftBlocks.WIRELESS_MODEM_NORMAL, () -> new BlockModelWirelessModem<>(ComputerCraftBlocks.WIRELESS_MODEM_NORMAL)
            .setTex(0, "computercraft:block/wireless_modem_normal_face", Side.TOP, Side.NORTH, Side.SOUTH, Side.EAST, Side.WEST)
            .setTex(0, "computercraft:block/modem_back", Side.BOTTOM)
        );

        ModelHelper.setBlockModel(ComputerCraftBlocks.WIRED_MODEM_FULL, () -> new BlockModelStandard<>(ComputerCraftBlocks.WIRED_MODEM_FULL)
            .setTex(0, "computercraft:block/wired_modem_face", Side.sides)
        );

        ModelHelper.setBlockModel(ComputerCraftBlocks.CABLE, () -> new BlockModelStandard<>(ComputerCraftBlocks.CABLE)
            .setTex(0, "computercraft:block/cable_core", Side.sides)
        );

        ModelHelper.setBlockModel(ComputerCraftBlocks.DISK_DRIVE, () -> new BlockModelDiskDrive<>(ComputerCraftBlocks.DISK_DRIVE)
            .setTex(0, "computercraft:block/disk_drive_side", Side.SOUTH, Side.EAST, Side.WEST)
            .setTex(0, "computercraft:block/disk_drive_top", Side.TOP, Side.BOTTOM)
            .setTex(0, "computercraft:block/disk_drive_front", Side.NORTH)
        );

        ModelHelper.setBlockModel(ComputerCraftBlocks.TURTLE_NORMAL, () -> new BlockModelComputer<>(ComputerCraftBlocks.TURTLE_NORMAL)
            .setTex(0, "computercraft:block/computer_normal_side", Side.SOUTH, Side.EAST, Side.WEST)
            .setTex(0, "computercraft:block/computer_normal_top", Side.TOP, Side.BOTTOM)
            .setTex(0, "computercraft:block/computer_normal_front", Side.NORTH)
        );

        ModelHelper.setBlockModel(ComputerCraftBlocks.MONITOR_ADVANCED, () -> new BlockModelMonitor<>(ComputerCraftBlocks.MONITOR_ADVANCED)
            .setTex(0, "computercraft:block/monitor_advanced_4", Side.SOUTH, Side.EAST, Side.WEST )
            .setTex(0, "computercraft:block/monitor_advanced_0", Side.TOP, Side.BOTTOM)
            .setTex(0, "computercraft:block/monitor_advanced_15", Side.NORTH)
        );

        ComputerCraft.log.info("Block Models initialized.");
    }

    @Override
    public void initItemModels(ItemModelDispatcher dispatcher) {
        ModelHelper.setItemModel(ComputerCraftItems.DISK, () -> {
            ItemModelStandard itemModelStandard = new ItemModelStandard(ComputerCraftItems.DISK, MOD_ID);
            itemModelStandard.icon = TextureRegistry.getTexture(NamespaceID.getPermanent(MOD_ID, "item/disk_frame"));
            return itemModelStandard;
        });

        ComputerCraft.log.info("Item Models initialized.");
    }

    @Override
    public void initEntityModels(EntityRenderDispatcher dispatcher) {}

    @Override
    public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {
        ModelHelper.setTileEntityModel(TileMonitor.class, TileEntityMonitorRenderer::new);

        ComputerCraft.log.info("Tile Entity Models initialized.");
    }

    @Override
    public void initBlockColors(BlockColorDispatcher dispatcher) {}
}
