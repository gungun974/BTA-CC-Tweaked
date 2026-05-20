/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.client.gui;

import dan200.computercraft.client.gui.widgets.WidgetTerminal;
import dan200.computercraft.fabric.GLFWKeyboardManager;
import dan200.computercraft.fabric.GLFWMouseManager;
import dan200.computercraft.shared.computer.core.ClientComputer;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.inventory.ContainerComputerBase;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.render.renderer.GLRenderer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.Objects;
import java.util.UUID;

public class GuiComputer<T extends ContainerComputerBase> extends Screen {
    /**
     * The margin between the terminal and its border.
     */
    public static final int MARGIN = 2;
    /**
     * The width of the terminal border.
     */
    public static final int BORDER = 12;
    public static final int LIGHT_HEIGHT = 8;
    private static final int CORNER_TOP_Y = 28;
    private static final int CORNER_BOTTOM_Y = CORNER_TOP_Y + BORDER;
    private static final int CORNER_LEFT_X = BORDER;
    private static final int CORNER_RIGHT_X = CORNER_LEFT_X + BORDER;
    private static final int BORDER_RIGHT_X = 36;
    private static final int LIGHT_BORDER_Y = 56;
    private static final int LIGHT_CORNER_Y = 80;
    protected final ComputerFamily family;
    protected final ClientComputer computer;
    private final int termWidth;
    private final int termHeight;
    protected WidgetTerminal terminal;
    UUID glfwKeyCallbackId;
    UUID glfwCharCallbackId;
    UUID glfwScrollCallbackId;
    private int mouseButton = -1;

    public GuiComputer(T container, int termWidth, int termHeight) {
        super();
        this.family = container.getFamily();
        this.computer = container.getClientComputer();
        this.termWidth = termWidth;
        this.termHeight = termHeight;
        this.terminal = null;
    }

    public boolean isPauseScreen() {
        return false;
    }

    public void render(int mx, int my, float partialTick) {
        super.init();

        renderBackground();

        final int termPxWidth = terminal.getWidth();
        final int termPxHeight = terminal.getHeight();

        final int wrapperX = (width - termPxWidth) / 2;
        final int wrapperY = (height - termPxHeight) / 2;

        if (terminal != null) {
            terminal.draw(wrapperX, wrapperY, GLRenderer.modelM4f());
        }

        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (Objects.requireNonNull(family) == ComputerFamily.ADVANCED) {
            this.mc.textureManager.loadTexture("/assets/computercraft/textures/gui/corners_advanced.png").bind();
        } else {
            this.mc.textureManager.loadTexture("/assets/computercraft/textures/gui/corners_normal.png").bind();
        }

        if (mouseButton != -1) {
            terminal.mouseDragged(
                mx - wrapperX - MARGIN,
                my - wrapperY - MARGIN,
                mouseButton
            );
        }

        doRender(
            wrapperX - MARGIN,
            wrapperY - MARGIN,
            termPxWidth + MARGIN * 2,
            termPxHeight + MARGIN * 2,
            false
        );
    }

    public void doRender(int x, int y, int width, int height, boolean withLight) {
        int endX = x + width;
        int endY = y + height;


        renderCorner(x - BORDER, y - BORDER, CORNER_LEFT_X, CORNER_TOP_Y);

        // Vertical bars
        renderLine(x - BORDER, y, 0, CORNER_TOP_Y, BORDER, endY - y);
        renderLine(endX, y, BORDER_RIGHT_X, CORNER_TOP_Y, BORDER, endY - y);

        // Top bar
        renderLine(x, y - BORDER, 0, 0, endX - x, BORDER);
        renderCorner(x - BORDER, y - BORDER, CORNER_LEFT_X, CORNER_TOP_Y);
        renderCorner(endX, y - BORDER, CORNER_RIGHT_X, CORNER_TOP_Y);

        // Bottom bar. We allow for drawing a stretched version, which allows for additional elements (such as the
        // pocket computer's lights).
        if (withLight) {
            renderTexture(x, endY, 0, LIGHT_BORDER_Y, endX - x, BORDER + LIGHT_HEIGHT, BORDER, BORDER + LIGHT_HEIGHT);
            renderTexture(x - BORDER, endY, CORNER_LEFT_X, LIGHT_CORNER_Y, BORDER, BORDER + LIGHT_HEIGHT);
            renderTexture(endX, endY, CORNER_RIGHT_X, LIGHT_CORNER_Y, BORDER, BORDER + LIGHT_HEIGHT);
        } else {
            renderLine(x, endY, 0, BORDER, endX - x, BORDER);
            renderCorner(x - BORDER, endY, CORNER_LEFT_X, CORNER_BOTTOM_Y);
            renderCorner(endX, endY, CORNER_RIGHT_X, CORNER_BOTTOM_Y);
        }
    }

    private void renderLine(int x, int y, int u, int v, int width, int height) {
        renderTexture(x, y, u, v, width, height, BORDER, BORDER);
    }

    private void renderCorner(int x, int y, int u, int v) {
        renderTexture(x, y, u, v, BORDER, BORDER, BORDER, BORDER);
    }

    private void renderTexture(int x, int y, int u, int v, int width, int height) {
        renderTexture(x, y, u, v, width, height, width, height);
    }

    private void renderTexture(int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight) {
        drawTexturedModalRect(x, (double) y, u, v, width, height, textureWidth, textureHeight);
    }

    protected void initTerminal() {
        super.init();

        terminal = new WidgetTerminal(mc, () -> computer, termWidth, termHeight, MARGIN, MARGIN, MARGIN, MARGIN);
    }

    @Override
    public void init() {
        initTerminal();

        GLFWKeyboardManager.getInstance().removeKeyObserver(glfwKeyCallbackId);
        glfwKeyCallbackId = GLFWKeyboardManager.getInstance().addKeyObserver(
            this::glfwKeyCallback
        );

        GLFWKeyboardManager.getInstance().removeCharObserver(glfwCharCallbackId);
        glfwCharCallbackId = GLFWKeyboardManager.getInstance().addCharObserver(
            this::glfwCharCallback
        );

        GLFWMouseManager.getInstance().removeScrollObserver(glfwScrollCallbackId);
        glfwScrollCallbackId = GLFWMouseManager.getInstance().addScrollObserver(
            this::glfwScrollCallback
        );
    }

    @Override
    public void removed() {
        GLFWKeyboardManager.getInstance().removeKeyObserver(glfwKeyCallbackId);
        GLFWKeyboardManager.getInstance().removeCharObserver(glfwCharCallbackId);
        GLFWMouseManager.getInstance().removeScrollObserver(glfwScrollCallbackId);
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

    public void glfwScrollCallback(long window, double xoffset, double yoffset) {
        int mx = Mouse.getEventX() * this.width / this.mc.resolution.getWidthScreenCoords();
        int my = this.height - Mouse.getEventY() * this.height / this.mc.resolution.getHeightScreenCoords() - 1;

        final int termPxWidth = terminal.getWidth();
        final int termPxHeight = terminal.getHeight();

        final int wrapperX = (width - termPxWidth) / 2;
        final int wrapperY = (height - termPxHeight) / 2;

        terminal.mouseScrolled(
            mx - wrapperX - MARGIN,
            my - wrapperY - MARGIN,
            yoffset
        );
    }

    @Override
    public void mouseClicked(int mx, int my, int buttonNum) {
        super.mouseClicked(mx, my, buttonNum);
        final int termPxWidth = terminal.getWidth();
        final int termPxHeight = terminal.getHeight();

        final int wrapperX = (width - termPxWidth) / 2;
        final int wrapperY = (height - termPxHeight) / 2;

        this.mouseButton = buttonNum;

        terminal.mouseClicked(
            mx - wrapperX - MARGIN,
            my - wrapperY - MARGIN,
            buttonNum
        );
    }

    @Override
    public void mouseReleased(int mx, int my, int buttonNum) {
        super.mouseReleased(mx, my, buttonNum);
        final int termPxWidth = terminal.getWidth();
        final int termPxHeight = terminal.getHeight();

        final int wrapperX = (width - termPxWidth) / 2;
        final int wrapperY = (height - termPxHeight) / 2;

        if (mouseButton != -1) {
            terminal.mouseDragged(
                mx - wrapperX - MARGIN,
                my - wrapperY - MARGIN,
                mouseButton
            );
        }

        if (buttonNum != -1) {
            this.mouseButton = -1;
            terminal.mouseReleased(
                mx - wrapperX - MARGIN,
                my - wrapperY - MARGIN,
                buttonNum
            );
        }
    }

    @Override
    public void tick() {
        super.tick();
        terminal.update();
    }

    @Override
    public void keyPressed(char eventCharacter, int eventKey, int mx, int my) {
        if (eventKey == Keyboard.KEY_ESCAPE) {
            this.mc.displayScreen(null);
        }
    }
}
