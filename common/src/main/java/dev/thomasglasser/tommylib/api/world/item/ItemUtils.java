package dev.thomasglasser.tommylib.api.world.item;

import dev.thomasglasser.sherdsapi.api.SherdsApiDataComponents;
import dev.thomasglasser.tommylib.api.registration.DeferredBlock;
import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import dev.thomasglasser.tommylib.api.registration.DeferredItem;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.state.BlockState;

public final class ItemUtils {
    /**
     * Creates a function for a {@link BlockItem} with a name different from its block
     * 
     * @param block the block to create the {@link BlockItem} for
     * @return the function for creating the {@link BlockItem}
     */
    public static Function<Item.Properties, Item> createBlockItemWithCustomItemName(DeferredBlock<?> block) {
        return properties -> new BlockItem(block.get(), properties.useItemDescriptionPrefix());
    }

    /**
     * Creates a function for a {@link BlockStateItem} with a name different from its block
     * 
     * @param state the blockstate to create the {@link BlockStateItem} for
     * @return the function for creating the {@link BlockStateItem}
     */
    public static Function<Item.Properties, Item> createBlockStateItemWithCustomItemName(Supplier<BlockState> state) {
        return properties -> new BlockStateItem(state.get(), properties.useItemDescriptionPrefix());
    }

    /**
     * Registers a sherd item with the given name and sherd pattern.
     *
     * @param provider   The item provider.
     * @param name       The registry name of the sherd item.
     * @param properties The item properties.
     * @return The registered sherd item holder.
     */
    public static DeferredItem<Item> registerSherd(DeferredRegister.Items provider, String name, Item.Properties properties) {
        return provider.registerSimpleItem(name + "_pottery_sherd", properties.rarity(Rarity.UNCOMMON).component(SherdsApiDataComponents.SHERD_PATTERN.get(), Identifier.fromNamespaceAndPath(provider.getNamespace(), name + "_pottery_pattern")));
    }

    /**
     * Registers an armor trim smithing template item with the given pattern key and rarity.
     * 
     * @param provider The item provider.
     * @param key      The pattern key.
     * @param rarity   The item rarity
     * @return The registered smithing template item holder.
     */
    public static DeferredItem<SmithingTemplateItem> registerArmorTrimSmithingTemplate(DeferredRegister.Items provider, ResourceKey<TrimPattern> key, Rarity rarity) {
        return provider.registerItem(key.identifier().getPath() + "_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate, new Item.Properties().rarity(rarity));
    }

    /**
     * Registers a spawn egg item with the given name and entity type.
     * 
     * @param provider   The item provider.
     * @param entityType The entity type of the spawn egg item.
     * @return The registered spawn egg item holder.
     */
    public static DeferredItem<SpawnEggItem> registerSpawnEgg(DeferredRegister.Items provider, DeferredHolder<EntityType<?>, EntityType<? extends Mob>> entityType) {
        return provider.registerItem(entityType.getId().getPath() + "_spawn_egg", properties -> new SpawnEggItem(properties.spawnEgg(entityType.get())));
    }

    /**
     * Registers a banner pattern item with the given name, patterns, and properties.
     *
     * @param provider The item provider.
     * @param name     The registry name of the banner pattern item.
     * @param rarity   The rarity of the banner pattern item.
     * @param patterns The patterns of the banner pattern item.
     * @return The registered banner pattern item holder.
     */
    public static DeferredItem<?> registerBannerPattern(DeferredRegister.Items provider, String name, Rarity rarity, TagKey<BannerPattern> patterns) {
        return provider.registerSimpleItem(name + "_banner_pattern", new Item.Properties().rarity(rarity).stacksTo(1).component(DataComponents.PROVIDES_BANNER_PATTERNS, patterns));
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
            ItemStack remainder = item.getItem().getCraftingRemainder();
            item.shrink(d);
            return remainder;
        }
        return ItemStack.EMPTY;
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
