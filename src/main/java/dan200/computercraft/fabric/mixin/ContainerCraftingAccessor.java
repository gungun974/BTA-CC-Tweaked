package dan200.computercraft.fabric.mixin;

import net.minecraft.core.player.inventory.container.ContainerCrafting;
import net.minecraft.server.entity.player.PlayerServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = ContainerCrafting.class, remap = false)
public interface ContainerCraftingAccessor {
    @Accessor
    int getWidth();
}
