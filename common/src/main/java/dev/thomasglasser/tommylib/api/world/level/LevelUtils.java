package dev.thomasglasser.tommylib.api.world.level;

import dev.thomasglasser.tommylib.api.tags.ConventionalBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public final class LevelUtils {
    private LevelUtils() {}

    /**
     * Safely destroy a block at a position, if the block is not unbreakable.
     * 
     * @param level The level to destroy the block in.
     * @param pos   The position of the block to destroy.
     */
    public static void safeFall(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (!state.is(ConventionalBlockTags.UNBREAKABLE_BLOCKS))
            FallingBlockEntity.fall(level, pos, state);
    }

    /**
     * Safely destroy a block at a position, if the block is not unbreakable.
     * 
     * @param level The level to destroy the block in.
     * @param pos   The position of the block to destroy.
     * @param drop  Whether to drop the block.
     */
    public static void safeDestroy(Level level, BlockPos pos, boolean drop) {
        if (!level.getBlockState(pos).is(ConventionalBlockTags.UNBREAKABLE_BLOCKS)) {
            level.destroyBlock(pos, drop);
        }
    }

    /**
     * Spawn particles in a beam from the entity's eyes.
     * 
     * @param particleOptions The particle options to spawn.
     * @param level           The level to spawn the particles in.
     * @param entity          The entity to spawn the particles from.
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
}
