package dev.thomasglasser.tommylib.api.world.level.block;

import dev.thomasglasser.tommylib.api.registration.RegistryObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public record LeavesSet(ResourceLocation id,
                        RegistryObject<Block> leaves,
                        RegistryObject<Block> sapling,
                        RegistryObject<Block> pottedSapling)
{}
