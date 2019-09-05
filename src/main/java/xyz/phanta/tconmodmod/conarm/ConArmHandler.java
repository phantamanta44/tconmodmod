package xyz.phanta.tconmodmod.conarm;

import slimeknights.tconstruct.library.modifiers.IModifier;

import java.util.function.Function;

public interface ConArmHandler {

    boolean isConArmLoaded();

    Function<String, ? extends IModifier> getModifierRegistry();

    void doPreMutation();

    void doPostMutation();

    void logModifiers();

    class Noop implements ConArmHandler {

        @Override
        public boolean isConArmLoaded() {
            return false;
        }

        @Override
        public Function<String, ? extends IModifier> getModifierRegistry() {
            return id -> null;
        }

        @Override
        public void doPreMutation() {
            // NO-OP
        }

        @Override
        public void doPostMutation() {
            // NO-OP
        }

        @Override
        public void logModifiers() {
            // NO-OP
        }

    }

}
