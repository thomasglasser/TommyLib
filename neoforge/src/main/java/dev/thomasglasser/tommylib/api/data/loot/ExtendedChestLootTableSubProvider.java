package dev.thomasglasser.tommylib.api.data.loot;

import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;

public abstract class ExtendedChestLootTableSubProvider implements LootTableSubProvider
{
	protected String modId;

	public ExtendedChestLootTableSubProvider(String modId)
	{
		this.modId = modId;
	}

	protected ResourceLocation modLoc(String name)
	{
		return new ResourceLocation(modId, "chests/" + name);
	}
}
