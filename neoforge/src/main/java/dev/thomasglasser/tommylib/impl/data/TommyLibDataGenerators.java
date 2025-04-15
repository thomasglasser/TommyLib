package dev.thomasglasser.tommylib.impl.data;

import dev.thomasglasser.tommylib.TommyLib;
import dev.thomasglasser.tommylib.api.data.info.ModRegistryDumpReport;
import dev.thomasglasser.tommylib.impl.data.lang.TommyLibEnUsLanguageProvider;
import dev.thomasglasser.tommylib.impl.data.tags.TommyLibBlockTagsProvider;
import dev.thomasglasser.tommylib.impl.data.tags.TommyLibItemTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class TommyLibDataGenerators {
    public static void onGatherData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();

        // Server
        event.createProvider((output, lookupProvider) -> new ModRegistryDumpReport(packOutput, TommyLib.MOD_ID, lookupProvider));
        event.createBlockAndItemTags(TommyLibBlockTagsProvider::new, TommyLibItemTagsProvider::new);

        // Client
        event.createProvider(TommyLibEnUsLanguageProvider::new);
    }
}
