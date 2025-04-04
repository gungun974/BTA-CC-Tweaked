package dan200.computercraft.shared.integration;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.shared.recipe.ImpostorShapelessRecipe;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShapeless;
import turing.tmb.TMB;
import turing.tmb.api.ITMBPlugin;
import turing.tmb.api.TMBEntrypoint;
import turing.tmb.api.runtime.ITMBRuntime;
import turing.tmb.vanilla.ShapelessCraftingRecipeTranslator;
import turing.tmb.vanilla.VanillaPlugin;

import java.util.ArrayList;
import java.util.List;

public class TMBIntegration implements ITMBPlugin, TMBEntrypoint {

    List<RecipeEntryCrafting<?, ?>> useEntryCrafting = new ArrayList<>();

    @Override
    public void registerRecipes(ITMBRuntime runtime) {
        for (RecipeEntryCrafting<?, ?> entryCrafting : Registries.RECIPES.getAllCraftingRecipes()) {
            if (entryCrafting instanceof ImpostorShapelessRecipe) {
                if (useEntryCrafting.contains(entryCrafting)) {
                    continue;
                }
                ImpostorShapelessRecipe impostorShapelessRecipe = (ImpostorShapelessRecipe) entryCrafting;
                runtime.getRecipeIndex().registerRecipe(VanillaPlugin.shapelessCraftingCategory, new RecipeEntryCraftingShapeless(
                    impostorShapelessRecipe.getInput(),
                    impostorShapelessRecipe.getOutput()),
                    ShapelessCraftingRecipeTranslator::new
                );
                useEntryCrafting.add(entryCrafting);
            }
        }
    }

    @Override
    public void onGatherPlugins(boolean isReload) {
        TMB.LOGGER.info("Loading plugin: "+this.getClass().getSimpleName()+" from "+ ComputerCraft.MOD_ID);
        TMB.registerPlugin(this);
    }
}
