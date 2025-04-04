package dan200.computercraft.shared.common;

import com.mojang.nbt.NbtIo;
import com.mojang.nbt.tags.CompoundTag;
import dan200.computercraft.ComputerCraft;
import dan200.computercraft.shared.computer.recipe.ComputerUpgradeRecipe;
import dan200.computercraft.shared.recipe.ImpostorShapelessRecipe;
import dan200.computercraft.shared.util.NBTUtil;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.DataLoader;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShapeless;
import net.minecraft.core.item.ItemStack;
import turniplabs.halplibe.util.RecipeEntrypoint;

public class ComputerCraftRecipe implements RecipeEntrypoint {

    public static ComputerCraftRecipeNamespace COMPUTER_CRAFT = new ComputerCraftRecipeNamespace();
    public static RecipeGroup<RecipeEntryCrafting<?,?>> WORKBENCH;
    public static RecipeGroup<ComputerUpgradeRecipe> COMPUTER_UPGRADE;
    public static RecipeGroup<ImpostorShapelessRecipe> IMPOSTOR_SHAPELESS;

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

    public void registerNamespaces(){
        COMPUTER_CRAFT.register("workbench", WORKBENCH);
        COMPUTER_CRAFT.register("computer_upgrade", COMPUTER_UPGRADE);
        COMPUTER_CRAFT.register("impostor_shapeless", IMPOSTOR_SHAPELESS);
        Registries.RECIPES.register("computercraft", COMPUTER_CRAFT);
    }

    public void resetGroups(){
        WORKBENCH = new RecipeGroup<RecipeEntryCrafting<?, ?>>(new RecipeSymbol(new ItemStack(Blocks.WORKBENCH)));
        COMPUTER_UPGRADE = new RecipeGroup<ComputerUpgradeRecipe>(new RecipeSymbol(new ItemStack(Blocks.WORKBENCH)));
        IMPOSTOR_SHAPELESS = new RecipeGroup<ImpostorShapelessRecipe>(new RecipeSymbol(new ItemStack(Blocks.WORKBENCH)));
    }

    public void load(){
        Registries.RECIPE_TYPES.register("computercraft:computer_upgrade", ComputerUpgradeRecipe.class);
        Registries.RECIPE_TYPES.register("computercraft:impostor_shapeless", ImpostorShapelessRecipe.class);
//        Registry.register( Registry.RECIPE_SERIALIZER, new Identifier( ComputerCraft.MOD_ID, "colour" ), ColourableRecipe.SERIALIZER );
//        Registry.register( Registry.RECIPE_SERIALIZER, new Identifier( ComputerCraft.MOD_ID, "computer_upgrade" ), ComputerUpgradeRecipe.SERIALIZER );
//        Registry.register( Registry.RECIPE_SERIALIZER,
//            new Identifier( ComputerCraft.MOD_ID, "pocket_computer_upgrade" ),
//            PocketComputerUpgradeRecipe.SERIALIZER );
//        Registry.register( Registry.RECIPE_SERIALIZER, new Identifier( ComputerCraft.MOD_ID, "disk" ), DiskRecipe.SERIALIZER );
//        Registry.register( Registry.RECIPE_SERIALIZER, new Identifier( ComputerCraft.MOD_ID, "printout" ), PrintoutRecipe.SERIALIZER );
//        Registry.register( Registry.RECIPE_SERIALIZER, new Identifier( ComputerCraft.MOD_ID, "turtle" ), TurtleRecipe.SERIALIZER );
//        Registry.register( Registry.RECIPE_SERIALIZER, new Identifier( ComputerCraft.MOD_ID, "turtle_upgrade" ), TurtleUpgradeRecipe.SERIALIZER );
//        Registry.register( Registry.RECIPE_SERIALIZER, new Identifier( ComputerCraft.MOD_ID, "impostor_shaped" ), ImpostorRecipe.SERIALIZER );
//        Registry.register( Registry.RECIPE_SERIALIZER, new Identifier( ComputerCraft.MOD_ID, "impostor_shapeless" ), ImpostorShapelessRecipe.SERIALIZER );
//        Registry.register( Registry.LOOT_CONDITION_TYPE, new Identifier( ComputerCraft.MOD_ID, "block_named" ), BlockNamedEntityLootCondition.TYPE );
//        Registry.register( Registry.LOOT_CONDITION_TYPE, new Identifier( ComputerCraft.MOD_ID, "player_creative" ), PlayerCreativeLootCondition.TYPE );
//        Registry.register( Registry.LOOT_CONDITION_TYPE, new Identifier( ComputerCraft.MOD_ID, "has_id" ), HasComputerIdLootCondition.TYPE );

        DataLoader.loadRecipesFromFile("/assets/computercraft/recipes/workbench.json");
        ComputerCraft.log.info("{} recipes in {} groups.", COMPUTER_CRAFT.getAllRecipes().size(), COMPUTER_CRAFT.size());
    }
}
