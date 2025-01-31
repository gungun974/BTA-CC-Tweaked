package dan200.computercraft.fabric;

import org.lwjgl.glfw.GLFWCharCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GLFWKeyboardManager {

    private static GLFWKeyboardManager instance;

    private final Map<UUID, GLFWKeyCallbackI> keyObservers;
    private final Map<UUID, GLFWCharCallbackI> charObservers;

    private GLFWKeyboardManager() {
        keyObservers = new HashMap<>();
        charObservers = new HashMap<>();
    }

    public static synchronized GLFWKeyboardManager getInstance() {
        if (instance == null) {
            instance = new GLFWKeyboardManager();
        }
        return instance;
    }

    public UUID addKeyObserver(GLFWKeyCallbackI observer) {
        UUID id = UUID.randomUUID();
        keyObservers.put(id, observer);
        return id;
    }

    public void removeKeyObserver(UUID id) {
        keyObservers.remove(id);
    }

    public void notifyKeyObservers(long window, int key, int scancode, int action, int mods) {
        keyObservers.values().forEach(observer -> observer.invoke(window, key, scancode, action, mods));
    }

    public UUID addCharObserver(GLFWCharCallbackI observer) {
        UUID id = UUID.randomUUID();
        charObservers.put(id, observer);
        return id;
    }

    public void removeCharObserver(UUID id) {
        charObservers.remove(id);
    }

    public void notifyCharObservers(long window, int codepoint) {
        charObservers.values().forEach(observer -> observer.invoke(window, codepoint));
    }
}
