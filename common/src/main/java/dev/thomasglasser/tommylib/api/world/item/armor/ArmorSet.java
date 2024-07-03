package dev.thomasglasser.tommylib.api.world.item.armor;

import dev.thomasglasser.tommylib.api.registration.DeferredItem;
import java.util.List;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;

/**
 * Holder for a set of armor, with a helmet, chestplate, leggings, and boots.
 */
public class ArmorSet {
    public static final Item.Properties DEFAULT_PROPERTIES = new Item.Properties().stacksTo(1);

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

            case MAINHAND, OFFHAND, BODY -> null;
            case FEET -> FEET;
            case LEGS -> LEGS;
            case CHEST -> CHEST;
            case HEAD -> HEAD;
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

        return null;
    }

    public List<DeferredItem<ArmorItem>> getAll() {
        return List.of(HEAD, CHEST, LEGS, FEET);
    }

    public List<ArmorItem> getAllAsItems() {
        return List.of(HEAD.get(), CHEST.get(), LEGS.get(), FEET.get());
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getName() {
        return name;
    }
}
