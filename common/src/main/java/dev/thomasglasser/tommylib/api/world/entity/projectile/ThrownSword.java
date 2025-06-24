package dev.thomasglasser.tommylib.api.world.entity.projectile;

import dev.thomasglasser.tommylib.api.world.item.ItemUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * A projectile that moves and behaves like a Trident.
 */
public abstract class ThrownSword extends AbstractArrow {
    private static final EntityDataAccessor<Byte> DATA_LOYALTY = SynchedEntityData.defineId(ThrownSword.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> DATA_FOIL = SynchedEntityData.defineId(ThrownSword.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> DATA_BASE_DAMAGE = SynchedEntityData.defineId(ThrownSword.class, EntityDataSerializers.FLOAT);

    private final SoundEvent returnSound;

    private boolean dealtDamage;
    private boolean playedReturnSound;

    public ThrownSword(EntityType<? extends ThrownSword> entity, Level level) {
        super(entity, level);
        this.pickup = Pickup.ALLOWED;
        this.returnSound = null;
    }

    public ThrownSword(EntityType<? extends ThrownSword> entityType, double x, double y, double z, Level level, ItemStack pickupItemStack, float baseDamage, @Nullable SoundEvent hitGroundSound, @Nullable SoundEvent returnSound) {
        super(entityType, x, y, z, level, pickupItemStack, pickupItemStack);
        this.pickup = Pickup.ALLOWED;
        setSoundEvent(hitGroundSound != null ? hitGroundSound : getDefaultHitGroundSoundEvent());
        this.returnSound = returnSound != null ? returnSound : getDefaultReturnSoundEvent();
        this.entityData.set(DATA_BASE_DAMAGE, baseDamage);
        this.entityData.set(DATA_LOYALTY, ItemUtils.getLoyaltyFromItem(pickupItemStack, level, this));
        this.entityData.set(DATA_FOIL, pickupItemStack.hasFoil());
    }

    public ThrownSword(EntityType<? extends ThrownSword> entityType, LivingEntity shooter, Level level, ItemStack pickupItemStack, float baseDamage, @Nullable SoundEvent hitGroundSound, @Nullable SoundEvent returnSound) {
        this(entityType, shooter.getX(), shooter.getEyeY() - 0.1F, shooter.getZ(), level, pickupItemStack, baseDamage, hitGroundSound, returnSound);
        setOwner(shooter);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_LOYALTY, (byte) 0);
        builder.define(DATA_FOIL, false);
        builder.define(DATA_BASE_DAMAGE, 1f);
    }

    public byte getLoyalty() {
        return this.entityData.get(DATA_LOYALTY);
    }

    public boolean isFoil() {
        return this.entityData.get(DATA_FOIL);
    }

    @Override
    public double getBaseDamage() {
        return this.entityData.get(DATA_BASE_DAMAGE);
    }

    @Override
    public void setBaseDamage(double baseDamage) {
        this.entityData.set(DATA_BASE_DAMAGE, (float) baseDamage);
    }

    public boolean isInGround() {
        return inGround;
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity owner = this.getOwner();
        int loyalty = this.getLoyalty();
        if (loyalty > 0 && (this.dealtDamage || this.isNoPhysics()) && owner != null) {
            if (!this.isAcceptableReturnOwner()) {
                if (!this.level().isClientSide && this.pickup == Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 ownerDirection = owner.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + ownerDirection.y * 0.015 * (double) loyalty, this.getZ());
                if (this.level().isClientSide) {
                    this.yOld = this.getY();
                }

                double speed = 0.05D * (double) loyalty;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(ownerDirection.normalize().scale(speed)));
                if (!playedReturnSound) {
                    this.playSound(getReturnSoundEvent(), 10.0F, 1.0F);
                    playedReturnSound = true;
                }
            }
        }

        super.tick();
    }

    private boolean isAcceptableReturnOwner() {
        Entity entity = this.getOwner();
        return entity != null && entity.isAlive() && (!(entity instanceof ServerPlayer) || !entity.isSpectator());
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 pStartVec, Vec3 pEndVec) {
        return this.dealtDamage ? null : super.findHitEntity(pStartVec, pEndVec);
    }

    protected void onHitEntity(EntityHitResult pResult) {
        Entity entity = pResult.getEntity();
        float damage = (float) getBaseDamage();
        Entity entity1 = this.getOwner();
        DamageSource damagesource = this.damageSources().trident(this, entity1 == null ? this : entity1);
        if (this.level() instanceof ServerLevel serverLevel) {
            damage = EnchantmentHelper.modifyDamage(serverLevel, getPickupItem(), entity, damagesource, damage);
        }

        this.dealtDamage = true;
        if (entity.hurt(damagesource, damage)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (this.level() instanceof ServerLevel serverLevel) {
                EnchantmentHelper.doPostAttackEffectsWithItemSource(serverLevel, entity, damagesource, this.getWeaponItem());
            }

            if (entity instanceof LivingEntity livingentity) {
                this.doKnockback(livingentity, damagesource);
                this.doPostHurtEffects(livingentity);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
        this.playSound(getHitGroundSoundEvent());
    }

    protected boolean tryPickup(Player p_150196_) {
        return super.tryPickup(p_150196_) || this.isNoPhysics() && this.ownedBy(p_150196_) && p_150196_.getInventory().add(this.getPickupItem());
    }

    @Override
    public ItemStack getWeaponItem() {
        return getPickupItemStackOrigin();
    }

    public void playerTouch(Player pEntity) {
        if (this.ownedBy(pEntity) || this.getOwner() == null) {
            super.playerTouch(pEntity);
        }
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.dealtDamage = compoundTag.getBoolean("DealtDamage");
        this.playedReturnSound = compoundTag.getBoolean("PlayedReturnSound");
        setBaseDamage(compoundTag.getFloat("BaseDamage"));
        this.entityData.set(DATA_LOYALTY, ItemUtils.getLoyaltyFromItem(getPickupItemStackOrigin(), level(), this));
        this.entityData.set(DATA_FOIL, getPickupItemStackOrigin().hasFoil());
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);

        compoundTag.putBoolean("DealtDamage", this.dealtDamage);
        compoundTag.putBoolean("PlayedReturnSound", this.playedReturnSound);
        compoundTag.putFloat("BaseDamage", (float) this.getBaseDamage());
    }

    public void tickDespawn() {
        int i = this.getLoyalty();
        if (this.pickup != Pickup.ALLOWED || i <= 0) {
            super.tickDespawn();
        }
    }

    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    protected SoundEvent getDefaultReturnSoundEvent() {
        return SoundEvents.TRIDENT_RETURN;
    }

    protected final SoundEvent getReturnSoundEvent() {
        return returnSound;
    }
}
