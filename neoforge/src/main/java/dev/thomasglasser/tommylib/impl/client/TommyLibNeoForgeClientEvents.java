package dev.thomasglasser.tommylib.impl.client;

import dev.thomasglasser.tommylib.api.client.ClientUtils;
import dev.thomasglasser.tommylib.api.client.animation.AnimationUtils;
import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

public class TommyLibNeoForgeClientEvents
{
	public static void onClientSetup(FMLClientSetupEvent event)
	{
		if (TommyLibServices.PLATFORM.isModLoaded("playeranimator"))
			AnimationUtils.registerPlayerForAnimation();
	}

	public static void onBuildCreativeTabContent(BuildCreativeModeTabContentsEvent event)
	{
		event.acceptAll(ClientUtils.getItemsForTab(event.getTabKey()));
	}

	public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event)
	{
		ClientUtils.getKeyMappings().forEach(event::register);
	}

	public static void onEntityJoinLevel(EntityJoinLevelEvent event)
	{
		if (event.getLevel().isClientSide)
		{
			TommyLibClientEvents.onEntityJoinLevel(event.getEntity());
		}
	}
}
