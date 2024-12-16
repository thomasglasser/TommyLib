package dev.thomasglasser.tommylib.api.world.item;

import dev.thomasglasser.sherdsapi.api.SherdsApiDataComponents;
import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import dev.thomasglasser.tommylib.api.registration.DeferredItem;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

public final class ItemUtils {
    /**
     * Registers an item with the given name and properties.
     * 
     * @param provider The item provider.
     * @param name     The registry name of the item.
     * @param item     The item supplier.
     * @return The registered item holder.
     * @param <T> The item type.
     */
    public static <T extends Item> DeferredItem<T> register(DeferredRegister.Items provider, String name, Supplier<T> item) {
        return provider.register(name, item);
    }

    /**
     * Registers a sherd item with the given name.
     *
     * @param provider   The item provider.
     * @param name       The registry name of the sherd item.
     * @param properties The item properties.
     * @return The registered sherd item holder.
     */
    public static DeferredItem<Item> registerSherd(DeferredRegister.Items provider, String name, Item.Properties properties) {
        return register(provider, name + "_pottery_sherd", () -> new Item(properties.component(SherdsApiDataComponents.SHERD_PATTERN.get(), ResourceLocation.fromNamespaceAndPath(provider.getNamespace(), name + "_pottery_pattern"))));
    }

    /**
     * Registers a smithing template item with the given pattern key.
     * 
     * @param provider The item provider.
     * @param key      The pattern key.
     * @return The registered smithing template item holder.
     */
    public static DeferredItem<SmithingTemplateItem> registerSmithingTemplate(DeferredRegister.Items provider, ResourceKey<TrimPattern> key) {
        return register(provider, key.location().getPath() + "_armor_trim_smithing_template", () -> (SmithingTemplateItem.createArmorTrimTemplate(key)));
    }

    /**
     * Registers a spawn egg item with the given name, entity type, and colors.
     * 
     * @param provider       The item provider.
     * @param name           The registry name of the spawn egg item.
     * @param entityType     The entity type of the spawn egg item.
     * @param primaryColor   The primary color of the spawn egg item.
     * @param secondaryColor The secondary color of the spawn egg item.
     * @return The registered spawn egg item holder.
     */
    public static DeferredItem<SpawnEggItem> registerSpawnEgg(DeferredRegister.Items provider, String name, Supplier<EntityType<? extends Mob>> entityType, int primaryColor, int secondaryColor) {
        return register(provider, name, () -> new SpawnEggItem(entityType.get(), primaryColor, secondaryColor, new Item.Properties()));
    }

    /**
     * Safely shrinks the item stack by the given amount, checking for creative mode.
     *
     * @param d      The amount to shrink the item stack by.
     * @param item   The item stack to shrink.
     * @param player The player to check.
     * @return The remainder of the item stack.
     */
    public static ItemStack safeShrink(int d, ItemStack item, Player player) {
        if (!player.getAbilities().instabuild) {
            Item remainder = item.getItem().getCraftingRemainingItem();
            item.shrink(d);
            return remainder == null ? ItemStack.EMPTY : new ItemStack(remainder);
        }
        return ItemStack.EMPTY;
    }

    /**
     * Checks if the GeckoLib mod is loaded.
     * 
     * @return True if the GeckoLib mod is loaded, false otherwise.
     */
    public static boolean isGeckoLoaded() {
        return TommyLibServices.PLATFORM.isModLoaded("geckolib");
    }

    /**
     * Gets the loyalty enchantment level from the given item stack.
     * 
     * @param stack  The item stack to get the loyalty enchantment level from.
     * @param level  The level to use.
     * @param entity The thrown entity
     * @return The loyalty enchantment level from the given item stack.
     */
    public static byte getLoyaltyFromItem(ItemStack stack, Level level, Entity entity) {
        return level instanceof ServerLevel serverlevel
                ? (byte) Mth.clamp(EnchantmentHelper.getTridentReturnToOwnerAcceleration(serverlevel, stack, entity), 0, 127)
                : 0;
    }
}
