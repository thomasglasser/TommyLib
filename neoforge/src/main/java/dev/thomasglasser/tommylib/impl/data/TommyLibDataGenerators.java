package dev.thomasglasser.tommylib.impl.data;

import dev.thomasglasser.tommylib.api.data.DataGenerationUtils;
import dev.thomasglasser.tommylib.impl.data.lang.TommyLibEnUsLanguageProvider;
import dev.thomasglasser.tommylib.impl.data.tags.TommyLibBlockTagsProvider;
import dev.thomasglasser.tommylib.impl.data.tags.TommyLibItemTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class TommyLibDataGenerators {
    public static void onGatherData(GatherDataEvent event) {
        // Server
        DataGenerationUtils.createBlockAndItemTags(event, TommyLibBlockTagsProvider::new, TommyLibItemTagsProvider::new);

        // Client
        event.createProvider(TommyLibEnUsLanguageProvider::new);
    }
}
