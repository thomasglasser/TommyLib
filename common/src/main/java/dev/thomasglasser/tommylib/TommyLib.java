package dev.thomasglasser.tommylib;

import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import dev.thomasglasser.tommylib.impl.network.TommyLibPayloads;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TommyLib
{
    public static final String MOD_ID = "tommylib";
    public static final String MOD_NAME = "TommyLib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        LOGGER.info("Initializing {} for {} in a {} environment...", MOD_NAME, TommyLibServices.PLATFORM.getPlatformName(), TommyLibServices.PLATFORM.getEnvironmentName());

        TommyLibPayloads.init();
    }

    public static ResourceLocation modLoc(String s)
    {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, s);
    }
}