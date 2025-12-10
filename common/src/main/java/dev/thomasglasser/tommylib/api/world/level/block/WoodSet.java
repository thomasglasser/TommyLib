package dev.thomasglasser.tommylib.api.world.level.block;

import dev.thomasglasser.tommylib.api.registration.DeferredBlock;
import dev.thomasglasser.tommylib.api.registration.DeferredItem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;

/**
 * Represents a set of blocks that are all part of the same wood type.
 * 
 * @param id              The ID of the wood type
 * @param log             The log block holder
 * @param strippedLog     The stripped log block holder
 * @param wood            The wood block holder
 * @param strippedWood    The stripped wood block holder
 * @param planks          The planks block holder
 * @param slab            The slab block holder
 * @param stairs          The stairs block holder
 * @param pressurePlate   The pressure plate block holder
 * @param button          The button block holder
 * @param fence           The fence block holder
 * @param fenceGate       The fence gate block holder
 * @param door            The door block holder
 * @param trapdoor        The trapdoor block holder
 * @param sign            The sign block holder
 * @param wallSign        The wall sign block holder
 * @param hangingSign     The hanging sign block holder
 * @param wallHangingSign The wall hanging sign block holder
 * @param boatItem        The boat item holder
 * @param chestBoatItem   The chest boat item holder
 * @param logsBlockTag    The tag key for the log block tag
 * @param logsItemTag     The tag key for the log item tag
 */
public record WoodSet(Identifier id,
        DeferredBlock<? extends RotatedPillarBlock> log,
        DeferredBlock<? extends RotatedPillarBlock> strippedLog,
        DeferredBlock<? extends RotatedPillarBlock> wood,
        DeferredBlock<? extends RotatedPillarBlock> strippedWood,
        DeferredBlock<?> planks,
        DeferredBlock<? extends SlabBlock> slab,
        DeferredBlock<? extends StairBlock> stairs,
        DeferredBlock<? extends PressurePlateBlock> pressurePlate,
        DeferredBlock<? extends ButtonBlock> button,
        DeferredBlock<? extends FenceBlock> fence,
        DeferredBlock<? extends FenceGateBlock> fenceGate,
        DeferredBlock<? extends DoorBlock> door,
        DeferredBlock<? extends TrapDoorBlock> trapdoor,
        DeferredBlock<? extends StandingSignBlock> sign,
        DeferredBlock<? extends WallSignBlock> wallSign,
        DeferredBlock<? extends CeilingHangingSignBlock> hangingSign,
        DeferredBlock<? extends WallHangingSignBlock> wallHangingSign,
        DeferredItem<? extends BoatItem> boatItem,
        DeferredItem<? extends BoatItem> chestBoatItem,
        TagKey<Block> logsBlockTag,
        TagKey<Item> logsItemTag) {
    /**
     * Gets all blocks in this set.
     * 
     * @return A list of all blocks in this set
     */
    public List<Block> getAllBlocks() {
        return List.of(log.get(), strippedLog.get(), wood.get(), strippedWood.get(), planks.get(), slab.get(), stairs.get(), pressurePlate.get(), button.get(), fence.get(), fenceGate.get(), door.get(), trapdoor.get(), sign.get(), wallSign.get(), hangingSign.get(), wallHangingSign.get());
    }

    /**
     * Gets both boat items in this set.
     *
     * @return A list of both boat items in this set
     */
    public List<Item> getBoatItems() {
        return List.of(boatItem.get(), chestBoatItem.get());
    }

    /**
     * Gets all items in this set.
     *
     * @return A list of all items in this set
     */
    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>(getAllBlocks().stream().map(Block::asItem).toList());
        items.addAll(getBoatItems());
        return items;
    }

    /**
     * Converts this set to a block family for data generation.
     * 
     * @return The block family
     */
    public BlockFamily toBlockFamily() {
        return new BlockFamily.Builder(planks().get())
                .button(button().get())
                .fence(fence().get())
                .fenceGate(fenceGate().get())
                .pressurePlate(pressurePlate().get())
                .sign(sign().get(), wallSign().get())
                .slab(slab().get())
                .stairs(stairs().get())
                .door(door().get())
                .trapdoor(trapdoor().get())
                .recipeGroupPrefix("wooden")
                .recipeUnlockedBy("has_planks")
                .getFamily();
    }
}
