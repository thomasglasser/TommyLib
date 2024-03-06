package dev.thomasglasser.tommylib.api.packs;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

public record PackHolder(ResourceLocation id, boolean required, PackType type)
{
	public String titleKey()
	{
		return key() + ".name";
	}
	public String descriptionKey()
	{
		return key() + ".description";
	}

	private String key()
	{
		return "pack." + this.id.getNamespace() + "." + this.id.getPath();
	}
}
