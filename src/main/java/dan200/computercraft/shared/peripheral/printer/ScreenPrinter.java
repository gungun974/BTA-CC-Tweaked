/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.peripheral.printer;

import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.lwjgl.opengl.GL11;

public class ScreenPrinter extends ScreenContainerAbstract {
    public ScreenPrinter(ContainerInventory inventoryplayer, TilePrinter tilePrinter) {
        super(new MenuPrinter(inventoryplayer, tilePrinter));
    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
        this.font.drawString(I18n.getInstance().translateKey("tile.computercraft.printer.name"), 8, 6, 4210752);
        this.font.drawString(I18n.getInstance().translateKey("gui.dispenser.label.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.textureManager.loadTexture("/assets/computercraft/textures/gui/printer.png").bind();
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
    }
}
