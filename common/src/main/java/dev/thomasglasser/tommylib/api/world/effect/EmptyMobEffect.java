package dev.thomasglasser.tommylib.api.world.effect;

import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * An empty mob effect that does nothing. Displays as "No Effects" on Potions.
 */
public class EmptyMobEffect extends MobEffect {
    public EmptyMobEffect(int color) {
        super(MobEffectCategory.NEUTRAL, color);
    }

    /**
     * Instantaneous version of the EmptyMobEffect.
     */
    public static class Instantaneous extends InstantenousMobEffect {
        public Instantaneous(int color) {
            super(MobEffectCategory.NEUTRAL, color);
        }

        @Override
        public boolean isInstantenous() {
            return true;
        }
    }
}
