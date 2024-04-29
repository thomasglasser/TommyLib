package dev.thomasglasser.tommylib.api.packs;

import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.KnownPack;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;

public record PackInfo(KnownPack knownPack, PackType type, PackSource source)
{
	public static final PackSelectionConfig BUILT_IN_SELECTION_CONFIG = new PackSelectionConfig(false, Pack.Position.TOP, false);

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
		return "pack." + this.knownPack.namespace() + "." + this.knownPack.id();
	}
}
