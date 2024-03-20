package dev.thomasglasser.tommylib.api.world.item.armor;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

public abstract class BaseGeoArmorItem extends ArmorItem implements GeoArmorItem
{
	protected BaseGeoArmorItem(ArmorMaterial material, Type type, Properties properties)
	{
		super(material, type, properties);
	}
}
