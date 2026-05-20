package dan200.computercraft.fabric.mixin;


import dan200.computercraft.shared.util.DropConsumer;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BlockLogic.class, remap = false)
public class LogicBlockMixin {
    @Inject(
        method = "dropWithCause",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/block/BlockLogic;getBreakResult(Lnet/minecraft/core/world/World;Lnet/minecraft/core/enums/EnumDropCause;Lnet/minecraft/core/world/pos/TilePosc;ILnet/minecraft/core/block/entity/TileEntity;)[Lnet/minecraft/core/item/ItemStack;",
            ordinal = 0,
            shift = At.Shift.AFTER
        ),
        cancellable = true
    )
    private void dropStack(World world, EnumDropCause dropCause, TilePosc tilePos, int data, TileEntity tileEntity, Player player, CallbackInfo ci) {
        ItemStack[] drops = ((BlockLogic) ((Object) this)).getBreakResult(world, dropCause, tilePos, data, tileEntity);

        boolean shouldCancel = false;
        if (drops != null) {
            int j = 0;

            for (int dropsLength = drops.length; j < dropsLength; j++) {
                ItemStack drop = drops[j];
                if (drop != null) {
                    if (DropConsumer.onHarvestDrops(world, tilePos, drop)) {
                        shouldCancel = true;
                    }
                }
            }
        }

        if (shouldCancel) {
            ci.cancel();
        }
    }
}
