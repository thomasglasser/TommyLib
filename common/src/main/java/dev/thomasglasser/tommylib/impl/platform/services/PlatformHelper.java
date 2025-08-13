package dev.thomasglasser.tommylib.impl.platform.services;

import org.jetbrains.annotations.Nullable;

/**
 * Mod loader related helpers
 */
public interface PlatformHelper {
    /**
     * Gets the name of the current platform.
     *
     * @return The name of the current platform
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded
     * @return Whether the mod is loaded
     */
    boolean isModLoaded(String modId);

    /**
     * Checks if the game is in a development environment.
     *
     * @return Whether the game is in a development environment
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment.
     *
     * @return The name of the environment
     */
    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }

    /**
     * Checks whether the mod is running on the client side.
     * 
     * @return Whether the mod is running on the client side
     */
    boolean isClientSide();

    /**
     * Gets the version of the mod with the specified ID
     * 
     * @param modId The mod ID to check
     * @return The version of the mod with the specified ID, or null if it is not loaded
     */
    @Nullable
    String getModVersion(String modId);
}
