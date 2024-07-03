package dev.thomasglasser.tommylib.api.tags;

import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class TagUtils {
    /**
     * Registers a conventional tag for the given registry.
     * 
     * @param registry the registry the tag is for
     * @param tagId    the id of the tag
     * @return the tag key
     */
    public static <T> TagKey<T> registerConventional(ResourceKey<? extends Registry<T>> registry, String tagId) {
        return TagKey.create(registry, ResourceLocation.fromNamespaceAndPath("c", tagId));
    }

    /**
     * Creates a tag key for logs of the given {@link WoodSet}.
     * 
     * @param registry the registry to create the tag key for
     * @param set      the wood set to create the tag key for
     * @return the tag key
     * @param <T> the type of the registry
     */
    public static <T> TagKey<T> logs(ResourceKey<Registry<T>> registry, WoodSet set) {
        return TagKey.create(registry, ResourceLocation.fromNamespaceAndPath(set.id().getNamespace(), set.id().getPath() + "_logs"));
    }
}
