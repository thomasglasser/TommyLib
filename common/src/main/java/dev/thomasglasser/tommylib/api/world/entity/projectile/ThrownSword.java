package dev.thomasglasser.tommylib.api.world.entity.projectile;

import dev.thomasglasser.tommylib.api.world.item.ItemUtils;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * A projectile that moves and behaves like a Trident. Can be enchanted with {@link Enchantments#LOYALTY}
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

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity entity = this.getOwner();
        int i = this.entityData.get(ID_LOYALTY);
        if (i > 0 && (this.dealtDamage || this.isNoPhysics()) && entity != null) {
            if (!this.isAcceptibleReturnOwner()) {
                if (!this.level().isClientSide && this.pickup == Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 vec3 = entity.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015D * (double) i, this.getZ());
                if (this.level().isClientSide) {
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

    /**
     * Gets the EntityHitResult representing the entity hit
     */
    @Nullable
    protected EntityHitResult findHitEntity(Vec3 pStartVec, Vec3 pEndVec) {
        return this.dealtDamage ? null : super.findHitEntity(pStartVec, pEndVec);
    }

    /**
     * Called when the arrow hits an entity
     */
    protected void onHitEntity(EntityHitResult pResult) {
        Entity entity = pResult.getEntity();
        float f = 8.0F;
        Entity entity1 = this.getOwner();
        DamageSource damagesource = this.damageSources().arrow(this, entity1 == null ? this : entity1);
        if (this.level() instanceof ServerLevel serverlevel) {
            f = EnchantmentHelper.modifyDamage(serverlevel, getPickupItem(), entity, damagesource, f);
        }

        this.dealtDamage = true;
        if (entity.hurt(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (this.level() instanceof ServerLevel serverlevel1) {
                EnchantmentHelper.doPostAttackEffectsWithItemSource(serverlevel1, entity, damagesource, this.getWeaponItem());
            }

            if (entity instanceof LivingEntity livingentity) {
                this.doKnockback(livingentity, damagesource);
                this.doPostHurtEffects(livingentity);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
        this.playSound(getDefaultHitGroundSoundEvent());
    }

    protected boolean tryPickup(Player p_150196_) {
        return super.tryPickup(p_150196_) || this.isNoPhysics() && this.ownedBy(p_150196_) && p_150196_.getInventory().add(this.getPickupItem());
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return Items.AIR.getDefaultInstance();
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void playerTouch(Player pEntity) {
        if (this.ownedBy(pEntity) || this.getOwner() == null) {
            super.playerTouch(pEntity);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.dealtDamage = pCompound.getBoolean("DealtDamage");
        this.entityData.set(ID_LOYALTY, ItemUtils.getLoyaltyFromItem(getDefaultPickupItem(), level(), this));
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("DealtDamage", this.dealtDamage);
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
