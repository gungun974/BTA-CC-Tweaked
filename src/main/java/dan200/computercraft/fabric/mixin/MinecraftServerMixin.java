package dan200.computercraft.fabric.mixin;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.core.computer.MainThread;
import dan200.computercraft.fabric.IWorldDirNameAccess;
import dan200.computercraft.shared.util.PortableTickScheduler;
import dan200.computercraft.shared.util.TickScheduler;
import net.minecraft.core.world.save.ISaveFormat;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MinecraftServer.class, remap = false)
public abstract class MinecraftServerMixin implements IWorldDirNameAccess {
    @Inject(method = "doTick()V", at = @At("HEAD"))
    private void tickComputers(CallbackInfo info) {
        PortableTickScheduler.mainPortableTickScheduler.tickAtStart();
        MainThread.executePendingTasks();
        ComputerCraft.serverComputerRegistry.update();
        TickScheduler.tick();
    }

    @Inject(method = "doTick()V", at = @At("TAIL"))
    private void tickEnd(CallbackInfo info) {
        PortableTickScheduler.mainPortableTickScheduler.tickAtEnd();
    }

    @Unique
    public String worldDirName;

    @Inject(
        method = "initWorld",
        at = @At("HEAD")
    )
    private void rememberWorldDirName(ISaveFormat saveFormat, String worldDirName, long l, CallbackInfo ci) {
        this.worldDirName = worldDirName;
    }

    @Override
    public String cc_bta$getWorldDirName() {
        return worldDirName;
    }
}
