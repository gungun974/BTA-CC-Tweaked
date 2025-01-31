package dan200.computercraft.fabric.mixin;

import com.github.zarzelcow.legacylwjgl3.implementation.glfw.GLFWKeyboardImplementation;
import dan200.computercraft.ComputerCraft;
import dan200.computercraft.fabric.GLFWKeyboardManager;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.EventQueue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.ByteBuffer;

import static com.github.zarzelcow.legacylwjgl3.implementation.glfw.GLFWKeyboardImplementation.translateKeyFromGLFW;
import static org.lwjgl.glfw.GLFW.glfwGetKeyName;

@Mixin(value = GLFWKeyboardImplementation.class, remap = false)
public class GLFWKeyboardImplementationMixin {

    @Shadow
    private GLFWKeyCallback keyCallback;
    @Shadow

    private GLFWCharCallback charCallback;

    @Shadow
    private long windowHandle;

    @Shadow
    private final byte[] key_down_buffer = new byte[Keyboard.KEYBOARD_SIZE];


    @Shadow
    private void putKeyboardEvent(int keycode, byte state, int ch, long nanos, boolean repeat) {}

    @Inject(method = "createKeyboard()V", at = @At("TAIL"))
    private void tickComputers(CallbackInfo info) {
        this.keyCallback = GLFWKeyCallback.create((window, glfwKey, scancode, action, mods) -> {
            int key = translateKeyFromGLFW(glfwKey);
            if (action == GLFW.GLFW_PRESS) {
                this.key_down_buffer[key] = 1;
            } else if (action == GLFW.GLFW_RELEASE) {
                this.key_down_buffer[key] = 0;
            }
            putKeyboardEvent(key, this.key_down_buffer[key], 0, System.nanoTime(), action == GLFW.GLFW_REPEAT);
            GLFWKeyboardManager.getInstance().notifyKeyObservers(window, glfwKey, scancode, action, mods);
        });

        this.charCallback = GLFWCharCallback.create((window, codepoint) -> {
                // if the keycode is 0 minecraft instead uses the character code as the key pressed, not sure why
                // but a keycode of -1 is used instead to fix this issue
                putKeyboardEvent(-1, (byte) 1, codepoint, System.nanoTime(), false);
            GLFWKeyboardManager.getInstance().notifyCharObservers(window, codepoint);
            }
        );

        this.windowHandle = Display.getHandle();
        GLFW.glfwSetKeyCallback(this.windowHandle, this.keyCallback);
        GLFW.glfwSetCharCallback(this.windowHandle, this.charCallback);
    }
}
