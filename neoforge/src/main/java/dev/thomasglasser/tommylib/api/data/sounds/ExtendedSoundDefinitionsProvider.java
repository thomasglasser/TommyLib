package dev.thomasglasser.tommylib.api.data.sounds;

import dev.thomasglasser.tommylib.api.registration.RegistryObject;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

import java.util.ArrayList;

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

	private SoundDefinition define(String subtitle, SoundDefinition.Sound... sounds)
	{
		return SoundDefinition.definition().with(sounds).subtitle("subtitles." + subtitle);
	}

	protected void add(RegistryObject<SoundEvent> sound)
	{
		add(sound, define(sound.get().getLocation().getPath(), sound(sound.get().getLocation())));
	}

	protected void add(RegistryObject<SoundEvent> sound, int variants)
	{
		add(sound, defineVariants(sound.get().getLocation().getPath(), sound.get().getLocation(), variants));
	}

	private SoundDefinition defineVariants(String subtitle, ResourceLocation sound, int variants)
	{
		ArrayList<SoundDefinition.Sound> sounds = new ArrayList<>();
		for (int i = 1; i < variants + 1; i++)
		{
			sounds.add(sound(new ResourceLocation(sound.getNamespace(), sound.getPath() + i)));
		}
		return define(subtitle, sounds.toArray(new SoundDefinition.Sound[] {}));
	}

	protected static SoundDefinition.Sound sound(ResourceLocation location)
	{
		if (location.getPath().contains("."))
		{
			return sound(new ResourceLocation(location.getNamespace(), location.getPath().replace('.', '/')));
		}
		return SoundDefinitionsProvider.sound(location);
	}
}
