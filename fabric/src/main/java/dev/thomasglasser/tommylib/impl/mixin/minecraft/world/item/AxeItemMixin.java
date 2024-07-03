package dev.thomasglasser.tommylib.impl.mixin.minecraft.world.item;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.thomasglasser.tommylib.api.world.level.block.BlockUtils;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AxeItem.class)
public class AxeItemMixin {
    @ModifyExpressionValue(method = "getStripped", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
    private Object getStripped(Object original, BlockState originalState) {
        if (original == null) {
            return BlockUtils.getStripped(originalState);
        }
        return original;
    }
}
