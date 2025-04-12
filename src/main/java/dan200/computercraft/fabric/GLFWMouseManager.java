package dan200.computercraft.fabric;

import org.lwjgl.glfw.GLFWScrollCallbackI;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GLFWMouseManager {

    private static GLFWMouseManager instance;

    private final Map<UUID, GLFWScrollCallbackI> scrollObservers;

    private GLFWMouseManager() {
        scrollObservers = new HashMap<>();
    }

    public static synchronized GLFWMouseManager getInstance() {
        if (instance == null) {
            instance = new GLFWMouseManager();
        }
        return instance;
    }

    public UUID addScrollObserver(GLFWScrollCallbackI observer) {
        UUID id = UUID.randomUUID();
        scrollObservers.put(id, observer);
        return id;
    }

    public void removeScrollObserver(UUID id) {
        scrollObservers.remove(id);
    }

    public void notifyScrollObservers(long window, double xoffset, double yoffset) {
        scrollObservers.values().forEach(observer -> observer.invoke(window, xoffset, yoffset));
    }
}
