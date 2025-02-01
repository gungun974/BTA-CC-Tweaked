/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.client.gui;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.client.FrameInfo;
import dan200.computercraft.core.terminal.Terminal;
import dan200.computercraft.core.terminal.TextBuffer;
import dan200.computercraft.shared.util.Colour;
import dan200.computercraft.shared.util.Palette;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.tessellator.Tessellator;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static dan200.computercraft.core.terminal.Terminal.getColour;

public final class FixedWidthFontRenderer
{
    public static final int FONT_HEIGHT = 9;
    public static final int FONT_WIDTH = 6;
    public static final float WIDTH = 256.0f;
    public static final float BACKGROUND_START = (WIDTH - 6.0f) / WIDTH;
    public static final float BACKGROUND_END = (WIDTH - 4.0f) / WIDTH;
//    private static final Matrix4f IDENTITY = AffineTransformation.identity()
//        .getMatrix();
//    private static final Identifier FONT = new Identifier( "computercraft", "textures/gui/term_font.png" );
//    public static final RenderLayer TYPE = Type.MAIN;
//
//
//    private FixedWidthFontRenderer()
//    {
//    }
//
    public static void drawString( float x, float y, @Nonnull TextBuffer text, @Nonnull TextBuffer textColour, @Nullable TextBuffer backgroundColour,
                                   @Nonnull Palette palette, boolean greyscale, float leftMarginSize, float rightMarginSize )
    {
        bindFont();

        drawString(
            x,
            y,
            text,
            textColour,
            backgroundColour,
            palette,
            greyscale,
            leftMarginSize,
            rightMarginSize );
    }

    private static void bindFont()
    {
        Minecraft.getMinecraft().textureManager.loadTexture("/assets/computercraft/textures/gui/term_font.png").bind();
        //TODO: RenderSystem.texParameter( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP );
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
    }

    public static void drawString2( float x, float y, @Nonnull TextBuffer text,
                                   @Nonnull TextBuffer textColour, @Nullable TextBuffer backgroundColour, @Nonnull Palette palette, boolean greyscale,
                                   float leftMarginSize, float rightMarginSize )
    {
        if( backgroundColour != null )
        {
            drawBackground( x, y, backgroundColour, palette, greyscale, leftMarginSize, rightMarginSize, FONT_HEIGHT );
        }

        //ComputerCraft.log.info(text.toString());

        for( int i = 0; i < text.length(); i++ )
        {
            double[] colour = palette.getColour( getColour( textColour.charAt( i ), Colour.BLACK ) );
            float r, g, b;
            if( greyscale )
            {
                r = g = b = toGreyscale( colour );
            }
            else
            {
                r = (float) colour[0];
                g = (float) colour[1];
                b = (float) colour[2];
            }

            // Draw char
            int index = text.charAt( i );
            if( index > 255 )
            {
                index = '?';
            }
            drawChar( x + i * FONT_WIDTH, y, index, r, g, b );
        }

    }

    private static void drawBackground( float x, float y,
                                        @Nonnull TextBuffer backgroundColour, @Nonnull Palette palette, boolean greyscale, float leftMarginSize,
                                        float rightMarginSize, float height )
    {
        if( leftMarginSize > 0 )
        {
            drawQuad( x - leftMarginSize, y, leftMarginSize, height, palette, greyscale, backgroundColour.charAt( 0 ) );
        }

        if( rightMarginSize > 0 )
        {
            drawQuad(
                x + backgroundColour.length() * FONT_WIDTH,
                y,
                rightMarginSize,
                height,
                palette,
                greyscale,
                backgroundColour.charAt( backgroundColour.length() - 1 ) );
        }

        // Batch together runs of identical background cells.
        int blockStart = 0;
        char blockColour = '\0';
        for( int i = 0; i < backgroundColour.length(); i++ )
        {
            char colourIndex = backgroundColour.charAt( i );
            if( colourIndex == blockColour )
            {
                continue;
            }

            if( blockColour != '\0' )
            {
                drawQuad( x + blockStart * FONT_WIDTH, y, FONT_WIDTH * (i - blockStart), height, palette, greyscale, blockColour );
            }

            blockColour = colourIndex;
            blockStart = i;
        }

        if( blockColour != '\0' )
        {
            drawQuad(
                x + blockStart * FONT_WIDTH,
                y,
                FONT_WIDTH * (backgroundColour.length() - blockStart),
                height,
                palette,
                greyscale,
                blockColour );
        }
    }

    public static int getColour( char c, Colour def )
    {
        return 15 - Terminal.getColour( c, def );
    }
//
    public static float toGreyscale( double[] rgb )
    {
        return (float) ((rgb[0] + rgb[1] + rgb[2]) / 3);
    }

    private static void drawChar( float x, float y, int index, float r, float g, float b )
    {
        // Short circuit to avoid the common case - the texture should be blank here after all.
        if( index == '\0' || index == ' ' )
        {
            return;
        }

        int column = index % 16;
        int row = index / 16;

        int xStart = 1 + column * (FONT_WIDTH + 2);
        int yStart = 1 + row * (FONT_HEIGHT + 2);

        Tessellator tessellator = Tessellator.instance;

//        buffer.vertex( transform, x, y, 0f )
//            .color( r, g, b, 1.0f )
//            .texture( xStart / WIDTH, yStart / WIDTH )
//            .next();
//        buffer.vertex( transform, x, y + FONT_HEIGHT, 0f )
//            .color( r, g, b, 1.0f )
//            .texture( xStart / WIDTH, (yStart + FONT_HEIGHT) / WIDTH )
//            .next();
//        buffer.vertex( transform, x + FONT_WIDTH, y, 0f )
//            .color( r, g, b, 1.0f )
//            .texture( (xStart + FONT_WIDTH) / WIDTH, yStart / WIDTH )
//            .next();
//        buffer.vertex( transform, x + FONT_WIDTH, y, 0f )
//            .color( r, g, b, 1.0f )
//            .texture( (xStart + FONT_WIDTH) / WIDTH, yStart / WIDTH )
//            .next();
//        buffer.vertex( transform, x, y + FONT_HEIGHT, 0f )
//            .color( r, g, b, 1.0f )
//            .texture( xStart / WIDTH, (yStart + FONT_HEIGHT) / WIDTH )
//            .next();
//        buffer.vertex( transform, x + FONT_WIDTH, y + FONT_HEIGHT, 0f )
//            .color( r, g, b, 1.0f )
//            .texture( (xStart + FONT_WIDTH) / WIDTH, (yStart + FONT_HEIGHT) / WIDTH )
//            .next();


        tessellator.startDrawing(6);

        tessellator.setColorRGBA_F(r,g,b, 1f);
        tessellator.addVertexWithUV(x, y, 0, xStart / WIDTH, yStart / WIDTH);

        tessellator.addVertexWithUV(x, y + FONT_HEIGHT, 0f, xStart / WIDTH, (yStart + FONT_HEIGHT) / WIDTH );

        tessellator.addVertexWithUV(x + FONT_WIDTH, y, 0f, (xStart + FONT_WIDTH) / WIDTH, yStart / WIDTH );

        tessellator.draw();

        tessellator.startDrawing(6);

        tessellator.setColorRGBA_F(r,g,b, 1f);

        tessellator.addVertexWithUV(x + FONT_WIDTH, y, 0f, (xStart + FONT_WIDTH) / WIDTH, yStart / WIDTH );

        tessellator.addVertexWithUV(x, y + FONT_HEIGHT, 0f, xStart / WIDTH, (yStart + FONT_HEIGHT) / WIDTH );

        tessellator.addVertexWithUV(x + FONT_WIDTH, y + FONT_HEIGHT, 0f, (xStart + FONT_WIDTH) / WIDTH, (yStart + FONT_HEIGHT) / WIDTH );

        tessellator.draw();

    }

    private static void drawQuad( float x, float y, float width, float height, Palette palette,
                                  boolean greyscale, char colourIndex )
    {
        double[] colour = palette.getColour( getColour( colourIndex, Colour.BLACK ) );
        float r, g, b;
        if( greyscale )
        {
            r = g = b = toGreyscale( colour );
        }
        else
        {
            r = (float) colour[0];
            g = (float) colour[1];
            b = (float) colour[2];
        }

        drawQuad( x, y, width, height, r, g, b );
    }

    private static void drawQuad( float x, float y, float width, float height, float r, float g, float b )
    {

        Tessellator tessellator = Tessellator.instance;

//        buffer.vertex( transform, x, y, 0 )
//            .color( r, g, b, 1.0f )
//            .texture( BACKGROUND_START, BACKGROUND_START )
//            .next();
//        buffer.vertex( transform, x, y + height, 0 )
//            .color( r, g, b, 1.0f )
//            .texture( BACKGROUND_START, BACKGROUND_END )
//            .next();
//        buffer.vertex( transform, x + width, y, 0 )
//            .color( r, g, b, 1.0f )
//            .texture( BACKGROUND_END, BACKGROUND_START )
//            .next();
//        buffer.vertex( transform, x + width, y, 0 )
//            .color( r, g, b, 1.0f )
//            .texture( BACKGROUND_END, BACKGROUND_START )
//            .next();
//        buffer.vertex( transform, x, y + height, 0 )
//            .color( r, g, b, 1.0f )
//            .texture( BACKGROUND_START, BACKGROUND_END )
//            .next();
//        buffer.vertex( transform, x + width, y + height, 0 )
//            .color( r, g, b, 1.0f )
//            .texture( BACKGROUND_END, BACKGROUND_END )
//            .next();

        tessellator.startDrawing(6);

        tessellator.setColorRGBA_F(r,g,b, 1f);

        tessellator.addVertexWithUV(x, y, 0, BACKGROUND_START, BACKGROUND_START);
        tessellator.addVertexWithUV(x, y + height, 0, BACKGROUND_START, BACKGROUND_END);
        tessellator.addVertexWithUV(x + width, y, 0, BACKGROUND_END, BACKGROUND_START);

        tessellator.draw();

        tessellator.startDrawing(6);

        tessellator.setColorRGBA_F(r,g,b, 1f);

        tessellator.addVertexWithUV(x + width, y, 0, BACKGROUND_END, BACKGROUND_START);
        tessellator.addVertexWithUV(x, y + height, 0, BACKGROUND_START, BACKGROUND_END);
        tessellator.addVertexWithUV(x + width, y + height, 0, BACKGROUND_END, BACKGROUND_END);

        tessellator.draw();
    }

    public static void drawTerminalWithoutCursor( float x, float y,
                                                  @Nonnull Terminal terminal, boolean greyscale, float topMarginSize, float bottomMarginSize,
                                                  float leftMarginSize, float rightMarginSize )
    {
        Palette palette = terminal.getPalette();
        int height = terminal.getHeight();

        // Top and bottom margins
        drawBackground(
            x,
            y - topMarginSize,
            terminal.getBackgroundColourLine( 0 ),
            palette,
            greyscale,
            leftMarginSize,
            rightMarginSize,
            topMarginSize );

        drawBackground(
            x,
            y + height * FONT_HEIGHT,
            terminal.getBackgroundColourLine( height - 1 ),
            palette,
            greyscale,
            leftMarginSize,
            rightMarginSize,
            bottomMarginSize );

        // The main text
        for( int i = 0; i < height; i++ )
        {
            drawString2(
                x,
                y + FixedWidthFontRenderer.FONT_HEIGHT * i,
                terminal.getLine( i ),
                terminal.getTextColourLine( i ),
                terminal.getBackgroundColourLine( i ),
                palette,
                greyscale,
                leftMarginSize,
                rightMarginSize );
        }
    }

    public static void drawCursor( float x, float y, @Nonnull Terminal terminal,
                                   boolean greyscale )
    {
        Palette palette = terminal.getPalette();
        int width = terminal.getWidth();
        int height = terminal.getHeight();

        int cursorX = terminal.getCursorX();
        int cursorY = terminal.getCursorY();
        if( terminal.getCursorBlink() && cursorX >= 0 && cursorX < width && cursorY >= 0 && cursorY < height && FrameInfo.getGlobalCursorBlink() )
        {
            double[] colour = palette.getColour( 15 - terminal.getTextColour() );
            float r, g, b;
            if( greyscale )
            {
                r = g = b = toGreyscale( colour );
            }
            else
            {
                r = (float) colour[0];
                g = (float) colour[1];
                b = (float) colour[2];
            }

            drawChar(x + cursorX * FONT_WIDTH, y + cursorY * FONT_HEIGHT, '_', r, g, b );
        }
    }

    public static void drawTerminal( float x, float y, @Nonnull Terminal terminal, boolean greyscale, float topMarginSize, float bottomMarginSize,
                                     float leftMarginSize, float rightMarginSize )
    {
        bindFont();

        drawTerminalWithoutCursor(  x, y, terminal, greyscale, topMarginSize, bottomMarginSize, leftMarginSize, rightMarginSize );
        drawCursor( x, y, terminal, greyscale );
    }

    public static void drawEmptyTerminal( float x, float y, float width, float height )
    {
        bindFont();

        Colour colour = Colour.BLACK;

        drawQuad(x, y, width, height, colour.getR(), colour.getG(), colour.getB() );

        //renderer.draw();
    }
//
//    public static void drawBlocker( @Nonnull Matrix4f transform, @Nonnull VertexConsumerProvider renderer, float x, float y, float width, float height )
//    {
//        Colour colour = Colour.BLACK;
//        drawQuad( transform, renderer.getBuffer( Type.BLOCKER ), x, y, width, height, colour.getR(), colour.getG(), colour.getB() );
//    }
//
//    private static final class Type extends RenderPhase
//    {
//        private static final int GL_MODE = GL11.GL_TRIANGLES;
//
//        private static final VertexFormat FORMAT = VertexFormats.POSITION_COLOR_TEXTURE;
//
//        static final RenderLayer MAIN = RenderLayer.of( "terminal_font", FORMAT, GL_MODE, 1024, false, false, // useDelegate, needsSorting
//            RenderLayer.MultiPhaseParameters.builder()
//                .texture( new RenderPhase.Texture( FONT,
//                    false,
//                    false ) ) // blur, minimap
//                .alpha( ONE_TENTH_ALPHA )
//                .lightmap( DISABLE_LIGHTMAP )
//                .writeMaskState( COLOR_MASK )
//                .build( false ) );
//
//        static final RenderLayer BLOCKER = RenderLayer.of( "terminal_blocker", FORMAT, GL_MODE, 256, false, false, // useDelegate, needsSorting
//            RenderLayer.MultiPhaseParameters.builder()
//                .texture( new RenderPhase.Texture( FONT,
//                    false,
//                    false ) ) // blur, minimap
//                .alpha( ONE_TENTH_ALPHA )
//                .writeMaskState( ALL_MASK )
//                .lightmap( DISABLE_LIGHTMAP )
//                .build( false ) );
//
//        private Type( String name, Runnable setup, Runnable destroy )
//        {
//            super( name, setup, destroy );
//        }
//    }
}
