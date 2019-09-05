package xyz.phanta.tconmodmod.conarm;

import c4.conarm.common.armor.modifiers.ModExtraArmorTrait;
import c4.conarm.lib.ArmoryRegistry;
import slimeknights.tconstruct.library.modifiers.IModifier;
import xyz.phanta.tconmodmod.TMMConfig;
import xyz.phanta.tconmodmod.TconModMod;
import xyz.phanta.tconmodmod.modifier.ModReinforcedConArmTMM;
import xyz.phanta.tconmodmod.util.ConArmReflect;
import xyz.phanta.tconmodmod.util.TconReflect;

import java.util.Comparator;
import java.util.function.Function;

public class ConArmHandlerImpl implements ConArmHandler {

    @Override
    public boolean isConArmLoaded() {
        return true;
    }

    @Override
    public Function<String, ? extends IModifier> getModifierRegistry() {
        return ArmoryRegistry::getArmorModifier;
    }

    @Override
    public void doPreMutation() {
        if (TMMConfig.reinforcedConArmConfig.replaceReinforced) {
            TconModMod.LOGGER.info("Applying \"reinforced_armor\" replacement...");
            TconReflect.getModifierRegistry().remove("reinforced_armor");
            ConArmReflect.getModifierRegistry().remove("reinforced_armor");
            TconReflect.getTraitRegistry().remove("reinforced_armor");
            new ModReinforcedConArmTMM();
        }
    }

    @Override
    public void doPostMutation() {
        if (TMMConfig.globalUnlimitedEmbossConArm) {
            TconModMod.LOGGER.info("Applying Construct's Armoury global unlimited emboss...");
            for (IModifier mod : ArmoryRegistry.getAllArmorModifiers()) {
                if (mod instanceof ModExtraArmorTrait) {
                    TconReflect.getAspects((ModExtraArmorTrait)mod).removeIf(ConArmReflect::instanceOfExtraTraitAspect);
                }
            }
        }
    }

    @Override
    public void logModifiers() {
        TconModMod.LOGGER.info("Construct's Armoury modifiers:");
        ArmoryRegistry.getAllArmorModifiers().stream()
                .sorted(Comparator.comparing(IModifier::getIdentifier))
                .distinct()
                .forEach(m -> TconModMod.LOGGER.info("- {} = {} ({})",
                        m.getIdentifier(), m.getLocalizedName(), m.getClass().getCanonicalName()));
    }

}
