package dev.thomasglasser.tommylib.api.world.item.armor;

import dev.thomasglasser.tommylib.api.registration.DeferredItem;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Holder for a set of armor with helpers.
 */
public record ArmorSet(String name, DeferredItem<ArmorItem> head, DeferredItem<ArmorItem> chest, DeferredItem<ArmorItem> legs, DeferredItem<ArmorItem> feet) {

    /**
     * The default {@link Item.Properties} settings for armor items.
     */
    public static final UnaryOperator<Item.Properties> DEFAULT_PROPERTIES = properties -> properties.stacksTo(1);
    public DeferredItem<ArmorItem> getForSlot(EquipmentSlot slot) {
        return switch (slot) {
            case FEET -> feet;
            case LEGS -> legs;
            case CHEST -> chest;
            case HEAD -> head;
            default -> throw new IllegalArgumentException("Invalid slot: " + slot);
        };
    }

    public EquipmentSlot getForItem(ArmorItem item) {
        if (item == head.get()) {
            return EquipmentSlot.HEAD;
        } else if (item == chest.get()) {
            return EquipmentSlot.CHEST;
        } else if (item == legs.get()) {
            return EquipmentSlot.LEGS;
        } else if (item == feet.get()) {
            return EquipmentSlot.FEET;
        }
        throw new IllegalArgumentException("Item is not part of this set: " + item);
    }

    public List<DeferredItem<ArmorItem>> getAll() {
        return ReferenceArrayList.of(head, chest, legs, feet);
    }

    public List<ResourceKey<Item>> getAllKeys() {
        return ReferenceArrayList.of(head.getKey(), chest.getKey(), legs.getKey(), feet.getKey());
    }

    public List<ArmorItem> getAllAsItems() {
        return ReferenceArrayList.of(head.get(), chest.get(), legs.get(), feet.get());
    }

    public List<ItemStack> getAllAsStacks() {
        return ReferenceArrayList.of(head.toStack(), chest.toStack(), legs.toStack(), feet.toStack());
    }
}
