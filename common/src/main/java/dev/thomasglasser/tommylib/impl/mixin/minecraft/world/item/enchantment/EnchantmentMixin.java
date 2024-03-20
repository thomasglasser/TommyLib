package dev.thomasglasser.tommylib.impl.mixin.minecraft.world.item.enchantment;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.thomasglasser.tommylib.api.world.item.Enchantable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Enchantment.class)
public class EnchantmentMixin
{
    @ModifyReturnValue(method = "canEnchant", at = @At(value = "TAIL"))
    boolean canEnchant(boolean original, ItemStack stack)
    {
        if (stack.getItem() instanceof Enchantable enchantable && enchantable.canEnchant((Enchantment)(Object)this, stack))
            return true;
        return original;
    }
}
