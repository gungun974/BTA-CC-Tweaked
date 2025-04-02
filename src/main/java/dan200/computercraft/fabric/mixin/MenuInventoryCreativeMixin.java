package dan200.computercraft.fabric.mixin;

import dan200.computercraft.shared.common.ComputerCraftBlocks;
import dan200.computercraft.shared.turtle.items.ItemTurtle;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.menu.MenuInventoryCreative;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Objects;

@Mixin(value = MenuInventoryCreative.class, remap = false)
public class MenuInventoryCreativeMixin
{
    @Shadow
    public static List<ItemStack> creativeItems;

    @Shadow
    public static int creativeItemsCount;

    @Unique
    private static int extraCount = 0;

    @Inject(
        method = "<clinit>",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/Block;hasTag(Lnet/minecraft/core/data/tag/Tag;)Z"),
        locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private static void addTurtlesFamily(CallbackInfo ci, int count, int id)
    {
        if (id == ComputerCraftBlocks.TURTLE_NORMAL.id() || id == ComputerCraftBlocks.TURTLE_ADVANCED.id()) {
            int before = creativeItems.size();
            if (Item.itemsList[id] != null) {
                ((ItemTurtle) Objects.requireNonNull(Item.itemsList[id])).addToCreativeMenu(creativeItems);
            }
            extraCount += creativeItems.size() - before;
        }
    }

    @Inject(
        method = "<clinit>",
        at = @At("TAIL")
    )
    private static void addUpItemCount(CallbackInfo ci)
    {
        creativeItemsCount += extraCount;
    }
}
