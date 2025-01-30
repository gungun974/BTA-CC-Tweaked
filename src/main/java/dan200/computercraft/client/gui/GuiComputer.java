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
import dan200.computercraft.shared.computer.inventory.ContainerComputerBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.container.ScreenTrommel;
import net.minecraft.client.input.InputType;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.client.util.debug.DebugRender;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.Arrays;

//import static dan200.computercraft.client.render.ComputerBorderRenderer.BORDER;
//import static dan200.computercraft.client.render.ComputerBorderRenderer.MARGIN;

public class GuiComputer<T extends ContainerComputerBase> extends Screen
{
    protected final ComputerFamily family;
    private int border;

    public boolean isPauseScreen() {
        return false;
    }


    public GuiComputer( T container, int termWidth, int termHeight )
    {
        super();
        this.family = container.getFamily();
        this.computer = container.getClientComputer();
        this.termWidth = termWidth;
        this.termHeight = termHeight;
        this.terminal = null;
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
        super.init();

        renderBackground();

        final int termPxWidth = terminal.getWidth();
        final int termPxHeight = terminal.getHeight();

        final int wrapperX = (width - termPxWidth) / 2;
        final int wrapperY = (height - termPxHeight) / 2;

        if (terminal != null){
            terminal.draw( wrapperX, wrapperY );
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.textureManager.loadTexture("/assets/computercraft/textures/gui/corners_normal.png").bind();

        doRender(
            wrapperX - MARGIN,
            wrapperY - MARGIN,
            termPxWidth + MARGIN * 2,
            termPxHeight + MARGIN * 2,
            false
        );
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
    protected WidgetWrapper terminalWrapper;


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
        this.border = border;
        //client.keyboard.setRepeatEvents( true );

        super.init();

        terminal = new WidgetTerminal( mc, () -> computer, termWidth, termHeight, MARGIN, MARGIN, MARGIN, MARGIN );

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
    @Override
    public void tick()
    {
        super.tick();
        terminal.update();
    }

    @Override
    public void updateEvents() {
        int mouseX = Mouse.getEventX() * this.width / this.mc.resolution.getWidthScreenCoords();
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.resolution.getHeightScreenCoords() - 1;

        while(Mouse.next() && this.mc.inputType != InputType.CONTROLLER) {
            if (Mouse.getEventButtonState()) {
                this.mouseClicked(mouseX, mouseY, Mouse.getEventButton());
            } else {
                this.mouseReleased(mouseX, mouseY, Mouse.getEventButton());
            }
        }

        while(Keyboard.next()) {
            int eventKey = Keyboard.getEventKey();
            char eventChar = Keyboard.getEventCharacter();
            boolean keyState = Keyboard.getEventKeyState();

            if (!keyState) {
                this.keyReleased(eventChar, eventKey, mouseX, mouseY);
            } else {
                if (eventKey == Keyboard.KEY_F11) {
                    this.mc.gameWindow.toggleFullscreen();
                } else {
                    this.keyPressed(eventChar, eventKey, mouseX, mouseY);
                }
            }
        }
    }


    @Override
    public void keyPressed(char eventCharacter, int eventKey, int mx, int my) {
        if (eventKey == -1){
            return;
        }
        super.keyPressed(eventCharacter, eventKey, mx, my);
        terminal.keyPressed(eventKey, 0, 0);
        terminal.charTyped(Keyboard.getKeyName(eventKey).charAt(0), 0);

    }

    public void keyReleased(char eventCharacter, int eventKey, int mx, int my) {
        terminal.keyReleased(eventKey, 0, 0);
    }
}
