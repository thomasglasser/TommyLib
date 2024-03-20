package dev.thomasglasser.tommylib.impl;

import dev.thomasglasser.tommylib.api.world.item.armor.GeoArmorItem;
import net.minecraft.world.item.Item;

public class GeckoLibUtils
{
	public static boolean isGeoArmorItem(Object obj)
	{
		return obj instanceof GeoArmorItem;
	}

	public static boolean isSkintight(Item item)
	{
		return item instanceof GeoArmorItem geoArmorItem && geoArmorItem.isSkintight();
	}
}
