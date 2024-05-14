package dev.thomasglasser.tommylib.impl.mixin.minecraft.world.item.alchemy;

import dev.thomasglasser.tommylib.api.world.effect.EmptyMobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.PotionContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;

@Mixin(PotionContents.class)
public class PotionContentsMixin
{
    @ModifyVariable(method = "addPotionTooltip(Ljava/lang/Iterable;Ljava/util/function/Consumer;FF)V", argsOnly = true, index = 0, at = @At("HEAD"))
    private static Iterable<MobEffectInstance> addPotionTooltip(Iterable<MobEffectInstance> value)
    {
        ArrayList<MobEffectInstance> list = new ArrayList<>();
        value.forEach(effect -> {
            if (!(effect.getEffect().value() instanceof EmptyMobEffect || effect.getEffect().value() instanceof EmptyMobEffect.Instantaneous))
            {
                list.add(effect);
            }
        });
        return list;
    }
}
