package xyz.phanta.tconmodmod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = TconModMod.MOD_ID, version = TconModMod.VERSION, useMetadata = true)
public class TconModMod {

    public static final String MOD_ID = "tconmodmod";
    public static final String VERSION = "1.0.4";

    @SuppressWarnings("NullableProblems")
    @Mod.Instance(MOD_ID)
    public static TconModMod INSTANCE;

    @SuppressWarnings("NullableProblems")
    @SidedProxy(
            clientSide = "xyz.phanta.tconmodmod.client.ClientProxy",
            serverSide = "xyz.phanta.tconmodmod.CommonProxy")
    public static CommonProxy PROXY;

    @SuppressWarnings("NullableProblems")
    public static Logger LOGGER;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();
        PROXY.onPreInit(event);
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        PROXY.onInit(event);
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        PROXY.onPostInit(event);
    }

}
