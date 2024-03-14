package dev.thomasglasser.tommylib.api.world.item;

import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import dev.thomasglasser.tommylib.api.registration.RegistrationProvider;
import dev.thomasglasser.tommylib.api.registration.RegistryObject;
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

    public static Map<ResourceKey<CreativeModeTab>, ArrayList<ResourceLocation>> getItemTabs() {
        return ITEM_TABS;
    }

    public static <T extends Item> RegistryObject<T> register(RegistrationProvider<Item> provider, String name, Supplier<T> item, List<ResourceKey<CreativeModeTab>> tabs)
    {
        for (ResourceKey<CreativeModeTab> tab: tabs) {
            ArrayList<ResourceLocation> list = ItemUtils.getItemTabs().computeIfAbsent(tab, empty -> new ArrayList<>());
            list.add(new ResourceLocation(provider.getModId(), name));
        }
        return provider.register(name, item);
    }

    public static RegistryObject<Item> registerSherd(RegistrationProvider<Item> provider, String name)
    {
        RegistryObject<Item> sherd = register(provider, name, () -> new Item(new Item.Properties()), List.of(CreativeModeTabs.INGREDIENTS));
        return sherd;
    }

    public static RegistryObject<SmithingTemplateItem> registerSmithingTemplate(RegistrationProvider<Item> provider, ResourceKey<TrimPattern> key)
    {
        return register(provider, key.location().getPath() + "_armor_trim_smithing_template", () -> (SmithingTemplateItem.createArmorTrimTemplate(key)), List.of(CreativeModeTabs.INGREDIENTS));
    }

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

    public static RegistryObject<SpawnEggItem> registerSpawnEgg(RegistrationProvider<Item> provider, String name, Supplier<EntityType<? extends Mob>> entityType, int primaryColor, int secondaryColor)
    {
        return register(provider, name, TommyLibServices.ITEM.makeSpawnEgg(entityType, primaryColor, secondaryColor, new Item.Properties()), List.of(CreativeModeTabs.SPAWN_EGGS));
    }
}
