package xyz.phanta.tconmodmod.mutator.impl;

import com.google.gson.JsonElement;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import xyz.phanta.tconmodmod.mutator.MutationStrategy;
import xyz.phanta.tconmodmod.util.TconReflect;

@MutationStrategy.Register
public class MutationPerLevel implements MutationStrategy<Modifier> {

    @Override
    public String getIdentifier() {
        return "per_level";
    }

    @Override
    public Class<Modifier> getTargetClass() {
        return Modifier.class;
    }

    @Override
    public void mutate(Modifier modifier, JsonElement data) {
        int count = data.getAsInt();
        for (ModifierAspect aspect : TconReflect.getAspects(modifier)) {
            if (aspect instanceof ModifierAspect.MultiAspect) {
                TconReflect.setCountPerLevel((ModifierAspect.MultiAspect)aspect, count);
            }
        }
    }

}
