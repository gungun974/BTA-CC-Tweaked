package dan200.computercraft.fabric.mixin;

import net.minecraft.server.entity.player.PlayerServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = PlayerServer.class, remap = false)
public interface PlayerServerAccessor {
    @Accessor
    int getCurrentWindowId();

    @Invoker("getNextWindowId")
    void invokeGetNextWindowId();
}
