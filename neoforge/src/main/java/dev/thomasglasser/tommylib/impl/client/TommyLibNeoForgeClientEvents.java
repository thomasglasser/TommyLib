package dev.thomasglasser.tommylib.impl.client;

import dev.thomasglasser.tommylib.api.client.ClientUtils;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

public class TommyLibNeoForgeClientEvents
{
	public static void onBuildCreativeTabContent(BuildCreativeModeTabContentsEvent event)
	{
		event.acceptAll(ClientUtils.getItemsForTab(event.getTabKey()));
	}

	public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event)
	{
		ClientUtils.getKeyMappings().forEach(event::register);
	}
}
