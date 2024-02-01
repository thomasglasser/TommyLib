package dev.thomasglasser.tommylib.impl.data;

import dev.thomasglasser.tommylib.api.data.tags.ExtendedBlockTagsProvider;
import dev.thomasglasser.tommylib.impl.data.tags.TommyLibBlockTagsProvider;
import dev.thomasglasser.tommylib.impl.data.tags.TommyLibItemTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class TommyLibDataGenerators
{
	public static void onGatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		boolean server = event.includeServer();
		boolean client = event.includeClient();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		// Server
		ExtendedBlockTagsProvider blockTagsProvider = new TommyLibBlockTagsProvider(packOutput, lookupProvider, existingFileHelper);
		generator.addProvider(server, blockTagsProvider);
		generator.addProvider(server, new TommyLibItemTagsProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));

		// Client
	}
}
