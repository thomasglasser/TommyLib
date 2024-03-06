package dev.thomasglasser.tommylib.api.world.level.block;

import dev.thomasglasser.tommylib.api.registration.RegistrationProvider;
import dev.thomasglasser.tommylib.api.registration.RegistryObject;
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
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.apache.commons.lang3.function.TriFunction;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockUtils
{
	public static final Function<RegistryObject<? extends Block>, BlockItem> BLOCK_ITEM_FUNCTION = ((block) -> new BlockItem(block.get(), new Item.Properties()));
	public static final BiFunction<RegistryObject<? extends Block>, Item.Properties, BlockItem> BLOCK_ITEM_WITH_PROPERTIES_FUNCTION = ((block, properties) -> new BlockItem(block.get(), properties));

	public static <T extends Block> RegistryObject<T> register(RegistrationProvider<Block> provider, String name, Supplier<T> block)
	{
		return provider.register(name, block);
	}

	public static <T extends Block> RegistryObject<T> registerBlockAndItemAndWrap(
			RegistrationProvider<Block> provider,
			String name,
			Supplier<T> blockFactory,
			TriFunction<String, Supplier<Item>, List<ResourceKey<CreativeModeTab>>, RegistryObject<? extends Item>> itemFactory,
			List<ResourceKey<CreativeModeTab>> tabs)
	{
		RegistryObject<T> block = register(provider, name, blockFactory);
		itemFactory.apply(name, () -> BLOCK_ITEM_FUNCTION.apply(block), tabs);
		return block;
	}

	public static <T extends Block> RegistryObject<T> registerBlockAndItemAndWrap(
			RegistrationProvider<Block> provider,
			String name,
			Supplier<T> blockFactory,
			TriFunction<String, Supplier<Item>, List<ResourceKey<CreativeModeTab>>, RegistryObject<? extends Item>> itemFactory,
			Item.Properties properties,
			List<ResourceKey<CreativeModeTab>> tabs)
	{
		RegistryObject<T> block = register(provider, name, blockFactory);
		itemFactory.apply(name, () -> BLOCK_ITEM_WITH_PROPERTIES_FUNCTION.apply(block, properties), tabs);
		return block;
	}

	public static WoodSet registerWoodSet(RegistrationProvider<Block> provider, ResourceLocation id, MapColor mapColor, MapColor logMapColor, Supplier<TagKey<Block>> logsBlockTag, Supplier<TagKey<Item>> logsItemTag, TriFunction<String, Supplier<Item>, List<ResourceKey<CreativeModeTab>>, RegistryObject<? extends Item>> itemFactory)
	{
		return new WoodSet(id,
				registerBlockAndItemAndWrap(provider, id.getPath() + "_planks", () -> new Block(BlockBehaviour.Properties.of().mapColor(mapColor).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).ignitedByLava()), itemFactory, List.of(CreativeModeTabs.BUILDING_BLOCKS)),
				registerBlockAndItemAndWrap(provider, id.getPath() + "_log", () -> Blocks.log(mapColor, logMapColor), itemFactory, List.of(CreativeModeTabs.BUILDING_BLOCKS, CreativeModeTabs.NATURAL_BLOCKS)),
				registerBlockAndItemAndWrap(provider, "stripped_" + id.getPath() + "_log", () -> Blocks.log(mapColor, mapColor), itemFactory, List.of(CreativeModeTabs.BUILDING_BLOCKS)),
				registerBlockAndItemAndWrap(provider, id.getPath() + "_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(mapColor).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()), itemFactory, List.of(CreativeModeTabs.BUILDING_BLOCKS)),
				registerBlockAndItemAndWrap(provider, "stripped_" + id.getPath() + "_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(mapColor).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()), itemFactory, List.of(CreativeModeTabs.BUILDING_BLOCKS)),
				logsBlockTag,
				logsItemTag);
	}

	public static LeavesSet registerLeavesSet(RegistrationProvider<Block> provider, ResourceLocation id, TreeGrower treeGrower, TriFunction<String, Supplier<Item>, List<ResourceKey<CreativeModeTab>>, RegistryObject<? extends Item>> itemFactory)
	{
		RegistryObject<Block> sapling = registerBlockAndItemAndWrap(provider, id.getPath() + "_sapling", () -> new SaplingBlock(treeGrower, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)), itemFactory, List.of(CreativeModeTabs.NATURAL_BLOCKS));
		return new LeavesSet(id,
				registerBlockAndItemAndWrap(provider, id.getPath() + "_leaves", () -> Blocks.leaves(SoundType.GRASS), itemFactory, List.of(CreativeModeTabs.NATURAL_BLOCKS)),
				sapling,
				register(provider, "potted_" + id.getPath() + "_sapling", () -> Blocks.flowerPot(sapling.get())));
	}
}
