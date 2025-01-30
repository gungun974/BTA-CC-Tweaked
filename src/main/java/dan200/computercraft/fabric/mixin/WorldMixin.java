package dan200.computercraft.fabric.mixin;

import dan200.computercraft.ComputerCraft;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = World.class, remap = false)
public abstract class WorldMixin {
    @Inject(method = "tick()V", at = @At("TAIL"))
    private void tickComputers(CallbackInfo info) {
        ComputerCraft.serverComputerRegistry.update();
    }
}
