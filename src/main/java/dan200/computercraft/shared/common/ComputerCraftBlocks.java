package dan200.computercraft.shared.common;

import dan200.computercraft.shared.computer.blocks.BlockLogicComputer;
import dan200.computercraft.shared.computer.blocks.BlockModelComputer;
import dan200.computercraft.shared.computer.blocks.TileEntityComputer;
import dan200.computercraft.shared.computer.items.ItemBlockComputer;
import dan200.computercraft.shared.peripheral.modem.wired.BlockCable;
import dan200.computercraft.shared.peripheral.modem.wired.BlockWiredModemFull;
import dan200.computercraft.shared.peripheral.modem.wired.TileCable;
import dan200.computercraft.shared.peripheral.modem.wired.TileWiredModemFull;
import dan200.computercraft.shared.peripheral.modem.wireless.BlockModelWirelessModem;
import dan200.computercraft.shared.peripheral.modem.wireless.BlockWirelessModem;
import dan200.computercraft.shared.peripheral.modem.wireless.TileWirelessModem;
import dan200.computercraft.shared.peripheral.speaker.BlockSpeaker;
import dan200.computercraft.shared.peripheral.speaker.SpeakerPeripheral;
import dan200.computercraft.shared.peripheral.speaker.TileSpeaker;
import net.minecraft.client.render.block.model.BlockModelFullyRotatable;
import net.minecraft.client.render.block.model.BlockModelRotatable;
import net.minecraft.client.render.block.model.BlockModelWireRedstone;
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
    public static final Block<?> SPEAKER;

    public static final Block<?> WIRELESS_MODEM_NORMAL;

    public static final Block<?> WIRED_MODEM_FULL;
    public static final Block<?> CABLE;

    public ComputerCraftBlocks() {

    }

    static {
        final IconCoordinate a = TextureRegistry.getTexture("computercraft:block/computer_normal_front");

        try {
            TextureRegistry.initializeAllFiles("computercraft", a.parentAtlas, false);
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        EntityHelper.createTileEntity(TileEntityComputer.class, new NamespaceID(MOD_ID, "computer"));

        COMPUTER_NORMAL = new BlockBuilder(MOD_ID)
            .setBlockModel(BlockModelComputer::new)
            .setTextures("computercraft:block/computer_normal_side")
            .setTopTexture("computercraft:block/computer_normal_top")
            .setBottomTexture("computercraft:block/computer_normal_top")
            .setNorthTexture("computercraft:block/computer_normal_front")
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(TileEntityComputer::new)
            .setBlockItem(ItemBlockComputer::new)
            .build("computer_normal", 10000, b -> new BlockLogicComputer(b));

        EntityHelper.createTileEntity(TileSpeaker.class, new NamespaceID(MOD_ID, "speaker"));

        SPEAKER = new BlockBuilder(MOD_ID)
            .setBlockModel(BlockModelRotatable::new)
            .setTextures("computercraft:block/speaker_side")
            .setTopTexture("computercraft:block/speaker_top")
            .setBottomTexture("computercraft:block/speaker_top")
            .setNorthTexture("computercraft:block/speaker_front")
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(TileSpeaker::new)
            .build("speaker", 10001, b -> new BlockSpeaker(b));

        EntityHelper.createTileEntity(TileWirelessModem.class, new NamespaceID(MOD_ID, "wireless_modem_normal"));

        WIRELESS_MODEM_NORMAL = new BlockBuilder(MOD_ID)
            .setBlockModel(BlockModelWirelessModem::new)
            .setTopTexture("computercraft:block/wireless_modem_normal_face")
            .setEastWestTextures("computercraft:block/wireless_modem_normal_face")
            .setNorthSouthTextures("computercraft:block/wireless_modem_normal_face")
            .setBottomTexture("computercraft:block/modem_back")
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(() -> new TileWirelessModem(false))
            .build("wireless_modem_normal", 10002, b -> new BlockWirelessModem(b, false));

        EntityHelper.createTileEntity(TileWiredModemFull.class, new NamespaceID(MOD_ID, "wired_modem_full"));

        WIRED_MODEM_FULL = new BlockBuilder(MOD_ID)
            .setTextures("computercraft:block/wired_modem_face")
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(TileWiredModemFull::new)
            .build("wired_modem_full", 10003, b -> new BlockWiredModemFull(b, Material.stone));

        EntityHelper.createTileEntity(TileCable.class, new NamespaceID(MOD_ID, "cable"));

        CABLE = new BlockBuilder(MOD_ID)
            .setTextures("computercraft:block/cable_core")
            .setHardness(1.5f)
            .setResistance(10f)
            .setTags(BlockTags.MINEABLE_BY_PICKAXE)
            .setTileEntity(TileCable::new)
            .build("cable", 10004, b -> new BlockCable(b, Material.stone));
    }
}
