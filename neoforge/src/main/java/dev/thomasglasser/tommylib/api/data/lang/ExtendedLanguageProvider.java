package dev.thomasglasser.tommylib.api.data.lang;

import dev.thomasglasser.tommylib.api.packs.PackInfo;
import dev.thomasglasser.tommylib.api.registration.RegistryObject;
import dev.thomasglasser.tommylib.api.world.level.block.LeavesSet;
import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import net.minecraft.client.KeyMapping;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.apache.commons.lang3.text.WordUtils;

public abstract class ExtendedLanguageProvider extends LanguageProvider
{
	protected String modId;

	public ExtendedLanguageProvider(PackOutput output, String modid, String locale)
	{
		super(output, modid, locale);
		modId = modid;
	}

	public void addDesc(Item item, String desc)
	{
		add(item.getDescriptionId() + ".desc", desc);
	}

	public void add(BannerPattern pattern, String name)
	{
		for (DyeColor color: DyeColor.values())
		{
			add("block.minecraft.banner." + modId + "." + pattern.getHashname() + "." + color.getName(), WordUtils.capitalize(color.getName().replace('_', ' ')) + " " + name);
		}
	}

	public void add(Item key, Potion potion, String name) {
		add(PotionUtils.setPotion(new ItemStack(key), potion), name);
	}

	public void addBiome(ResourceKey<Biome> biome, String name)
	{
		add("biome." + biome.location().getNamespace() + "." + biome.location().getPath(), name);
	}

	public void addPotions(Potion potion, String name)
	{
		add(Items.POTION, potion, "Bottle of " + name);
		add(Items.SPLASH_POTION, potion, "Splash Bottle of " + name);
		add(Items.LINGERING_POTION, potion, "Lingering Bottle of " + name);
		add(Items.TIPPED_ARROW, potion, "Arrow of " + name);
	}

	public void add(KeyMapping key, String name)
	{
		add(key.getName(), name);
	}

	public void addAdvancement(String category, String key, String titleString, String descString) {
		String title = "advancement." + modId + "." + category + "." + key + ".title";
		String desc = "advancement." + modId + "." + category + "." + key + ".desc";

		add(title, titleString);
		add(desc, descString);
	}

	public void addCreativeTab(RegistryObject<CreativeModeTab> tab, String name)
	{
		add(tab.getId().toLanguageKey("item_group"), name);
	}

	public void add(RegistryObject<SoundEvent> sound, String name)
	{
		add("subtitles." + sound.get().getLocation().getPath(), name);
	}

	public void add(RegistryObject<PaintingVariant> painting, String title, String author)
	{
		add(painting.getId().toLanguageKey("painting") + ".title", title);
		add(painting.getId().toLanguageKey("painting") + ".author", author);
	}

	public void add(EntityType<?> key, String name, Item egg) {
		add(key.getDescriptionId(), name);
		add(egg, name + " Spawn Egg");
	}

	public void addSherd(Item item, String name)
	{
		add(item, name + " Pottery Sherd");
	}

	public void addPluginConfig(ResourceLocation location, String modName, String name)
	{
		add("config.jade.plugin_" + location.toLanguageKey(), modName + " " + name + " Config");
	}

	public void add(Component component, String name)
	{
		add(((TranslatableContents)component.getContents()).getKey(), name);
	}

	public void add(WoodSet set, String name)
	{
		add(set.planks().get(), name + " Planks");
		add(set.log().get(), name + " Log");
		add(set.strippedLog().get(), "Stripped " + name + " Log");
		add(set.wood().get(), name + " Wood");
		add(set.strippedWood().get(), "Stripped " + name + " Wood");
	}

	public void add(LeavesSet set, String name)
	{
		add(set.sapling().get(), name + " Sapling");
		add(set.leaves().get(), name + " Leaves");
		add(set.pottedSapling().get(), "Potted " + name + " Sapling");
	}

	protected void addConfig(String field, String name)
	{
		add(modId + ".midnightconfig." + field, name);
	}

	protected void addConfig(Enum<?> e, String name)
	{
		{
			add(modId + ".midnightconfig.enum." + e.getDeclaringClass().getSimpleName() + "." + e.name(), name);
		}
	}

	protected void addConfigCategory(String field, String name)
	{
		add(modId + ".midnightconfig.category." + field, name);
	}

	protected void add(PackInfo packInfo, String title, String description)
	{
		add(packInfo.titleKey(), title);
		add(packInfo.descriptionKey(), description);
	}

	protected void addConfigTitle(String title)
	{
		add(modId + ".midnightconfig.title", title);
	}
}
