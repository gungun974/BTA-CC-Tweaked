package dan200.computercraft.fabric.mixin;

import dan200.computercraft.shared.recipe.ImpostorShapedRecipe;
import dan200.computercraft.shared.recipe.ImpostorShapelessRecipe;
import dan200.computercraft.shared.turtle.recipes.TurtleRecipe;
import net.minecraft.client.gui.guidebook.crafting.RecipePageCrafting;
import net.minecraft.client.gui.guidebook.crafting.displays.DisplayAdapterShaped;
import net.minecraft.client.gui.guidebook.crafting.displays.DisplayAdapterShapeless;
import net.minecraft.client.gui.guidebook.crafting.displays.RecipeDisplayAdapter;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = RecipePageCrafting.class, remap = false)
public class RecipePageCraftingMixin {
    @Shadow
    public static Map<Class<? extends RecipeEntryCrafting<?, ?>>, RecipeDisplayAdapter<?>> recipeToDisplayAdapterMap;

    @Inject(
        method = "<clinit>",
        at = @At("TAIL")
    )
    private static void injectImpostorRecipes(CallbackInfo ci) {
        recipeToDisplayAdapterMap.put(TurtleRecipe.class, new DisplayAdapterShaped());
        recipeToDisplayAdapterMap.put(ImpostorShapedRecipe.class, new DisplayAdapterShaped());
        recipeToDisplayAdapterMap.put(ImpostorShapelessRecipe.class, new DisplayAdapterShapeless());
    }
}
