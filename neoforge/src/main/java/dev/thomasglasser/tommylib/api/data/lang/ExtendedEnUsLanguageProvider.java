package dev.thomasglasser.tommylib.api.data.lang;

import dev.thomasglasser.tommylib.api.packs.PackInfo;
import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import dev.thomasglasser.tommylib.api.registration.DeferredItem;
import dev.thomasglasser.tommylib.api.world.level.block.LeavesSet;
import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import java.util.List;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.apache.commons.lang3.text.WordUtils;

/**
 * Extension of {@link LanguageProvider} for English that provides functionality for mod holders.
 */
public abstract class ExtendedEnUsLanguageProvider extends LanguageProvider {
    protected String modId;

    public ExtendedEnUsLanguageProvider(PackOutput output, String modid) {
        super(output, modid, "en_us");
        modId = modid;
    }

    @Override
    public void add(Item key, String name) {
        add(key.getName(key.getDefaultInstance()), name);
    }

    @Override
    public void add(ItemStack key, String name) {
        add(key.getItem().getName(key), name);
    }

    /**
     * Adds a translation for a {@link ResourceKey} and name.
     *
     * @param key  The key to add the translation for.
     * @param name The name of the key.
     */
    protected void add(ResourceKey<?> key, String name) {
        add(key.location().toLanguageKey(key.registry().getPath()), name);
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
     * Adds a translation for a given {@link Potion} with the default {@link PotionItem}s.
     * 
     * @param potion The potion to add the translation for.
     * @param name   The name of the potion.
     */
    public void addPotions(Holder<Potion> potion, String name) {
        String title = potion.value().getEffects().isEmpty() ? "Bottle" : "Potion";
        add(PotionContents.createItemStack(Items.POTION, potion), title + " of " + name);
        add(PotionContents.createItemStack(Items.SPLASH_POTION, potion), "Splash " + title + " of " + name);
        add(PotionContents.createItemStack(Items.LINGERING_POTION, potion), "Lingering " + title + " of " + name);
        add(PotionContents.createItemStack(Items.TIPPED_ARROW, potion), "Arrow of " + name);
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
     * Adds a translation for a given {@link CreativeModeTab}.
     * 
     * @param tab  The tab to add the translation for.
     * @param name The name of the tab.
     */
    public void add(CreativeModeTab tab, String name) {
        add(tab.getDisplayName().getString(), name);
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
        add(set.log().get(), name + " Log");
        add(set.strippedLog().get(), "Stripped " + name + " Log");
        add(set.wood().get(), name + " Wood");
        add(set.strippedWood().get(), "Stripped " + name + " Wood");
        add(set.planks().get(), name + " Planks");
        add(set.slab().get(), name + " Slab");
        add(set.stairs().get(), name + " Stairs");
        add(set.pressurePlate().get(), name + " Pressure Plate");
        add(set.button().get(), name + " Button");
        add(set.fence().get(), name + " Fence");
        add(set.fenceGate().get(), name + " Fence Gate");
        add(set.door().get(), name + " Door");
        add(set.trapdoor().get(), name + " Trapdoor");
        add(set.sign().get(), name + " Sign");
        add(set.hangingSign().get(), name + " Hanging Sign");
        add(set.boatItem().get(), name + " Boat");
        add(set.chestBoatItem().get(), name + " Boat with Chest");
        add(set.logsBlockTag(), name + " Logs");
        add(set.logsItemTag(), name + " Logs");
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

    /**
     * Adds a translation for a given armor trim {@link Item}.
     *
     * @param item The item to add the translation for.
     */
    protected void addArmorTrim(Item item, String name) {
        add(item, name + " Armor Trim");
    }

    /**
     * Adds a translation for a given {@link DamageType}.
     * 
     * @param key     the key of the damage type
     * @param message the message of the damage type
     */
    protected void addAttack(ResourceKey<DamageType> key, String message) {
        add("death.attack." + key.location().getPath(), message);
    }

    /**
     * Adds a translation for a given {@link DamageType} with a player suffix.
     * 
     * @param key          the key of the damage type
     * @param message      the message of the damage type
     * @param playerSuffix the message suffix for the player-specific attack
     */
    protected void addAttackWithPlayer(ResourceKey<DamageType> key, String message, String playerSuffix) {
        addAttack(key, message);
        add("death.attack." + key.location().getPath() + ".player", message + " " + playerSuffix);
    }

    /**
     * Adds a translation for a given {@link DamageType} with an item suffix.
     * 
     * @param key        the key of the damage type
     * @param message    the message of the damage type
     * @param itemSuffix the message suffix for the item-specific attack
     */
    protected void addAttackWithItem(ResourceKey<DamageType> key, String message, String itemSuffix) {
        addAttack(key, message);
        add("death.attack." + key.location().toShortLanguageKey() + ".item", message + " " + itemSuffix);
    }

    /**
     * Adds a translation for a given {@link ModConfigSpec.ConfigValue} with a tooltip.
     * 
     * @param configValue The config value to add the translation for.
     * @param name        The name of the config value.
     * @param tooltip     The tooltip of the config value.
     */
    protected void addConfig(ModConfigSpec.ConfigValue<?> configValue, String name, String tooltip) {
        List<String> keys = configValue.getPath();
        String configKey = modId + ".configuration." + keys.getLast();
        add(configKey, name);
        add(configKey + ".tooltip", tooltip);
    }

    /**
     * Adds a translation for a mod config screen.
     * 
     * @param name The name of the config screen (typically the mod name).
     */
    protected void addConfigTitle(String name) {
        add(modId + ".configuration.title", name);
    }

    /**
     * Adds a translation for a config section.
     * 
     * @param key  The key of the config section.
     * @param name The name of the config section.
     */
    protected void addConfigSection(String key, String name) {
        add(modId + ".configuration." + key, name);
    }

    /**
     * Adds a translation for a config section with a tooltip.
     *
     * @param key     The key of the config section.
     * @param name    The name of the config section.
     * @param tooltip The tooltip of the config section.
     */
    protected void addConfigSection(String key, String name, String tooltip) {
        add(modId + ".configuration." + key, name);
        add(modId + ".configuration." + key + ".tooltip", tooltip);
    }

    /**
     * Adds a translation for a banner pattern item.
     * 
     * @param item The item to add the translation for.
     * @param name The name of the pattern item.
     */
    protected void addPatternItem(DeferredItem<BannerPatternItem> item, String name) {
        add(item.get(), "Banner Pattern");
        add(item.get().getDescriptionId() + ".desc", name);
    }

    /**
     * Adds a translation for a banner pattern and item.
     * 
     * @param pattern The pattern
     * @param item    The item for the pattern
     * @param name    The name of the pattern
     */
    protected void addPatternAndItem(ResourceKey<BannerPattern> pattern, DeferredItem<BannerPatternItem> item, String name) {
        addPattern(pattern, name);
        addPatternItem(item, name);
    }
}
