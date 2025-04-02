package dan200.computercraft.shared.pocket.inventory;

import dan200.computercraft.client.gui.FixedWidthFontRenderer;
import dan200.computercraft.client.gui.widgets.WidgetTerminal;
import dan200.computercraft.core.terminal.Terminal;
import dan200.computercraft.fabric.GLFWKeyboardManager;
import dan200.computercraft.shared.computer.core.ClientComputer;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.inventory.ContainerComputerBase;
import dan200.computercraft.shared.pocket.items.ItemPocketComputer;
import dan200.computercraft.shared.turtle.blocks.TileTurtle;
import dan200.computercraft.shared.turtle.inventory.MenuPocketComputer;
import dan200.computercraft.shared.turtle.inventory.MenuTurtle;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.Objects;
import java.util.UUID;

public class ScreenPocketComputer<T extends ContainerComputerBase> extends ScreenContainerAbstract {
    public static final int PLAYER_START_Y = 134;
    public static final int TURTLE_START_X = 175;

    protected final ComputerFamily family;
    protected final ClientComputer computer;
    private final int termWidth;
    private final int termHeight;

    protected WidgetTerminal terminal;

    public ScreenPocketComputer(T container, int termWidth, int termHeight, ContainerInventory inventoryplayer, ItemPocketComputer item) {
        super(new MenuPocketComputer(inventoryplayer, item));
        this.family = container.getFamily();
        this.computer = container.getClientComputer();
        this.termWidth = termWidth;
        this.termHeight = termHeight;
        this.xSize = 254;
        this.ySize = 217;
    }

    UUID glfwKeyCallbackId;
    UUID glfwCharCallbackId;

    @Override
    public void init()
    {
        super.init();

        terminal = new WidgetTerminal( mc, () -> computer, termWidth, termHeight, 0, 0, 0, 0 );

        GLFWKeyboardManager.getInstance().removeKeyObserver(glfwKeyCallbackId);
        glfwKeyCallbackId = GLFWKeyboardManager.getInstance().addKeyObserver(
            this::glfwKeyCallback
        );

        GLFWKeyboardManager.getInstance().removeCharObserver(glfwCharCallbackId);
        glfwCharCallbackId = GLFWKeyboardManager.getInstance().addCharObserver(
            this::glfwCharCallback
        );
    }

    @Override
    public void removed() {
        GLFWKeyboardManager.getInstance().removeKeyObserver(glfwKeyCallbackId);
        GLFWKeyboardManager.getInstance().removeCharObserver(glfwCharCallbackId);
    }

    public void glfwKeyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == 0) {
            terminal.keyReleased(key, scancode, mods);
            return;
        }

        terminal.keyPressed(key, scancode, mods);
    }

    public void glfwCharCallback(long window, int codepoint) {
        terminal.charTyped((char) codepoint);
    }

    @Override
    public void tick()
    {
        super.tick();
        terminal.update();
    }

    @Override
    public void keyPressed(char eventCharacter, int eventKey, int mx, int my) {
        if (eventKey == Keyboard.KEY_ESCAPE) {
            this.mc.displayScreen((Screen)null);
        }
    }

//    @Override
//    protected void drawGuiContainerBackgroundLayer(float f) {
//        int j = (this.width - this.xSize) / 2;
//        int k = (this.height - this.ySize) / 2;
//
//        if (terminal != null) {
//            final int termPxWidth = terminal.getWidth();
//            final int termPxHeight = terminal.getHeight();
//
//            final int wrapperX = (width - termPxWidth) / 2;
//            final int wrapperY = (height - termPxHeight - 80) / 2;
//
//            FixedWidthFontRenderer.drawEmptyTerminal(
//                wrapperX - 1,
//                wrapperY - 1,
//                termPxWidth + 2,
//                termPxHeight + 2
//            );
//
//            terminal.draw(wrapperX, wrapperY);
//        }
//
//
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//
//        if (family == ComputerFamily.ADVANCED) {
//            this.mc.textureManager.loadTexture("/assets/computercraft/textures/gui/pocket_advanced.png").bind();
//        } else {
//            this.mc.textureManager.loadTexture("/assets/computercraft/textures/gui/pocket_normal.png").bind();
//        }
//
//        this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
//
//        Terminal terminal = this.computer != null ? this.computer.getTerminal() : null;
//
//        if (terminal != null) {
//            // Draw selection slot
//            int slot = terminal.getSelectedSlot();
//            if( slot >= 0 )
//            {
//                int slotX = slot % 4;
//                int slotY = slot / 4;
//                this.drawTexturedModalRect( j + TURTLE_START_X - 2 + slotX * 18, k + PLAYER_START_Y - 2 + slotY * 18,
//                    0,
//                    217,
//                    24,
//                    24 );
//            }
//        }
//    }


    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {

    }

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

        if (Objects.requireNonNull(family) == ComputerFamily.ADVANCED) {
            this.mc.textureManager.loadTexture("/assets/computercraft/textures/gui/corners_advanced.png").bind();
        } else {
            this.mc.textureManager.loadTexture("/assets/computercraft/textures/gui/corners_normal.png").bind();
        }

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
}
