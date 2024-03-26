package dev.thomasglasser.tommylib.impl.data.tags;

import com.mojang.datafixers.util.Pair;
import dev.thomasglasser.tommylib.TommyLib;
import dev.thomasglasser.tommylib.api.data.tags.ExtendedItemTagsProvider;
import dev.thomasglasser.tommylib.api.tags.TommyLibItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class TommyLibItemTagsProvider extends ExtendedItemTagsProvider
{
	private static final Pair<ResourceLocation, ResourceLocation> WOODEN_RODS = Pair.of(neoforgeLoc("rods/wooden"), cLoc("wooden_rods"));

	public TommyLibItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future, CompletableFuture<TagLookup<Block>> blockTagsProvider, ExistingFileHelper existingFileHelper)
	{
		super(output, future, blockTagsProvider, TommyLib.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider lookupProvider)
	{
		tag(TommyLibItemTags.WOODEN_RODS)
				.add(Items.STICK)
				.addOptionalTag(WOODEN_RODS.getFirst())
				.addOptionalTag(WOODEN_RODS.getSecond())
				.addOptionalTag(cLoc("wood_sticks"));
		tag(TommyLibItemTags.IRON_INGOTS)
				.add(Items.IRON_INGOT)
				.addOptionalTag(neoforgeLoc("ingots/iron"))
				.addOptionalTag(cLoc("iron_ingots"));
		TommyLibItemTags.DYES_MAP.forEach((dyeColor, itemTagKey) ->
				tag(itemTagKey)
						.add(DyeItem.byColor(dyeColor))
						.addOptionalTag(neoforgeLoc("dyes/" + dyeColor.getName()))
						.addOptionalTag(cLoc(dyeColor.getName() + "_dyes")));

		tag(TommyLibItemTags.UNBREAKABLE)
				.add(Items.BEDROCK)
				.add(Items.BARRIER)
				.add(Items.CHAIN_COMMAND_BLOCK)
				.add(Items.COMMAND_BLOCK)
				.add(Items.REPEATING_COMMAND_BLOCK)
				.add(Items.END_PORTAL_FRAME)
				.add(Items.JIGSAW)
				.add(Items.LIGHT)
				.add(Items.AIR)
				.add(Items.STRUCTURE_VOID)
				.add(Items.STRUCTURE_BLOCK);
	}
}
