/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.client.gui;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.client.gui.widgets.WidgetTerminal;
import dan200.computercraft.client.gui.widgets.WidgetWrapper;
import dan200.computercraft.client.render.ComputerBorderRenderer;
import dan200.computercraft.shared.computer.core.ClientComputer;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.container.ScreenTrommel;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

//import static dan200.computercraft.client.render.ComputerBorderRenderer.BORDER;
//import static dan200.computercraft.client.render.ComputerBorderRenderer.MARGIN;

public class GuiComputer extends Screen
{
    public boolean isPauseScreen() {
        return false;
    }


    public IconCoordinate termTexture;
    public IconCoordinate cornersTexture;

    public GuiComputer(ClientComputer computer, int termWidth, int termHeight )
    {
        super();
        this.setDefaultTextures();
        //this.family = container.getFamily();
        //this.computer = (ClientComputer) container.getComputer();
        this.computer = computer;
        this.termWidth = termWidth;
        this.termHeight = termHeight;
        this.terminal = null;
    }

    protected void setDefaultTextures() {
        this.termTexture = TextureRegistry.getTexture("computercraft:gui/term_background");
        //this.cornersTexture = TextureRegistry.getTexture("minecraft:gui/widgets/button/button");
        this.cornersTexture = TextureRegistry.getTexture("computercraft:gui/corners_normal");
        ComputerCraft.log.info(String.valueOf(this.cornersTexture.width));
    }

        /**
     * The margin between the terminal and its border.
     */
    public static final int MARGIN = 2;
    /**
     * The width of the terminal border.
     */
    public static final int BORDER = 12;
    private static final int CORNER_TOP_Y = 28;
    private static final int CORNER_BOTTOM_Y = CORNER_TOP_Y + BORDER;
    private static final int CORNER_LEFT_X = BORDER;
    private static final int CORNER_RIGHT_X = CORNER_LEFT_X + BORDER;
    private static final int BORDER_RIGHT_X = 36;
    private static final int LIGHT_BORDER_Y = 56;
    private static final int LIGHT_CORNER_Y = 80;

    public static final int LIGHT_HEIGHT = 8;
    private static final float TEX_SCALE = 1 / 256.0f;

    public void render(int mx, int my, float partialTick) {
            boolean tblink = true; //terminal.getCursorBlink();
            int tw = 50; //terminal.getWidth();
            int th = 30; //terminal.getHeight();
            int tx = 10; //terminal.getCursorX();
            int ty = 10; //terminal.getCursorY();
            //String[] tlines = terminal.getLines();
            renderBackground();
            int termWidth = 4 + tw * 10;//Computers.fixedWidthFontRenderer.FONT_WIDTH;
            int termHeight = 4 + th * 10;//Computers.fixedWidthFontRenderer.FONT_HEIGHT;

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            int startX = (this.width - termWidth) / 2;
            int startY = (this.height - termHeight) / 2;
            int endX = startX + termWidth;
            int endY = startY + termHeight;



        //drawIconTextureDouble(0, 0, 256, 256, 0, 0, 256, 256, cornersTexture);

        //drawTexturedModalRect(startX - 12, startY - 12, 12, 28, 12, 12); //top left corner

        this.mc.textureManager.loadTexture("/assets/computercraft/textures/gui/corners_normal.png").bind();

        doRender(  50, 50, 100, 100, false );

        if (terminal != null){
            terminal.draw(0, 0);
        }
    }

    public void doRender( int x, int y, int width, int height, boolean withLight )
    {
        int endX = x + width;
        int endY = y + height;


        renderCorner( x - BORDER, y - BORDER, CORNER_LEFT_X, CORNER_TOP_Y );

        // Vertical bars
        renderLine( x - BORDER, y, 0, CORNER_TOP_Y, BORDER, endY - y );
        renderLine( endX, y, BORDER_RIGHT_X, CORNER_TOP_Y, BORDER, endY - y );

        // Top bar
        renderLine( x, y - BORDER, 0, 0, endX - x, BORDER );
        renderCorner( x - BORDER, y - BORDER, CORNER_LEFT_X, CORNER_TOP_Y );
        renderCorner( endX, y - BORDER, CORNER_RIGHT_X, CORNER_TOP_Y );

        // Bottom bar. We allow for drawing a stretched version, which allows for additional elements (such as the
        // pocket computer's lights).
        if( withLight )
        {
            renderTexture( x, endY, 0, LIGHT_BORDER_Y, endX - x, BORDER + LIGHT_HEIGHT, BORDER, BORDER + LIGHT_HEIGHT );
            renderTexture( x - BORDER, endY, CORNER_LEFT_X, LIGHT_CORNER_Y, BORDER, BORDER + LIGHT_HEIGHT );
            renderTexture( endX, endY, CORNER_RIGHT_X, LIGHT_CORNER_Y, BORDER, BORDER + LIGHT_HEIGHT );
        }
        else
        {
            renderLine( x, endY, 0, BORDER, endX - x, BORDER );
            renderCorner( x - BORDER, endY, CORNER_LEFT_X, CORNER_BOTTOM_Y );
            renderCorner( endX, endY, CORNER_RIGHT_X, CORNER_BOTTOM_Y );
        }
    }

    private void renderLine( int x, int y, int u, int v, int width, int height )
    {
        renderTexture( x, y, u, v, width, height, BORDER, BORDER );
    }

    private void renderCorner( int x, int y, int u, int v )
    {
        renderTexture( x, y, u, v, BORDER, BORDER, BORDER, BORDER );
    }

    private void renderTexture( int x, int y, int u, int v, int width, int height )
    {
        renderTexture( x, y, u, v, width, height, width, height );
    }

    private void renderTexture( int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight )
    {
        drawTexturedModalRect((double) x, (double) y, u, v, width, height, textureWidth, textureHeight);
    }

    //protected final ComputerFamily family;
    protected final ClientComputer computer;
    private final int termWidth;
    private final int termHeight;
//
   protected WidgetTerminal terminal;
//    protected WidgetWrapper terminalWrapper;


//    public static GuiComputer<ContainerComputer> create( ContainerComputer container, PlayerInventory inventory, Text component )
//    {
//        return new GuiComputer<>( container, inventory, component, ComputerCraft.computerTermWidth, ComputerCraft.computerTermHeight );
//    }
//
//    public static GuiComputer<ContainerPocketComputer> createPocket( ContainerPocketComputer container, PlayerInventory inventory, Text component )
//    {
//        return new GuiComputer<>( container, inventory, component, ComputerCraft.pocketTermWidth, ComputerCraft.pocketTermHeight );
//    }
//
//    public static GuiComputer<ContainerViewComputer> createView( ContainerViewComputer container, PlayerInventory inventory, Text component )
//    {
//        return new GuiComputer<>( container, inventory, component, container.getWidth(), container.getHeight() );
//    }
//
    protected void initTerminal( int border, int widthExtra, int heightExtra )
    {
        //client.keyboard.setRepeatEvents( true );

        /*
        int termPxWidth = termWidth * FixedWidthFontRenderer.FONT_WIDTH;
        int termPxHeight = termHeight * FixedWidthFontRenderer.FONT_HEIGHT;

        backgroundWidth = termPxWidth + MARGIN * 2 + border * 2 + widthExtra;
        backgroundHeight = termPxHeight + MARGIN * 2 + border * 2 + heightExtra;
         */

        super.init();

        terminal = new WidgetTerminal( mc, () -> computer, termWidth, termHeight, MARGIN, MARGIN, MARGIN, MARGIN );
        //terminalWrapper = new WidgetWrapper( terminal, MARGIN + border + x, MARGIN + border + y, termPxWidth, termPxHeight );

        //children.add( terminalWrapper );
        //setFocused( terminalWrapper );
    }

    @Override
    public void init()
    {
        initTerminal( BORDER, 0, 0 );
    }
//
//    @Override
//    public void render( @Nonnull MatrixStack stack, int mouseX, int mouseY, float partialTicks )
//    {
//        this.renderBackground( stack );
//        super.render( stack, mouseX, mouseY, partialTicks );
//        drawMouseoverTooltip( stack, mouseX, mouseY );
//    }
//
//    @Override
//    protected void drawForeground( @Nonnull MatrixStack transform, int mouseX, int mouseY )
//    {
//        // Skip rendering labels.
//    }
//
//    @Override
//    public void drawBackground( @Nonnull MatrixStack stack, float partialTicks, int mouseX, int mouseY )
//    {
//        // Draw terminal
//        terminal.draw( terminalWrapper.getX(), terminalWrapper.getY() );
//
//        // Draw a border around the terminal
//        RenderSystem.color4f( 1, 1, 1, 1 );
//        client.getTextureManager()
//            .bindTexture( ComputerBorderRenderer.getTexture( family ) );
//        ComputerBorderRenderer.render( terminalWrapper.getX() - MARGIN, terminalWrapper.getY() - MARGIN,
//            getZOffset(), terminalWrapper.getWidth() + MARGIN * 2, terminalWrapper.getHeight() + MARGIN * 2 );
//    }
//
//    @Override
//    public boolean mouseDragged( double x, double y, int button, double deltaX, double deltaY )
//    {
//        return (getFocused() != null && getFocused().mouseDragged( x, y, button, deltaX, deltaY )) || super.mouseDragged( x, y, button, deltaX, deltaY );
//    }
//
//    @Override
//    public boolean mouseReleased( double mouseX, double mouseY, int button )
//    {
//        return (getFocused() != null && getFocused().mouseReleased( mouseX, mouseY, button )) || super.mouseReleased( x, y, button );
//    }
//
//    @Override
//    public boolean keyPressed( int key, int scancode, int modifiers )
//    {
//        // Forward the tab key to the terminal, rather than moving between controls.
//        if( key == GLFW.GLFW_KEY_TAB && getFocused() != null && getFocused() == terminalWrapper )
//        {
//            return getFocused().keyPressed( key, scancode, modifiers );
//        }
//
//        return super.keyPressed( key, scancode, modifiers );
//    }
//
//    @Override
//    public void removed()
//    {
//        super.removed();
//        children.remove( terminal );
//        terminal = null;
//        client.keyboard.setRepeatEvents( false );
//    }
//
//    @Override
//    public void tick()
//    {
//        super.tick();
//        terminal.update();
//    }
}
