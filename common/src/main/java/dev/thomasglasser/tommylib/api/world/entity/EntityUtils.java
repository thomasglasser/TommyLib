package dev.thomasglasser.tommylib.api.world.entity;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Helpers for entities and entity interactions
 */
public class EntityUtils {
    public static final BiPredicate<LivingEntity, LivingEntity> TARGET_TOO_FAR_PREDICATE = (entity, target) ->
            entity.getAttributes().hasAttribute(Attributes.FOLLOW_RANGE) && entity.distanceToSqr(target) >= Math.pow(entity.getAttributeValue(Attributes.FOLLOW_RANGE), 2);

    /**
     * Spawn particles in a beam from the entity's eyes.
     *
     * @param particleOptions The particle to spawn
     * @param level           The level to spawn the particles in
     * @param entity          The entity to spawn the particles from
     */
    public static void beamParticles(ParticleOptions particleOptions, Level level, Entity entity) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            Vec3 look = entity.getViewVector(0);
            Vec3 eyepos = entity.getEyePosition(0).add(look.x * entity.getBbWidth(), 0, look.z * entity.getBbWidth());
            for (double i = 0; i <= 200d; i += 0.1d) {
                Vec3 traceVec2 = eyepos.add(look.x * i, look.y * i, look.z * i);
                Vec3 b = new Vec3(traceVec2.x, traceVec2.y, traceVec2.z);
                for (int j = 0; j < 3; ++j) {
                    double d1 = 0.0D;
                    double d2 = level.getRandom().nextGaussian() * 0.02D;
                    double d3 = level.getRandom().nextGaussian() * 0.02D;
                    double d4 = level.getRandom().nextGaussian() * 0.02D;
                    double d6 = b.x();
                    double d7 = b.y();
                    double d8 = b.z();
                    serverLevel.sendParticles(particleOptions, d6, d7, d8, 1, d2, d3, d4, 0);
                }
            }
        }
    }

    /**
     * Collects every item an entity has in a {@link Set}.
     * 
     * @param entity The entity to check for items
     * @return The {@link Set} of any items the entity has
     */
    public static Set<ItemStack> getInventory(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            Set<ItemStack> inventory = new ReferenceOpenHashSet<>();
            for (ItemStack stack : livingEntity.getAllSlots()) {
                inventory.add(stack);
            }
            if (livingEntity instanceof Player player) {
                inventory.addAll(player.getInventory().items);
            } else if (livingEntity instanceof InventoryCarrier carrier) {
                inventory.addAll(carrier.getInventory().getItems());
            }
            return inventory;
        } else if (entity instanceof ItemEntity itemEntity) {
            return ReferenceOpenHashSet.of(itemEntity.getItem());
        }
        return ReferenceOpenHashSet.of();
    }

    /**
     * Checks if the entity has any items matching the predicate in its inventory.
     * 
     * @param entity    The entity to check
     * @param predicate The predicate to test
     * @return Whether the entity has any matching items in its inventory
     */
    public static boolean hasAnyInInventory(Entity entity, Predicate<ItemStack> predicate) {
        for (ItemStack stack : getInventory(entity)) {
            if (predicate.test(stack))
                return true;
        }
        return false;
    }
}
