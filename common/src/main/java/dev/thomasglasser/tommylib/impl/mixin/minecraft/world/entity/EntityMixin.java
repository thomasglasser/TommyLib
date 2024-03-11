package dev.thomasglasser.tommylib.impl.mixin.minecraft.world.entity;

import dev.thomasglasser.tommylib.api.world.entity.DataHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class)
public class EntityMixin implements DataHolder
{
    @Unique(silent = true)
    private final CompoundTag persistentData = new CompoundTag();

    @Unique(silent = true)
    @Override
    public CompoundTag getPersistentData() {
        return persistentData;
    }
}
