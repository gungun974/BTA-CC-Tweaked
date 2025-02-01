package dan200.computercraft.fabric.mixin;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.core.computer.MainThread;
import dan200.computercraft.shared.util.TickScheduler;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MinecraftServer.class, remap = false)
public abstract class MinecraftServerMixin {
    @Inject(method = "doTick()V", at = @At("HEAD"))
    private void tickComputers(CallbackInfo info) {
        MainThread.executePendingTasks();
        ComputerCraft.serverComputerRegistry.update();
        TickScheduler.tick();
    }
}
