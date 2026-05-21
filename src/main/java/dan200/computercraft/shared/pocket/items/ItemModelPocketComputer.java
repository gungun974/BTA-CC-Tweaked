package dan200.computercraft.shared.pocket.items;

import dan200.computercraft.shared.common.IColouredItem;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemModelPocketComputer extends ItemModelStandard {
    public static final IconCoordinate POCKET_COMPUTER_FRAME = TextureRegistry.getTexture("computercraft:item/pocket_computer_frame");
    public static final IconCoordinate POCKET_COMPUTER_ON = TextureRegistry.getTexture("computercraft:item/pocket_computer_on");
    public static final IconCoordinate POCKET_COMPUTER_BLINK = TextureRegistry.getTexture("computercraft:item/pocket_computer_blink");
    public static final IconCoordinate POCKET_COMPUTER_LIGHT = TextureRegistry.getTexture("computercraft:item/pocket_computer_light");
    public static final IconCoordinate POCKET_COMPUTER_COLOUR = TextureRegistry.getTexture("computercraft:item/pocket_computer_colour");

    public ItemModelPocketComputer(Item item) {
        super(item, false);
    }

    @Override
    public @NotNull IconCoordinate getIcon(@Nullable Entity entity, @NotNull ItemStack itemStack) {
        return switch (ItemPocketComputer.getState(itemStack)) {
            case ON -> POCKET_COMPUTER_ON;
            case BLINKING -> POCKET_COMPUTER_BLINK;
            default -> POCKET_COMPUTER_FRAME;
        };
    }

    @Override
    protected void renderSingle(@NotNull TessellatorGeneral tessellator, @Nullable Entity holder, @NotNull ItemStack itemStack, boolean items3d, byte lightIndex, int color, float partialTick, boolean mirrorX) {
        super.renderSingle(tessellator, holder, itemStack, items3d, lightIndex, color, partialTick, mirrorX);

        int colour = IColouredItem.getColourBasic(itemStack);
        if (colour != -1) {
            renderCoordinate(tessellator, POCKET_COMPUTER_COLOUR, lightIndex, (colour & 0x00FFFFFF) | 0xFF000000, items3d, mirrorX);
        } else {
            renderCoordinate(tessellator, this.icon, lightIndex, color, items3d, mirrorX);
        }

        int lightState = ItemPocketComputer.getLightState(itemStack);
        if (lightState != -1) {
            renderCoordinate(tessellator, POCKET_COMPUTER_LIGHT, lightIndex, (lightState & 0x00FFFFFF) | 0xFF000000, items3d, mirrorX);
        }
    }
}
