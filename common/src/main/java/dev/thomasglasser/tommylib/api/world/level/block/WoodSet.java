package dev.thomasglasser.tommylib.api.world.level.block;

import dev.thomasglasser.tommylib.api.registration.DeferredBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.Supplier;

/**
 * Represents a set of blocks that are all part of the same wood type.
 * @param id The ID of the wood type
 * @param planks The planks block holder
 * @param log The log block holder
 * @param strippedLog The stripped log block holder
 * @param wood The wood block holder
 * @param strippedWood The stripped wood block holder
 * @param logsBlockTag The tag key for the log block tag
 * @param logsItemTag The tag key for the log item tag
 */
public record WoodSet(ResourceLocation id,
                      DeferredBlock<?> planks,
                      DeferredBlock<?> log,
                      DeferredBlock<?> strippedLog,
                      DeferredBlock<?> wood,
                      DeferredBlock<?> strippedWood,
                      Supplier<TagKey<Block>> logsBlockTag,
                      Supplier<TagKey<Item>> logsItemTag)
{
	/**
	 * Gets all blocks in this set.
	 * @return A list of all blocks in this set
	 */
	public List<Block> getAll()
	{
		return List.of(planks.get(), log.get(), strippedLog.get(), wood.get(), strippedWood.get());
	}
}
