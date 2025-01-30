package dan200.computercraft.fabric.mixin;

import dan200.computercraft.ComputerCraft;
import net.minecraft.client.world.WorldClient;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = WorldClient.class, remap = false)
public abstract class WorldClientMixin extends World {
    public void tick() {
        ComputerCraft.serverComputerRegistry.update();
        super.tick();
    }
}
