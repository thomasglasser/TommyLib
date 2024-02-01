package dev.thomasglasser.tommylib.api.tags;

import dev.thomasglasser.tommylib.TommyLib;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class TommyLibBlockTags
{
	public static final TagKey<Block> UNBREAKABLE = create("unbreakable");

	private static TagKey<Block> create(String name)
	{
		return TagKey.create(Registries.BLOCK, TommyLib.modLoc(name));
	}
}
