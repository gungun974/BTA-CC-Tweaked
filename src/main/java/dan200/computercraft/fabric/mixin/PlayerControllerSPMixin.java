package dan200.computercraft.fabric.mixin;

import dan200.computercraft.shared.common.ComputerCraftBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.controller.PlayerControllerSP;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.gamemode.Gamemodes;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = PlayerControllerSP.class, remap = false)
public abstract class PlayerControllerSPMixin {
    @Inject(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/item/ItemStack;onBlockDestroyed(Lnet/minecraft/core/world/World;Lnet/minecraft/core/entity/player/Player;Lnet/minecraft/core/block/Block;Lnet/minecraft/core/world/pos/TilePosc;Lnet/minecraft/core/util/helper/Side;)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void keepComputerAliveInCreative(TilePosc tilePos, Side side, CallbackInfoReturnable<Boolean> cir, World world, Player player, Block block, int data, TileEntity tileEntity, boolean removed, ItemStack item) {
        final Minecraft mc = Minecraft.getMinecraft();
        if (removed && mc.thePlayer.getGamemode() == Gamemodes.CREATIVE && (
            block.id() == ComputerCraftBlocks.COMPUTER_NORMAL.id() ||
                block.id() == ComputerCraftBlocks.COMPUTER_ADVANCED.id() ||
                block.id() == ComputerCraftBlocks.TURTLE_NORMAL.id() ||
                block.id() == ComputerCraftBlocks.TURTLE_ADVANCED.id()
        )) {
            block.onHarvest(mc.currentWorld, mc.thePlayer, tilePos, data, tileEntity);
        }
    }
}
