package dan200.computercraft.fabric.mixin;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.shared.peripheral.monitor.TileMonitor;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.camera.ICamera;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TileEntityRenderDispatcher.class, remap = false)
public abstract class TileEntityRenderDispatcherMixin {
    @Shadow
    public double viewLerpPosX;

    @Shadow
    public double viewLerpPosY;

    @Shadow
    public double viewLerpPosZ;

    @Shadow
    public static double renderPosX;

    @Shadow
    public static double renderPosY;

    @Shadow
    public static double renderPosZ;

    @Shadow
    public static TileEntityRenderDispatcher instance;

    @Inject(
        method = "renderTileEntity(Lnet/minecraft/client/render/tessellator/TessellatorGeneral;Lnet/minecraft/client/render/camera/ICamera;Lnet/minecraft/core/world/World;Lnet/minecraft/core/block/entity/TileEntity;F)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private <T extends TileEntity> void customMonitorRenderingDistance(@NotNull TessellatorGeneral tessellator, @NotNull ICamera camera, @NotNull World world, @NotNull TileEntity tileEntity, float partialTick, CallbackInfo ci) {
        TileEntityRenderer<T> renderer = (TileEntityRenderer<T>) instance.getRenderer(tileEntity);
        if (renderer != null) {
            if (tileEntity instanceof TileMonitor && tileEntity.getDistanceFrom(this.viewLerpPosX, this.viewLerpPosY, this.viewLerpPosZ) < ComputerCraft.monitorDistanceSq) {
                if (renderer.isVisible((T) tileEntity, camera, partialTick)) {
                    GLRenderer.setLightmapCoord1i(world.getLightIndex(tileEntity.tilePos, 0));
                    GLRenderer.setColor3f(1.0F, 1.0F, 1.0F);
                    renderer.doRender(tessellator, (T) tileEntity, (double) tileEntity.tilePos.x - renderPosX, (double) tileEntity.tilePos.y - renderPosY, (double) tileEntity.tilePos.z - renderPosZ, partialTick);
                    ci.cancel();
                }
            }
        }

    }
}
