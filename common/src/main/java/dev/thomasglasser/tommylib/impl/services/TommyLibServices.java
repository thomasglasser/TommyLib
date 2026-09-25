package dev.thomasglasser.tommylib.impl.services;

import dev.thomasglasser.tommylib.api.TommyLibConstants;
import java.util.ServiceLoader;

public class TommyLibServices {
    public static final RegistrationService REGISTRATION = load(RegistrationService.class);

    private static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        TommyLibConstants.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
