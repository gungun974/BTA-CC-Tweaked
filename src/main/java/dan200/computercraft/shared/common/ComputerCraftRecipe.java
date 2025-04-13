package dan200.computercraft.shared.common;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.shared.computer.recipe.ComputerUpgradeRecipe;
import dan200.computercraft.shared.media.recipes.DiskRecipe;
import dan200.computercraft.shared.media.recipes.PrintoutRecipe;
import dan200.computercraft.shared.pocket.recipes.PocketComputerUpgradeRecipe;
import dan200.computercraft.shared.recipe.ColourableRecipe;
import dan200.computercraft.shared.recipe.ImpostorShapedRecipe;
import dan200.computercraft.shared.recipe.ImpostorShapelessRecipe;
import dan200.computercraft.shared.turtle.recipes.TurtleRecipe;
import dan200.computercraft.shared.turtle.recipes.TurtleUpgradeRecipe;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.DataLoader;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import turniplabs.halplibe.util.RecipeEntrypoint;

public class ComputerCraftRecipe implements RecipeEntrypoint {

    public static ComputerCraftRecipeNamespace COMPUTER_CRAFT = new ComputerCraftRecipeNamespace();
    public static RecipeGroup<RecipeEntryCrafting<?, ?>> WORKBENCH;
    public static RecipeGroup<ColourableRecipe> COLOUR;
    public static RecipeGroup<ComputerUpgradeRecipe> COMPUTER_UPGRADE;
    public static RecipeGroup<ImpostorShapelessRecipe> IMPOSTOR_SHAPELESS;
    public static RecipeGroup<ImpostorShapedRecipe> IMPOSTOR_SHAPED;
    public static RecipeGroup<TurtleRecipe> TURTLE;
    public static RecipeGroup<TurtleUpgradeRecipe> TURTLE_UPGRADE;
    public static RecipeGroup<PocketComputerUpgradeRecipe> POCKET_COMPUTER_UPGRADE;
    public static RecipeGroup<DiskRecipe> DISK;
    public static RecipeGroup<PrintoutRecipe> PRINTOUT;

    @Override
    public void onRecipesReady() {
        ComputerCraft.log.info("Loading ComputerCraft recipes...");
        resetGroups();
        registerNamespaces();
        load();
    }

    @Override
    public void initNamespaces() {
        ComputerCraft.log.info("Loading ComputerCraft recipe namespaces...");
        resetGroups();

        registerNamespaces();
    }

    public void registerNamespaces() {
        COMPUTER_CRAFT.register("workbench", WORKBENCH);
        COMPUTER_CRAFT.register("colour", COLOUR);
        COMPUTER_CRAFT.register("computer_upgrade", COMPUTER_UPGRADE);
        COMPUTER_CRAFT.register("impostor_shapeless", IMPOSTOR_SHAPELESS);
        COMPUTER_CRAFT.register("impostor_shaped", IMPOSTOR_SHAPED);
        COMPUTER_CRAFT.register("turtle", TURTLE);
        COMPUTER_CRAFT.register("turtle_upgrade", TURTLE_UPGRADE);
        COMPUTER_CRAFT.register("pocket_computer_upgrade", POCKET_COMPUTER_UPGRADE);
        COMPUTER_CRAFT.register("disk", DISK);
        COMPUTER_CRAFT.register("printout", PRINTOUT);
        Registries.RECIPES.register("computercraft", COMPUTER_CRAFT);
    }

    public void resetGroups() {
        WORKBENCH = new RecipeGroup<RecipeEntryCrafting<?, ?>>(new RecipeSymbol(new ItemStack(Blocks.WORKBENCH)));
        COLOUR = new RecipeGroup<ColourableRecipe>(new RecipeSymbol(new ItemStack(Blocks.WORKBENCH)));
        COMPUTER_UPGRADE = new RecipeGroup<ComputerUpgradeRecipe>(new RecipeSymbol(new ItemStack(Blocks.WORKBENCH)));
        IMPOSTOR_SHAPELESS = new RecipeGroup<ImpostorShapelessRecipe>(new RecipeSymbol(new ItemStack(Blocks.WORKBENCH)));
        IMPOSTOR_SHAPED = new RecipeGroup<ImpostorShapedRecipe>(new RecipeSymbol(new ItemStack(Blocks.WORKBENCH)));
        TURTLE = new RecipeGroup<TurtleRecipe>(new RecipeSymbol(new ItemStack(Blocks.WORKBENCH)));
        TURTLE_UPGRADE = new RecipeGroup<TurtleUpgradeRecipe>(new RecipeSymbol(new ItemStack(Blocks.WORKBENCH)));
        POCKET_COMPUTER_UPGRADE = new RecipeGroup<PocketComputerUpgradeRecipe>(new RecipeSymbol(new ItemStack(Blocks.WORKBENCH)));
        DISK = new RecipeGroup<DiskRecipe>(new RecipeSymbol(new ItemStack(Blocks.WORKBENCH)));
        PRINTOUT = new RecipeGroup<PrintoutRecipe>(new RecipeSymbol(new ItemStack(Blocks.WORKBENCH)));
        Registries.RECIPES.unregister("computercraft");
    }

    public void load() {
        Registries.RECIPE_TYPES.register("computercraft:colour", ColourableRecipe.class);
        Registries.RECIPE_TYPES.register("computercraft:computer_upgrade", ComputerUpgradeRecipe.class);
        Registries.RECIPE_TYPES.register("computercraft:impostor_shapeless", ImpostorShapelessRecipe.class);
        Registries.RECIPE_TYPES.register("computercraft:impostor_shaped", ImpostorShapedRecipe.class);
        Registries.RECIPE_TYPES.register("computercraft:turtle", TurtleRecipe.class);
        Registries.RECIPE_TYPES.register("computercraft:turtle_upgrade", TurtleUpgradeRecipe.class);
        Registries.RECIPE_TYPES.register("computercraft:pocket_computer_upgrade", PocketComputerUpgradeRecipe.class);
        Registries.RECIPE_TYPES.register("computercraft:disk", DiskRecipe.class);
        Registries.RECIPE_TYPES.register("computercraft:printout", PrintoutRecipe.class);

        DataLoader.loadRecipesFromFile("/assets/computercraft/recipes/workbench.json");
        DataLoader.loadRecipesFromFile("/assets/computercraft/recipes/turtle_normal.json");
        DataLoader.loadRecipesFromFile("/assets/computercraft/recipes/turtle_advanced.json");
        DataLoader.loadRecipesFromFile("/assets/computercraft/recipes/pocket_normal.json");
        DataLoader.loadRecipesFromFile("/assets/computercraft/recipes/pocket_advanced.json");

        ComputerCraft.log.info("{} recipes in {} groups.", COMPUTER_CRAFT.getAllRecipes().size(), COMPUTER_CRAFT.size());
    }
}
