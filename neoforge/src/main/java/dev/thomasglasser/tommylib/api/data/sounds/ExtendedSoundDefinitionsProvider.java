package dev.thomasglasser.tommylib.api.data.sounds;

import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

import java.util.ArrayList;

/**
 * Extension of {@link SoundDefinitionsProvider} that provides functionality for mod holders.
 */
public abstract class ExtendedSoundDefinitionsProvider extends SoundDefinitionsProvider
{
	/**
	 * Creates a new instance of this data provider.
	 *
	 * @param output The {@linkplain PackOutput} instance provided by the data generator.
	 * @param modId  The mod ID of the current mod.
	 * @param helper The existing file helper provided by the event you are initializing this provider in.
	 */
	protected ExtendedSoundDefinitionsProvider(PackOutput output, String modId, ExistingFileHelper helper)
	{
		super(output, modId, helper);
	}

	/**
	 * Defines a {@link SoundDefinition} with a subtitle.
	 * @param subtitle The subtitle to use.
	 * @param sounds The sounds to use.
	 * @return The defined {@link SoundDefinition}.
	 */
	private SoundDefinition define(String subtitle, SoundDefinition.Sound... sounds)
	{
		return SoundDefinition.definition().with(sounds).subtitle("subtitles." + subtitle);
	}

	/**
	 * Adds a sound to the provider.
	 * @param sound The sound to add.
	 */
	protected void add(DeferredHolder<SoundEvent, ?> sound)
	{
		add(sound.get(), define(sound.get().getLocation().getPath(), sound(sound.get().getLocation())));
	}

	/**
	 * Adds a sound to the provider with multiple variants.
	 * @param sound The sound to add.
	 * @param variants The number of variants to add.
	 */
	protected void add(DeferredHolder<SoundEvent, ?> sound, int variants)
	{
		add(sound.get(), defineVariants(sound.get().getLocation().getPath(), sound.get().getLocation(), variants));
	}

	/**
	 * Defines a {@link SoundDefinition} with multiple variants.
	 * @param subtitle The subtitle to use.
	 * @param sound The sound to use.
	 * @param variants The number of variants to add.
	 * @return The defined {@link SoundDefinition}.
	 */
	private SoundDefinition defineVariants(String subtitle, ResourceLocation sound, int variants)
	{
		ArrayList<SoundDefinition.Sound> sounds = new ArrayList<>();
		for (int i = 1; i < variants + 1; i++)
		{
			sounds.add(sound(new ResourceLocation(sound.getNamespace(), sound.getPath() + i)));
		}
		return define(subtitle, sounds.toArray(new SoundDefinition.Sound[] {}));
	}

	/**
	 * Gets a sound from a {@link ResourceLocation}.
	 * @param location The location of the sound.
	 * @return The sound.
	 */
	protected static SoundDefinition.Sound sound(ResourceLocation location)
	{
		if (location.getPath().contains("."))
		{
			return sound(new ResourceLocation(location.getNamespace(), location.getPath().replace('.', '/')));
		}
		return SoundDefinitionsProvider.sound(location);
	}
}
