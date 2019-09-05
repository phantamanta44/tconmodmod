package xyz.phanta.tconmodmod.mutator;

import com.google.gson.JsonElement;
import slimeknights.tconstruct.library.modifiers.IModifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface MutationStrategy<M extends IModifier> {

    String getIdentifier();

    Class<M> getTargetClass();

    void mutate(M modifier, JsonElement data);

    default void mutateConArm(M modifier, JsonElement data) {
        mutate(modifier, data);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Register {
        // NO-OP
    }

}
