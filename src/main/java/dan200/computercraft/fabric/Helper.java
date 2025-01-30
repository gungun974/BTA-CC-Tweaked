package dan200.computercraft.fabric;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Global;

public class Helper {
    public static boolean isServerEnvironment() {
        return Global.isServer;
    }

    public static boolean isSinglePlayer() {
        if (Global.isServer) {
            return false;
        }

        return !Minecraft.getMinecraft().isMultiplayerWorld();
    }
}
