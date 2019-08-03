package xyz.phanta.tconmodmod.mutator.impl;

import com.google.gson.JsonElement;
import slimeknights.tconstruct.tools.modifiers.ModExtraTrait;
import xyz.phanta.tconmodmod.mutator.MutationStrategy;
import xyz.phanta.tconmodmod.util.TconReflect;

@MutationStrategy.Register
public class MutationUnlimitedEmboss implements MutationStrategy<ModExtraTrait> {

    @Override
    public String getIdentifier() {
        return "unlimited_emboss";
    }

    @Override
    public Class<ModExtraTrait> getTargetClass() {
        return ModExtraTrait.class;
    }

    @Override
    public void mutate(ModExtraTrait modifier, JsonElement data) {
        TconReflect.getAspects(modifier).removeIf(TconReflect::instanceOfExtraTraitAspect);
    }

}
