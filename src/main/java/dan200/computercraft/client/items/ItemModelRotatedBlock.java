package dan200.computercraft.client.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.model.ItemModelBlock;
import net.minecraft.core.item.block.ItemBlock;

@Environment(EnvType.CLIENT)
public class ItemModelRotatedBlock extends ItemModelBlock {
    public ItemModelRotatedBlock(ItemBlock<?> itemBlock) {
        super(itemBlock);
    }
}
