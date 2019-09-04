package xyz.phanta.tconmodmod;

import net.minecraftforge.common.config.Config;

@Config(modid = TconModMod.MOD_ID)
public class TMMConfig {

    @Config.Comment({
            "Whether to log all Tinkers' modifiers on startup or not.",
            "May be useful for modpack developers, but is not recommended for normal gameplay!"
    })
    public static boolean logModifiers = false;

    @Config.Comment({
            "Enabling this causes all embossments to become unlimited."
    })
    public static boolean globalUnlimitedEmboss = false;

    @Config.Comment({
            "Enabling this causes all Construct's Armoury armour embossments to become unlimited."
    })
    public static boolean globalUnlimitedEmbossConArm = false;

    public static final Reinforced reinforcedConfig = new Reinforced();

    public static class Reinforced {

        @Config.Comment({
                "Enabling this causes TMM to replace the default \"reinforced\" modifier with its own version.",
                "This allows TMM to modify certain properties that would otherwise be unmodifiable."
        })
        public boolean replaceReinforced = true;

        @Config.Comment({
                "The chance to not damage the tool per level of \"reinforced\".",
                "Requires replaceReinforced to be enabled!"
        })
        public double chancePerLevel = 0.2D;

    }

    public static final ReinforcedConArm reinforcedConArmConfig = new ReinforcedConArm();

    public static class ReinforcedConArm {

        @Config.Comment({
                "Enabling this causes TMM to replace the Construct's Armoury \"reinforced_armor\" modifier with its own version.",
                "This allows TMM to modify certain properties that would otherwise be unmodifiable."
        })
        public boolean replaceReinforced = true;

        @Config.Comment({
                "The chance to not damage the armour per level of \"reinforced_armor\".",
                "Requires replaceReinforced to be enabled!"
        })
        public double chancePerLevel = 0.2D;

    }

}
