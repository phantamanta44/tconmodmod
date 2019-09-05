package xyz.phanta.tconmodmod;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.tools.modifiers.ModExtraTrait;
import xyz.phanta.tconmodmod.conarm.ConArmHandler;
import xyz.phanta.tconmodmod.conarm.ConArmHandlerImpl;
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
import java.util.function.Function;

public class CommonProxy {

    private static final TypeToken<List<ModifierEntry>> TYPE_MUT_LIST = new TypeToken<List<ModifierEntry>>() {
        // NO-OP
    };
    private static final Gson GSON = new Gson();

    @Nullable
    private List<ModifierEntry> configTcon;
    @Nullable
    private List<ModifierEntry> configConArm;

    @Nullable
    private ConArmHandler conArmHandler = null;

    public void onPreInit(FMLPreInitializationEvent event) {
        Path configDir = event.getModConfigurationDirectory().toPath();
        configTcon = loadConfig(configDir, "tconmodmod.json", "base Tinkers' Construct");
        if (getConArm().isConArmLoaded()) {
            configConArm = loadConfig(configDir, "tconmodmod_conarm.json", "Construct's Armoury");
        }
        ModifierMutator.init(event.getAsmData());
    }

    @Nullable
    private static List<ModifierEntry> loadConfig(Path configDir, String fileName, String name) {
        Path configFile = configDir.resolve(fileName);
        if (Files.exists(configFile)) {
            TconModMod.LOGGER.info("Loading {} config...", name);
            try (Reader in = Files.newBufferedReader(configFile)) {
                return GSON.fromJson(in, TYPE_MUT_LIST.getType());
            } catch (Exception e) {
                throw new IllegalStateException("Couldn't load " + name + " config!", e);
            }
        } else {
            TconModMod.LOGGER.info("No {} config found.", name);
            return null;
        }
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
            getConArm().logModifiers();
        }
        if (TMMConfig.reinforcedConfig.replaceReinforced) {
            TconModMod.LOGGER.info("Applying \"reinforced\" replacement...");
            TconReflect.getModifierRegistry().remove("reinforced");
            TconReflect.getTraitRegistry().remove("reinforced");
            new ModReinforcedTMM(); // tcon registers modifiers/traits in the superconstructor
        }
        getConArm().doPreMutation();
        if (configTcon != null) {
            applyMutation("base Tinkers' Construct", false, configTcon, TinkerRegistry::getModifier);
        }
        if (configConArm != null) {
            applyMutation("Construct's Armoury", true, configConArm, getConArm().getModifierRegistry());
        }
        if (TMMConfig.globalUnlimitedEmboss) {
            TconModMod.LOGGER.info("Applying global unlimited emboss...");
            for (IModifier mod : TinkerRegistry.getAllModifiers()) {
                if (mod instanceof ModExtraTrait) {
                    TconReflect.getAspects((ModExtraTrait)mod).removeIf(TconReflect::instanceOfExtraTraitAspect);
                }
            }
        }
        getConArm().doPostMutation();
    }

    private static void applyMutation(String name, boolean conArm,
                                      List<ModifierEntry> mutations, Function<String, ? extends IModifier> registry) {
        TconModMod.LOGGER.info("Applying modifications for {}...", name);
        Map<String, ModifierMutator<?>> targetCache = new HashMap<>();
        for (ModifierEntry modEntry : mutations) {
            for (String target : modEntry.getTargets()) {
                ModifierMutator<?> mutator = targetCache.get(target);
                if (mutator == null) {
                    IModifier mod = registry.apply(target);
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
                        mutator.tryMutate(mutEntry, conArm);
                    } catch (Exception e) {
                        TconModMod.LOGGER.warn("Mutation failed! Moving on...", e);
                    }
                }
            }
        }
    }

    public ConArmHandler getConArm() {
        if (conArmHandler == null) {
            if (Loader.isModLoaded("conarm")) {
                conArmHandler = new ConArmHandlerImpl();
            } else {
                conArmHandler = new ConArmHandler.Noop();
            }
        }
        return conArmHandler;
    }

}
