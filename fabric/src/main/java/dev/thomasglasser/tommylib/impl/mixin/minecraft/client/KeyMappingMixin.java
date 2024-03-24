package dev.thomasglasser.tommylib.impl.mixin.minecraft.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.resources.language.I18n;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(KeyMapping.class)
public class KeyMappingMixin implements Comparable<KeyMapping>
{
	@Shadow @Final public static Map<String, Integer> CATEGORY_SORT_ORDER;

	@Shadow @Final private String category;

	@Shadow @Final private String name;

	/**
	 * @author Thomas Glasser
	 * Fixes errors with using the order map, code copied from NeoForge
	 */
	@Override
	@Overwrite
	public int compareTo(KeyMapping other) {
		if (this.category.equals(other.getCategory())) return I18n.get(this.name).compareTo(I18n.get(other.getName()));
		Integer tCat = CATEGORY_SORT_ORDER.get(this.category);
		Integer oCat = CATEGORY_SORT_ORDER.get(other.getCategory());
		if (tCat == null && oCat != null) return 1;
		if (tCat != null && oCat == null) return -1;
		if (tCat == null) return I18n.get(this.category).compareTo(I18n.get(other.getCategory()));
		return  tCat.compareTo(oCat);
	}
}
