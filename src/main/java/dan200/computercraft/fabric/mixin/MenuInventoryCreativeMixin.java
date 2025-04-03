package dan200.computercraft.fabric.mixin;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.shared.common.ComputerCraftBlocks;
import dan200.computercraft.shared.common.ComputerCraftItems;
import dan200.computercraft.shared.media.items.ItemDisk;
import dan200.computercraft.shared.pocket.items.ItemPocketComputer;
import dan200.computercraft.shared.turtle.items.ItemTurtle;
import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.menu.MenuInventoryCreative;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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

    @Shadow
    protected List<ItemStack> searchedItems;
    @Unique
    private static int extraCount = 0;

    @Inject(
        method = "<clinit>",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/Block;hasTag(Lnet/minecraft/core/data/tag/Tag;)Z"),
        locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private static void addBlocks(CallbackInfo ci, int count, int id)
    {
        if (id == ComputerCraftBlocks.TURTLE_NORMAL.id() || id == ComputerCraftBlocks.TURTLE_ADVANCED.id()) {
            int before = creativeItems.size();
            if (Item.itemsList[id] != null) {
                ((ItemTurtle) Objects.requireNonNull(Item.itemsList[id])).addToCreativeMenu(creativeItems);
            }
            extraCount += creativeItems.size() - before;
        }
    }

    @Redirect(
        method = "<clinit>",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/core/item/Item;hasTag(Lnet/minecraft/core/data/tag/Tag;)Z")
    )
    private static boolean addItems(Item item, Tag<Item> tag)
    {
        if (item.id == ComputerCraftItems.POCKET_COMPUTER_NORMAL.id || item.id == ComputerCraftItems.POCKET_COMPUTER_ADVANCED.id) {
            int before = creativeItems.size();
            ((ItemPocketComputer) item).addToCreativeMenu(creativeItems);
            extraCount += creativeItems.size() - before;
            return true;
        }
        if (item.id == ComputerCraftItems.DISK.id) {
            int before = creativeItems.size();
            ((ItemDisk) item).addToCreativeMenu(creativeItems);
            extraCount += creativeItems.size() - before;
            return true;
        }
        return item.hasTag(tag);
    }

    @Inject(
        method = "<clinit>",
        at = @At(value = "FIELD", target = "Lnet/minecraft/core/player/inventory/menu/MenuInventoryCreative;creativeItemsCount:I", shift = At.Shift.AFTER)
    )
    private static void addUpItemCount(CallbackInfo ci)
    {
        creativeItemsCount += extraCount;
    }
}
