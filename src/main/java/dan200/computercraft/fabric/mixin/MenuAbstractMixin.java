package dan200.computercraft.fabric.mixin;

import dan200.computercraft.shared.pocket.items.ItemPocketComputer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = MenuAbstract.class, remap = false)
public class MenuAbstractMixin {
    @Redirect(
        method = "broadcastChanges",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/core/item/ItemStack;areItemStacksEqual(Lnet/minecraft/core/item/ItemStack;Lnet/minecraft/core/item/ItemStack;)Z")
    )
    private boolean areItemStacksEqualWithPocketTag(ItemStack itemstack, ItemStack itemstack1) {
        if (itemstack == null && itemstack1 == null) {
            return true;
        }

        if (itemstack != null && itemstack.getItem() instanceof ItemPocketComputer) {
            return ItemStack.areItemStacksEqual(itemstack, itemstack1) && itemstack.getData().equals(itemstack1.getData());
        }
        return ItemStack.areItemStacksEqual(itemstack, itemstack1);
    }
}
