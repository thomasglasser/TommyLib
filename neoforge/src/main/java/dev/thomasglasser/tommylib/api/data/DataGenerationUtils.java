package dev.thomasglasser.tommylib.api.data;

import com.mojang.datafixers.util.Pair;
import dev.thomasglasser.tommylib.api.data.advancements.ExtendedAdvancementProvider;
import dev.thomasglasser.tommylib.api.data.info.ModRegistryDumpReport;
import dev.thomasglasser.tommylib.api.data.tags.ExtendedBlockTagsProvider;
import dev.thomasglasser.tommylib.api.data.tags.ExtendedItemTagsProvider;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.Nullable;

public class DataGenerationUtils {
    public static <T extends DataProvider> T createProvider(GatherDataEvent event, DataProviderFromOutputExistingFileHelper<T> builder) {
        return event.addProvider(builder.create(event.getGenerator().getPackOutput(), event.getExistingFileHelper()));
    }

    public static <T extends DataProvider> T createProvider(GatherDataEvent event, DataProviderFromOutputLookupExistingFileHelper<T> builder) {
        return event.addProvider(builder.create(event.getGenerator().getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper()));
    }

    public static ModRegistryDumpReport createRegistryDumpReport(GatherDataEvent event, String modId) {
        return event.addProvider(new ModRegistryDumpReport(event.getGenerator().getPackOutput(), event.getLookupProvider(), modId));
    }

    public static <B extends ExtendedBlockTagsProvider, I extends ExtendedItemTagsProvider> Pair<B, I> createBlockAndItemTags(GatherDataEvent event, DataProviderFromOutputLookupExistingFileHelper<B> blockTagsProvider, ItemTagsWithExistingFileHelperProvider<I> itemTagsProvider) {
        B blockTags = createProvider(event, blockTagsProvider);
        I itemTags = itemTagsProvider.create(event.getGenerator().getPackOutput(), event.getLookupProvider(), blockTags.contentsGetter(), event.getExistingFileHelper());
        event.addProvider(itemTags);
        return Pair.of(blockTags, itemTags);
    }

    public static <T extends LanguageProvider> void createLangDependent(GatherDataEvent event, GatherDataEvent.DataProviderFromOutput<T> lang, DataProviderFromOutputLookupProviderLangExistingFileHelperProvider<? extends ExtendedAdvancementProvider> advancements, DataProviderFromOutputLookupProviderLang<?>... providers) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        T langProvider = lang.create(packOutput);
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        BiConsumer<String, String> add = langProvider::add;
        event.addProvider(advancements.create(packOutput, lookupProvider, add, event.getExistingFileHelper()));
        for (DataProviderFromOutputLookupProviderLang<?> provider : providers) {
            event.addProvider(provider.create(packOutput, lookupProvider, add));
        }
        event.addProvider(langProvider);
    }

    @FunctionalInterface
    public interface DataProviderFromOutputExistingFileHelper<T extends DataProvider> {
        T create(PackOutput output, @Nullable ExistingFileHelper existingFileHelper);
    }

    @FunctionalInterface
    public interface DataProviderFromOutputLookupExistingFileHelper<T extends DataProvider> {
        T create(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper);
    }

    @FunctionalInterface
    public interface ItemTagsWithExistingFileHelperProvider<T extends ExtendedItemTagsProvider> {
        T create(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper);
    }

    @FunctionalInterface
    public interface DataProviderFromOutputLookupProviderLang<T extends DataProvider> {
        T create(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, BiConsumer<String, String> lang);
    }

    @FunctionalInterface
    public interface DataProviderFromOutputLookupProviderLangExistingFileHelperProvider<T extends DataProvider> {
        T create(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, BiConsumer<String, String> lang, @Nullable ExistingFileHelper existingFileHelper);
    }
}
