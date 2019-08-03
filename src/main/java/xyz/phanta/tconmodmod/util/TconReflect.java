package xyz.phanta.tconmodmod.util;

import slimeknights.mantle.util.RecipeMatch;
import slimeknights.mantle.util.RecipeMatchRegistry;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.tools.modifiers.ModExtraTrait;
import slimeknights.tconstruct.tools.modifiers.ToolModifier;

import java.lang.reflect.Field;
import java.util.List;
import java.util.PriorityQueue;

@SuppressWarnings("unchecked")
public class TconReflect {

    private static final Field fField_modifiers;

    private static final Field fModifier_aspects;
    private static final Field fRecipeMatchRegistry_items;

    private static final Field fMultiAspect_dataAspect;
    private static final Field fMultiAspect_levelAspect;

    private static final Field fDataAspect_color;
    private static final Field fAbstractTrait_color;
    private static final Field fToolModifier_color;

    private static final Field fMultiAspect_countPerLevel;

    private static final Field fLevelAspect_maxLevel;
    private static final Field fModifierTrait_maxLevel;

    private static final Class cModExtraTrait_ExtraTraitAspect;

    static {
        try {
            fField_modifiers = Field.class.getDeclaredField("modifiers");
            fField_modifiers.setAccessible(true);

            fModifier_aspects = Modifier.class.getDeclaredField("aspects");
            fModifier_aspects.setAccessible(true);
            fRecipeMatchRegistry_items = RecipeMatchRegistry.class.getDeclaredField("items");
            fRecipeMatchRegistry_items.setAccessible(true);

            fMultiAspect_dataAspect = ModifierAspect.MultiAspect.class.getDeclaredField("dataAspect");
            fMultiAspect_dataAspect.setAccessible(true);
            fMultiAspect_levelAspect = ModifierAspect.MultiAspect.class.getDeclaredField("levelAspect");
            fMultiAspect_levelAspect.setAccessible(true);

            fDataAspect_color = ModifierAspect.DataAspect.class.getDeclaredField("color");
            unfinal(fDataAspect_color);
            fDataAspect_color.setAccessible(true);
            fAbstractTrait_color = AbstractTrait.class.getDeclaredField("color");
            fAbstractTrait_color.setAccessible(true);
            fToolModifier_color = ToolModifier.class.getDeclaredField("color");
            fToolModifier_color.setAccessible(true);

            fMultiAspect_countPerLevel = ModifierAspect.MultiAspect.class.getDeclaredField("countPerLevel");
            unfinal(fMultiAspect_countPerLevel);
            fMultiAspect_countPerLevel.setAccessible(true);

            fLevelAspect_maxLevel = ModifierAspect.LevelAspect.class.getDeclaredField("maxLevel");
            unfinal(fLevelAspect_maxLevel);
            fLevelAspect_maxLevel.setAccessible(true);
            fModifierTrait_maxLevel = ModifierTrait.class.getDeclaredField("maxLevel");
            unfinal(fModifierTrait_maxLevel);
            fModifierTrait_maxLevel.setAccessible(true);

            cModExtraTrait_ExtraTraitAspect = Class.forName(ModExtraTrait.class.getCanonicalName() + "$ExtraTraitAspect");
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize tcon reflection!", e);
        }
    }

    public static void unfinal(Field field) {
        try {
            fField_modifiers.setInt(field, fField_modifiers.getInt(field) & ~java.lang.reflect.Modifier.FINAL);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to un-final-ize field: " + field, e);
        }
    }

    public static List<ModifierAspect> getAspects(Modifier mod) {
        try {
            return (List<ModifierAspect>)fModifier_aspects.get(mod);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to read Modifier.aspects!", e);
        }
    }

    public static PriorityQueue<RecipeMatch> getRecipeMatchers(RecipeMatchRegistry reg) {
        try {
            return (PriorityQueue<RecipeMatch>)fRecipeMatchRegistry_items.get(reg);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to read RecipeMatchRegistry.items!", e);
        }
    }

    public static ModifierAspect.DataAspect extractDataAspect(ModifierAspect.MultiAspect aspect) {
        try {
            return (ModifierAspect.DataAspect)fMultiAspect_dataAspect.get(aspect);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to read MultiAspect.dataAspect!", e);
        }
    }

    public static ModifierAspect.LevelAspect extractLevelAspect(ModifierAspect.MultiAspect aspect) {
        try {
            return (ModifierAspect.LevelAspect)fMultiAspect_levelAspect.get(aspect);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to read MultiAspect.levelAspect!", e);
        }
    }

    public static void setAspectColour(ModifierAspect.DataAspect aspect, int colour) {
        try {
            fDataAspect_color.setInt(aspect, colour);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to write DataAspect.color!", e);
        }
    }

    public static void setModColour(Object mod, int colour) {
        if (mod instanceof AbstractTrait) {
            try {
                fAbstractTrait_color.setInt(mod, colour);
            } catch (Exception e) {
                throw new IllegalStateException("Failed to write AbstractTrait.color!", e);
            }
        } else if (mod instanceof ToolModifier) {
            try {
                fToolModifier_color.setInt(mod, colour);
            } catch (Exception e) {
                throw new IllegalStateException("Failed to set ToolModifier.color!", e);
            }
        }
    }

    public static void setCountPerLevel(ModifierAspect.MultiAspect aspect, int count) {
        try {
            fMultiAspect_countPerLevel.setInt(aspect, count);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to write MultiAspect.countPerLevel!", e);
        }
    }

    public static void setMaxLevel(ModifierAspect.LevelAspect aspect, int maxLevel) {
        try {
            fLevelAspect_maxLevel.setInt(aspect, maxLevel);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to write LevelAspect.maxLevel!", e);
        }
    }

    public static void setMaxLevel(ModifierTrait trait, int maxLevel) {
        try {
            fModifierTrait_maxLevel.setInt(trait, maxLevel);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to write ModifierTrait.maxLevel!", e);
        }
    }

    public static boolean instanceOfExtraTraitAspect(Object o) {
        return cModExtraTrait_ExtraTraitAspect.isInstance(o);
    }

}
