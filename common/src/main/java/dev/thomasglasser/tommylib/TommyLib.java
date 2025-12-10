package dev.thomasglasser.tommylib;

import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import dev.thomasglasser.tommylib.api.world.level.levelgen.feature.TommyLibFeatures;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TommyLib {
    public static final String MOD_NAMESPACE = "tommylib";
    public static final String MOD_NAME = "TommyLib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        LOGGER.info("Initializing {} for {} in a {} environment...", MOD_NAME, TommyLibServices.PLATFORM.getPlatformName(), TommyLibServices.PLATFORM.getEnvironmentName());

        TommyLibFeatures.init();
    }

    public static Identifier modId(String path) {
        return Identifier.fromNamespaceAndPath(MOD_NAMESPACE, path);
    }

    public enum Dependencies {
        GECKOLIB("geckolib"),
        PLAYERANIMATOR("playeranimator");

        private final String modNamespace;

        Dependencies(String modNamespace) {
            this.modNamespace = modNamespace;
        }

        public boolean isLoaded() {
            return TommyLibServices.PLATFORM.isModLoaded(modNamespace);
        }
    }
}
