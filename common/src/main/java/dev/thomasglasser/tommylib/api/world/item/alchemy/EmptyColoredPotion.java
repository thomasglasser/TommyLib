package dev.thomasglasser.tommylib.api.world.item.alchemy;

import net.minecraft.world.item.alchemy.Potion;

/**
 * An empty potion that uses a custom color.
 */
public class EmptyColoredPotion extends Potion {
    private int color;

    public EmptyColoredPotion(String name, int color) {
        super(name);
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
