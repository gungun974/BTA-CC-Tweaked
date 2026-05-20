/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.client.gui.widgets;

import dan200.computercraft.client.gui.FixedWidthFontRenderer;
import dan200.computercraft.core.terminal.Terminal;
import dan200.computercraft.shared.computer.core.ClientComputer;
import dan200.computercraft.shared.computer.core.IComputer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiElement;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.input.Keyboard;

import java.util.BitSet;
import java.util.function.Supplier;

import static dan200.computercraft.client.gui.FixedWidthFontRenderer.FONT_HEIGHT;
import static dan200.computercraft.client.gui.FixedWidthFontRenderer.FONT_WIDTH;

public class WidgetTerminal extends Gui implements GuiElement {
    private static final float TERMINATE_TIME = 0.5f;

    private final Minecraft client;
    private final Supplier<ClientComputer> computer;
    private final int termWidth;
    private final int termHeight;
    private final int leftMargin;
    private final int rightMargin;
    private final int topMargin;
    private final int bottomMargin;
    private final BitSet keysDown = new BitSet(256);
    private float terminateTimer = -1;
    private float rebootTimer = -1;
    private float shutdownTimer = -1;
    private int lastMouseButton = -1;
    private int lastMouseX = -1;
    private int lastMouseY = -1;

    public WidgetTerminal(Minecraft client, Supplier<ClientComputer> computer, int termWidth, int termHeight, int leftMargin, int rightMargin,
                          int topMargin, int bottomMargin) {
        this.client = client;
        this.computer = computer;
        this.termWidth = termWidth;
        this.termHeight = termHeight;
        this.leftMargin = leftMargin;
        this.rightMargin = rightMargin;
        this.topMargin = topMargin;
        this.bottomMargin = bottomMargin;
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public void setX(int i) {
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public void setY(int i) {
    }

    @Override
    public int getWidth() {
        return termWidth * FONT_WIDTH;
    }

    @Override
    public void setWidth(int i) {
    }

    @Override
    public int getHeight() {
        return termHeight * FONT_HEIGHT;
    }

    @Override
    public void setHeight(int i) {
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        ClientComputer computer = this.computer.get();
        if (computer == null || !computer.isColour() || button < 0 || button > 2) return;

        Terminal term = computer.getTerminal();
        if (term != null) {
            int charX = (int) (mouseX / FONT_WIDTH);
            int charY = (int) (mouseY / FONT_HEIGHT);
            charX = Math.min(Math.max(charX, 0), term.getWidth() - 1);
            charY = Math.min(Math.max(charY, 0), term.getHeight() - 1);

            computer.mouseClick(button + 1, charX + 1, charY + 1);
            lastMouseButton = button;
            lastMouseX = charX;
            lastMouseY = charY;
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        ClientComputer computer = this.computer.get();
        if (computer == null || !computer.isColour() || button < 0 || button > 2) return;

        Terminal term = computer.getTerminal();
        if (term != null) {
            int charX = (int) (mouseX / FONT_WIDTH);
            int charY = (int) (mouseY / FONT_HEIGHT);
            charX = Math.min(Math.max(charX, 0), term.getWidth() - 1);
            charY = Math.min(Math.max(charY, 0), term.getHeight() - 1);

            if (lastMouseButton == button) {
                computer.mouseUp(lastMouseButton + 1, charX + 1, charY + 1);
                lastMouseButton = -1;
            }
            lastMouseX = charX;
            lastMouseY = charY;
        }
    }

    public void mouseDragged(double mouseX, double mouseY, int button) {
        ClientComputer computer = this.computer.get();
        if (computer == null || !computer.isColour() || button < 0 || button > 2) return;

        Terminal term = computer.getTerminal();
        if (term != null) {
            int charX = (int) (mouseX / FONT_WIDTH);
            int charY = (int) (mouseY / FONT_HEIGHT);
            charX = Math.min(Math.max(charX, 0), term.getWidth() - 1);
            charY = Math.min(Math.max(charY, 0), term.getHeight() - 1);

            if (button == lastMouseButton && (charX != lastMouseX || charY != lastMouseY)) {
                computer.mouseDrag(button + 1, charX + 1, charY + 1);
                lastMouseX = charX;
                lastMouseY = charY;
            }
        }
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        ClientComputer computer = this.computer.get();
        if (computer == null || !computer.isColour() || delta == 0) return false;

        Terminal term = computer.getTerminal();
        if (term != null) {
            int charX = (int) (mouseX / FONT_WIDTH);
            int charY = (int) (mouseY / FONT_HEIGHT);
            charX = Math.min(Math.max(charX, 0), term.getWidth() - 1);
            charY = Math.min(Math.max(charY, 0), term.getHeight() - 1);

            computer.mouseScroll(delta < 0 ? 1 : -1, charX + 1, charY + 1);
            lastMouseX = charX;
            lastMouseY = charY;
        }
        return true;
    }

    public void keyPressed(int key, int scancode, int modifiers) {
        if (key == Keyboard.KEY_ESCAPE) return;

        if ((modifiers & GLFW.GLFW_MOD_CONTROL) != 0) {
            switch (key) {
                case GLFW.GLFW_KEY_T:
                    if (terminateTimer < 0) terminateTimer = 0;
                    return;
                case GLFW.GLFW_KEY_S:
                    if (shutdownTimer < 0) shutdownTimer = 0;
                    return;
                case GLFW.GLFW_KEY_R:
                    if (rebootTimer < 0) rebootTimer = 0;
                    return;
                case GLFW.GLFW_KEY_V:
                    String clipboard = client.readFromClipboard();
                    if (clipboard != null) {
                        int newLineIndex1 = clipboard.indexOf("\r");
                        int newLineIndex2 = clipboard.indexOf("\n");
                        if (newLineIndex1 >= 0 && newLineIndex2 >= 0) {
                            clipboard = clipboard.substring(0, Math.min(newLineIndex1, newLineIndex2));
                        } else if (newLineIndex1 >= 0) {
                            clipboard = clipboard.substring(0, newLineIndex1);
                        } else if (newLineIndex2 >= 0) {
                            clipboard = clipboard.substring(0, newLineIndex2);
                        }
                        if (!clipboard.isEmpty()) {
                            if (clipboard.length() > 512) clipboard = clipboard.substring(0, 512);
                            queueEvent("paste", clipboard);
                        }
                        return;
                    }
            }
        }

        if (key >= 0 && terminateTimer < 0 && rebootTimer < 0 && shutdownTimer < 0) {
            boolean repeat = keysDown.get(key);
            keysDown.set(key);
            IComputer computer = this.computer.get();
            if (computer != null) computer.keyDown(key, repeat);
        }
    }

    public void keyReleased(int key, int scancode, int modifiers) {
        if (key >= 0 && keysDown.get(key)) {
            keysDown.set(key, false);
            IComputer computer = this.computer.get();
            if (computer != null) computer.keyUp(key);
        }

        switch (key) {
            case GLFW.GLFW_KEY_T:
                terminateTimer = -1;
                break;
            case GLFW.GLFW_KEY_R:
                rebootTimer = -1;
                break;
            case GLFW.GLFW_KEY_S:
                shutdownTimer = -1;
                break;
            case GLFW.GLFW_KEY_LEFT_CONTROL:
            case GLFW.GLFW_KEY_RIGHT_CONTROL:
                terminateTimer = rebootTimer = shutdownTimer = -1;
                break;
        }
    }

    public void charTyped(char ch) {
        if (ch >= 32 && ch <= 126 || ch >= 160 && ch <= 255) {
            queueEvent("char", Character.toString(ch));
        }
    }

    private void queueEvent(String event, Object... args) {
        ClientComputer computer = this.computer.get();
        if (computer != null) computer.queueEvent(event, args);
    }

    private void queueEvent(String event) {
        ClientComputer computer = this.computer.get();
        if (computer != null) computer.queueEvent(event);
    }

    public void update() {
        if (terminateTimer >= 0 && terminateTimer < TERMINATE_TIME && (terminateTimer += 0.05f) > TERMINATE_TIME) {
            queueEvent("terminate");
        }
        if (shutdownTimer >= 0 && shutdownTimer < TERMINATE_TIME && (shutdownTimer += 0.05f) > TERMINATE_TIME) {
            ClientComputer computer = this.computer.get();
            if (computer != null) computer.shutdown();
        }
        if (rebootTimer >= 0 && rebootTimer < TERMINATE_TIME && (rebootTimer += 0.05f) > TERMINATE_TIME) {
            ClientComputer computer = this.computer.get();
            if (computer != null) computer.reboot();
        }
    }

    public void draw(int originX, int originY, Matrix4f matrix) {
        synchronized (computer) {
            ClientComputer computer = this.computer.get();
            Terminal terminal = computer != null ? computer.getTerminal() : null;
            if (terminal != null) {
                FixedWidthFontRenderer.drawTerminal(
                    matrix,
                    originX, originY,
                    terminal, !computer.isColour(),
                    topMargin, bottomMargin, leftMargin, rightMargin
                );
            } else {
                FixedWidthFontRenderer.drawEmptyTerminal(
                    matrix,
                    originX - leftMargin,
                    originY - topMargin,
                    termWidth * FONT_WIDTH + leftMargin + rightMargin,
                    termHeight * FONT_HEIGHT + topMargin + bottomMargin
                );
            }
        }
    }
}
