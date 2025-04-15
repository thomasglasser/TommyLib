package dev.thomasglasser.tommylib.api.world.item.armor;

import dev.thomasglasser.tommylib.api.registration.DeferredItem;
import java.util.List;
import java.util.function.UnaryOperator;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Holder for a set of armor, with a helmet, chestplate, leggings, and boots.
 */
public class ArmorSet {
    public static final UnaryOperator<Item.Properties> DEFAULT_PROPERTIES = properties -> properties.stacksTo(1);

    public final DeferredItem<?> HEAD;
    public final DeferredItem<?> CHEST;
    public final DeferredItem<?> LEGS;
    public final DeferredItem<?> FEET;

    private final String name;
    private final String displayName;

    public ArmorSet(String name, String displayName, DeferredItem<?> head, DeferredItem<?> chest, DeferredItem<?> legs, DeferredItem<?> feet) {
        this.name = name;
        this.displayName = displayName;

        HEAD = head;
        CHEST = chest;
        LEGS = legs;
        FEET = feet;
    }

    public DeferredItem<?> getForSlot(EquipmentSlot slot) {
        return switch (slot) {
            case MAINHAND, OFFHAND, BODY, SADDLE -> null;
            case FEET -> FEET;
            case LEGS -> LEGS;
            case CHEST -> CHEST;
            case HEAD -> HEAD;
        };
    }

    public EquipmentSlot getForItem(Item item) {
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

    public List<DeferredItem<?>> getAll() {
        return List.of(HEAD, CHEST, LEGS, FEET);
    }

    public List<Item> getAllAsItems() {
        return List.of(HEAD.get(), CHEST.get(), LEGS.get(), FEET.get());
    }

    public List<ItemStack> getAllAsStacks() {
        return List.of(HEAD.toStack(), CHEST.toStack(), LEGS.toStack(), FEET.toStack());
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getName() {
        return name;
    }
}
