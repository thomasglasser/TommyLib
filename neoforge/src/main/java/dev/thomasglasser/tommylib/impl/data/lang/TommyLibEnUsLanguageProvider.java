package dev.thomasglasser.tommylib.impl.data.lang;

import dev.thomasglasser.tommylib.TommyLib;
import dev.thomasglasser.tommylib.api.data.lang.ExtendedLanguageProvider;
import dev.thomasglasser.tommylib.api.tags.ConventionalBlockTags;
import dev.thomasglasser.tommylib.api.tags.ConventionalItemTags;
import net.minecraft.data.PackOutput;

public class TommyLibEnUsLanguageProvider extends ExtendedLanguageProvider
{
	public TommyLibEnUsLanguageProvider(PackOutput output)
	{
		super(output, TommyLib.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations()
	{
		add(ConventionalBlockTags.UNBREAKABLE_BLOCKS, "Unbreakable Blocks");
		add(ConventionalItemTags.UNBREAKABLE_BLOCKS, "Unbreakable Blocks");
	}
}
