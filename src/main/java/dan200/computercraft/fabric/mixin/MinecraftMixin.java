package dan200.computercraft.fabric.mixin;

import dan200.computercraft.fabric.IWorldDirNameAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.core.world.save.ISaveFormat;
import net.minecraft.core.world.settings.WorldConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(value = Minecraft.class, remap = false)
public abstract class MinecraftMixin implements IWorldDirNameAccess {
    @Shadow

    private ISaveFormat saveFormat;
    @Unique
    public String worldDirName;

    @Inject(method = "createAndStartWorld", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/Minecraft;changeWorld(Lnet/minecraft/client/world/WorldClient;Ljava/lang/String;)V"
    ))
    public void rememberNewWorldDirName(WorldConfiguration worldConfiguration, CallbackInfo ci) {
        this.worldDirName = worldConfiguration.getFolderName(this.saveFormat);
    }

    @Inject(method = "startWorld", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/Minecraft;changeWorld(Lnet/minecraft/client/world/WorldClient;Ljava/lang/String;)V"
    ))
    public void rememberWorldDirName(String worldDirName, CallbackInfo ci) {
        this.worldDirName = worldDirName;
    }

    @Override
    public String cc_bta$getWorldDirName() {
        return worldDirName;
    }
}
