package dev.thomasglasser.tommylib.impl.mixin.minecraft.world.item.alchemy;

import dev.thomasglasser.tommylib.api.world.item.alchemy.EmptyColoredPotion;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PotionContents.class)
public class PotionContentsMixin {
    @Shadow
    @Final
    private Optional<Holder<Potion>> potion;

    @ModifyConstant(method = "getColor()I", constant = @Constant(intValue = -13083194))
    public int defaultColor(int original) {
        if (potion.isPresent() && potion.get().value() instanceof EmptyColoredPotion emptyColoredPotion)
            return emptyColoredPotion.getColor();
        return original;
    }
}
