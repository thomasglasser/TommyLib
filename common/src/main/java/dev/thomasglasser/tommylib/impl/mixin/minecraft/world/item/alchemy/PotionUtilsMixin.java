package dev.thomasglasser.tommylib.impl.mixin.minecraft.world.item.alchemy;

import dev.thomasglasser.tommylib.api.world.effect.EmptyMobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(PotionUtils.class)
public class PotionUtilsMixin
{
    @ModifyVariable(method = "addPotionTooltip(Ljava/util/List;Ljava/util/List;FF)V", argsOnly = true, index = 0, at = @At("HEAD"))
    private static List<MobEffectInstance> minejago_addPotionTooltip(List<MobEffectInstance> value)
    {
        value.removeIf(instance -> instance.getEffect() instanceof EmptyMobEffect);
        return value;
    }
}
