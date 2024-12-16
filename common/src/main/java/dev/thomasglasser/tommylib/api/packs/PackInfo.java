package dev.thomasglasser.tommylib.api.packs;

import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.KnownPack;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;

/**
 * Represents a pack that is shown in the pack selection screen.
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
     * Gets the pack's name.
     * 
     * @return The translation key for the pack's name
     */
    public String titleKey() {
        return key() + ".name";
    }

    /**
     * Gets the pack's description.
     * 
     * @return The translation key for the pack's description
     */
    public String descriptionKey() {
        return key() + ".description";
    }

    /**
     * Gets the pack's default key.
     * 
     * @return The translation key for the pack
     */
    private String key() {
        return "pack." + this.knownPack.namespace() + "." + this.knownPack.id();
    }
}
