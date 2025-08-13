package dev.thomasglasser.tommylib.impl.mixin.minecraft.world.item.alchemy;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.thomasglasser.tommylib.api.world.item.alchemy.EmptyColoredPotion;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PotionContents.class)
public class PotionContentsMixin {
    @Shadow
    @Final
    private Optional<Integer> customColor;

    @Shadow
    @Final
    private Optional<Holder<Potion>> potion;

    @ModifyReturnValue(method = "getColor()I", at = @At("RETURN"))
    public int getEmptyColoredPotionColor(int original) {
        if (customColor.isEmpty() && potion.isPresent() && potion.get().value() instanceof EmptyColoredPotion emptyColoredPotion)
            return emptyColoredPotion.getColor();
        return original;
    }

    @ModifyReturnValue(method = "getColor(Lnet/minecraft/core/Holder;)I", at = @At("RETURN"))
    private static int getEmptyColoredPotionColor(int original, Holder<Potion> potion) {
        if (potion.value() instanceof EmptyColoredPotion emptyColoredPotion)
            return emptyColoredPotion.getColor();
        return original;
    }
}
