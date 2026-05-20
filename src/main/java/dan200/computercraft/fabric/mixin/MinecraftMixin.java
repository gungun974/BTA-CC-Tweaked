package dan200.computercraft.fabric.mixin;

import dan200.computercraft.fabric.IWorldDirNameAccess;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Minecraft.class, remap = false)
public abstract class MinecraftMixin implements IWorldDirNameAccess {
    @Unique
    public String worldDirName;

    @Inject(
        method = "startWorld(Ljava/lang/String;)V",
        at = @At("HEAD")
    )
    private void rememberWorldDirName(String worldDirName, CallbackInfo ci) {
        this.worldDirName = worldDirName;
    }

    @Override
    public String cc_bta$getWorldDirName() {
        return worldDirName;
    }
}
