package dan200.computercraft.fabric.mixin;

import dan200.computercraft.shared.util.DropConsumer;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Entity.class, remap = false)
public class EntityMixin {
    @Shadow
    public double z;

    @Shadow
    public double y;

    @Shadow
    public double x;

    @Inject(
        method = "dropItem(Lnet/minecraft/core/item/ItemStack;F)Lnet/minecraft/core/entity/EntityItem;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void dropStack(ItemStack stack, float verticalOffset, CallbackInfoReturnable<EntityItem> cir) {
        if (DropConsumer.onLivingDrops((Entity) (Object) this, stack)) {
            EntityItem entityitem = new EntityItem(null, x, y + (double) verticalOffset, z, stack);

            cir.cancel();
            cir.setReturnValue(entityitem);
        }
    }
}
