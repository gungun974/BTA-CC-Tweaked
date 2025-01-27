package dan200.computercraft.fabric.mixin;

import dan200.computercraft.ComputerCraft;
import net.minecraft.core.data.gamerule.GameRules;
import net.minecraft.core.net.packet.PacketGameRule;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.World;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.net.PlayerList;
import net.minecraft.server.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = World.class, remap = false)
public abstract class WorldMixin {
    @Inject(method = "tick()V", at = @At("TAIL"))
    private void tickComputers(CallbackInfo info) {
        ComputerCraft.serverComputerRegistry.update();
    }
}
