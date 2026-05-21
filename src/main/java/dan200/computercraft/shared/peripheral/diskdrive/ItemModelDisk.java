package dan200.computercraft.shared.peripheral.diskdrive;

import dan200.computercraft.shared.common.ComputerCraftItems;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemModelDisk extends ItemModelStandard {
    public static final IconCoordinate DISK_COLOUR = TextureRegistry.getTexture("computercraft:item/disk_colour");

    public ItemModelDisk(Item item) {
        super(item, false);
    }

    @Override
    protected void renderSingle(@NotNull TessellatorGeneral tessellator, @Nullable Entity holder, @NotNull ItemStack itemStack, boolean items3d, byte lightIndex, int color, float partialTick, boolean mirrorX) {
        super.renderSingle(tessellator, holder, itemStack, items3d, lightIndex, color, partialTick, mirrorX);

        int diskColour = ComputerCraftItems.DISK.getColour(itemStack);
        if (diskColour != -1) {
            renderCoordinate(tessellator, DISK_COLOUR, lightIndex, (diskColour & 0x00FFFFFF) | 0xFF000000, items3d, mirrorX);
        }
    }
}
