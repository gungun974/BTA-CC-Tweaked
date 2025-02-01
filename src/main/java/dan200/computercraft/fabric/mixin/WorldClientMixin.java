package dan200.computercraft.fabric.mixin;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.client.FrameInfo;
import net.minecraft.client.world.WorldClient;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = WorldClient.class, remap = false)
public abstract class WorldClientMixin extends World {
    public void tick() {
        ComputerCraft.serverComputerRegistry.update();
        FrameInfo.onTick();
        super.tick();
    }
}
