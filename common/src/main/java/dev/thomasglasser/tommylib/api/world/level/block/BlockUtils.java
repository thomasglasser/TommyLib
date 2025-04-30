package dev.thomasglasser.tommylib.api.world.level.block;

import dev.thomasglasser.tommylib.api.registration.DeferredBlock;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;
import dev.thomasglasser.tommylib.api.tags.TagUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
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
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
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
import org.jetbrains.annotations.Nullable;

public class BlockUtils {
    /**
     * Map of blocks that can be stripped to their stripped versions
     */
    private static final Map<ResourceLocation, DeferredBlock<?>> STRIPPABLES = new Object2ObjectOpenHashMap<>();

    /**
     * Register a block with the given provider and name
     * 
     * @param provider The provider to register the block with
     * @param name     The registry name of the block
     * @param block    The block supplier
     * @return The block holder
     * @param <T> The block type
     */
    public static <T extends Block> DeferredBlock<T> register(DeferredRegister.Blocks provider, String name, Supplier<T> block) {
        return provider.register(name, block);
    }

    /**
     * Register a block and item with the given provider and name
     * 
     * @param provider     The provider to register the block with
     * @param name         The registry name of the block
     * @param blockFactory The block supplier
     * @param itemProvider The provider to register the item with
     * @return The block holder
     * @param <T> The block type
     */
    public static <T extends Block> DeferredBlock<T> registerBlockAndItemAndWrap(
            DeferredRegister.Blocks provider,
            String name,
            Supplier<T> blockFactory,
            DeferredRegister.Items itemProvider) {
        DeferredBlock<T> block = register(provider, name, blockFactory);
        itemProvider.registerSimpleBlockItem(block);
        return block;
    }

    /**
     * Register a block and item with the given provider and name
     *
     * @param provider          The provider to register the block with
     * @param name              The registry name of the block
     * @param blockFactory      The block supplier
     * @param itemProvider      The provider to register the item with
     * @param blockItemFunction The function to create the block item
     * @return The block holder
     * @param <T> The block type
     */
    public static <T extends Block> DeferredBlock<T> registerBlockAndItemAndWrap(
            DeferredRegister.Blocks provider,
            String name,
            Supplier<T> blockFactory,
            DeferredRegister.Items itemProvider,
            Function<Block, Item> blockItemFunction) {
        DeferredBlock<T> block = register(provider, name, blockFactory);
        itemProvider.register(name, () -> blockItemFunction.apply(block.get()));
        return block;
    }

    /**
     * Register a block and item with the given provider and name
     * 
     * @param provider     The provider to register the block with
     * @param name         The registry name of the block
     * @param blockFactory The block supplier
     * @param itemProvider The provider to register the item with
     * @param properties   The item properties
     * @return The block holder
     * @param <T> The block type
     */
    public static <T extends Block> DeferredBlock<T> registerBlockAndItemAndWrap(
            DeferredRegister.Blocks provider,
            String name,
            Supplier<T> blockFactory,
            DeferredRegister.Items itemProvider,
            Item.Properties properties) {
        DeferredBlock<T> block = register(provider, name, blockFactory);
        itemProvider.registerSimpleBlockItem(block, properties);
        return block;
    }

    /**
     * Register a {@link WoodSet} with the given provider and name in the provider's namespace
     *
     * @param provider     The provider to register the blocks with
     * @param name         The name of the wood set
     * @param mapColor     The map color of the wood set
     * @param logMapColor  The map color of the logs
     * @param woodType     The wood type of the set
     * @param boatType     The boat type of the set
     * @param itemProvider The provider to register the item with
     * @return The wood set
     */
    public static WoodSet registerWoodSet(DeferredRegister.Blocks provider, String name, MapColor mapColor, MapColor logMapColor, Supplier<WoodType> woodType, Boat.Type boatType, DeferredRegister.Items itemProvider) {
        DeferredBlock<RotatedPillarBlock> log = registerBlockAndItemAndWrap(provider, name + "_log", () -> (RotatedPillarBlock) Blocks.log(mapColor, logMapColor, woodType.get().soundType()), itemProvider);
        DeferredBlock<RotatedPillarBlock> strippedLog = registerBlockAndItemAndWrap(provider, "stripped_" + name + "_log", () -> (RotatedPillarBlock) Blocks.log(mapColor, mapColor, woodType.get().soundType()), itemProvider);
        DeferredBlock<RotatedPillarBlock> wood = registerBlockAndItemAndWrap(provider, name + "_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(mapColor).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(woodType.get().soundType()).ignitedByLava()), itemProvider);
        DeferredBlock<RotatedPillarBlock> strippedWood = registerBlockAndItemAndWrap(provider, "stripped_" + name + "_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(mapColor).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(woodType.get().soundType()).ignitedByLava()), itemProvider);
        STRIPPABLES.put(log.getId(), strippedLog);
        STRIPPABLES.put(wood.getId(), strippedWood);
        DeferredBlock<Block> planks = registerBlockAndItemAndWrap(provider, name + "_planks", () -> new Block(BlockBehaviour.Properties.of().mapColor(mapColor).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(woodType.get().soundType()).ignitedByLava()), itemProvider);
        DeferredBlock<StandingSignBlock> sign = register(provider, name + "_sign", () -> new StandingSignBlock(woodType.get(), BlockBehaviour.Properties.of().mapColor(mapColor).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).ignitedByLava()));
        DeferredBlock<WallSignBlock> wallSign = register(provider, name + "_wall_sign", () -> new WallSignBlock(woodType.get(), BlockBehaviour.Properties.of().mapColor(mapColor).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).dropsLike(sign.get()).ignitedByLava()));
        itemProvider.register(name + "_sign", () -> new SignItem(new Item.Properties().stacksTo(16), sign.get(), wallSign.get()));
        DeferredBlock<CeilingHangingSignBlock> hangingSign = register(provider, name + "_hanging_sign", () -> new CeilingHangingSignBlock(woodType.get(), BlockBehaviour.Properties.of().mapColor(mapColor).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).ignitedByLava()));
        DeferredBlock<WallHangingSignBlock> wallHangingSign = register(provider, name + "_wall_hanging_sign", () -> new WallHangingSignBlock(woodType.get(), BlockBehaviour.Properties.of().mapColor(mapColor).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).dropsLike(hangingSign.get()).ignitedByLava()));
        itemProvider.register(name + "_hanging_sign", () -> new HangingSignItem(hangingSign.get(), wallHangingSign.get(), new Item.Properties().stacksTo(16)));
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(provider.getNamespace(), name);
        return new WoodSet(id,
                log,
                strippedLog,
                wood,
                strippedWood,
                planks,
                registerBlockAndItemAndWrap(provider, name + "_slab", () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(mapColor).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(woodType.get().soundType()).ignitedByLava()), itemProvider),
                registerBlockAndItemAndWrap(provider, name + "_stairs", () -> (StairBlock) Blocks.legacyStair(planks.get()), itemProvider),
                registerBlockAndItemAndWrap(provider, name + "_pressure_plate", () -> new PressurePlateBlock(woodType.get().setType(), BlockBehaviour.Properties.of().mapColor(mapColor).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(0.5F).ignitedByLava().pushReaction(PushReaction.DESTROY)), itemProvider),
                registerBlockAndItemAndWrap(provider, name + "_button", () -> (ButtonBlock) Blocks.woodenButton(woodType.get().setType()), itemProvider),
                registerBlockAndItemAndWrap(provider, name + "_fence", () -> new FenceBlock(BlockBehaviour.Properties.of().mapColor(mapColor).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).ignitedByLava().sound(woodType.get().soundType())), itemProvider),
                registerBlockAndItemAndWrap(provider, name + "_fence_gate", () -> new FenceGateBlock(woodType.get(), BlockBehaviour.Properties.of().mapColor(mapColor).forceSolidOn().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).ignitedByLava()), itemProvider),
                registerBlockAndItemAndWrap(provider, name + "_door", () -> new DoorBlock(woodType.get().setType(), BlockBehaviour.Properties.of().mapColor(mapColor).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY)), itemProvider, (block) -> new DoubleHighBlockItem(block, new Item.Properties())),
                registerBlockAndItemAndWrap(provider, name + "_trapdoor", () -> new TrapDoorBlock(woodType.get().setType(), BlockBehaviour.Properties.of().mapColor(mapColor).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().isValidSpawn(Blocks::never).ignitedByLava()), itemProvider),
                sign,
                wallSign,
                hangingSign,
                wallHangingSign,
                itemProvider.register(name + "_boat", () -> new BoatItem(false, boatType, new Item.Properties().stacksTo(1))),
                itemProvider.register(name + "_chest_boat", () -> new BoatItem(true, boatType, new Item.Properties().stacksTo(1))),
                TagUtils.createLogs(Registries.BLOCK, id),
                TagUtils.createLogs(Registries.ITEM, id));
    }

    /**
     * Register a {@link LeavesSet} with the given provider and name in the provider's namespace
     *
     * @param provider     The provider to register the blocks with
     * @param treeGrower   The tree grower for the sapling
     * @param itemProvider The provider to register the item with
     * @return The leaves set
     */
    public static LeavesSet registerLeavesSet(DeferredRegister.Blocks provider, String name, TreeGrower treeGrower, DeferredRegister.Items itemProvider) {
        DeferredBlock<?> sapling = registerBlockAndItemAndWrap(provider, name + "_sapling", () -> new SaplingBlock(treeGrower, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)), itemProvider);
        return new LeavesSet(ResourceLocation.fromNamespaceAndPath(provider.getNamespace(), name),
                registerBlockAndItemAndWrap(provider, name + "_leaves", () -> Blocks.leaves(SoundType.GRASS), itemProvider),
                sapling,
                register(provider, "potted_" + name + "_sapling", () -> Blocks.flowerPot(sapling.get())));
    }

    /**
     * Gets the stripped version of a block
     * 
     * @param originalState The original block state
     * @return The stripped block
     */
    public static @Nullable Block getStripped(BlockState originalState) {
        DeferredBlock<?> ro = STRIPPABLES.get(originalState.getBlock().builtInRegistryHolder().key().location());
        return ro != null ? ro.get() : null;
    }
}
