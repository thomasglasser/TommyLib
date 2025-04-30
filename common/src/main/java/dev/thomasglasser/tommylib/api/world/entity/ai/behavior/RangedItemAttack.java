package dev.thomasglasser.tommylib.api.world.entity.ai.behavior;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.BowAttack;

/**
 * An extension of {@link BowAttack} to allow for ranged attacks with any item.
 *
 * @deprecated Coming to SBL soon
 *
 * @param <E> The entity that will be performing the attack.
 */
@Deprecated(forRemoval = true, since = "31.0.0")
public class RangedItemAttack<E extends LivingEntity & RangedAttackMob> extends BowAttack<E> {
    private Item item;

    public RangedItemAttack(int delayTicks, Item item) {
        super(delayTicks);
        this.item = item;
    }

    @Override
    protected void start(E entity) {
        if (this.target != null) {
            BehaviorUtils.lookAtEntity(entity, this.target);
            entity.startUsingItem(ProjectileUtil.getWeaponHoldingHand(entity, item));
        }
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
