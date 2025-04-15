package dev.thomasglasser.tommylib.api.world.level.block;

import dev.thomasglasser.tommylib.api.registration.DeferredBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SaplingBlock;

/**
 * A set of blocks related to a leaf block.
 * 
 * @param id            The ID of the leaf block
 * @param leaves        The leaves block
 * @param sapling       The sapling block
 * @param pottedSapling The potted sapling block
 */
public record LeavesSet(ResourceLocation id,
        DeferredBlock<LeavesBlock> leaves,
        DeferredBlock<SaplingBlock> sapling,
        DeferredBlock<FlowerPotBlock> pottedSapling) {}
