package dan200.computercraft.fabric.mixin;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.shared.peripheral.monitor.TileMonitor;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.Global;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.world.World;
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
    public abstract <T extends TileEntity> void renderTileEntity(Tessellator tessellator, T tileEntity, double x, double y, double z, float partialTick);

    @Shadow
    public static double renderPosX;

    @Shadow
    public static double renderPosY;

    @Shadow
    public static double renderPosZ;

    @Shadow
    public static TileEntityRenderDispatcher instance;

    @Inject(
        method = "renderTileEntity(Lnet/minecraft/client/render/tessellator/Tessellator;Lnet/minecraft/core/block/entity/TileEntity;F)V",
        at = @At("HEAD")
    )
    private <T extends TileEntity> void customMonitorRenderingDistance(Tessellator tessellator, T tileEntity, float partialTick, CallbackInfo ci) {
        if (tileEntity instanceof TileMonitor && tileEntity.getDistanceFrom(this.viewLerpPosX, this.viewLerpPosY, this.viewLerpPosZ) < ComputerCraft.monitorDistanceSq) {
            float brightness = 1.0F;
            if (LightmapHelper.isLightmapEnabled()) {
                LightmapHelper.setLightmapCoord(this.worldObj.getLightmapCoord(tileEntity.x, tileEntity.y, tileEntity.z, 0));
            } else if (!Global.accessor.isFullbrightEnabled()) {
                brightness = this.worldObj.getLightBrightness(tileEntity.x, tileEntity.y, tileEntity.z);
            }

            GL11.glColor3f(brightness, brightness, brightness);
            this.renderTileEntity(tessellator, tileEntity, (double) tileEntity.x - renderPosX, (double) tileEntity.y - renderPosY, (double) tileEntity.z - renderPosZ, partialTick);
        }
    }
}
