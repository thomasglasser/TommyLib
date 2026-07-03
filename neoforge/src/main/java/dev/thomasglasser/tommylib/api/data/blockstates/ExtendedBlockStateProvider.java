package dev.thomasglasser.tommylib.api.data.blockstates;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.thomasglasser.tommylib.api.registration.DeferredBlock;
import dev.thomasglasser.tommylib.api.registration.DeferredItem;
import dev.thomasglasser.tommylib.api.world.level.block.LeavesSet;
import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

/// Extension of {@link BlockStateProvider} that provides helpers and {@link BlockModelGenerators} support.
public abstract class ExtendedBlockStateProvider extends BlockStateProvider {
    protected static final ExistingFileHelper.ResourceType TEXTURE_RESOURCE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");

    protected final Map<Block, BlockStateGenerator> stateMap = new Reference2ReferenceOpenHashMap<>();
    protected final Map<ResourceLocation, Supplier<JsonElement>> modelMap = new Reference2ReferenceOpenHashMap<>();
    protected final String modId;
    protected final PackOutput output;
    protected final ExistingFileHelper existingFileHelper;
    @Nullable
    protected final ExtendedBlockModelGenerators blockModelGenerators;

    protected ExtendedBlockStateProvider(PackOutput output, String modId, ExistingFileHelper existingFileHelper) {
        super(output, modId, existingFileHelper);
        this.modId = modId;
        this.output = output;
        this.existingFileHelper = existingFileHelper;
        this.blockModelGenerators = makeBlockModelGenerators(getBlockModelGeneratorsProvider());
    }

    /**
     * Creates a new instance of {@link ExtendedBlockModelGenerators} using the provided generator provider.
     * 
     * @param provider The function to create the {@link ExtendedBlockModelGenerators} instance.
     * @return The new instance of {@link ExtendedBlockModelGenerators}.
     */
    protected @Nullable ExtendedBlockModelGenerators makeBlockModelGenerators(BiFunction<Consumer<BlockStateGenerator>, BiConsumer<ResourceLocation, Supplier<JsonElement>>, ? extends ExtendedBlockModelGenerators> provider) {
        if (provider == null) {
            return null;
        }
        Consumer<BlockStateGenerator> stateConsumer = generator -> {
            Block block = generator.getBlock();
            BlockStateGenerator blockstategenerator = stateMap.put(block, generator);
            if (blockstategenerator != null) {
                throw new IllegalStateException("Duplicate blockstate definition for " + block);
            }
        };
        BiConsumer<ResourceLocation, Supplier<JsonElement>> modelConsumer = (id, json) -> {
            Supplier<JsonElement> supplier = modelMap.put(id, json);
            if (supplier != null) {
                throw new IllegalStateException("Duplicate model definition for " + id);
            }
        };
        return provider.apply(stateConsumer, modelConsumer);
    }

    /**
     * Gets the {@link BlockModelGenerators} provider, which is null by default.
     * 
     * @return The {@link BlockModelGenerators} provider
     */
    protected BiFunction<Consumer<BlockStateGenerator>, BiConsumer<ResourceLocation, Supplier<JsonElement>>, ? extends ExtendedBlockModelGenerators> getBlockModelGeneratorsProvider() {
        return null;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        if (blockModelGenerators != null) {
            blockModelGenerators.run();
            return CompletableFuture.allOf(super.run(cache), blockModelGenerators.generateAll(cache));
        }
        return super.run(cache);
    }

    /**
     * Creates a new {@link ResourceLocation} in the block subfolder with the mod namespace.
     * 
     * @param block The block to create the {@link ResourceLocation} for.
     * @return The new {@link ResourceLocation}.
     */
    protected ResourceLocation blockLoc(DeferredBlock<?> block) {
        return block.getId().withPrefix(ModelProvider.BLOCK_FOLDER + "/");
    }

    /**
     * Creates a new {@link ResourceLocation} in the item subfolder with the mod namespace.
     * 
     * @param item The item to create the {@link ResourceLocation} for.
     * @return The new {@link ResourceLocation}.
     */
    protected ResourceLocation itemLoc(DeferredItem<?> item) {
        return item.getId().withPrefix(ModelProvider.ITEM_FOLDER + "/");
    }

    /**
     * Creates a new {@link ResourceLocation} in the block subfolder with the Minecraft namespace.
     * 
     * @param path The path of the new {@link ResourceLocation}.
     * @return The new {@link ResourceLocation}.
     */
    protected ResourceLocation mcBlockLoc(String path) {
        return ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/" + path);
    }

    /**
     * Creates a new {@link ResourceLocation} in the item subfolder with the Minecraft namespace.
     *
     * @param path The path of the new {@link ResourceLocation}.
     * @return The new {@link ResourceLocation}.
     */
    protected ResourceLocation mcItemLoc(String path) {
        return ResourceLocation.withDefaultNamespace(ModelProvider.ITEM_FOLDER + "/" + path);
    }

    /**
     * Creates a new {@link ResourceLocation} in the block subfolder with the mod namespace.
     *
     * @param path The path of the new {@link ResourceLocation}.
     * @return The new {@link ResourceLocation}.
     */
    protected ResourceLocation modBlockLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(modId, ModelProvider.BLOCK_FOLDER + "/" + path);
    }

    /**
     * Creates a new {@link ResourceLocation} in the item subfolder with the mod namespace.
     *
     * @param path The path of the new {@link ResourceLocation}.
     * @return The new {@link ResourceLocation}.
     */
    protected ResourceLocation modItemLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(modId, ModelProvider.ITEM_FOLDER + "/" + path);
    }

    /**
     * Generates relevant blockstates and models for a {@link WoodSet}.
     * 
     * @param set The {@link WoodSet} to generate blockstates and models for.
     */
    protected void woodSet(WoodSet set) {
        logBlock(set.log().get());
        logBlock(set.strippedLog().get());
        simpleBlock(set.wood().get(), models().cubeAll(set.wood().getId().getPath(), blockLoc(set.log())));
        simpleBlock(set.strippedWood().get(), models().cubeAll(set.strippedWood().getId().getPath(), blockLoc(set.strippedLog())));
        simpleBlock(set.planks().get());
        slabBlock(set.slab().get(), blockLoc(set.planks()));
        stairsBlock(set.stairs().get(), blockLoc(set.planks()));
        pressurePlateBlock(set.pressurePlate().get(), blockLoc(set.planks()));
        buttonBlock(set.button().get(), blockLoc(set.planks()));
        fenceBlock(set.fence().get(), blockLoc(set.planks()));
        fenceGateBlock(set.fenceGate().get(), blockLoc(set.planks()));
        doorBlockWithRenderType(set.door().get(), blockLoc(set.door()).withSuffix("_bottom"), blockLoc(set.door()).withSuffix("_top"), RenderType.cutout().name);
        trapdoorBlockWithRenderType(set.trapdoor().get(), blockLoc(set.trapdoor()), true, RenderType.cutout().name);
        signBlock(set.sign().get(), set.wallSign().get(), blockLoc(set.planks()));
        hangingSignBlock(set.hangingSign().get(), set.wallHangingSign().get(), blockLoc(set.strippedLog()));
    }

    /**
     * Generates a slab block with the provided texture.
     * 
     * @param block   The slab block to generate.
     * @param texture The texture to use for the slab block.
     */
    protected void slabBlock(SlabBlock block, ResourceLocation texture) {
        super.slabBlock(block, texture, texture);
    }

    /**
     * Generates relevant blockstates and models for a {@link LeavesSet}.
     * 
     * @param set The {@link LeavesSet} to generate blockstates and models for.
     */
    protected void leavesSet(LeavesSet set) {
        simpleBlock(set.leaves().get(), models().withExistingParent(set.leaves().getId().getPath(), mcBlockLoc("leaves")).texture("all", blockLoc(set.leaves())));
        simpleBlock(set.sapling().get(), models().cross(set.id().getPath() + "_sapling", blockLoc(set.sapling())).renderType("cutout"));
        simpleBlock(set.pottedSapling().get(), models().withExistingParent("potted_" + set.id().getPath() + "_sapling", mcBlockLoc("flower_pot_cross")).texture("plant", blockLoc(set.sapling())).renderType("cutout"));
    }

    /**
     * Extension of {@link BlockModelGenerators} that exposes its methods and variables for use in {@link ExtendedBlockStateProvider}.
     */
    protected abstract class ExtendedBlockModelGenerators extends BlockModelGenerators {
        protected final Consumer<BlockStateGenerator> blockStateOutput;
        protected final BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput;

        public ExtendedBlockModelGenerators(Consumer<BlockStateGenerator> pBlockStateOutput, BiConsumer<ResourceLocation, Supplier<JsonElement>> pModelOutput) {
            super(pBlockStateOutput, pModelOutput, item -> {});
            blockStateOutput = pBlockStateOutput;
            modelOutput = pModelOutput;
        }

        public CompletableFuture<?> generateAll(CachedOutput cache) {
            Set<CompletableFuture<?>> futures = new ReferenceOpenHashSet<>();
            for (Map.Entry<ResourceLocation, Supplier<JsonElement>> entry : modelMap.entrySet()) {
                futures.add(DataProvider.saveStable(cache, entry.getValue().get().getAsJsonObject(), getPath(entry.getKey())));
            }
            for (Map.Entry<Block, BlockStateGenerator> entry : stateMap.entrySet()) {
                futures.add(saveBlockState(cache, entry.getValue().get().getAsJsonObject(), entry.getKey()));
            }
            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[] {}));
        }

        private CompletableFuture<?> saveBlockState(CachedOutput cache, JsonObject stateJson, Block owner) {
            ResourceLocation blockName = Preconditions.checkNotNull(key(owner));
            Path outputPath = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                    .resolve(blockName.getNamespace()).resolve("blockstates").resolve(blockName.getPath() + ".json");
            return DataProvider.saveStable(cache, stateJson, outputPath);
        }

        @Override
        public abstract void run();

        /**
         * Gets the key for the provided block.
         * 
         * @param block The block to get the key for.
         * @return The key for the provided block.
         */
        protected ResourceLocation key(Block block) {
            return BuiltInRegistries.BLOCK.getKey(block);
        }

        /**
         * Gets the path for the provided {@link ResourceLocation}.
         * 
         * @param loc The {@link ResourceLocation} to get the path for.
         * @return The path for the provided {@link ResourceLocation}.
         */
        protected Path getPath(ResourceLocation loc) {
            return output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(loc.getNamespace()).resolve("models").resolve(loc.getPath() + ".json");
        }
    }
}
