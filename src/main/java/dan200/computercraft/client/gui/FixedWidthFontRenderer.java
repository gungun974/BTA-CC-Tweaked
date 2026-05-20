/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.client.gui;

import dan200.computercraft.client.FrameInfo;
import dan200.computercraft.core.terminal.Terminal;
import dan200.computercraft.core.terminal.TextBuffer;
import dan200.computercraft.shared.util.Colour;
import dan200.computercraft.shared.util.Palette;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.renderer.DrawMode;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public final class FixedWidthFontRenderer {
    public static final int FONT_HEIGHT = 9;
    public static final int FONT_WIDTH = 6;
    public static final float WIDTH = 256.0f;
    public static final float BACKGROUND_START = (WIDTH - 6.0f) / WIDTH;
    public static final float BACKGROUND_END = (WIDTH - 4.0f) / WIDTH;

    private FixedWidthFontRenderer() {
    }

    public static void bindFont() {
        Minecraft.getMinecraft().textureManager.loadTexture("/assets/computercraft/textures/gui/term_font.png").bind();
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
    }

    private static void drawChar(@NotNull TessellatorGeneral tessellator, float x, float y, int index, float r, float g, float b) {
        if (index == '\0' || index == ' ') return;

        int column = index % 16;
        int row = index / 16;

        int xStart = 1 + column * (FONT_WIDTH + 2);
        int yStart = 1 + row * (FONT_HEIGHT + 2);

        tessellator.setColor4f(r, g, b, 1.0f);
        tessellator.addVertexWithUV(x, y, 0, xStart / WIDTH, yStart / WIDTH);
        tessellator.addVertexWithUV(x, y + FONT_HEIGHT, 0, xStart / WIDTH, (yStart + FONT_HEIGHT) / WIDTH);
        tessellator.addVertexWithUV(x + FONT_WIDTH, y, 0, (xStart + FONT_WIDTH) / WIDTH, yStart / WIDTH);
        tessellator.addVertexWithUV(x + FONT_WIDTH, y, 0, (xStart + FONT_WIDTH) / WIDTH, yStart / WIDTH);
        tessellator.addVertexWithUV(x, y + FONT_HEIGHT, 0, xStart / WIDTH, (yStart + FONT_HEIGHT) / WIDTH);
        tessellator.addVertexWithUV(x + FONT_WIDTH, y + FONT_HEIGHT, 0, (xStart + FONT_WIDTH) / WIDTH, (yStart + FONT_HEIGHT) / WIDTH);
    }

    private static void drawQuad(@NotNull TessellatorGeneral tessellator, float x, float y, float width, float height,
                                 @NotNull Palette palette, boolean greyscale, char colourIndex) {
        double[] colour = palette.getColour(getColour(colourIndex, Colour.BLACK));
        float r, g, b;
        if (greyscale) {
            r = g = b = toGreyscale(colour);
        } else {
            r = (float) colour[0];
            g = (float) colour[1];
            b = (float) colour[2];
        }
        drawQuad(tessellator, x, y, width, height, r, g, b);
    }

    private static void drawQuad(@NotNull TessellatorGeneral tessellator, float x, float y, float width, float height,
                                 float r, float g, float b) {
        tessellator.setColor4f(r, g, b, 1.0f);
        tessellator.addVertexWithUV(x, y, 0, BACKGROUND_START, BACKGROUND_START);
        tessellator.addVertexWithUV(x, y + height, 0, BACKGROUND_START, BACKGROUND_END);
        tessellator.addVertexWithUV(x + width, y, 0, BACKGROUND_END, BACKGROUND_START);
        tessellator.addVertexWithUV(x + width, y, 0, BACKGROUND_END, BACKGROUND_START);
        tessellator.addVertexWithUV(x, y + height, 0, BACKGROUND_START, BACKGROUND_END);
        tessellator.addVertexWithUV(x + width, y + height, 0, BACKGROUND_END, BACKGROUND_END);
    }

    private static void drawBackground(@NotNull TessellatorGeneral tessellator, float x, float y,
                                       @NotNull TextBuffer backgroundColour, @NotNull Palette palette, boolean greyscale,
                                       float leftMarginSize, float rightMarginSize, float height) {
        if (leftMarginSize > 0) {
            drawQuad(tessellator, x - leftMarginSize, y, leftMarginSize, height, palette, greyscale, backgroundColour.charAt(0));
        }

        if (rightMarginSize > 0) {
            drawQuad(tessellator,
                x + backgroundColour.length() * FONT_WIDTH,
                y,
                rightMarginSize,
                height,
                palette,
                greyscale,
                backgroundColour.charAt(backgroundColour.length() - 1));
        }

        int blockStart = 0;
        char blockColour = '\0';
        for (int i = 0; i < backgroundColour.length(); i++) {
            char colourIndex = backgroundColour.charAt(i);
            if (colourIndex == blockColour) continue;

            if (blockColour != '\0') {
                drawQuad(tessellator, x + blockStart * FONT_WIDTH, y, FONT_WIDTH * (i - blockStart), height, palette, greyscale, blockColour);
            }

            blockColour = colourIndex;
            blockStart = i;
        }

        if (blockColour != '\0') {
            drawQuad(tessellator,
                x + blockStart * FONT_WIDTH,
                y,
                FONT_WIDTH * (backgroundColour.length() - blockStart),
                height,
                palette,
                greyscale,
                blockColour);
        }
    }

    private static void drawStringVertices(@NotNull TessellatorGeneral tessellator, float x, float y,
                                           @NotNull TextBuffer text, @NotNull TextBuffer textColour,
                                           @Nullable TextBuffer backgroundColour, @NotNull Palette palette,
                                           boolean greyscale, float leftMarginSize, float rightMarginSize) {
        if (backgroundColour != null) {
            drawBackground(tessellator, x, y, backgroundColour, palette, greyscale, leftMarginSize, rightMarginSize, FONT_HEIGHT);
        }

        for (int i = 0; i < text.length(); i++) {
            double[] colour = palette.getColour(getColour(textColour.charAt(i), Colour.BLACK));
            float r, g, b;
            if (greyscale) {
                r = g = b = toGreyscale(colour);
            } else {
                r = (float) colour[0];
                g = (float) colour[1];
                b = (float) colour[2];
            }

            int index = text.charAt(i);
            if (index > 255) index = '?';
            drawChar(tessellator, x + i * FONT_WIDTH, y, index, r, g, b);
        }
    }

    private static void drawTerminalVertices(@NotNull TessellatorGeneral tessellator, float x, float y,
                                             @NotNull Terminal terminal, boolean greyscale,
                                             float topMarginSize, float bottomMarginSize,
                                             float leftMarginSize, float rightMarginSize) {
        Palette palette = terminal.getPalette();
        int height = terminal.getHeight();

        drawBackground(tessellator, x, y - topMarginSize,
            terminal.getBackgroundColourLine(0),
            palette, greyscale, leftMarginSize, rightMarginSize, topMarginSize);

        drawBackground(tessellator, x, y + height * FONT_HEIGHT,
            terminal.getBackgroundColourLine(height - 1),
            palette, greyscale, leftMarginSize, rightMarginSize, bottomMarginSize);

        for (int i = 0; i < height; i++) {
            drawStringVertices(tessellator,
                x, y + FONT_HEIGHT * i,
                terminal.getLine(i),
                terminal.getTextColourLine(i),
                terminal.getBackgroundColourLine(i),
                palette, greyscale, leftMarginSize, rightMarginSize);
        }
    }

    public static void drawString(@NotNull Matrix4f matrix, float x, float y,
                                  @NotNull TextBuffer text, @NotNull TextBuffer textColour,
                                  @Nullable TextBuffer backgroundColour, @NotNull Palette palette,
                                  boolean greyscale, float leftMarginSize, float rightMarginSize) {
        TessellatorGeneral tessellator = GLRenderer.getTessellator();
        GLRenderer.pushFrame();
        GLRenderer.modelM4f().set(matrix);
        bindFont();
        tessellator.startDrawing(DrawMode.TRIANGLES);
        drawStringVertices(tessellator, x, y, text, textColour, backgroundColour, palette, greyscale, leftMarginSize, rightMarginSize);
        tessellator.draw();
        GLRenderer.popFrame();
    }

    public static void drawCursor(@NotNull Matrix4f matrix, float x, float y,
                                  @NotNull Terminal terminal, boolean greyscale) {
        Palette palette = terminal.getPalette();
        int width = terminal.getWidth();
        int height = terminal.getHeight();

        int cursorX = terminal.getCursorX();
        int cursorY = terminal.getCursorY();
        if (terminal.getCursorBlink() && cursorX >= 0 && cursorX < width && cursorY >= 0 && cursorY < height && FrameInfo.getGlobalCursorBlink()) {
            double[] colour = palette.getColour(15 - terminal.getTextColour());
            float r, g, b;
            if (greyscale) {
                r = g = b = toGreyscale(colour);
            } else {
                r = (float) colour[0];
                g = (float) colour[1];
                b = (float) colour[2];
            }

            TessellatorGeneral tessellator = GLRenderer.getTessellator();
            GLRenderer.pushFrame();
            GLRenderer.modelM4f().set(matrix);
            bindFont();
            tessellator.startDrawing(DrawMode.TRIANGLES);
            drawChar(tessellator, x + cursorX * FONT_WIDTH, y + cursorY * FONT_HEIGHT, '_', r, g, b);
            tessellator.draw();
            GLRenderer.popFrame();
        }
    }

    public static void drawTerminal(@NotNull Matrix4f matrix, float x, float y,
                                    @NotNull Terminal terminal, boolean greyscale,
                                    float topMarginSize, float bottomMarginSize,
                                    float leftMarginSize, float rightMarginSize) {
        TessellatorGeneral tessellator = GLRenderer.getTessellator();
        GLRenderer.pushFrame();
        GLRenderer.modelM4f().set(matrix);
        bindFont();
        tessellator.startDrawing(DrawMode.TRIANGLES);
        drawTerminalVertices(tessellator, x, y, terminal, greyscale, topMarginSize, bottomMarginSize, leftMarginSize, rightMarginSize);
        tessellator.draw();
        GLRenderer.popFrame();

        drawCursor(matrix, x, y, terminal, greyscale);
    }

    public static void drawEmptyTerminal(@NotNull Matrix4f matrix, float x, float y,
                                         float width, float height) {
        TessellatorGeneral tessellator = GLRenderer.getTessellator();
        GLRenderer.pushFrame();
        GLRenderer.modelM4f().set(matrix);
        bindFont();
        tessellator.startDrawing(DrawMode.TRIANGLES);
        drawQuad(tessellator, x, y, width, height, Colour.BLACK.getR(), Colour.BLACK.getG(), Colour.BLACK.getB());
        tessellator.draw();
        GLRenderer.popFrame();
    }

    public static void drawBlocker(@NotNull Matrix4f matrix, float x, float y,
                                   float width, float height) {
        TessellatorGeneral tessellator = GLRenderer.getTessellator();
        GLRenderer.pushFrame();
        GLRenderer.modelM4f().set(matrix);
        bindFont();
        tessellator.startDrawing(DrawMode.TRIANGLES);
        drawQuad(tessellator, x, y, width, height, Colour.BLACK.getR(), Colour.BLACK.getG(), Colour.BLACK.getB());
        tessellator.draw();
        GLRenderer.popFrame();
    }

    public static int getColour(char c, Colour def) {
        return 15 - Terminal.getColour(c, def);
    }

    public static float toGreyscale(double[] rgb) {
        return (float) ((rgb[0] + rgb[1] + rgb[2]) / 3);
    }
}
