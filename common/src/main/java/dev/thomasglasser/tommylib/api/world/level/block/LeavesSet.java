package dev.thomasglasser.tommylib.api.world.level.block;

import dev.thomasglasser.tommylib.api.registration.DeferredBlock;
import net.minecraft.resources.ResourceLocation;

/**
 * A set of blocks related to a leaf block.
 * @param id The ID of the leaf block
 * @param leaves The leaf block
 * @param sapling The sapling block
 * @param pottedSapling The potted sapling block
 */
public record LeavesSet(ResourceLocation id,
                        DeferredBlock<?> leaves,
                        DeferredBlock<?> sapling,
                        DeferredBlock<?> pottedSapling)
{}
