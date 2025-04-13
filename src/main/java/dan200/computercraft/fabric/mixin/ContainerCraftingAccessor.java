package dan200.computercraft.fabric.mixin;

import net.minecraft.core.player.inventory.container.ContainerCrafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ContainerCrafting.class, remap = false)
public interface ContainerCraftingAccessor {
    @Accessor
    int getWidth();
}
