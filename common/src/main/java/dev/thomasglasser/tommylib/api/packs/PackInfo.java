package dev.thomasglasser.tommylib.api.packs;

import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import java.util.Objects;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.KnownPack;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;

/**
 * Represents a pack shown in the pack selection screen.
 * 
 * @param knownPack The pack information
 * @param type      The pack side (resource or data)
 * @param source    The pack source (built-in, feature, etc.)
 */
public record PackInfo(KnownPack knownPack, PackType type, PackSource source) {

    /**
     * A built-in pack that is not automatically enabled
     */
    public static final PackSource BUILT_IN_OPTIONAL = PackSource.create(PackSource.BUILT_IN::decorate, false);

    /**
     * A pack source for features that aren't experiments so shouldn't be in the experiments screen
     */
    public static final PackSource FEATURE_NOT_EXPERIMENT = PackSource.create(PackSource.FEATURE::decorate, false);

    /**
     * The default pack selection config for built-in packs.
     */
    public static final PackSelectionConfig BUILT_IN_SELECTION_CONFIG = new PackSelectionConfig(false, Pack.Position.TOP, false);
    /**
     * Creates a new {@link PackInfo} instance based on the provided parameters.
     *
     * @param namespace The namespace of the pack
     * @param id        The identifier of the pack
     * @param type      The type of the pack (data or resource)
     * @param source    The source of the pack (e.g., built-in, feature)
     * @return A new {@link PackInfo} instance containing the specified parameters
     * @throws NullPointerException If the mod version for the specified namespace is null
     */
    public static PackInfo create(String namespace, String id, PackType type, PackSource source) {
        return new PackInfo(new KnownPack(namespace, id, Objects.requireNonNull(TommyLibServices.PLATFORM.getModVersion(namespace))), type, source);
    }

    /**
     * Gets the pack's default file path in the project.
     *
     * @return The default file path for the pack
     */
    public String path() {
        return "packs/" + this.knownPack.namespace() + "/" + this.knownPack.id();
    }

    /**
     * Gets the translation key for the pack's title.
     * 
     * @return The translation key for the pack's title
     */
    public String titleKey() {
        return key() + ".name";
    }

    /**
     * Gets the translation key for the pack's description.
     * 
     * @return The translation key for the pack's description
     */
    public String descriptionKey() {
        return key() + ".description";
    }

    /**
     * Gets the pack's base translation key.
     * 
     * @return The base translation key for the pack
     */
    private String key() {
        return "pack." + this.knownPack.namespace() + "." + this.knownPack.id();
    }
}
