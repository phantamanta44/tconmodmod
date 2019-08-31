package xyz.phanta.tconmodmod;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.tools.modifiers.ModExtraTrait;
import xyz.phanta.tconmodmod.model.ModifierEntry;
import xyz.phanta.tconmodmod.model.MutationEntry;
import xyz.phanta.tconmodmod.modifier.ModReinforcedTMM;
import xyz.phanta.tconmodmod.mutator.ModifierMutator;
import xyz.phanta.tconmodmod.util.TconReflect;

import javax.annotation.Nullable;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonProxy {

    private static final TypeToken<List<ModifierEntry>> TYPE_MUT_LIST = new TypeToken<List<ModifierEntry>>() {
        // NO-OP
    };

    @Nullable
    private List<ModifierEntry> config = null;

    public void onPreInit(FMLPreInitializationEvent event) {
        Path confFile = event.getModConfigurationDirectory().toPath().resolve("tconmodmod.json");
        if (Files.exists(confFile)) {
            TconModMod.LOGGER.info("Loading config...");
            try (Reader in = Files.newBufferedReader(confFile)) {
                config = new Gson().fromJson(in, TYPE_MUT_LIST.getType());
            } catch (Exception e) {
                throw new IllegalStateException("Couldn't load config!", e);
            }
        } else {
            TconModMod.LOGGER.warn("No config found!");
            TconModMod.LOGGER.warn("See mod documentation for more details on proper configuration.");
        }
        ModifierMutator.init(event.getAsmData());
    }

    public void onInit(FMLInitializationEvent event) {
        // NO-OP
    }

    public void onPostInit(FMLPostInitializationEvent event) {
        if (TMMConfig.logModifiers) {
            TconModMod.LOGGER.info("Modifier logging was requested. This can be disabled in the config!");
            TinkerRegistry.getAllModifiers().stream()
                    .sorted(Comparator.comparing(IModifier::getIdentifier))
                    .distinct()
                    .forEach(m -> TconModMod.LOGGER.info("- {} = {} ({})",
                            m.getIdentifier(), m.getLocalizedName(), m.getClass().getCanonicalName()));
        }
        if (TMMConfig.reinforcedConfig.replaceReinforced) {
            TconModMod.LOGGER.info("Applying \"reinforced\" replacement...");
            TconReflect.getModifierRegistry().remove("reinforced");
            TconReflect.getTraitRegistry().remove("reinforced");
            new ModReinforcedTMM(); // tcon registers modifiers/traits in the superconstructor
        }
        if (config != null) {
            TconModMod.LOGGER.info("Applying modifications...");
            Map<String, ModifierMutator<?>> targetCache = new HashMap<>();
            for (ModifierEntry modEntry : config) {
                for (String target : modEntry.getTargets()) {
                    ModifierMutator<?> mutator = targetCache.get(target);
                    if (mutator == null) {
                        IModifier mod = TinkerRegistry.getModifier(target);
                        if (mod != null) {
                            mutator = new ModifierMutator<>(mod);
                            targetCache.put(target, mutator);
                        } else {
                            TconModMod.LOGGER.warn("Skipping unknown modifier: {}", target);
                            continue;
                        }
                    }
                    for (MutationEntry mutEntry : modEntry.getMutations()) {
                        TconModMod.LOGGER.info("Applying mutation: {} -> {}", mutEntry.getType(), target);
                        try {
                            mutator.tryMutate(mutEntry);
                        } catch (Exception e) {
                            TconModMod.LOGGER.warn("Mutation failed! Moving on...", e);
                        }
                    }
                }
            }
        }
        if (TMMConfig.globalUnlimitedEmboss) {
            TconModMod.LOGGER.info("Applying global unlimited emboss...");
            for (IModifier mod : TinkerRegistry.getAllModifiers()) {
                if (mod instanceof ModExtraTrait) {
                    TconReflect.getAspects((ModExtraTrait)mod).removeIf(TconReflect::instanceOfExtraTraitAspect);
                }
            }
        }
    }

}
