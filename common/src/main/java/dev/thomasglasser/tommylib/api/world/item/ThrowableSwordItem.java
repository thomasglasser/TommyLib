package dev.thomasglasser.tommylib.api.world.item;

import dev.thomasglasser.tommylib.api.world.entity.projectile.ThrownSword;
import java.util.function.Supplier;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;

/**
 * An {@link Item} that can be thrown as a {@link ThrownSword}.
 */
public class ThrowableSwordItem extends Item implements ProjectileItem {
    private final Supplier<EntityType<? extends ThrownSword>> projectile;
    private final Holder<SoundEvent> throwSound;
    private final Holder<SoundEvent> hitGroundSound;

    public ThrowableSwordItem(Supplier<EntityType<? extends ThrownSword>> projectile, Holder<SoundEvent> throwSound, Holder<SoundEvent> hitGroundSound, Properties pProperties) {
        super(pProperties);
        this.projectile = projectile;
        this.throwSound = throwSound;
        this.hitGroundSound = hitGroundSound;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.SPEAR;
    }

    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player player) {
            int i = this.getUseDuration(stack, entity) - timeLeft;
            if (i >= 10) {
                if (!isTooDamagedToUse(stack)) {
                    if (!level.isClientSide()) {
                        stack.hurtAndBreak(1, player, entity.getUsedItemHand());
                        ThrownSword thrown = getThrown(level, stack, player);
                        thrown.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);
                        if (player.hasInfiniteMaterials()) {
                            thrown.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        }

                        level.addFreshEntity(thrown);
                        level.playSound(null, thrown, throwSound.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                        if (!player.hasInfiniteMaterials()) {
                            player.getInventory().removeItem(stack);
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (isTooDamagedToUse(itemstack)) {
            return InteractionResult.FAIL;
        } else {
            player.startUsingItem(hand);
            return InteractionResult.CONSUME;
        }
    }

    private static boolean isTooDamagedToUse(ItemStack stack) {
        return stack.getDamageValue() >= stack.getMaxDamage() - 1;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (remainingUseDuration <= 1) {
            livingEntity.releaseUsingItem();
        }
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
        return getThrown(level, position, itemStack);
    }

    public ThrownSword getThrown(Level level, ItemStack stack, LivingEntity owner) {
        return new ThrownSword(projectile.get(), level, owner, stack, hitGroundSound);
    }

    public ThrownSword getThrown(Level level, Position pos, ItemStack stack) {
        return new ThrownSword(projectile.get(), level, pos.x(), pos.y(), pos.z(), stack, hitGroundSound);
    }
}
