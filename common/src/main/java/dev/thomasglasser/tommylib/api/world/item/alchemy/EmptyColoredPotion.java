package dev.thomasglasser.tommylib.api.world.item.alchemy;

import dev.thomasglasser.tommylib.impl.mixin.minecraft.world.item.alchemy.PotionContentsMixin;
import net.minecraft.world.item.alchemy.Potion;
import org.jetbrains.annotations.Nullable;

/**
 * An empty potion that uses a custom color.
 *
 * @see PotionContentsMixin
 */
public class EmptyColoredPotion extends Potion {
    private int color;

    public EmptyColoredPotion(@Nullable String name, int color) {
        super(name);
        this.color = color;
    }

    public EmptyColoredPotion(int color) {
        this(null, color);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
