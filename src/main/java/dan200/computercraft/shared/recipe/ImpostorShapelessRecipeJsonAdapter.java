package dan200.computercraft.shared.recipe;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.adapter.RecipeJsonAdapter;
import net.minecraft.core.item.ItemStack;

public class ImpostorShapelessRecipeJsonAdapter implements RecipeJsonAdapter<ImpostorShapelessRecipe> {
//    @Override
//    public RecipeEntryCraftingShaped deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//        JsonObject obj = json.getAsJsonObject();
//        List<String> pattern = obj.get("pattern").getAsJsonArray().asList().stream().map(JsonElement::getAsString).collect(Collectors.toList());
//        List<RecipeSymbol> symbols = obj.get("symbols").getAsJsonArray().asList().stream().map((E)->context.<RecipeSymbol>deserialize(E,RecipeSymbol.class)).collect(Collectors.toList());
//        ItemStack result = context.deserialize(obj.get("result").getAsJsonObject(),ItemStack.class);
//        boolean consumeContainers = obj.has("consumeContainers") ? obj.get("consumeContainers").getAsBoolean() : DEFAULT_CONSUME_CONTAINER;
//        boolean mirror = obj.has("mirror") ? obj.get("mirror").getAsBoolean() : DEFAULT_ALLOW_MIRROR;
//        return RecipeRegistry.parseRecipe(pattern, symbols, result, consumeContainers, mirror);
//    }

    @Override
    public ImpostorShapelessRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        List<RecipeSymbol> symbols = obj.get("inputs").getAsJsonArray().asList().stream().map((E)->context.<RecipeSymbol>deserialize(E,RecipeSymbol.class)).collect(Collectors.toList());
        ItemStack result = context.deserialize(obj.get("result").getAsJsonObject(),ItemStack.class);
        return new ImpostorShapelessRecipe(symbols,result);
    }

    @Override
    public JsonElement serialize(ImpostorShapelessRecipe src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name",src.toString());
        obj.addProperty("type", Registries.RECIPE_TYPES.getKey(src.getClass()));
        List<RecipeSymbol> symbols = src.getInput();
        obj.add("inputs",context.serialize(symbols));
        obj.add("result",context.serialize(src.getOutput()));
        return obj;
    }
}
