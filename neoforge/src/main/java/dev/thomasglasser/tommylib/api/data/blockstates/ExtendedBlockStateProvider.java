package dev.thomasglasser.tommylib.api.data.blockstates;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.thomasglasser.tommylib.api.world.level.block.LeavesSet;
import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
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
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
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

public abstract class ExtendedBlockStateProvider extends BlockStateProvider
{
	protected static final ExistingFileHelper.ResourceType TEXTURE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");

	protected final Map<Block, BlockStateGenerator> STATE_MAP = Maps.newHashMap();
	protected final Map<ResourceLocation, Supplier<JsonElement>> MODEL_MAP = Maps.newHashMap();
	protected final ExtendedBlockModelGenerators blockModelGenerators;
	protected final PackOutput output;
	protected final ExistingFileHelper existingFileHelper;

	public ExtendedBlockStateProvider(PackOutput output, String modId, ExistingFileHelper exFileHelper) {
		super(output, modId, exFileHelper);
		this.blockModelGenerators = getBlockModelGenerators() != null ? makeBlockModelGenerators(getBlockModelGenerators()) : null;
		this.output = output;
		existingFileHelper = exFileHelper;
	}

	private ExtendedBlockModelGenerators makeBlockModelGenerators(TriFunction<Consumer<BlockStateGenerator>, BiConsumer<ResourceLocation, Supplier<JsonElement>>, Consumer<Item>, ? extends ExtendedBlockModelGenerators> generator)
	{
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

	protected TriFunction<Consumer<BlockStateGenerator>, BiConsumer<ResourceLocation, Supplier<JsonElement>>, Consumer<Item>, ? extends ExtendedBlockModelGenerators> getBlockModelGenerators()
	{
		return null;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		if (blockModelGenerators != null)
		{
			blockModelGenerators.run();
			return CompletableFuture.allOf(super.run(cache), blockModelGenerators.generateAll(cache));
		}
		return super.run(cache);
	}

	public ResourceLocation modBlockModel(String path)
	{
		return modLoc("block/" + path);
	}

	public ResourceLocation modItemModel(String path)
	{
		return modLoc("item/" + path);
	}

	public static ResourceLocation mcBlockModel(String path)
	{
		return new ResourceLocation("block/" + path);
	}

	protected void woodSet(WoodSet set)
	{
		simpleBlock(set.planks().get());
		logBlock((RotatedPillarBlock) set.log().get());
		//        logBlock((RotatedPillarBlock) set.strippedLog().get());
		simpleBlock(set.wood().get(), models().cubeAll(set.id().getPath() + "_wood", modBlockModel(set.id().getPath() + "_log")));
		//        simpleBlock(set.strippedWood().get());
	}

	protected void leavesSet(LeavesSet set)
	{
		simpleBlock(set.leaves().get(), models().withExistingParent(BuiltInRegistries.BLOCK.getKey(set.leaves().get()).getPath(), mcBlockModel("leaves")).texture("all", modBlockModel(BuiltInRegistries.BLOCK.getKey(set.leaves().get()).getPath())));
		simpleBlock(set.sapling().get(), models().cross(set.id().getPath() + "_sapling", modBlockModel(BuiltInRegistries.BLOCK.getKey(set.sapling().get()).getPath())).renderType("cutout"));
		simpleBlock(set.pottedSapling().get(), models().withExistingParent("potted_" + set.id().getPath() + "_sapling", mcBlockModel("flower_pot_cross")).texture("plant", modBlockModel(BuiltInRegistries.BLOCK.getKey(set.sapling().get()).getPath())).renderType("cutout"));
	}

	protected abstract class ExtendedBlockModelGenerators extends BlockModelGenerators
	{
		public ExtendedBlockModelGenerators(Consumer<BlockStateGenerator> pBlockStateOutput, BiConsumer<ResourceLocation, Supplier<JsonElement>> pModelOutput, Consumer<Item> pSkippedAutoModelsOutput) {
			super(pBlockStateOutput, pModelOutput, pSkippedAutoModelsOutput);
		}

		public CompletableFuture<?> generateAll(CachedOutput cache)
		{
			List<CompletableFuture<?>> futures = new ArrayList<>();
			for (Map.Entry<ResourceLocation, Supplier<JsonElement>> entry : MODEL_MAP.entrySet()) {
				futures.add(DataProvider.saveStable(cache, entry.getValue().get().getAsJsonObject(), getPath(entry.getKey())));
			}
			for (Map.Entry<Block, BlockStateGenerator> entry : STATE_MAP.entrySet()) {
				futures.add(saveBlockState(cache, entry.getValue().get().getAsJsonObject(), entry.getKey()));
			}
			return CompletableFuture.allOf(futures.toArray(new CompletableFuture[]{}));
		}

		private CompletableFuture<?> saveBlockState(CachedOutput cache, JsonObject stateJson, Block owner) {
			ResourceLocation blockName = Preconditions.checkNotNull(key(owner));
			Path outputPath = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
					.resolve(blockName.getNamespace()).resolve("blockstates").resolve(blockName.getPath() + ".json");
			return DataProvider.saveStable(cache, stateJson, outputPath);
		}

		@Override
		public abstract void run();

		protected ResourceLocation key(Block block) {
			return BuiltInRegistries.BLOCK.getKey(block);
		}

		protected Path getPath(ResourceLocation loc) {
			return output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(loc.getNamespace()).resolve("models").resolve(loc.getPath() + ".json");
		}
	}
}
