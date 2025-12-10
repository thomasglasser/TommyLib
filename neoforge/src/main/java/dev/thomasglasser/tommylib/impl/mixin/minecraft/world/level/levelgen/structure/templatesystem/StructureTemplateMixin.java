package dev.thomasglasser.tommylib.impl.mixin.minecraft.world.level.levelgen.structure.templatesystem;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.painting.Painting;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StructureTemplate.class)
public class StructureTemplateMixin {
    // Injects into StructureTemplate#placeEntities, inside the lambda of createEntityIgnoreException
    @Inject(method = "lambda$addEntitiesToWorld$5", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;snapTo(DDDFF)V", shift = At.Shift.AFTER))
    private static void fixPaintingPlacement(StructurePlaceSettings placementIn, Vec3 vec31, ServerLevelAccessor p_74524_, Entity entity, CallbackInfo ci) {
        if (!(entity instanceof Painting painting)) {
            return;
        }

        var pos = new BlockPos.MutableBlockPos();
        pos.set(painting.getPos());
        var variant = painting.getVariant().value();

        var width = variant.width();
        var height = variant.height();
        var direction = painting.getDirection();

        // paintings with an even height seem to always be moved upwards...
        if (height % 2 == 0) {
            pos.move(0, -1, 0);
        }

        // paintings with an even width seem to be moved in the clockwise direction of their facing direction,
        // if they're west or south.
        if (width % 2 == 0 && (direction == Direction.WEST || direction == Direction.SOUTH)) {
            var moveTo = direction.getClockWise().getUnitVec3i();
            pos.move(moveTo);
        }

        painting.setPos(pos.getCenter());
    }
}
