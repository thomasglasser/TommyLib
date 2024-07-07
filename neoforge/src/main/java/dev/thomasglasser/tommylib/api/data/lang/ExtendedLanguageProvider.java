package dev.thomasglasser.tommylib.api.data.lang;

import dev.thomasglasser.tommylib.api.packs.PackInfo;
import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import dev.thomasglasser.tommylib.api.world.level.block.LeavesSet;
import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.apache.commons.lang3.text.WordUtils;

/**
 * Extension of {@link LanguageProvider} that provides functionality for mod holders.
 */
public abstract class ExtendedLanguageProvider extends LanguageProvider {
    protected String modId;

    public ExtendedLanguageProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
        modId = modid;
    }

    /**
     * Adds a translation for an {@link Item} description.
     * 
     * @param item The item to add the description for.
     * @param desc The description to add.
     */
    public void addDesc(Item item, String desc) {
        add(item.getDescriptionId() + ".desc", desc);
    }

    /**
     * Adds a translation for a given {@link BannerPattern} for all {@link DyeColor}s.
     * 
     * @param pattern The pattern to add the translation for.
     * @param name    The name of the pattern.
     */
    public void addPattern(ResourceKey<BannerPattern> pattern, String name) {
        for (DyeColor color : DyeColor.values()) {
            add("block.minecraft." + pattern.location().toLanguageKey("banner") + "." + color.getName(), WordUtils.capitalize(color.getName().replace('_', ' ')) + " " + name);
        }
    }

    /**
     * Adds a translation for a given {@link PaintingVariant}.
     * 
     * @param key    The {@link ResourceKey} of the painting to add the translation for.
     * @param title  The title of the painting.
     * @param author The author of the painting.
     */
    public void addPaintingVariant(ResourceKey<PaintingVariant> key, String title, String author) {
        add(key.location().toLanguageKey("painting") + ".title", title);
        add(key.location().toLanguageKey("painting") + ".author", author);
    }

    /**
     * Adds a translation for a given {@link Item} with the given {@link Potion}
     * 
     * @param key    The item to add the translation for.
     * @param potion The potion to add the translation for.
     * @param name   The name of the potion.
     */
    public void add(Item key, Holder<Potion> potion, String name) {
        add(PotionContents.createItemStack(key, potion), name);
    }

    /**
     * Adds a translation for a given {@link Biome}.
     * 
     * @param biome The biome to add the translation for.
     * @param name  The name of the biome.
     */
    public void addBiome(ResourceKey<Biome> biome, String name) {
        add("biome." + biome.location().getNamespace() + "." + biome.location().getPath(), name);
    }

    /**
     * Adds a translation for a given {@link Potion} with the default {@link PotionItem}s.
     * 
     * @param potion The potion to add the translation for.
     * @param name   The name of the potion.
     */
    public void addPotions(Holder<Potion> potion, String name) {
        add(Items.POTION, potion, "Bottle of " + name);
        add(Items.SPLASH_POTION, potion, "Splash Bottle of " + name);
        add(Items.LINGERING_POTION, potion, "Lingering Bottle of " + name);
        add(Items.TIPPED_ARROW, potion, "Arrow of " + name);
    }

    /**
     * Adds a translation for a given {@link KeyMapping}.
     * 
     * @param key  The key to add the translation for.
     * @param name The name of the key.
     */
    public void add(KeyMapping key, String name) {
        add(key.getName(), name);
    }

    /**
     * Adds a translation for a given {@link Advancement} name and description.
     * 
     * @param category    The category of the advancement.
     * @param key         The key of the advancement.
     * @param titleString The title of the advancement.
     * @param descString  The description of the advancement.
     */
    public void addAdvancement(String category, String key, String titleString, String descString) {
        String title = "advancement." + modId + "." + category + "." + key + ".title";
        String desc = "advancement." + modId + "." + category + "." + key + ".desc";

        add(title, titleString);
        add(desc, descString);
    }

    /**
     * Adds a translation for a given {@link CreativeModeTab}.
     * 
     * @param tab  The tab to add the translation for.
     * @param name The name of the tab.
     */
    public void addCreativeTab(DeferredHolder<CreativeModeTab, ?> tab, String name) {
        add(tab.getId().toLanguageKey("item_group"), name);
    }

    /**
     * Adds a translation for a given {@link SoundEvent}.
     * 
     * @param sound The sound to add the translation for.
     * @param name  The name of the sound.
     */
    public void add(SoundEvent sound, String name) {
        add("subtitles." + sound.getLocation().getPath(), name);
    }

    /**
     * Adds a translation for a given {@link EntityType} and spawn egg.
     * 
     * @param key  The key of the entity.
     * @param name The name of the entity.
     * @param egg  The spawn egg of the entity.
     */
    public void add(EntityType<?> key, String name, Item egg) {
        add(key.getDescriptionId(), name);
        add(egg, name + " Spawn Egg");
    }

    /**
     * Adds a translation for a given sherd {@link Item} and name.
     * 
     * @param item The sherd to add the translation for.
     * @param name The name of the item.
     */
    public void addSherd(Item item, String name) {
        add(item, name + " Pottery Sherd");
    }

    /**
     * Adds a translation for a given Jade config
     * 
     * @param location The location of the config.
     * @param modName  The name of the mod.
     * @param name     The name of the config.
     */
    public void addPluginConfig(ResourceLocation location, String modName, String name) {
        add("config.jade.plugin_" + location.toLanguageKey(), modName + " " + name + " Config");
    }

    /**
     * Adds a translation for a Component's translation key.
     * 
     * @param component The component to add the translation for.
     * @param name      The name for the key.
     */
    public void add(Component component, String name) {
        add(((TranslatableContents) component.getContents()).getKey(), name);
    }

    /**
     * Adds translations for a {@link WoodSet}.
     * 
     * @param set  The set to add the translations for.
     * @param name The name of the set.
     */
    public void add(WoodSet set, String name) {
        add(set.planks().get(), name + " Planks");
        add(set.log().get(), name + " Log");
        add(set.strippedLog().get(), "Stripped " + name + " Log");
        add(set.wood().get(), name + " Wood");
        add(set.strippedWood().get(), "Stripped " + name + " Wood");
    }

    /**
     * Adds translations for a {@link LeavesSet}.
     * 
     * @param set  The set to add the translations for.
     * @param name The name of the set.
     */
    public void add(LeavesSet set, String name) {
        add(set.sapling().get(), name + " Sapling");
        add(set.leaves().get(), name + " Leaves");
        add(set.pottedSapling().get(), "Potted " + name + " Sapling");
    }

    /**
     * Adds a translation for a given built-in pack.
     * 
     * @param packInfo    The pack to add the translation for.
     * @param title       The title of the pack.
     * @param description The description of the pack.
     */
    protected void add(PackInfo packInfo, String title, String description) {
        add(packInfo.titleKey(), title);
        add(packInfo.descriptionKey(), description);
    }

    /**
     * Adds a translation for a given villager profession.
     * 
     * @param profession The profession to add the translation for.
     * @param name       The name of the profession.
     */
    protected void addProfession(DeferredHolder<VillagerProfession, ?> profession, String name) {
        add(BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.VILLAGER).toLanguageKey("entity") + "." + profession.getKey().location().toShortLanguageKey(), name);
    }
}
