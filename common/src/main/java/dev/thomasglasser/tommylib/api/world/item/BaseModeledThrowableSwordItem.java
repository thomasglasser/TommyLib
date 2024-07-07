package dev.thomasglasser.tommylib.api.world.item;

import dev.thomasglasser.tommylib.api.world.entity.projectile.ThrownSword;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Tier;

/**
 * Base class of {@link ModeledItem} for {@link ThrowableSwordItem}s that performs NeoForge display setup automatically.
 */
public abstract class BaseModeledThrowableSwordItem extends ThrowableSwordItem implements ModeledItem {
    public BaseModeledThrowableSwordItem(Supplier<EntityType<? extends ThrownSword>> projectile, Holder<SoundEvent> throwSound, Holder<SoundEvent> hitGroundSound, Tier pTier, Properties pProperties) {
        super(projectile, throwSound, hitGroundSound, pTier, pProperties);
    }
}
