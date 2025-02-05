package dev.thomasglasser.tommylib.api.data.info;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

public class ModRegistryDumpReport implements DataProvider {
    protected final PackOutput output;
    protected final String modId;
    protected final CompletableFuture<HolderLookup.Provider> lookupProvider;

    public ModRegistryDumpReport(PackOutput output, String modId, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        this.output = output;
        this.modId = modId;
        this.lookupProvider = lookupProvider;
    }

    public CompletableFuture<?> run(CachedOutput output) {
        return this.lookupProvider.thenCompose((registries) -> {
            JsonObject jsonobject = new JsonObject();
            registries.listRegistries().map(registries::lookup).forEach((registry) -> registry.ifPresent(reference -> jsonobject.add(reference.key().location().toString(), dumpRegistry(reference))));
            Path path = this.output.getOutputFolder(PackOutput.Target.REPORTS).resolve("registries.json");
            return DataProvider.saveStable(output, jsonobject, path);
        });
    }

    private JsonElement dumpRegistry(HolderLookup.RegistryLookup<?> registry) {
        JsonArray jsonarray = new JsonArray();
        registry.listElements().sorted(Comparator.comparing(Holder.Reference::key)).forEach((reference) -> {
            if (reference.key().location().getNamespace().equals(modId)) {
                jsonarray.add(reference.key().location().toString());
            }
        });
        if (jsonarray.isEmpty())
            return null;
        return jsonarray;
    }

    @Override
    public String getName() {
        return modId + " Registry Dump";
    }
}
