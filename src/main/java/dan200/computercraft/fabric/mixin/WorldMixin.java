package dan200.computercraft.fabric.mixin;

import dan200.computercraft.shared.common.ComputerCraftBlocks;
import dan200.computercraft.shared.util.DropConsumer;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = World.class, remap = false)
public abstract class WorldMixin {
    @Shadow
    public @Nullable Block<?> getBlock(int x, int y, int z) {
        return null;
    }

    @Inject(method = "getSignal(IIILnet/minecraft/core/util/helper/Side;)Z", at = @At("HEAD"), cancellable = true)
    public void computerSignal(int x, int y, int z, Side side, CallbackInfoReturnable<Boolean> cir) {
        Block<?> block = this.getBlock(x, y, z);
        if (block == Blocks.blocksList[ComputerCraftBlocks.COMPUTER_NORMAL.id()]) {
            if (block != null) {
                cir.setReturnValue(block.getSignal((World) (Object) this, x, y, z, side));
            }
        }
    }

    @Inject(
        method = "entityJoinedWorld",
        at = @At("HEAD"),
        cancellable = true
    )
    public void addEntity(Entity entity, CallbackInfoReturnable<Boolean> ci) {
        if( DropConsumer.onEntitySpawn( entity ) )
        {
            ci.setReturnValue( false );
        }
    }
}
