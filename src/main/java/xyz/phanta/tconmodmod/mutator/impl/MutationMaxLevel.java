package xyz.phanta.tconmodmod.mutator.impl;

import com.google.gson.JsonElement;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;
import xyz.phanta.tconmodmod.mutator.MutationStrategy;
import xyz.phanta.tconmodmod.util.TconReflect;

import java.lang.reflect.Field;

@MutationStrategy.Register
public class MutationMaxLevel implements MutationStrategy<Modifier> {

    @Override
    public String getIdentifier() {
        return "max_level";
    }

    @Override
    public Class<Modifier> getTargetClass() {
        return Modifier.class;
    }

    @Override
    public void mutate(Modifier modifier, JsonElement data) {
        int maxLevel = data.getAsInt();
        try {
            Field fMax = modifier.getClass().getDeclaredField("max");
            TconReflect.unfinal(fMax);
            fMax.setAccessible(true);
            fMax.setInt(modifier, maxLevel);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to write " + modifier.getClass() + ".max!");
        }
        if (modifier instanceof ModifierTrait) {
            TconReflect.setMaxLevel((ModifierTrait)modifier, maxLevel);
        }
        for (ModifierAspect aspect : TconReflect.getAspects(modifier)) {
            if (aspect instanceof ModifierAspect.LevelAspect) {
                TconReflect.setMaxLevel((ModifierAspect.LevelAspect)aspect, maxLevel);
            } else if (aspect instanceof ModifierAspect.MultiAspect) {
                TconReflect.setMaxLevel(TconReflect.extractLevelAspect((ModifierAspect.MultiAspect)aspect), maxLevel);
            }
        }
    }

}