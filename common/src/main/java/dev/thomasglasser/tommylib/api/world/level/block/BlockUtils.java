package dev.thomasglasser.tommylib.api.world.level.block;

import dev.thomasglasser.tommylib.api.registration.DeferredBlock;
import dev.thomasglasser.tommylib.api.registration.DeferredItem;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.apache.commons.lang3.function.TriFunction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockUtils
{
	/**
	 * Functions to create a BlockItem from a DeferredBlock
	 */

	public static final Function<DeferredBlock<?>, BlockItem> BLOCK_ITEM_FUNCTION = ((block) -> new BlockItem(block.get(), new Item.Properties()));
	public static final BiFunction<DeferredBlock<?>, Item.Properties, BlockItem> BLOCK_ITEM_WITH_PROPERTIES_FUNCTION = ((block, properties) -> new BlockItem(block.get(), properties));

	/**
	 * Map of blocks that can be stripped to their stripped versions
	 */
	private static final Map<ResourceLocation, DeferredBlock<?>> STRIPPABLES = new HashMap<>();

	/**
	 * Register a block with the given provider and name
	 * @param provider The provider to register the block with
	 * @param name The registry name of the block
	 * @param block The block supplier
	 * @return The block holder
	 * @param <T> The block type
	 */
	public static <T extends Block> DeferredBlock<T> register(DeferredRegister.Blocks provider, String name, Supplier<T> block)
	{
		return provider.register(name, block);
	}

	/**
	 * Register a block and item with the given provider and name
	 * @param provider The provider to register the block with
	 * @param name The registry name of the block
	 * @param blockFactory The block supplier
	 * @param itemFactory The item supplier
	 * @param tabs The creative mode tabs to add the item to
	 * @return The block holder
	 * @param <T> The block type
	 */
	public static <T extends Block> DeferredBlock<T> registerBlockAndItemAndWrap(
			DeferredRegister.Blocks provider,
			String name,
			Supplier<T> blockFactory,
			TriFunction<String, Supplier<Item>, List<ResourceKey<CreativeModeTab>>, DeferredItem<?>> itemFactory,
			List<ResourceKey<CreativeModeTab>> tabs)
	{
		DeferredBlock<T> block = register(provider, name, blockFactory);
		itemFactory.apply(name, () -> BLOCK_ITEM_FUNCTION.apply(block), tabs);
		return block;
	}

	/**
	 * Register a block and item with the given provider and name
	 * @param provider The provider to register the block with
	 * @param name The registry name of the block
	 * @param blockFactory The block supplier
	 * @param itemFactory The item supplier
	 * @param properties The item properties
	 * @param tabs The creative mode tabs to add the item to
	 * @return The block holder
	 * @param <T> The block type
	 */
	public static <T extends Block> DeferredBlock<T> registerBlockAndItemAndWrap(
			DeferredRegister.Blocks provider,
			String name,
			Supplier<T> blockFactory,
			TriFunction<String, Supplier<Item>, List<ResourceKey<CreativeModeTab>>, DeferredItem<?>> itemFactory,
			Item.Properties properties,
			List<ResourceKey<CreativeModeTab>> tabs)
	{
		DeferredBlock<T> block = register(provider, name, blockFactory);
		itemFactory.apply(name, () -> BLOCK_ITEM_WITH_PROPERTIES_FUNCTION.apply(block, properties), tabs);
		return block;
	}

	/**
	 * Register a {@link WoodSet} with the given provider and name in the provider's namespace
	 * @param provider The provider to register the blocks with
	 * @param name The name of the wood set
	 * @param mapColor The map color of the wood set
	 * @param logMapColor The map color of the logs
	 * @param itemFactory The item registration factory
	 * @return The wood set
	 */
	public static WoodSet registerWoodSet(DeferredRegister.Blocks provider, String name, MapColor mapColor, MapColor logMapColor, TriFunction<String, Supplier<Item>, List<ResourceKey<CreativeModeTab>>, DeferredItem<?>> itemFactory)
	{
		DeferredBlock<?> log = registerBlockAndItemAndWrap(provider, name + "_log", () -> Blocks.log(mapColor, logMapColor), itemFactory, List.of(CreativeModeTabs.BUILDING_BLOCKS, CreativeModeTabs.NATURAL_BLOCKS));
		DeferredBlock<?> strippedLog = registerBlockAndItemAndWrap(provider, "stripped_" + name + "_log", () -> Blocks.log(mapColor, mapColor), itemFactory, List.of(CreativeModeTabs.BUILDING_BLOCKS));
		DeferredBlock<?> wood = registerBlockAndItemAndWrap(provider, name + "_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(mapColor).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()), itemFactory, List.of(CreativeModeTabs.BUILDING_BLOCKS));
		DeferredBlock<?> strippedWood = registerBlockAndItemAndWrap(provider, "stripped_" + name + "_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(mapColor).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()), itemFactory, List.of(CreativeModeTabs.BUILDING_BLOCKS));
		STRIPPABLES.put(log.getId(), strippedLog);
		STRIPPABLES.put(wood.getId(), strippedWood);
		return new WoodSet(ResourceLocation.fromNamespaceAndPath(provider.getNamespace(), name),
				registerBlockAndItemAndWrap(provider, name + "_planks", () -> new Block(BlockBehaviour.Properties.of().mapColor(mapColor).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).ignitedByLava()), itemFactory, List.of(CreativeModeTabs.BUILDING_BLOCKS)),
				log,
				strippedLog,
				wood,
				strippedWood,
				TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(provider.getNamespace(), name + "_logs")),
				TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(provider.getNamespace(), name + "_logs")));
	}

	/**
	 * Register a {@link LeavesSet} with the given provider and name in the provider's namespace
	 * @param provider The provider to register the blocks with
	 * @param treeGrower The tree grower for the sapling
	 * @param itemFactory The item factory
	 * @return The leaves set
	 */
	public static LeavesSet registerLeavesSet(DeferredRegister.Blocks provider, String name, TreeGrower treeGrower, TriFunction<String, Supplier<Item>, List<ResourceKey<CreativeModeTab>>, DeferredItem<?>> itemFactory)
	{
		DeferredBlock<?> sapling = registerBlockAndItemAndWrap(provider, name + "_sapling", () -> new SaplingBlock(treeGrower, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)), itemFactory, List.of(CreativeModeTabs.NATURAL_BLOCKS));
		return new LeavesSet(ResourceLocation.fromNamespaceAndPath(provider.getNamespace(), name),
				registerBlockAndItemAndWrap(provider, name + "_leaves", () -> Blocks.leaves(SoundType.GRASS), itemFactory, List.of(CreativeModeTabs.NATURAL_BLOCKS)),
				sapling,
				register(provider, "potted_" + name + "_sapling", () -> Blocks.flowerPot(sapling.get())));
	}

	/**
	 * Gets the stripped version of a block
	 * @param originalState The original block state
	 * @return The stripped block
	 */
	public static Block getStripped(BlockState originalState) {
		DeferredBlock<?> ro = STRIPPABLES.get(originalState.getBlock().builtInRegistryHolder().key().location());
		return ro != null ? ro.get() : null;
	}
}
