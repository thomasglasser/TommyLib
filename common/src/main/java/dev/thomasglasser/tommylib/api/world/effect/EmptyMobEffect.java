package dev.thomasglasser.tommylib.api.world.effect;

import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class EmptyMobEffect extends MobEffect
{
	public EmptyMobEffect(int color) {
		super(MobEffectCategory.NEUTRAL, color);
	}

	public static class Instantaneous extends InstantenousMobEffect
	{
		public Instantaneous(int color) {
			super(MobEffectCategory.NEUTRAL, color);
		}

		@Override
		public boolean isInstantenous()
		{
			return true;
		}
	}
}
