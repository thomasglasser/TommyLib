package dev.thomasglasser.tommylib.impl.client;

import dev.thomasglasser.tommylib.api.client.ClientUtils;
import dev.thomasglasser.tommylib.api.world.item.ItemUtils;
import dev.thomasglasser.tommylib.api.world.item.ModeledItem;
import dev.thomasglasser.tommylib.impl.GeckoLibUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
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

	public static void registerClientReloadListeners(RegisterClientReloadListenersEvent event)
	{
		BuiltInRegistries.ITEM.stream().forEach(item ->
		{
			if (item instanceof ModeledItem)
				event.registerReloadListener(IClientItemExtensions.of(item).getCustomRenderer());
			else if (ItemUtils.isGeckoLoaded() && GeckoLibUtils.isGeoArmorItem(item))
				event.registerReloadListener(IClientItemExtensions.of(item).getCustomRenderer());
		});
	}
}
