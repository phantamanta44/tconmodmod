package xyz.phanta.tconmodmod.model;

import com.google.gson.JsonElement;

@SuppressWarnings("NullableProblems")
public class MutationEntry {

    private String type;
    private JsonElement value;

    public String getType() {
        return type;
    }

    public JsonElement getValue() {
        return value;
    }

}
