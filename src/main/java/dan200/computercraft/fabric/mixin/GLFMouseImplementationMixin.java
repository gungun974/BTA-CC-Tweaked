package dan200.computercraft.fabric.mixin;

import com.github.zarzelcow.legacylwjgl3.implementation.glfw.GLFWMouseImplementation;
import dan200.computercraft.fabric.GLFWKeyboardManager;
import dan200.computercraft.fabric.GLFWMouseManager;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.zarzelcow.legacylwjgl3.implementation.glfw.GLFWKeyboardImplementation.translateKeyFromGLFW;

@Mixin(value = GLFWMouseImplementation.class, remap = false)
public abstract class GLFMouseImplementationMixin {
    @Shadow
    private GLFWScrollCallback scrollCallback;

    @Shadow
    private long windowHandle;

    @Shadow
    private int accum_dz;

    @Shadow
    protected abstract void putMouseEvent(byte button, byte state, int dz, long nanos);

    @Inject(method = "createMouse()V", at = @At("TAIL"))
    private void addDirectAccess(CallbackInfo info) {
        this.scrollCallback = GLFWScrollCallback.create((window, xoffset, yoffset) -> {
            accum_dz += yoffset;
            putMouseEvent((byte)-1, (byte)0, (int) yoffset, System.nanoTime());
            GLFWMouseManager.getInstance().notifyScrollObservers(window, xoffset, yoffset);
        });

        this.windowHandle = Display.getHandle();
        GLFW.glfwSetScrollCallback(this.windowHandle, this.scrollCallback);
    }
}
