package dev.thomasglasser.tommylib.impl.platform.services;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.ValueInput;

public interface BlockEntityHelper {
    void handleUpdateTag(BlockEntity be, ValueInput input);
}
