package xyz.phanta.tconmodmod.mutator;

import net.minecraftforge.fml.common.discovery.ASMDataTable;
import org.apache.logging.log4j.message.Message;
import slimeknights.tconstruct.library.modifiers.IModifier;
import xyz.phanta.tconmodmod.TconModMod;
import xyz.phanta.tconmodmod.model.MutationEntry;

import java.util.HashMap;
import java.util.Map;

public class ModifierMutator<M extends IModifier> {

    private static final Map<String, MutationStrategy<?>> mutationTypes = new HashMap<>();

    public static void init(ASMDataTable asm) {
        for (ASMDataTable.ASMData annot : asm.getAll("xyz.phanta.tconmodmod.mutator.MutationStrategy$Register")) {
            try {
                MutationStrategy<?> strategy = (MutationStrategy<?>)Class.forName(annot.getClassName()).newInstance();
                mutationTypes.put(strategy.getIdentifier(), strategy);
            } catch (Exception e) {
                TconModMod.LOGGER.warn("Failed to load mutation strategy: " + annot.getClassName(), e);
            }
        }
    }

    private final M mod;

    public ModifierMutator(M mod) {
        this.mod = mod;
    }

    public void tryMutate(MutationEntry mutation) {
        MutationStrategy<?> strategy = mutationTypes.get(mutation.getType());
        if (strategy != null) {
            if (strategy.getTargetClass().isAssignableFrom(mod.getClass())) {
                try {
                    //noinspection unchecked
                    ((MutationStrategy<M>)strategy).mutate(mod, mutation.getValue());
                } catch (Exception e) {
                    Message message = TconModMod.LOGGER.getMessageFactory()
                            .newMessage("Mutation {} failed for modifier {}", mutation.getType(), mod.getIdentifier());
                    TconModMod.LOGGER.warn(message, e);
                }
            } else {
                TconModMod.LOGGER.warn("Skipping incompatible mutation {} for modifier {}", mutation.getType(), mod.getIdentifier());
            }
        } else {
            TconModMod.LOGGER.warn("Unknown mutation type: {}", mutation.getType());
        }
    }

}
