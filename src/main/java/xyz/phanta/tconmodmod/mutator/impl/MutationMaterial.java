package xyz.phanta.tconmodmod.mutator.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.modifiers.Modifier;
import xyz.phanta.tconmodmod.TconModMod;
import xyz.phanta.tconmodmod.mutator.MutationStrategy;
import xyz.phanta.tconmodmod.util.TconReflect;

import javax.annotation.Nullable;
import java.util.PriorityQueue;

@MutationStrategy.Register
public class MutationMaterial implements MutationStrategy<Modifier> {

    @Override
    public String getIdentifier() {
        return "material";
    }

    @Override
    public Class<Modifier> getTargetClass() {
        return Modifier.class;
    }

    @Override
    public void mutate(Modifier modifier, JsonElement data) {
        JsonArray matDto = data.getAsJsonArray();
        PriorityQueue<RecipeMatch> matchers = TconReflect.getRecipeMatchers(modifier);
        matchers.clear();
        for (JsonElement elem : matDto) {
            RecipeMatch matcher = parseMatcher(elem.getAsJsonObject());
            if (matcher != null) {
                matchers.add(matcher);
            }
        }
    }

    @Nullable
    private static RecipeMatch parseMatcher(JsonObject dto) {
        String type = dto.get("type").getAsString();
        int amount = dto.has("amount") ? dto.get("amount").getAsInt() : 1;
        int value = dto.has("value") ? dto.get("value").getAsInt() : 1;
        switch (type) {
            case "ore":
                return new RecipeMatch.Oredict(dto.get("ore").getAsString(), amount, value);
            case "item":
                String itemName = dto.get("item").getAsString();
                Item item = Item.getByNameOrId(itemName);
                if (item != null) {
                    return new RecipeMatch.Item(
                            new ItemStack(item, 1, dto.has("meta") ? dto.get("meta").getAsInt() : 0), amount, value);
                } else {
                    TconModMod.LOGGER.warn("Unknown item: {}", itemName);
                    return null;
                }
        }
        TconModMod.LOGGER.warn("Unknown item matcher type: {}", type);
        return null;
    }

}
