package dan200.computercraft.shared.common;

import dan200.computercraft.shared.computer.blocks.BlockLogicComputer;
import dan200.computercraft.shared.computer.blocks.BlockModelComputer;
import dan200.computercraft.shared.computer.blocks.TileEntityComputer;
import dan200.computercraft.shared.computer.items.ItemBlockComputer;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.util.collection.NamespaceID;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.EntityHelper;

import static dan200.computercraft.ComputerCraft.MOD_ID;

public class ComputerCraftBlocks {
    public static final Block<?> COMPUTER_NORMAL;

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
    }
}
