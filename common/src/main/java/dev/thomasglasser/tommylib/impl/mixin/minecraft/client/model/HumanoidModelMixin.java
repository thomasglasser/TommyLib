package dev.thomasglasser.tommylib.impl.mixin.minecraft.client.model;

import dev.thomasglasser.tommylib.api.world.item.ItemUtils;
import dev.thomasglasser.tommylib.impl.GeckoLibUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity>
{
    @Shadow @Final public ModelPart leftArm;

    @Shadow @Final public ModelPart rightArm;

    @Inject(method = "prepareMobModel(Lnet/minecraft/world/entity/LivingEntity;FFF)V", at = @At("TAIL"))
    private void tommylib_prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick, CallbackInfo ci)
    {
        Item chest = entity.getItemBySlot(EquipmentSlot.CHEST).getItem();
        if (ItemUtils.isGeckoLoaded() && GeckoLibUtils.isSkintight(chest))
        {
            this.leftArm.visible = false;
            this.rightArm.visible = false;
        }
    }
}
