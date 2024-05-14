package dev.thomasglasser.tommylib.api.world.item;

import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import dev.thomasglasser.tommylib.api.registration.DeferredItem;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.armortrim.TrimPattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class ItemUtils
{
    private static final HashMap<ResourceKey<CreativeModeTab>, ArrayList<ResourceLocation>> ITEM_TABS = new HashMap<>();

    /**
     * Gets a map of all mod items and the tabs they should appear in.
     * @return A map of all mod items and the tabs they should appear in.
     */
    public static Map<ResourceKey<CreativeModeTab>, ArrayList<ResourceLocation>> getItemTabs() {
        return ITEM_TABS;
    }

    /**
     * Registers an item with the given name and properties.
     * @param provider The item provider.
     * @param name The registry name of the item.
     * @param item The item supplier.
     * @param tabs The tabs the item should appear in.
     * @return The registered item holder.
     * @param <T> The item type.
     */
    public static <T extends Item> DeferredItem<T> register(DeferredRegister.Items provider, String name, Supplier<T> item, List<ResourceKey<CreativeModeTab>> tabs)
    {
        for (ResourceKey<CreativeModeTab> tab: tabs) {
            ArrayList<ResourceLocation> list = ItemUtils.getItemTabs().computeIfAbsent(tab, empty -> new ArrayList<>());
            list.add(new ResourceLocation(provider.getNamespace(), name));
        }
        return provider.register(name, item);
    }

    /**
     * Registers a sherd item with the given name.
     * @param provider The item provider.
     * @param name The registry name of the sherd item.
     * @return The registered sherd item holder.
     */
    public static DeferredItem<Item> registerSherd(DeferredRegister.Items provider, String name)
    {
	    return register(provider, name + "_pottery_sherd", () -> new Item(new Item.Properties()), List.of(CreativeModeTabs.INGREDIENTS));
    }

    /**
     * Registers a smithing template item with the given pattern key.
     * @param provider The item provider.
     * @param key The pattern key.
     * @return The registered smithing template item holder.
     */
    public static DeferredItem<SmithingTemplateItem> registerSmithingTemplate(DeferredRegister.Items provider, ResourceKey<TrimPattern> key)
    {
        return register(provider, key.location().getPath() + "_armor_trim_smithing_template", () -> (SmithingTemplateItem.createArmorTrimTemplate(key)), List.of(CreativeModeTabs.INGREDIENTS));
    }

    /**
     * Safely shrinks the item stack by the given amount, checking for creative mode.
     * @param d The amount to shrink the item stack by.
     * @param item The item stack to shrink.
     * @param player The player to check.
     * @return The remainder of the item stack.
     */
    public static ItemStack safeShrink(int d, ItemStack item, Player player)
    {
        if (!player.getAbilities().instabuild)
        {
            Item remainder = item.getItem().getCraftingRemainingItem();
            item.shrink(d);
            if (remainder != null)
                return remainder.getDefaultInstance();
            return ItemStack.EMPTY;
        }
        return ItemStack.EMPTY;
    }

    /**
     * Registers a spawn egg item with the given name, entity type, and colors.
     * @param provider The item provider.
     * @param name The registry name of the spawn egg item.
     * @param entityType The entity type of the spawn egg item.
     * @param primaryColor The primary color of the spawn egg item.
     * @param secondaryColor The secondary color of the spawn egg item.
     * @return The registered spawn egg item holder.
     */
    public static DeferredItem<SpawnEggItem> registerSpawnEgg(DeferredRegister.Items provider, String name, Supplier<EntityType<? extends Mob>> entityType, int primaryColor, int secondaryColor)
    {
        return register(provider, name, TommyLibServices.ITEM.makeSpawnEgg(entityType, primaryColor, secondaryColor, new Item.Properties()), List.of(CreativeModeTabs.SPAWN_EGGS));
    }

    /**
     * Checks if the GeckoLib mod is loaded.
     * @return True if the GeckoLib mod is loaded, false otherwise.
     */
    public static boolean isGeckoLoaded()
    {
        return TommyLibServices.PLATFORM.isModLoaded("geckolib");
    }
}
