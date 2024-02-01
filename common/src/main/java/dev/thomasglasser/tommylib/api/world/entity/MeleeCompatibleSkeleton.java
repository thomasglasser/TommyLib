package dev.thomasglasser.tommylib.api.world.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.example.SBLSkeleton;

public class MeleeCompatibleSkeleton extends SBLSkeleton
{
    public MeleeCompatibleSkeleton(EntityType<? extends MeleeCompatibleSkeleton> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public BrainActivityGroup<? extends SBLSkeleton> getCoreTasks() {
        return super.getCoreTasks().behaviours(
                new SetWalkTargetToAttackTarget<>().startCondition((entity) -> !(entity.isHolding(stack -> stack.getItem() instanceof BowItem)))
        );
    }
}
