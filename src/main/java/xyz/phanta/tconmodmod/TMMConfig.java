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

}
