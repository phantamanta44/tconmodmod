package xyz.phanta.tconmodmod.mutator.impl;

import com.google.gson.JsonElement;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import xyz.phanta.tconmodmod.mutator.MutationStrategy;
import xyz.phanta.tconmodmod.util.TconReflect;

import java.util.Iterator;
import java.util.List;

@MutationStrategy.Register
public class MutationModSlots implements MutationStrategy<Modifier> {

    @Override
    public String getIdentifier() {
        return "mod_slots";
    }

    @Override
    public Class<Modifier> getTargetClass() {
        return Modifier.class;
    }

    @Override
    public void mutate(Modifier modifier, JsonElement data) {
        ModifierAspect.FreeModifierAspect newAspect = ModSlotType.valueOf(data.getAsString().toUpperCase()).newAspect(modifier);
        List<ModifierAspect> aspects = TconReflect.getAspects(modifier);
        Iterator<ModifierAspect> modIter = aspects.iterator();
        while (modIter.hasNext()) {
            ModifierAspect aspect = modIter.next();
            if (aspect instanceof ModifierAspect.FreeModifierAspect) {
                modIter.remove();
                aspects.add(newAspect);
                break;
            } else if (aspect instanceof ModifierAspect.MultiAspect) {
                TconReflect.injectFreeModifierAspect((ModifierAspect.MultiAspect)aspect, newAspect);
            }
        }
    }

    private enum ModSlotType {

        NONE {
            @Override
            ModifierAspect.FreeModifierAspect newAspect(IModifier modifier) {
                return new ModifierAspect.FreeModifierAspect(0);
            }
        },
        FIRST {
            @Override
            ModifierAspect.FreeModifierAspect newAspect(IModifier modifier) {
                return new ModifierAspect.FreeFirstModifierAspect(modifier, 1);
            }
        },
        ALL {
            @Override
            ModifierAspect.FreeModifierAspect newAspect(IModifier modifier) {
                return (ModifierAspect.FreeModifierAspect)ModifierAspect.freeModifier;
            }
        };

        abstract ModifierAspect.FreeModifierAspect newAspect(IModifier modifier);

    }

}
