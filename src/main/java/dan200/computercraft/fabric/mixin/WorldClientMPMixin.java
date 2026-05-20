package dan200.computercraft.fabric.mixin;

import dan200.computercraft.client.FrameInfo;
import dan200.computercraft.shared.util.PortableTickScheduler;
import net.minecraft.client.world.WorldClientMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WorldClientMP.class, remap = false)
public abstract class WorldClientMPMixin {
    @Inject(method = "tick()V", at = @At("HEAD"))
    private void tickComputers(CallbackInfo info) {
        PortableTickScheduler.mainPortableTickScheduler.tickAtStart();
        FrameInfo.onTick();
    }

    @Inject(method = "tick()V", at = @At("TAIL"))
    private void tickEnd(CallbackInfo info) {
        PortableTickScheduler.mainPortableTickScheduler.tickAtEnd();
    }
}
