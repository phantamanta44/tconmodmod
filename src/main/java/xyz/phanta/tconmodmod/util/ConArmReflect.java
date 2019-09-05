package xyz.phanta.tconmodmod.util;

import c4.conarm.common.armor.modifiers.ModExtraArmorTrait;
import c4.conarm.lib.ArmoryRegistry;
import c4.conarm.lib.modifiers.ArmorModifierTrait;
import slimeknights.tconstruct.library.modifiers.IModifier;
import xyz.phanta.tconmodmod.TconModMod;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class ConArmReflect {

    @Nullable
    private static final Class<?> cModExtraArmorTrait_ExtraTraitAspect;
    @Nullable
    private static final Field fArmoryRegistry_armorModifiers;
    @Nullable
    private static final Field fArmorModifierTrait_maxLevel;

    static {
        try {
            if (TconModMod.PROXY.getConArm().isConArmLoaded()) {
                cModExtraArmorTrait_ExtraTraitAspect = Class.forName(ModExtraArmorTrait.class.getCanonicalName() + "$ExtraTraitAspect");
                fArmoryRegistry_armorModifiers = ArmoryRegistry.class.getDeclaredField("armorModifiers");
                fArmoryRegistry_armorModifiers.setAccessible(true);
                fArmorModifierTrait_maxLevel = ArmorModifierTrait.class.getDeclaredField("maxLevel");
                TconReflect.unfinal(fArmorModifierTrait_maxLevel);
                fArmorModifierTrait_maxLevel.setAccessible(true);
            } else {
                cModExtraArmorTrait_ExtraTraitAspect = null;
                fArmoryRegistry_armorModifiers = null;
                fArmorModifierTrait_maxLevel = null;
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize conarm reflection!", e);
        }
    }

    public static boolean instanceOfExtraTraitAspect(Object o) {
        return Objects.requireNonNull(cModExtraArmorTrait_ExtraTraitAspect).isInstance(o);
    }

    public static Map<String, IModifier> getModifierRegistry() {
        try {
            return (Map<String, IModifier>)Objects.requireNonNull(fArmoryRegistry_armorModifiers).get(null);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to read ArmoryRegistry.armorModifiers!", e);
        }
    }

    public static void setMaxLevel(ArmorModifierTrait trait, int maxLevel) {
        try {
            Objects.requireNonNull(fArmorModifierTrait_maxLevel).setInt(trait, maxLevel);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to write ArmorModifierTrait.maxLevel!", e);
        }
    }

}
