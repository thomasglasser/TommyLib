package dev.thomasglasser.tommylib.api.world.item.armor;

import dev.thomasglasser.tommylib.api.registration.DeferredItem;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Holder for a set of armor with helpers.
 */
public class ArmorSet {
    /**
     * The default {@link Item.Properties} for armor items, creates a new object every time for use.
     */
    public static final Supplier<Item.Properties> DEFAULT_PROPERTIES = () -> new Item.Properties().stacksTo(1);

    public final DeferredItem<ArmorItem> HEAD;
    public final DeferredItem<ArmorItem> CHEST;
    public final DeferredItem<ArmorItem> LEGS;
    public final DeferredItem<ArmorItem> FEET;

    private final String name;
    private final String displayName;

    public ArmorSet(String name, String displayName, DeferredItem<ArmorItem> head, DeferredItem<ArmorItem> chest, DeferredItem<ArmorItem> legs, DeferredItem<ArmorItem> feet) {
        this.name = name;
        this.displayName = displayName;

        HEAD = head;
        CHEST = chest;
        LEGS = legs;
        FEET = feet;
    }

    public DeferredItem<ArmorItem> getForSlot(EquipmentSlot slot) {
        return switch (slot) {
            case FEET -> FEET;
            case LEGS -> LEGS;
            case CHEST -> CHEST;
            case HEAD -> HEAD;
            default -> throw new IllegalArgumentException("Invalid slot: " + slot);
        };
    }

    public EquipmentSlot getForItem(ArmorItem item) {
        if (item == HEAD.get()) {
            return EquipmentSlot.HEAD;
        } else if (item == CHEST.get()) {
            return EquipmentSlot.CHEST;
        } else if (item == LEGS.get()) {
            return EquipmentSlot.LEGS;
        } else if (item == FEET.get()) {
            return EquipmentSlot.FEET;
        }
        throw new IllegalArgumentException("Item is not part of this set: " + item);
    }

    public List<DeferredItem<ArmorItem>> getAll() {
        return ReferenceArrayList.of(HEAD, CHEST, LEGS, FEET);
    }

    public List<ArmorItem> getAllAsItems() {
        return ReferenceArrayList.of(HEAD.get(), CHEST.get(), LEGS.get(), FEET.get());
    }

    public List<ItemStack> getAllAsStacks() {
        return ReferenceArrayList.of(HEAD.toStack(), CHEST.toStack(), LEGS.toStack(), FEET.toStack());
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getName() {
        return name;
    }
}
