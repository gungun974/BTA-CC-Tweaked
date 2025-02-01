package dan200.computercraft.fabric.mixin;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.client.FrameInfo;
import dan200.computercraft.core.computer.MainThread;
import dan200.computercraft.shared.util.TickScheduler;
import net.minecraft.client.world.WorldClient;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = WorldClient.class, remap = false)
public abstract class WorldClientMixin extends World {
    public void tick() {
        MainThread.executePendingTasks();
        ComputerCraft.serverComputerRegistry.update();
        TickScheduler.tick();
        FrameInfo.onTick();
        super.tick();
    }
}
