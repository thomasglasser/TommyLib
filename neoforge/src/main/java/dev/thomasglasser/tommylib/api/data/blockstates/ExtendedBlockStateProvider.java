package dev.thomasglasser.tommylib.api.data.blockstates;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.thomasglasser.tommylib.api.registration.DeferredBlock;
import dev.thomasglasser.tommylib.api.registration.DeferredItem;
import dev.thomasglasser.tommylib.api.world.level.block.LeavesSet;
import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.apache.commons.lang3.function.TriFunction;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Extension of {@link BlockStateProvider} that provides functionality for mod holders.
 */
public abstract class ExtendedBlockStateProvider extends BlockStateProvider {
    protected static final ExistingFileHelper.ResourceType TEXTURE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");

    protected final Map<Block, BlockStateGenerator> STATE_MAP = Maps.newHashMap();
    protected final Map<ResourceLocation, Supplier<JsonElement>> MODEL_MAP = Maps.newHashMap();
    protected final ExtendedBlockModelGenerators blockModelGenerators;
    protected final String modId;
    protected final PackOutput output;
    protected final ExistingFileHelper existingFileHelper;

    public ExtendedBlockStateProvider(PackOutput output, String modId, ExistingFileHelper exFileHelper) {
        super(output, modId, exFileHelper);
        this.blockModelGenerators = getBlockModelGenerators() != null ? makeBlockModelGenerators(getBlockModelGenerators()) : null;
        this.modId = modId;
        this.output = output;
        existingFileHelper = exFileHelper;
    }

    /**
     * Creates a new instance of {@link ExtendedBlockModelGenerators} using the provided generator.
     * 
     * @param generator The function to create the {@link ExtendedBlockModelGenerators} instance.
     * @return The new instance of {@link ExtendedBlockModelGenerators}.
     */
    private ExtendedBlockModelGenerators makeBlockModelGenerators(TriFunction<Consumer<BlockStateGenerator>, BiConsumer<ResourceLocation, Supplier<JsonElement>>, Consumer<Item>, ? extends ExtendedBlockModelGenerators> generator) {
        Consumer<BlockStateGenerator> consumer = (p_125120_) -> {
            Block block = p_125120_.getBlock();
            BlockStateGenerator blockstategenerator = STATE_MAP.put(block, p_125120_);
            if (blockstategenerator != null) {
                throw new IllegalStateException("Duplicate blockstate definition for " + block);
            }
        };
        Set<Item> set = Sets.newHashSet();
        BiConsumer<ResourceLocation, Supplier<JsonElement>> biconsumer = (p_125123_, p_125124_) -> {
            Supplier<JsonElement> supplier = MODEL_MAP.put(p_125123_, p_125124_);
            if (supplier != null) {
                throw new IllegalStateException("Duplicate model definition for " + p_125123_);
            }
        };
        Consumer<Item> consumer1 = set::add;
        return (generator.apply(consumer, biconsumer, consumer1));
    }

    /**
     * Gets the block model generators for this provider, which is null by default.
     * 
     * @return The block model generators for this provider.
     */
    protected TriFunction<Consumer<BlockStateGenerator>, BiConsumer<ResourceLocation, Supplier<JsonElement>>, Consumer<Item>, ? extends ExtendedBlockModelGenerators> getBlockModelGenerators() {
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
    public ResourceLocation blockLoc(DeferredBlock<?> block) {
        return block.getId().withPrefix(ModelProvider.BLOCK_FOLDER + "/");
    }

    /**
     * Creates a new {@link ResourceLocation} in the item subfolder with the mod namespace.
     * 
     * @param item The item to create the {@link ResourceLocation} for.
     * @return The new {@link ResourceLocation}.
     */
    public ResourceLocation itemLoc(DeferredItem<?> item) {
        return item.getId().withPrefix(ModelProvider.ITEM_FOLDER + "/");
    }

    /**
     * Creates a new {@link ResourceLocation} in the block subfolder with the Minecraft namespace.
     * 
     * @param path The path of the new {@link ResourceLocation}.
     * @return The new {@link ResourceLocation}.
     */
    public static ResourceLocation mcBlockLoc(String path) {
        return ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/" + path);
    }

    /**
     * Creates a new {@link ResourceLocation} in the item subfolder with the Minecraft namespace.
     *
     * @param path The path of the new {@link ResourceLocation}.
     * @return The new {@link ResourceLocation}.
     */
    public static ResourceLocation mcItemLoc(String path) {
        return ResourceLocation.withDefaultNamespace(ModelProvider.ITEM_FOLDER + "/" + path);
    }

    /**
     * Creates a new {@link ResourceLocation} in the block subfolder with the mod namespace.
     *
     * @param path The path of the new {@link ResourceLocation}.
     * @return The new {@link ResourceLocation}.
     */
    public ResourceLocation modBlockLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(modId, ModelProvider.BLOCK_FOLDER + "/" + path);
    }

    /**
     * Creates a new {@link ResourceLocation} in the item subfolder with the mod namespace.
     *
     * @param path The path of the new {@link ResourceLocation}.
     * @return The new {@link ResourceLocation}.
     */
    public ResourceLocation modItemLoc(String path) {
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
    public void slabBlock(SlabBlock block, ResourceLocation texture) {
        super.slabBlock(block, texture, texture);
    }

    /**
     * Generates hanging sign blocks with the provided texture.
     * 
     * @param signBlock     The ceiling hanging sign block to generate.
     * @param wallSignBlock The wall hanging sign block to generate.
     * @param texture       The texture to use for the sign blocks.
     */
    public void hangingSignBlock(CeilingHangingSignBlock signBlock, WallHangingSignBlock wallSignBlock, ResourceLocation texture) {
        ModelFile sign = models().sign(BuiltInRegistries.BLOCK.getKey(signBlock).getPath(), texture);
        hangingSignBlock(signBlock, wallSignBlock, sign);
    }

    /**
     * Generates hanging sign blocks with the provided model.
     * 
     * @param signBlock     The ceiling hanging sign block to generate.
     * @param wallSignBlock The wall hanging sign block to generate.
     * @param sign          The model to use for the sign blocks.
     */
    public void hangingSignBlock(CeilingHangingSignBlock signBlock, WallHangingSignBlock wallSignBlock, ModelFile sign) {
        simpleBlock(signBlock, sign);
        simpleBlock(wallSignBlock, sign);
    }

    /**
     * Generates relevant blockstates and models for a {@link LeavesSet}.
     * 
     * @param set The {@link LeavesSet} to generate blockstates and models for.
     */
    protected void leavesSet(LeavesSet set) {
        simpleBlock(set.leaves().get(), models().withExistingParent(BuiltInRegistries.BLOCK.getKey(set.leaves().get()).getPath(), mcBlockLoc("leaves")).texture("all", blockLoc(set.leaves())));
        simpleBlock(set.sapling().get(), models().cross(set.id().getPath() + "_sapling", blockLoc(set.sapling())).renderType("cutout"));
        simpleBlock(set.pottedSapling().get(), models().withExistingParent("potted_" + set.id().getPath() + "_sapling", mcBlockLoc("flower_pot_cross")).texture("plant", blockLoc(set.sapling())).renderType("cutout"));
    }

    /**
     * Extension of {@link BlockModelGenerators} that exposes its methods and variables for use in {@link ExtendedBlockStateProvider}.
     */
    protected abstract class ExtendedBlockModelGenerators extends BlockModelGenerators {
        protected final Consumer<BlockStateGenerator> blockStateOutput;
        protected final BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput;
        protected final Consumer<Item> skippedAutoModelsOutput;

        public ExtendedBlockModelGenerators(Consumer<BlockStateGenerator> pBlockStateOutput, BiConsumer<ResourceLocation, Supplier<JsonElement>> pModelOutput, Consumer<Item> pSkippedAutoModelsOutput) {
            super(pBlockStateOutput, pModelOutput, pSkippedAutoModelsOutput);
            blockStateOutput = pBlockStateOutput;
            modelOutput = pModelOutput;
            skippedAutoModelsOutput = pSkippedAutoModelsOutput;
        }

        public CompletableFuture<?> generateAll(CachedOutput cache) {
            List<CompletableFuture<?>> futures = new ArrayList<>();
            for (Map.Entry<ResourceLocation, Supplier<JsonElement>> entry : MODEL_MAP.entrySet()) {
                futures.add(DataProvider.saveStable(cache, entry.getValue().get().getAsJsonObject(), getPath(entry.getKey())));
            }
            for (Map.Entry<Block, BlockStateGenerator> entry : STATE_MAP.entrySet()) {
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
