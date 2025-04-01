package dan200.computercraft.fabric.mixin;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.shared.common.ComputerCraftBlocks;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.packet.PacketBlockUpdate;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.util.helper.Side;
import net.minecraft.server.entity.player.PlayerServer;
import net.minecraft.server.world.ServerPlayerController;
import net.minecraft.server.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Objects;

@Mixin(value = ServerPlayerController.class, remap = false)
public abstract class ServerPlayerControllerMixin {
    @Shadow
    public Player player;

    @Shadow
    private WorldServer thisWorld;

    @Inject(method = "mineBlock", at = @At(value = "TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void keepComputerAliveInCreative(int x, int y, int z, Side side, CallbackInfoReturnable<Boolean> cir, ItemStack heldItemStack, int id, int meta, Item heldItem, Block block, TileEntity tileEntity, boolean flag) {
        if (flag && this.player.getGamemode() == Gamemode.creative
            && (
            id == ComputerCraftBlocks.COMPUTER_NORMAL.id() ||
                id == ComputerCraftBlocks.COMPUTER_ADVANCED.id() ||
                id == ComputerCraftBlocks.TURTLE_NORMAL.id() ||
                id == ComputerCraftBlocks.TURTLE_ADVANCED.id()
        )
        ) {
            Objects.requireNonNull(Blocks.blocksList[id]).harvestBlock(this.thisWorld, this.player, x, y, z, meta, tileEntity);
            ((PlayerServer) this.player).playerNetServerHandler.sendPacket(new PacketBlockUpdate(x, y, z, this.thisWorld));
        }
    }
}
