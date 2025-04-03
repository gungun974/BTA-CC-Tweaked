package dan200.computercraft.shared.pocket.items;

import dan200.computercraft.ComputerCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Font;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.collection.NamespaceID;
import org.lwjgl.opengl.GL11;

import static dan200.computercraft.ComputerCraft.MOD_ID;

public class PocketComputerItemModel extends ItemModelStandard {
    public PocketComputerItemModel(Item item, String namespace) {
        super(item, namespace);
    }

    public static final IconCoordinate POCKET_COMPUTER_FRAME = TextureRegistry.getTexture("computercraft:item/pocket_computer_frame");
    public static final IconCoordinate POCKET_COMPUTER_ON = TextureRegistry.getTexture("computercraft:item/pocket_computer_on");
    public static final IconCoordinate POCKET_COMPUTER_BLINK = TextureRegistry.getTexture("computercraft:item/pocket_computer_blink");
    public static final IconCoordinate POCKET_COMPUTER_LIGHT = TextureRegistry.getTexture("computercraft:item/pocket_computer_light");

    @Override
    public void renderItemIntoGui(
        Tessellator tessellator, Font font, TextureManager textureManager, ItemStack itemStack, int x, int y, float brightness, float alpha
    ) {
        if (itemStack != null) {
            Minecraft mc = Minecraft.getMinecraft();
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glEnable(2884);
            IconCoordinate textureIndex = this.getIcon(mc.thePlayer, itemStack);
            textureIndex.parentAtlas.bind();
            if (this.useColor) {
                int color = this.getColor(itemStack);
                float r = (float)(color >> 16 & 0xFF) / 255.0F;
                float g = (float)(color >> 8 & 0xFF) / 255.0F;
                float b = (float)(color & 0xFF) / 255.0F;
                GL11.glColor4f(r * brightness, g * brightness, b * brightness, alpha);
            } else {
                GL11.glColor4f(brightness, brightness, brightness, alpha);
            }

            int lightState = ItemPocketComputer.getLightState(itemStack);

            switch (ItemPocketComputer.getState(itemStack)) {
                case OFF:
                    this.renderTexturedQuad(tessellator, x, y, POCKET_COMPUTER_FRAME);
                    this.renderTexturedQuad(tessellator, x, y, textureIndex);
                    break;
                case ON:
                    this.renderTexturedQuad(tessellator, x, y, POCKET_COMPUTER_ON);
                    this.renderTexturedQuad(tessellator, x, y, textureIndex);
                    break;
                case BLINKING:
                    this.renderTexturedQuad(tessellator, x, y, POCKET_COMPUTER_BLINK);
                    this.renderTexturedQuad(tessellator, x, y, textureIndex);
            }


            if (lightState != -1) {
                float r = (float)(lightState >> 16 & 0xFF) / 255.0F;
                float g = (float)(lightState >> 8 & 0xFF) / 255.0F;
                float b = (float)(lightState & 0xFF) / 255.0F;
                GL11.glColor4f(r * brightness, g * brightness, b * brightness, alpha);
                this.renderTexturedQuad(tessellator, x, y, POCKET_COMPUTER_LIGHT);
            }


            GL11.glEnable(2896);
            GL11.glDisable(2884);
            GL11.glDisable(3042);
        }
    }
}
