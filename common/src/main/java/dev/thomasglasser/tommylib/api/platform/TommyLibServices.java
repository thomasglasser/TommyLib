package dev.thomasglasser.tommylib.api.platform;

import dev.thomasglasser.tommylib.TommyLib;
import dev.thomasglasser.tommylib.impl.platform.services.ClientHelper;
import dev.thomasglasser.tommylib.impl.platform.services.NetworkHelper;
import dev.thomasglasser.tommylib.impl.platform.services.PlatformHelper;
import java.util.ServiceLoader;

/**
 * Platform-specific helpers that aid in resolving loader differences
 */
public class TommyLibServices {
    public static final PlatformHelper PLATFORM = load(PlatformHelper.class);
    public static final NetworkHelper NETWORK = load(NetworkHelper.class);
    public static final ClientHelper CLIENT = load(ClientHelper.class);

    /**
     * Loads a service using a service file in the META-INF/services/ folder.
     * 
     * @param clazz The class of the service to load
     * @return The loaded service
     * @param <T> The type of the service.
     */
    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        TommyLib.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
