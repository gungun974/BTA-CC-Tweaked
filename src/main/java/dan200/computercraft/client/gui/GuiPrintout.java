/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.client.gui;

import dan200.computercraft.client.render.PrintoutRenderer;
import dan200.computercraft.core.terminal.TextBuffer;
import dan200.computercraft.fabric.GLFWMouseManager;
import dan200.computercraft.shared.media.items.ItemPrintout;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.input.Keyboard;

import java.util.UUID;

public class GuiPrintout extends Screen {
    private final boolean book;
    private final int pages;
    private final TextBuffer[] text;
    private final TextBuffer[] colours;
    private int page;

    UUID glfwScrollCallbackId;

    public GuiPrintout(ItemStack stack) {
        super();

        String[] text = ItemPrintout.getText(stack);
        this.text = new TextBuffer[text.length];
        for (int i = 0; i < this.text.length; i++) {
            this.text[i] = new TextBuffer(text[i]);
        }

        String[] colours = ItemPrintout.getColours(stack);
        this.colours = new TextBuffer[colours.length];
        for (int i = 0; i < this.colours.length; i++) {
            this.colours[i] = new TextBuffer(colours[i]);
        }

        page = 0;
        pages = Math.max(this.text.length / ItemPrintout.LINES_PER_PAGE, 1);
        book = ((ItemPrintout) stack
            .getItem()).getType() == ItemPrintout.Type.BOOK;
    }

    @Override
    public void init() {
        GLFWMouseManager.getInstance().removeScrollObserver(glfwScrollCallbackId);
        glfwScrollCallbackId = GLFWMouseManager.getInstance().addScrollObserver(
            this::glfwScrollCallback
        );
    }

    @Override
    public void removed() {
        GLFWMouseManager.getInstance().removeScrollObserver(glfwScrollCallbackId);
    }

    public void glfwScrollCallback(long window, double xoffset, double yoffset) {
        if (yoffset < 0) {
            // Scroll up goes to the next page
            if (page < pages - 1) {
                page++;
            }
            return;
        }

        if (yoffset > 0) {
            // Scroll down goes to the previous page
            if (page > 0) {
                page--;
            }
            return;
        }

    }

    @Override
    public void render(int mx, int my, float partialTick) {
        super.init();
        // We must take the background further back in order to not overlap with our printed pages.
        zLevel -= 1;
        renderBackground();
        zLevel += 1;

        // Draw the printout
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.textureManager.loadTexture("/assets/computercraft/textures/gui/printout.png").bind();

        int x = (width - PrintoutRenderer.X_SIZE) / 2;
        int y = (height - PrintoutRenderer.Y_SIZE) / 2;

        PrintoutRenderer.drawBorder(x, y, zLevel, page, pages, book, GLRenderer.getTessellator());
        GLRenderer.modelM4f().translate(0, 0, 0.02f);
        PrintoutRenderer.drawText(x + PrintoutRenderer.X_TEXT_MARGIN, y + PrintoutRenderer.Y_TEXT_MARGIN, 0.02f, ItemPrintout.LINES_PER_PAGE * page, text, colours, GLRenderer.modelM4f());
    }

    @Override
    public void keyPressed(char eventCharacter, int eventKey, int mx, int my) {
        super.keyPressed(eventCharacter, eventKey, mx, my);

        if (eventKey == Keyboard.KEY_RIGHT) {
            if (page < pages - 1) {
                page++;
            }
            return;
        }

        if (eventKey == Keyboard.KEY_LEFT) {
            if (page > 0) {
                page--;
            }
        }
    }
}
