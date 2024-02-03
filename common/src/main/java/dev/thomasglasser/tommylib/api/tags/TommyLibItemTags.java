package dev.thomasglasser.tommylib.api.tags;

import dev.thomasglasser.tommylib.TommyLib;
import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public class TommyLibItemTags
{
	// Loader tags
	public static final TagKey<Item> WOODEN_RODS = create("wooden_rods");
	public static final TagKey<Item> IRON_INGOTS = create("iron_ingots");
	public static final Map<DyeColor, TagKey<Item>> DYES_MAP = dyesMap();

	private static TagKey<Item> create(String name)
	{
		return TagKey.create(Registries.ITEM, TommyLib.modLoc(name));
	}

	public static TagKey<Item> create(ResourceLocation name)
	{
		return TagKey.create(Registries.ITEM, name);
	}

	private static Map<DyeColor, TagKey<Item>> dyesMap()
	{
		Map<DyeColor, TagKey<Item>> map = new HashMap<>();
		for (DyeColor color : DyeColor.values())
		{
			map.put(color, create(color.getName() + "_dyes"));
		}
		return map;
	}
	public static TagKey<Item> logs(WoodSet set)
	{
		return create(new ResourceLocation(set.id().getNamespace(), set.id().getPath() + "_logs"));
	}
}
