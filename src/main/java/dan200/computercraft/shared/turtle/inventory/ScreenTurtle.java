package dan200.computercraft.shared.turtle.inventory;

import dan200.computercraft.shared.turtle.blocks.TileTurtle;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.lwjgl.opengl.GL11;

public class ScreenTurtle extends ScreenContainerAbstract {
    public ScreenTurtle(ContainerInventory inventoryplayer, TileTurtle tileentitydispenser) {
        super(new MenuTurtle(inventoryplayer, tileentitydispenser));
        this.xSize = 254;
        this.ySize = 217;
    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
        this.font.drawString(I18n.getInstance().translateKey("gui.dispenser.label.dispenser"), 8, 6, 4210752);
        this.font.drawString(I18n.getInstance().translateKey("gui.dispenser.label.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.textureManager.loadTexture("/assets/computercraft/textures/gui/turtle_normal.png").bind();
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
    }
}
