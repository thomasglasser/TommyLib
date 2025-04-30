package dev.thomasglasser.tommylib.api.world.item;

import dev.thomasglasser.tommylib.api.world.entity.projectile.ThrownSword;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Tier;

/**
 * A throwable sword item that has a model for easy anonymous class creation.
 *
 * @deprecated no longer needed in 1.21.5+
 */
@Deprecated(forRemoval = true, since = "31.0.0")
public abstract class ModeledThrowableSwordItem extends ThrowableSwordItem implements ModeledItem {
    public ModeledThrowableSwordItem(Supplier<EntityType<? extends ThrownSword>> projectile, Holder<SoundEvent> throwSound, Holder<SoundEvent> hitGroundSound, Tier pTier, Properties pProperties) {
        super(projectile, throwSound, hitGroundSound, pTier, pProperties);
    }
}
