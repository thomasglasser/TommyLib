package dev.thomasglasser.tommylib.api.world.item;

import dev.thomasglasser.tommylib.api.world.entity.projectile.ThrownSword;
import java.util.function.Supplier;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link SwordItem} that can be thrown as a {@link ThrownSword}.
 */
public class ThrowableSwordItem extends SwordItem implements ProjectileItem {
    private final Supplier<EntityType<? extends ThrownSword>> projectile;
    private final float baseProjectileDamage;
    private final @Nullable SoundEvent throwSound;
    private final @Nullable SoundEvent hitGroundSound;
    private final @Nullable SoundEvent returnSound;

    public ThrowableSwordItem(Supplier<EntityType<? extends ThrownSword>> projectile, float baseProjectileDamage, @Nullable SoundEvent throwSound, @Nullable SoundEvent hitGroundSound, @Nullable SoundEvent returnSound, Tier pTier, Properties pProperties) {
        super(pTier, pProperties);
        this.projectile = projectile;
        this.baseProjectileDamage = baseProjectileDamage;
        this.throwSound = throwSound;
        this.hitGroundSound = hitGroundSound;
        this.returnSound = returnSound;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            int i = this.getUseDuration(stack, entityLiving) - timeLeft;
            if (i >= 10) {
                if (!isTooDamagedToUse(stack)) {
                    if (!level.isClientSide) {
                        stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(entityLiving.getUsedItemHand()));
                        ThrownSword thrown = getThrown(player, level, stack);
                        thrown.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);
                        if (player.hasInfiniteMaterials()) {
                            thrown.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        }

                        level.addFreshEntity(thrown);
                        if (throwSound != null) {
                            level.playSound(null, thrown, throwSound, SoundSource.PLAYERS, 1.0F, 1.0F);
                        }
                        if (!player.hasInfiniteMaterials()) {
                            player.getInventory().removeItem(stack);
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (isTooDamagedToUse(itemstack)) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    private static boolean isTooDamagedToUse(ItemStack stack) {
        return stack.getDamageValue() >= stack.getMaxDamage() - 1;
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (remainingUseDuration <= 1) {
            livingEntity.releaseUsingItem();
        }
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack stack, Direction direction) {
        return getThrown(position, level, stack);
    }

    public ThrownSword getThrown(LivingEntity owner, Level level, ItemStack stack) {
        return new ThrownSword(projectile.get(), owner, level, stack, baseProjectileDamage, hitGroundSound, returnSound) {
            @Override
            protected ItemStack getDefaultPickupItem() {
                return ThrowableSwordItem.this.getDefaultInstance();
            }
        };
    }

    public ThrownSword getThrown(Position pos, Level level, ItemStack stack) {
        return new ThrownSword(projectile.get(), pos.x(), pos.y(), pos.z(), level, stack, baseProjectileDamage, hitGroundSound, returnSound) {
            @Override
            protected ItemStack getDefaultPickupItem() {
                return ThrowableSwordItem.this.getDefaultInstance();
            }
        };
    }
}
