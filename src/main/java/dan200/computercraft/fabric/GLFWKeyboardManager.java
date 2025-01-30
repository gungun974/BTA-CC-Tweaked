package dan200.computercraft.fabric;

import org.lwjgl.glfw.GLFWKeyCallbackI;

import java.util.ArrayList;
import java.util.List;

public class GLFWKeyboardManager {

    private static GLFWKeyboardManager instance;

    private List<GLFWKeyCallbackI> observers;

    private GLFWKeyboardManager() {
        observers = new ArrayList<>();
    }

    public static synchronized GLFWKeyboardManager getInstance() {
        if (instance == null) {
            instance = new GLFWKeyboardManager();
        }
        return instance;
    }

    public void addObserver(GLFWKeyCallbackI observer) {
        observers.add(observer);
    }

    public void removeObserver(GLFWKeyCallbackI observer) {
        observers.remove(observer);
    }

    public void notifyObservers(long window, int key, int scancode, int action, int mods) {
        for (GLFWKeyCallbackI observer : observers) {
            observer.invoke(window, key, scancode, action, mods);
        }
    }
}
