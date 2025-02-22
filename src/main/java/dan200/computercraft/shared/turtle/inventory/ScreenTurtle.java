package dan200.computercraft.shared.turtle.inventory;

import dan200.computercraft.client.gui.widgets.WidgetTerminal;
import dan200.computercraft.core.terminal.Terminal;
import dan200.computercraft.fabric.GLFWKeyboardManager;
import dan200.computercraft.shared.computer.core.ClientComputer;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.inventory.ContainerComputerBase;
import dan200.computercraft.shared.turtle.blocks.TileTurtle;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.UUID;

public class ScreenTurtle<T extends ContainerComputerBase> extends ScreenContainerAbstract {
    public static final int PLAYER_START_Y = 134;
    public static final int TURTLE_START_X = 175;

    protected final ComputerFamily family;
    protected final ClientComputer computer;
    private final int termWidth;
    private final int termHeight;

    protected WidgetTerminal terminal;

    public ScreenTurtle(T container, int termWidth, int termHeight, ContainerInventory inventoryplayer, TileTurtle tileentitydispenser) {
        super(new MenuTurtle(inventoryplayer, tileentitydispenser));
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

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;

        if (terminal != null){
            Tessellator tessellator = Tessellator.instance;

            tessellator.startDrawingQuads();

            tessellator.setColorRGBA_F(0,0,0, 1f);

            tessellator.drawRectangle(j, k, this.xSize, this.ySize);

            tessellator.draw();

            final int termPxWidth = terminal.getWidth();
            final int termPxHeight = terminal.getHeight();

            final int wrapperX = (width - termPxWidth) / 2 + 1;
            final int wrapperY = (height - termPxHeight - 80) / 2 + 1;

            terminal.draw( wrapperX, wrapperY );
        }


        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.textureManager.loadTexture("/assets/computercraft/textures/gui/turtle_normal.png").bind();
        this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);

        Terminal terminal = this.computer != null ? this.computer.getTerminal() : null;

        if (terminal != null) {
            // Draw selection slot
            int slot = terminal.getSelectedSlot();
            if( slot >= 0 )
            {
                int slotX = slot % 4;
                int slotY = slot / 4;
                this.drawTexturedModalRect( j + TURTLE_START_X - 2 + slotX * 18, k + PLAYER_START_Y - 2 + slotY * 18,
                    0,
                    217,
                    24,
                    24 );
            }
        }
    }
}
