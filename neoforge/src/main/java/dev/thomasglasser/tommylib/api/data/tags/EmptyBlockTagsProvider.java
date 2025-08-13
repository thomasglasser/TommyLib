package dev.thomasglasser.tommylib.api.data.tags;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public final class EmptyBlockTagsProvider extends ExtendedBlockTagsProvider {
    public EmptyBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, "", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {}
}
