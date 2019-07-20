package xyz.phanta.tconmodmod.model;

import java.util.List;

@SuppressWarnings("NullableProblems")
public class ModifierEntry {

    private String modifier;
    private List<MutationEntry> mutations;

    public String getModifier() {
        return modifier;
    }

    public List<MutationEntry> getMutations() {
        return mutations;
    }

}
