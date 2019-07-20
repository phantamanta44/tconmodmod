package xyz.phanta.tconmodmod;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.modifiers.IModifier;
import xyz.phanta.tconmodmod.model.ModifierEntry;
import xyz.phanta.tconmodmod.model.MutationEntry;
import xyz.phanta.tconmodmod.mutator.ModifierMutator;

import javax.annotation.Nullable;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

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
        if (config != null) {
            TconModMod.LOGGER.info("Applying modifications...");
            for (ModifierEntry modEntry : config) {
                IModifier mod = TinkerRegistry.getModifier(modEntry.getModifier());
                if (mod != null) {
                    TconModMod.LOGGER.info("Modifying: {}", mod.getIdentifier());
                    ModifierMutator<?> mutator = new ModifierMutator<>(mod);
                    for (MutationEntry mutEntry : modEntry.getMutations()) {
                        mutator.tryMutate(mutEntry);
                    }
                } else {
                    TconModMod.LOGGER.warn("Skipping unknown modifier: {}", modEntry.getModifier());
                }
            }
        }
    }

}
