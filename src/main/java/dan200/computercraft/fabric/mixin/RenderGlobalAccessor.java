package dan200.computercraft.fabric.mixin;

import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.RenderGlobal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = RenderGlobal.class, remap = false)
public interface RenderGlobalAccessor {
    @Accessor
    RenderBlocks getGlobalRenderBlocks();
}
