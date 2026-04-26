package dev.thomasglasser.tommylib.impl.mixin.minecraft.client;

import java.util.Map;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.resources.language.I18n;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyMapping.class)
public class KeyMappingMixin {
    @Shadow
    @Final
    private static Map<String, Integer> CATEGORY_SORT_ORDER;

    @Shadow
    @Final
    private String category;

    @Inject(method = "compareTo(Lnet/minecraft/client/KeyMapping;)I", at = @At("HEAD"), cancellable = true)
    private void tommylib$safeCompareTo(KeyMapping other, CallbackInfoReturnable<Integer> cir) {
        if (this.category.equals(other.getCategory())) {
            return;
        }

        Integer tCat = CATEGORY_SORT_ORDER.get(this.category);
        Integer oCat = CATEGORY_SORT_ORDER.get(other.getCategory());

        if (tCat != null && oCat != null) {
            return;
        }

        if (tCat == null && oCat == null) {
            cir.setReturnValue(I18n.get(this.category).compareTo(I18n.get(other.getCategory())));
        } else if (tCat == null) {
            cir.setReturnValue(1);
        } else {
            cir.setReturnValue(-1);
        }
    }
}
