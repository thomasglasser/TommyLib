package dev.thomasglasser.tommylib.impl;

import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TommyLib {
    public static final String MOD_ID = "tommylib";
    public static final String MOD_NAME = "TommyLib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        LOGGER.info("Initializing {} for {} in a {} environment...", MOD_NAME, TommyLibServices.PLATFORM.getPlatformName(), TommyLibServices.PLATFORM.getEnvironmentName());
    }

    public static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public enum Dependencies {
        GECKOLIB("geckolib"),
        PLAYERANIMATOR("playeranimator");

        private final String modId;

        Dependencies(String modId) {
            this.modId = modId;
        }

        public String getModId() {
            return modId;
        }

        public boolean isLoaded() {
            return TommyLibServices.PLATFORM.isModLoaded(getModId());
        }
    }
}
