package dev.thomasglasser.tommylib.impl.client;

import dev.thomasglasser.tommylib.api.client.ClientUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.BuiltInRegistries;

public class TommyLibFabricClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		ItemGroupEvents.MODIFY_ENTRIES_ALL.register((group, entries) ->
				entries.acceptAll(ClientUtils.getItemsForTab(BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(group).orElseThrow())));
	}
}
