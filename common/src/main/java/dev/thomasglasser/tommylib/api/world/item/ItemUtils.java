package dev.thomasglasser.tommylib.api.world.item;

import dev.thomasglasser.tommylib.api.registration.DeferredItem;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPattern;

/**
 * Item registration and handling helpers.
 */
public final class ItemUtils {
    /**
     * Registers an item with the given name and properties.
     * 
     * @param provider The item provider
     * @param name     The registry name of the item
     * @param item     The item supplier
     * @return The registered item holder
     * @param <T> The item type
     */
    public static <T extends Item> DeferredItem<T> register(DeferredRegister.Items provider, String name, Supplier<T> item) {
        return provider.register(name, item);
    }

    /**
     * Registers a smithing template item with the given pattern key.
     * 
     * @param provider The item provider
     * @param key      The pattern key
     * @return The registered smithing template item holder
     */
    public static DeferredItem<SmithingTemplateItem> registerSmithingTemplate(DeferredRegister.Items provider, ResourceKey<TrimPattern> key) {
        return register(provider, key.location().getPath() + "_armor_trim_smithing_template", () -> SmithingTemplateItem.createArmorTrimTemplate(key));
    }

    /**
     * Registers a banner pattern item with the given patterns and properties.
     *
     * @param provider   The item provider
     * @param patterns   The patterns of the banner pattern item
     * @param properties The item properties
     * @return The registered banner pattern item holder
     */
    public static DeferredItem<BannerPatternItem> registerBannerPattern(DeferredRegister.Items provider, TagKey<BannerPattern> patterns, Item.Properties properties) {
        return register(provider, patterns.location().getPath().replace("pattern_item/", "") + "_banner_pattern", () -> new BannerPatternItem(patterns, properties));
    }

    /**
     * Safely shrinks the item stack by the given amount, checking for creative mode, and adds the remainder to the player's inventory.
     *
     * @param amount The amount to shrink the item stack by
     * @param stack  The item stack to shrink
     * @param player The player to check
     */
    public static void safeShrink(int amount, ItemStack stack, Player player) {
        if (!player.getAbilities().instabuild) {
            Item remainder = stack.getItem().getCraftingRemainingItem();
            stack.shrink(amount);
            if (remainder != null) {
                for (int i = 0; i < amount; i++) {
                    player.addItem(remainder.getDefaultInstance());
                }
            }
        }
    }

    /**
     * Safely shrinks an item stack by 1
     * 
     * @param stack  The item stack to shrink
     * @param player The player to check
     */
    public static void safeShrink(ItemStack stack, Player player) {
        safeShrink(1, stack, player);
    }

    /**
     * Gets the loyalty enchantment level from the given item stack.
     * 
     * @param stack  The item stack to get the loyalty enchantment level from
     * @param level  The level to use
     * @param entity The thrown entity
     * @return The loyalty enchantment level from the given item stack
     */
    public static byte getLoyaltyFromItem(ItemStack stack, Level level, Entity entity) {
        return level instanceof ServerLevel serverlevel
                ? (byte) Mth.clamp(EnchantmentHelper.getTridentReturnToOwnerAcceleration(serverlevel, stack, entity), 0, 127)
                : 0;
    }
}
