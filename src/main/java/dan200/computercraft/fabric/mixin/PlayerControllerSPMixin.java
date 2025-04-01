package dan200.computercraft.fabric.mixin;

import dan200.computercraft.shared.common.ComputerCraftBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.controller.PlayerControllerSP;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.util.helper.Side;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Objects;

@Mixin(value = PlayerControllerSP.class, remap = false)
public abstract class PlayerControllerSPMixin {
    @Inject(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/player/gamemode/Gamemode;dropBlockOnBreak()Z"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void keepComputerAliveInCreative(int x, int y, int z, Side side, Player player, CallbackInfoReturnable<Boolean> cir, int id, int meta, TileEntity tileEntity, boolean removed, ItemStack item) {
        final Minecraft mc = Minecraft.getMinecraft();
        if (removed && mc.thePlayer.getGamemode() == Gamemode.creative && (
            id == ComputerCraftBlocks.COMPUTER_NORMAL.id() ||
                id == ComputerCraftBlocks.COMPUTER_ADVANCED.id() ||
                id == ComputerCraftBlocks.TURTLE_NORMAL.id() ||
                id == ComputerCraftBlocks.TURTLE_ADVANCED.id()
        )) {
            Objects.requireNonNull(Blocks.blocksList[id]).harvestBlock(mc.currentWorld, mc.thePlayer, x, y, z, meta, tileEntity);
        }
    }
}
