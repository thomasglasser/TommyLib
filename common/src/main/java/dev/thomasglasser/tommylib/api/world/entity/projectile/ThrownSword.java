package dev.thomasglasser.tommylib.api.world.entity.projectile;

import dev.thomasglasser.tommylib.api.world.item.ItemUtils;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * A projectile that moves and behaves like a {@link ThrownTrident}. Can be enchanted with {@link Enchantments#LOYALTY}
 */
public class ThrownSword extends AbstractArrow {
    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(ThrownSword.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(ThrownSword.class, EntityDataSerializers.BOOLEAN);

    @Nullable
    private final Holder<SoundEvent> hitGroundSound;

    private boolean dealtDamage;

    public ThrownSword(EntityType<? extends ThrownSword> entity, Level level) {
        super(entity, level);
        this.hitGroundSound = null;
    }

    public ThrownSword(EntityType<? extends ThrownSword> entityType, Level level, LivingEntity shooter, ItemStack pickupItemStack, Holder<SoundEvent> hitGroundSound) {
        super(entityType, shooter, level, pickupItemStack, null);
        this.hitGroundSound = hitGroundSound;
        this.entityData.set(ID_LOYALTY, ItemUtils.getLoyaltyFromItem(pickupItemStack, level, this));
        this.entityData.set(ID_FOIL, pickupItemStack.hasFoil());
    }

    public ThrownSword(EntityType<? extends ThrownSword> entityType, Level level, double x, double y, double z, ItemStack pickupItemStack, Holder<SoundEvent> hitGroundSound) {
        super(entityType, x, y, z, level, pickupItemStack, pickupItemStack);
        this.hitGroundSound = hitGroundSound;
        this.entityData.set(ID_LOYALTY, ItemUtils.getLoyaltyFromItem(pickupItemStack, level, this));
        this.entityData.set(ID_FOIL, pickupItemStack.hasFoil());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ID_LOYALTY, (byte) 0);
        builder.define(ID_FOIL, false);
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity entity = this.getOwner();
        int i = this.entityData.get(ID_LOYALTY);
        if (i > 0 && (this.dealtDamage || this.isNoPhysics()) && entity != null) {
            if (!this.isAcceptibleReturnOwner()) {
                if (level() instanceof ServerLevel serverlevel && this.pickup == Pickup.ALLOWED) {
                    this.spawnAtLocation(serverlevel, this.getPickupItem(), 0.1F);
                }

                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 vec3 = entity.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015D * (double) i, this.getZ());
                if (this.level().isClientSide()) {
                    this.yOld = this.getY();
                }

                double d0 = 0.05D * (double) i;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3.normalize().scale(d0)));
            }
        }

        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    public boolean isFoil() {
        return this.entityData.get(ID_FOIL);
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 pStartVec, Vec3 pEndVec) {
        return this.dealtDamage ? null : super.findHitEntity(pStartVec, pEndVec);
    }

    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        float f = 8.0F;
        Entity entity2 = this.getOwner();
        DamageSource damageSource = this.damageSources().trident(this, (Entity) (entity2 == null ? this : entity2));
        if (level() instanceof ServerLevel serverLevel) {
            f = EnchantmentHelper.modifyDamage(serverLevel, this.getWeaponItem(), entity, damageSource, f);
        }

        this.dealtDamage = true;
        if (entity.hurtOrSimulate(damageSource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (level() instanceof ServerLevel serverLevel) {
                EnchantmentHelper.doPostAttackEffectsWithItemSourceOnBreak(serverLevel, entity, damageSource, this.getWeaponItem(), (item) -> this.kill(serverLevel));
            }

            if (entity instanceof LivingEntity livingEntity) {
                this.doKnockback(livingEntity, damageSource);
                this.doPostHurtEffects(livingEntity);
            }
        }

        this.deflect(ProjectileDeflection.REVERSE, entity, owner, false);
        this.setDeltaMovement(this.getDeltaMovement().multiply(0.02, 0.2, 0.02));
        this.playSound(getDefaultHitGroundSoundEvent());
    }

    protected boolean tryPickup(Player p_150196_) {
        return super.tryPickup(p_150196_) || this.isNoPhysics() && this.ownedBy(p_150196_) && p_150196_.getInventory().add(this.getPickupItem());
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return Items.AIR.getDefaultInstance();
    }

    public void playerTouch(Player pEntity) {
        if (this.ownedBy(pEntity) || this.getOwner() == null) {
            super.playerTouch(pEntity);
        }
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.dealtDamage = input.getBooleanOr("DealtDamage", false);
        this.entityData.set(ID_LOYALTY, ItemUtils.getLoyaltyFromItem(getDefaultPickupItem(), level(), this));
    }

    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("DealtDamage", this.dealtDamage);
    }

    public void tickDespawn() {
        int i = this.entityData.get(ID_LOYALTY);
        if (this.pickup != Pickup.ALLOWED || i <= 0) {
            super.tickDespawn();
        }
    }

    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        if (hitGroundSound != null)
            return hitGroundSound.value();
        return super.getDefaultHitGroundSoundEvent();
    }
}
