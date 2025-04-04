/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.client.render;

import dan200.computercraft.client.gui.FixedWidthFontRenderer;
import dan200.computercraft.core.terminal.TextBuffer;
import dan200.computercraft.shared.util.Palette;
import net.minecraft.client.render.tessellator.Tessellator;

import static dan200.computercraft.client.gui.FixedWidthFontRenderer.FONT_HEIGHT;
import static dan200.computercraft.shared.media.items.ItemPrintout.LINES_PER_PAGE;

public final class PrintoutRenderer
{
    /**
     * Width of a page.
     */
    public static final int X_SIZE = 172;
    /**
     * Height of a page.
     */
    public static final int Y_SIZE = 209;
    /**
     * Padding between the left and right of a page and the text.
     */
    public static final int X_TEXT_MARGIN = 13;
    /**
     * Padding between the top and bottom of a page and the text.
     */
    public static final int Y_TEXT_MARGIN = 11;
    /**
     * Size of the leather cover.
     */
    public static final int COVER_SIZE = 12;
    private static final float BG_SIZE = 256.0f;
    /**
     * Width of the extra page texture.
     */
    private static final int X_FOLD_SIZE = 12;
    private static final int COVER_Y = Y_SIZE;
    private static final int COVER_X = X_SIZE + 4 * X_FOLD_SIZE;

    private PrintoutRenderer() {}

    public static void drawText( int x, int y, int start, TextBuffer[] text, TextBuffer[] colours )
    {
        for( int line = 0; line < LINES_PER_PAGE && line < text.length; line++ )
        {
            FixedWidthFontRenderer.drawString(
                x,
                y + line * FONT_HEIGHT,
                text[start + line],
                colours[start + line],
                null,
                Palette.DEFAULT,
                false,
                0,
                0 );
        }
    }

    public static void drawText( int x, int y, int start, String[] text, String[] colours )
    {
        for( int line = 0; line < LINES_PER_PAGE && line < text.length; line++ )
        {
            FixedWidthFontRenderer.drawString(
                x,
                y + line * FONT_HEIGHT,
                new TextBuffer( text[start + line] ),
                new TextBuffer( colours[start + line] ),
                null,
                Palette.DEFAULT,
                false,
                0,
                0 );
        }
    }

    public static void drawBorder( float x, float y, float z, int page, int pages, boolean isBook )
    {
        int leftPages = page;
        int rightPages = pages - page - 1;

        if( isBook )
        {
            // Border
            float offset = offsetAt( pages );
            float left = x - 4 - offset;
            float right = x + X_SIZE + offset - 4;

            // Left and right border
            drawTexture( left - 4, y - 8, z - 0.02f, COVER_X, 0, COVER_SIZE, Y_SIZE + COVER_SIZE * 2 );
            drawTexture( right, y - 8, z - 0.02f, COVER_X + COVER_SIZE, 0, COVER_SIZE, Y_SIZE + COVER_SIZE * 2 );

            // Draw centre panel (just stretched texture, sorry).
            drawTexture(
                x - offset,
                y,
                z - 0.02f,
                X_SIZE + offset * 2,
                Y_SIZE,
                COVER_X + COVER_SIZE / 2.0f,
                COVER_SIZE,
                COVER_SIZE,
                Y_SIZE );

            float borderX = left;
            while( borderX < right )
            {
                double thisWidth = Math.min( right - borderX, X_SIZE );
                drawTexture( borderX, y - 8, z - 0.02f, 0, COVER_Y, (float) thisWidth, COVER_SIZE );
                drawTexture( borderX, y + Y_SIZE - 4, z - 0.02f, 0, COVER_Y + COVER_SIZE, (float) thisWidth, COVER_SIZE );
                borderX += thisWidth;
            }
        }

        // Left half
        drawTexture( x, y, z, X_FOLD_SIZE * 2, 0, X_SIZE / 2.0f, Y_SIZE );
        for( int n = 0; n <= leftPages; n++ )
        {
            drawTexture( x - offsetAt( n ), y, z - 1e-3f * n,
                // Use the left "bold" fold for the outermost page
                n == leftPages ? 0 : X_FOLD_SIZE, 0, X_FOLD_SIZE, Y_SIZE );
        }

        // Right half
        drawTexture( x + X_SIZE / 2.0f, y, z, X_FOLD_SIZE * 2 + X_SIZE / 2.0f, 0, X_SIZE / 2.0f, Y_SIZE );
        for( int n = 0; n <= rightPages; n++ )
        {
            drawTexture(x + (X_SIZE - X_FOLD_SIZE) + offsetAt( n ), y, z - 1e-3f * n,
                // Two folds, then the main page. Use the right "bold" fold for the outermost page.
                X_FOLD_SIZE * 2 + X_SIZE + (n == rightPages ? X_FOLD_SIZE : 0), 0, X_FOLD_SIZE, Y_SIZE );
        }
    }

    public static float offsetAt( int page )
    {
        return (float) (32 * (1 - Math.pow( 1.2, -page )));
    }

    private static void drawTexture( float x, float y, float z, float u, float v, float width, float height )
    {
        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawingQuads();

        tessellator.addVertexWithUV( x, y + height, z, u / BG_SIZE, (v + height) / BG_SIZE );
        tessellator.addVertexWithUV( x + width, y + height, z, (u + width) / BG_SIZE, (v + height) / BG_SIZE );
        tessellator.addVertexWithUV(  x + width, y, z , (u + width) / BG_SIZE, v / BG_SIZE );
        tessellator.addVertexWithUV(  x, y, z , u / BG_SIZE, v / BG_SIZE );

        tessellator.draw();
    }

    private static void drawTexture( float x, float y, float z, float width, float height, float u, float v,
                                     float tWidth, float tHeight )
    {
        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawingQuads();

        tessellator.addVertexWithUV( x, y + height, z, u / BG_SIZE, (v + tHeight) / BG_SIZE );
        tessellator.addVertexWithUV( x + width, y + height, z, (u + tWidth) / BG_SIZE, (v + tHeight) / BG_SIZE );
        tessellator.addVertexWithUV( x + width, y, z, (u + tWidth) / BG_SIZE, v / BG_SIZE );
        tessellator.addVertexWithUV( x, y, z, u / BG_SIZE, v / BG_SIZE );

        tessellator.draw();
    }
}
