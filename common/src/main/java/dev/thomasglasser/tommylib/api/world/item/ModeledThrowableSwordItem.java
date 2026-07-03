package dev.thomasglasser.tommylib.api.world.item;

import dev.thomasglasser.tommylib.api.world.entity.projectile.ThrownSword;
import java.util.function.Supplier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Tier;
import org.jetbrains.annotations.Nullable;

/// A throwable sword item that has a model for easy anonymous class creation.
public abstract class ModeledThrowableSwordItem extends ThrowableSwordItem implements ModeledItem {
    public ModeledThrowableSwordItem(Supplier<EntityType<? extends ThrownSword>> projectile, float baseProjectileDamage, @Nullable SoundEvent throwSound, @Nullable SoundEvent hitGroundSound, @Nullable SoundEvent returnSound, Tier pTier, Properties pProperties) {
        super(projectile, baseProjectileDamage, throwSound, hitGroundSound, returnSound, pTier, pProperties);
    }
}
