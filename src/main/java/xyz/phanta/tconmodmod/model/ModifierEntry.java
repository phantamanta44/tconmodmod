package xyz.phanta.tconmodmod.model;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NullableProblems")
public class ModifierEntry {

    @Nullable
    private String modifier = null;
    @Nullable
    private List<String> modifiers = null;
    private List<MutationEntry> mutations;

    public List<String> getTargets() {
        List<String> targets = new ArrayList<>();
        if (modifiers != null) {
            targets.addAll(modifiers);
        }
        if (modifier != null) {
            targets.add(modifier);
        }
        return targets;
    }

    public List<MutationEntry> getMutations() {
        return mutations;
    }

}
