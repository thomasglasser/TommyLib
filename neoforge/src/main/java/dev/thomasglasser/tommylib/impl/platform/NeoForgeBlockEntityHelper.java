package dev.thomasglasser.tommylib.impl.platform;

import dev.thomasglasser.tommylib.impl.platform.services.BlockEntityHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;

public class NeoForgeBlockEntityHelper implements BlockEntityHelper
{
    @Override
    public void handleUpdateTag(BlockEntity be, CompoundTag tag, HolderLookup.Provider lookupProvider) {
        be.handleUpdateTag(tag, lookupProvider);
    }
}
