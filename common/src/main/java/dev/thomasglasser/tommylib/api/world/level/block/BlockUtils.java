package dev.thomasglasser.tommylib.api.world.level.block;

import dev.thomasglasser.tommylib.api.registration.DeferredBlock;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;
import dev.thomasglasser.tommylib.api.world.level.block.grower.ExtendedTreeGrower;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractBoat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.TintedParticleLeavesBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class BlockUtils {
    /**
     * Map of blocks that can be stripped to their stripped versions
     */
    private static final Map<ResourceLocation, DeferredBlock<?>> STRIPPABLES = new HashMap<>();

    /**
     * Creates a block resource key given a {@link ResourceLocation}
     * 
     * @param location the {@link ResourceLocation} to turn into a resource key
     * @return the resource key
     */
    public static ResourceKey<Block> blockId(ResourceLocation location) {
        return ResourceKey.create(Registries.BLOCK, location);
    }

    /**
     * Registers a block
     * 
     * @param provider   The provider to register with
     * @param name       The registry name of the block
     * @param block      The function to create the block
     * @param properties The block properties
     * @return The {@link DeferredBlock} containing the created block
     * @param <T> The type of the block
     */
    public static <T extends Block> DeferredBlock<T> register(DeferredRegister.Blocks provider, String name, Function<BlockBehaviour.Properties, T> block, Supplier<BlockBehaviour.Properties> properties) {
        return provider.register(name, id -> block.apply(properties.get().setId(blockId(id))));
    }

    /**
     * Registers a simple {@link Block}
     * 
     * @param provider   The provider to register with
     * @param name       The registry name of the block
     * @param properties The block properties
     * @return The {@link DeferredBlock} containing the created block
     */
    public static DeferredBlock<?> registerSimple(DeferredRegister.Blocks provider, String name, Supplier<BlockBehaviour.Properties> properties) {
        return register(provider, name, Block::new, properties);
    }

    /**
     * Registers a block with a matching item
     * 
     * @param provider        The provider to register with
     * @param name            The registry name of the block
     * @param blockFactory    The function to create the block
     * @param blockProperties The block properties
     * @param itemProvider    The provider to register the item with
     * @param itemProperties  The item properties
     * @return The {@link DeferredBlock} containing the created block
     * @param <T> The type of the block
     */
    public static <T extends Block> DeferredBlock<T> registerBlockAndItemAndWrap(
            DeferredRegister.Blocks provider,
            String name,
            Function<BlockBehaviour.Properties, T> blockFactory,
            Supplier<BlockBehaviour.Properties> blockProperties,
            DeferredRegister.Items itemProvider,
            Item.Properties itemProperties) {
        DeferredBlock<T> block = register(provider, name, blockFactory, blockProperties);
        itemProvider.registerSimpleBlockItem(name, block, itemProperties);
        return block;
    }

    /**
     * Registers a block with a matching item with the default item properties
     * 
     * @param provider     The provider to register with
     * @param name         The registry name of the block
     * @param blockFactory The function to create the block
     * @param properties   The block properties
     * @param itemProvider The provider to register the item with
     * @return The {@link DeferredBlock} containing the created block
     * @param <T> The type of the block
     */
    public static <T extends Block> DeferredBlock<T> registerBlockAndItemAndWrap(
            DeferredRegister.Blocks provider,
            String name,
            Function<BlockBehaviour.Properties, T> blockFactory,
            Supplier<BlockBehaviour.Properties> properties,
            DeferredRegister.Items itemProvider) {
        return registerBlockAndItemAndWrap(provider, name, blockFactory, properties, itemProvider, new Item.Properties());
    }

    /**
     * Registers a simple block with a matching item
     * 
     * @param provider        The provider to register with
     * @param name            The registry name of the block
     * @param blockProperties The block properties
     * @param itemProvider    The provider to register the item with
     * @param itemProperties  The item properties
     * @return The {@link DeferredBlock} containing the created block
     */
    public static DeferredBlock<?> registerSimpleBlockAndItemAndWrap(
            DeferredRegister.Blocks provider,
            String name,
            Supplier<BlockBehaviour.Properties> blockProperties,
            DeferredRegister.Items itemProvider,
            Item.Properties itemProperties) {
        DeferredBlock<?> block = registerSimple(provider, name, blockProperties);
        itemProvider.registerSimpleBlockItem(name, block, itemProperties);
        return block;
    }

    /**
     * Registers a simple block with a matching item with the default item properties
     * 
     * @param provider     The provider to register with
     * @param name         The registry name of the block
     * @param properties   The block properties
     * @param itemProvider The provider to register the item with
     * @return The {@link DeferredBlock} containing the created block
     */
    public static DeferredBlock<?> registerSimpleBlockAndItemAndWrap(
            DeferredRegister.Blocks provider,
            String name,
            Supplier<BlockBehaviour.Properties> properties,
            DeferredRegister.Items itemProvider) {
        return registerSimpleBlockAndItemAndWrap(provider, name, properties, itemProvider, new Item.Properties());
    }

    /**
     * Registers all the blocks and items in a {@link WoodSet}
     * 
     * @param provider     The provider to register with
     * @param name         The name of the {@link WoodSet}
     * @param mapColor     The map color of the {@link WoodSet}
     * @param logMapColor  The log map color of the {@link WoodSet}
     * @param woodType     The wood type of the {@link WoodSet}
     * @param boat         The boat entity for the {@link WoodSet}
     * @param chestBoat    The chest boat entity for the {@link WoodSet}
     * @param itemProvider The provider to register items with
     * @return The {@link WoodSet} containing all the registered objects
     */
    public static WoodSet registerWoodSet(DeferredRegister.Blocks provider, String name, MapColor mapColor, MapColor logMapColor, Supplier<WoodType> woodType, EntityType<? extends AbstractBoat> boat, EntityType<? extends AbstractBoat> chestBoat, DeferredRegister.Items itemProvider) {
        DeferredBlock<RotatedPillarBlock> log = registerBlockAndItemAndWrap(provider, name + "_log", RotatedPillarBlock::new, () -> Blocks.logProperties(mapColor, logMapColor, woodType.get().soundType()), itemProvider);
        DeferredBlock<RotatedPillarBlock> strippedLog = registerBlockAndItemAndWrap(provider, "stripped_" + name + "_log", RotatedPillarBlock::new, () -> Blocks.logProperties(mapColor, mapColor, woodType.get().soundType()), itemProvider);
        DeferredBlock<RotatedPillarBlock> wood = registerBlockAndItemAndWrap(provider, name + "_wood", RotatedPillarBlock::new, () -> BlockBehaviour.Properties.of()
                .mapColor(mapColor)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F)
                .sound(woodType.get().soundType())
                .ignitedByLava(), itemProvider);
        DeferredBlock<RotatedPillarBlock> strippedWood = registerBlockAndItemAndWrap(provider, "stripped_" + name + "_wood", RotatedPillarBlock::new, () -> BlockBehaviour.Properties.of()
                .mapColor(mapColor)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F)
                .sound(woodType.get().soundType())
                .ignitedByLava(), itemProvider);
        STRIPPABLES.put(log.getId(), strippedLog);
        STRIPPABLES.put(wood.getId(), strippedWood);
        DeferredBlock<?> planks = registerSimpleBlockAndItemAndWrap(provider, name + "_planks", () -> BlockBehaviour.Properties.of()
                .mapColor(mapColor)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F, 3.0F)
                .sound(woodType.get().soundType())
                .ignitedByLava(), itemProvider);
        DeferredBlock<StandingSignBlock> sign = register(provider, name + "_sign", properties -> new StandingSignBlock(woodType.get(), properties), () -> BlockBehaviour.Properties.of()
                .mapColor(mapColor)
                .forceSolidOn()
                .instrument(NoteBlockInstrument.BASS)
                .noCollission()
                .strength(1.0F)
                .ignitedByLava());
        DeferredBlock<WallSignBlock> wallSign = register(provider, name + "_wall_sign", properties -> new WallSignBlock(woodType.get(), properties), () -> Blocks.wallVariant(sign.get(), true)
                .mapColor(MapColor.WOOD)
                .forceSolidOn()
                .instrument(NoteBlockInstrument.BASS)
                .noCollission()
                .strength(1.0F)
                .ignitedByLava());
        itemProvider.registerItem(name + "_sign", properties -> new SignItem(sign.get(), wallSign.get(), properties), new Item.Properties().stacksTo(16));
        DeferredBlock<CeilingHangingSignBlock> hangingSign = register(provider, name + "_hanging_sign", properties -> new CeilingHangingSignBlock(woodType.get(), properties), () -> BlockBehaviour.Properties.of()
                .mapColor(log.get().defaultMapColor())
                .forceSolidOn()
                .instrument(NoteBlockInstrument.BASS)
                .noCollission()
                .strength(1.0F)
                .ignitedByLava());
        DeferredBlock<WallHangingSignBlock> wallHangingSign = register(provider, name + "_wall_hanging_sign", properties -> new WallHangingSignBlock(woodType.get(), properties), () -> Blocks.wallVariant(hangingSign.get(), true)
                .mapColor(log.get().defaultMapColor())
                .forceSolidOn()
                .instrument(NoteBlockInstrument.BASS)
                .noCollission()
                .strength(1.0F)
                .ignitedByLava());
        itemProvider.registerItem(name + "_hanging_sign", properties -> new HangingSignItem(hangingSign.get(), wallHangingSign.get(), properties), new Item.Properties().stacksTo(16));
        DeferredBlock<DoorBlock> door = register(provider, name + "_door", properties -> new DoorBlock(woodType.get().setType(), properties), () -> BlockBehaviour.Properties.of()
                .mapColor(planks.get().defaultMapColor())
                .instrument(NoteBlockInstrument.BASS)
                .strength(3.0F)
                .noOcclusion()
                .ignitedByLava()
                .pushReaction(PushReaction.DESTROY));
        itemProvider.registerItem(name + "_door", properties -> new DoubleHighBlockItem(door.get(), properties), new Item.Properties());
        TagKey<Block> logsTag = provider.createTagKey(name + "_logs");
        return new WoodSet(ResourceLocation.fromNamespaceAndPath(provider.getNamespace(), name),
                log,
                strippedLog,
                wood,
                strippedWood,
                planks,
                registerBlockAndItemAndWrap(provider, name + "_slab", SlabBlock::new, () -> BlockBehaviour.Properties.of()
                        .mapColor(mapColor)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.0F, 3.0F)
                        .sound(woodType.get().soundType())
                        .ignitedByLava(), itemProvider),
                registerBlockAndItemAndWrap(provider, name + "_stairs", properties -> new StairBlock(planks.get().defaultBlockState(), properties), () -> BlockBehaviour.Properties.ofLegacyCopy(planks.get()), itemProvider),
                registerBlockAndItemAndWrap(provider, name + "_pressure_plate", properties -> new PressurePlateBlock(woodType.get().setType(), properties), () -> BlockBehaviour.Properties.of()
                        .mapColor(planks.get().defaultMapColor())
                        .forceSolidOn()
                        .instrument(NoteBlockInstrument.BASS)
                        .noCollission()
                        .strength(0.5F)
                        .ignitedByLava()
                        .pushReaction(PushReaction.DESTROY), itemProvider),
                registerBlockAndItemAndWrap(provider, name + "_button", properties -> new ButtonBlock(woodType.get().setType(), 30, properties), Blocks::buttonProperties, itemProvider),
                registerBlockAndItemAndWrap(provider, name + "_fence", FenceBlock::new, () -> BlockBehaviour.Properties.of()
                        .mapColor(planks.get().defaultMapColor())
                        .forceSolidOn()
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.0F, 3.0F)
                        .sound(woodType.get().soundType())
                        .ignitedByLava(), itemProvider),
                registerBlockAndItemAndWrap(provider, name + "_fence_gate", properties -> new FenceGateBlock(woodType.get(), properties), () -> BlockBehaviour.Properties.of()
                        .mapColor(planks.get().defaultMapColor())
                        .forceSolidOn()
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.0F, 3.0F)
                        .ignitedByLava(), itemProvider),
                door,
                registerBlockAndItemAndWrap(provider, name + "_trapdoor", properties -> new TrapDoorBlock(woodType.get().setType(), properties), () -> BlockBehaviour.Properties.of()
                        .mapColor(mapColor)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(3.0F)
                        .noOcclusion()
                        .isValidSpawn(Blocks::never)
                        .ignitedByLava(), itemProvider),
                sign,
                wallSign,
                hangingSign,
                wallHangingSign,
                itemProvider.registerItem(name + "_boat", properties -> new BoatItem(boat, properties), new Item.Properties().stacksTo(1)),
                itemProvider.registerItem(name + "_chest_boat", properties -> new BoatItem(chestBoat, properties), new Item.Properties().stacksTo(1)),
                logsTag,
                TagKey.create(Registries.ITEM, logsTag.location()));
    }

    /**
     * Registers all the blocks and items in a {@link LeavesSet} with a {@link TreeGrower}
     * 
     * @param provider     The provider to register with
     * @param name         The name of the {@link LeavesSet}
     * @param treeGrower   The tree grower for the sapling
     * @param itemProvider The provider to register items with
     * @return The {@link LeavesSet} containing all the registered objects
     */
    public static LeavesSet registerLeavesSet(DeferredRegister.Blocks provider, String name, TreeGrower treeGrower, DeferredRegister.Items itemProvider) {
        DeferredBlock<SaplingBlock> sapling = registerBlockAndItemAndWrap(provider, name + "_sapling", properties -> new SaplingBlock(treeGrower, properties), () -> BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.GRASS)
                .pushReaction(PushReaction.DESTROY), itemProvider);
        return new LeavesSet(ResourceLocation.fromNamespaceAndPath(provider.getNamespace(), name),
                registerBlockAndItemAndWrap(provider, name + "_leaves", properties -> new TintedParticleLeavesBlock(0.01F, properties), () -> Blocks.leavesProperties(SoundType.GRASS), itemProvider),
                sapling,
                register(provider, "potted_" + name + "_sapling", properties -> new FlowerPotBlock(sapling.get(), properties), Blocks::flowerPotProperties));
    }

    /**
     * Registers all the blocks and items in a {@link LeavesSet} with an {@link ExtendedTreeGrower}
     * 
     * @param provider     The provider to register with
     * @param name         The name of the {@link LeavesSet}
     * @param treeGrower   The tree grower for the sapling
     * @param itemProvider The provider to register items with
     * @return The {@link LeavesSet} containing all the registered objects
     */
    public static LeavesSet registerLeavesSet(DeferredRegister.Blocks provider, String name, ExtendedTreeGrower treeGrower, DeferredRegister.Items itemProvider) {
        DeferredBlock<SaplingBlock> sapling = registerBlockAndItemAndWrap(provider, name + "_sapling", properties -> new ExtendedSaplingBlock(treeGrower, properties), () -> BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.GRASS)
                .pushReaction(PushReaction.DESTROY), itemProvider);
        return new LeavesSet(ResourceLocation.fromNamespaceAndPath(provider.getNamespace(), name),
                registerBlockAndItemAndWrap(provider, name + "_leaves", properties -> new TintedParticleLeavesBlock(0.01F, properties), () -> Blocks.leavesProperties(SoundType.GRASS), itemProvider),
                sapling,
                register(provider, "potted_" + name + "_sapling", properties -> new FlowerPotBlock(sapling.get(), properties), Blocks::flowerPotProperties));
    }

    /**
     * Gets the stripped version of a block
     * 
     * @param originalState The original block state
     * @return The stripped block
     */
    public static Block getStripped(BlockState originalState) {
        DeferredBlock<?> ro = STRIPPABLES.get(originalState.getBlock().builtInRegistryHolder().key().location());
        return ro != null ? ro.get() : null;
    }
}
