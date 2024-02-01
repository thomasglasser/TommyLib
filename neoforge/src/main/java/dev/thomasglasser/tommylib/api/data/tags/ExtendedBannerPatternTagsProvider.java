package dev.thomasglasser.tommylib.api.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BannerPatternTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ExtendedBannerPatternTagsProvider extends BannerPatternTagsProvider {
    public ExtendedBannerPatternTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, future, modId, existingFileHelper);
    }

    protected ResourceKey<BannerPattern> modKey(BannerPattern pattern)
    {
        return ResourceKey.create(Registries.BANNER_PATTERN, new ResourceLocation(modId, pattern.getHashname()));
    }
}