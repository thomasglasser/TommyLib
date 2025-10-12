package dev.thomasglasser.tommylib.impl.platform;

import dev.thomasglasser.tommylib.impl.platform.services.BlockEntityHelper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.ValueInput;

public class NeoForgeBlockEntityHelper implements BlockEntityHelper {
    @Override
    public void handleUpdateTag(BlockEntity be, ValueInput input) {
        be.handleUpdateTag(input);
    }
}
