package xyz.phanta.tconmodmod.mutator.impl;

import com.google.gson.JsonElement;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import xyz.phanta.tconmodmod.mutator.MutationStrategy;
import xyz.phanta.tconmodmod.util.TconReflect;

@MutationStrategy.Register
public class MutationColour implements MutationStrategy<Modifier> {

    @Override
    public String getIdentifier() {
        return "colour";
    }

    @Override
    public Class<Modifier> getTargetClass() {
        return Modifier.class;
    }

    @Override
    public void mutate(Modifier modifier, JsonElement data) {
        int colour = Integer.parseInt(data.getAsString(), 16);
        TconReflect.setModColour(modifier, colour);
        for (ModifierAspect aspect : TconReflect.getAspects(modifier)) {
            if (aspect instanceof ModifierAspect.DataAspect) {
                TconReflect.setAspectColour((ModifierAspect.DataAspect)aspect, colour);
            } else if (aspect instanceof ModifierAspect.MultiAspect) {
                TconReflect.setAspectColour(TconReflect.extractDataAspect((ModifierAspect.MultiAspect)aspect), colour);
            }
        }
    }

}
