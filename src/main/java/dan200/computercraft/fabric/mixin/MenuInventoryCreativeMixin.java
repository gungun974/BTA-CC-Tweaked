package dan200.computercraft.fabric.mixin;

import net.minecraft.core.player.inventory.menu.MenuInventoryCreative;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = MenuInventoryCreative.class, remap = false)
public class MenuInventoryCreativeMixin {
//    @Shadow
//    public static List<ItemStack> creativeItems;
//
//    @Shadow
//    public static int creativeItemsCount;
//    @Unique
//    private static int extraCount = 0;
//
//    @Inject(
//        method = "<clinit>",
//        at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/Block;hasTag(Lnet/minecraft/core/data/tag/Tag;)Z")
//
//    )
//    private static void addBlocks(CallbackInfo ci, @Local(name = "id") int i) {
//        Block<?> block = Blocks.blocksList[i];
//
//        if(block == null) {
//            return;
//        }
//
//        if (block.id() == ComputerCraftBlocks.TURTLE_NORMAL.id() || block.id() == ComputerCraftBlocks.TURTLE_ADVANCED.id()) {
//            int before = creativeItems.size();
//            ((ItemBlockTurtle) block.asItem()).addToCreativeMenu(creativeItems);
//            extraCount += creativeItems.size() - before;
//        }
//    }
//
//    @Inject(
//        method = "<clinit>",
//        at = @At(value = "INVOKE", target = "Lnet/minecraft/core/item/Item;hasTag(Lnet/minecraft/core/data/tag/Tag;)Z", shift = At.Shift.AFTER)
//    )
//    private static void addItems(CallbackInfo ci, @Local(name = "i") int i) {
//        Item item = Item.itemsList[i];
//
//        if(item == null) {
//            return;
//        }
//
//        if (item.id == ComputerCraftItems.POCKET_COMPUTER_NORMAL.id || item.id == ComputerCraftItems.POCKET_COMPUTER_ADVANCED.id) {
//            int before = creativeItems.size();
//            ((ItemPocketComputer) item).addToCreativeMenu(creativeItems);
//            extraCount += creativeItems.size() - before;
//        }
//        if (item.id == ComputerCraftItems.DISK.id) {
//            int before = creativeItems.size();
//            ((ItemDisk) item).addToCreativeMenu(creativeItems);
//            extraCount += creativeItems.size() - before;
//        }
//    }
//
//    @Inject(
//        method = "<clinit>",
//        at = @At(value = "FIELD", target = "Lnet/minecraft/core/player/inventory/menu/MenuInventoryCreative;creativeItemsCount:I", shift = At.Shift.AFTER)
//    )
//    private static void addUpItemCount(CallbackInfo ci) {
//        creativeItemsCount += extraCount;
//    }
}
