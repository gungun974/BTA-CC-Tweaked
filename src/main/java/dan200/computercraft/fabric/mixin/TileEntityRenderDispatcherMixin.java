package dan200.computercraft.fabric.mixin;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.shared.peripheral.monitor.TileMonitor;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.camera.ICamera;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.Global;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
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
    public World worldObj;

    @Shadow
    public static double renderPosX;

    @Shadow
    public static double renderPosY;

    @Shadow
    public static double renderPosZ;

    @Shadow
    public static TileEntityRenderDispatcher instance;

    @Inject(
        method = "renderTileEntity(Lnet/minecraft/client/render/tessellator/Tessellator;Lnet/minecraft/client/render/camera/ICamera;Lnet/minecraft/core/block/entity/TileEntity;F)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private <T extends TileEntity> void customMonitorRenderingDistance(@NotNull Tessellator tessellator, ICamera camera, @NotNull T tileEntity, float partialTick, CallbackInfo ci) {
        TileEntityRenderer<T> renderer = instance.getRenderer(tileEntity);
        if (renderer != null) {
            if (tileEntity instanceof TileMonitor && tileEntity.getDistanceFrom(this.viewLerpPosX, this.viewLerpPosY, this.viewLerpPosZ) < ComputerCraft.monitorDistanceSq) {
                if (renderer.isVisible(tileEntity, camera, partialTick)) {
                    assert this.worldObj != null;

                    float brightness;
                    if (LightmapHelper.isLightmapEnabled()) {
                        LightmapHelper.setLightmapCoord(this.worldObj.getLightmapCoord(tileEntity.x, tileEntity.y, tileEntity.z, 0));
                        brightness = 1.0F;
                    } else if (Global.accessor.isFullbrightEnabled()) {
                        brightness = 1.0F;
                    } else {
                        brightness = this.worldObj.getLightBrightness(tileEntity.x, tileEntity.y, tileEntity.z);
                    }

                    GL11.glColor3f(brightness, brightness, brightness);
                    renderer.doRender(tessellator, tileEntity, (double)tileEntity.x - renderPosX, (double)tileEntity.y - renderPosY, (double)tileEntity.z - renderPosZ, partialTick);
                    ci.cancel();
                }
            }
        }

    }
}
