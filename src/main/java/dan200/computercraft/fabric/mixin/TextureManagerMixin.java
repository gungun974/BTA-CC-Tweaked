package dan200.computercraft.fabric.mixin;

import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.dynamictexture.DynamicTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = TextureManager.class, remap = false)
public abstract class TextureManagerMixin {
    @Shadow
    protected abstract void addDynamicTexture(DynamicTexture texture);

//    @Inject(method = "addNativeDynamicTextures", at = @At("TAIL"))
//    void addCCNativeDynamicTextures(int state, CallbackInfo ci) {
//        if (state < ANIMATIONS_CUSTOM) {
//            IconCoordinate computerAdvancedFrontBlink = TextureRegistry.getTexture("computercraft:block/computer_advanced_front_blink");
//            this.addDynamicTexture(new DynamicTextureCustom(computerAdvancedFrontBlink, Objects.requireNonNull(computerAdvancedFrontBlink.getMeta("animation", AnimationProperties.class))));
//
//            IconCoordinate computerCommandFrontBlink = TextureRegistry.getTexture("computercraft:block/computer_command_front_blink");
//            this.addDynamicTexture(new DynamicTextureCustom(computerCommandFrontBlink, Objects.requireNonNull(computerCommandFrontBlink.getMeta("animation", AnimationProperties.class))));
//
//            IconCoordinate computerNormalFrontBlink = TextureRegistry.getTexture("computercraft:block/computer_normal_front_blink");
//            this.addDynamicTexture(new DynamicTextureCustom(computerNormalFrontBlink, Objects.requireNonNull(computerNormalFrontBlink.getMeta("animation", AnimationProperties.class))));
//
//            IconCoordinate pocketComputerBlink = TextureRegistry.getTexture("computercraft:item/pocket_computer_blink");
//            this.addDynamicTexture(new DynamicTextureCustom(pocketComputerBlink, Objects.requireNonNull(pocketComputerBlink.getMeta("animation", AnimationProperties.class))));
//        }
//    }
}
