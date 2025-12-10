package dev.thomasglasser.tommylib.impl.mixin.minecraft.client.renderer.entity.player;

import dev.thomasglasser.tommylib.TommyLib;
import dev.thomasglasser.tommylib.impl.GeckoLibUtils;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin<AvatarlikeEntity extends Avatar & ClientAvatarEntity> extends LivingEntityRenderer<AvatarlikeEntity, AvatarRenderState, PlayerModel> {
    private AvatarRendererMixin(EntityRendererProvider.Context context, PlayerModel model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("TAIL"))
    private void extractRenderState(AvatarlikeEntity entity, AvatarRenderState reusedState, float partialTick, CallbackInfo ci) {
        if (TommyLib.Dependencies.GECKOLIB.isLoaded()) {
            Item chest = entity.getItemBySlot(EquipmentSlot.CHEST).getItem();
            if (GeckoLibUtils.isSkintight(chest)) {
                reusedState.showLeftSleeve = false;
                reusedState.showRightSleeve = false;
                reusedState.showJacket = false;
            }
            Item head = entity.getItemBySlot(EquipmentSlot.HEAD).getItem();
            if (GeckoLibUtils.isSkintight(head)) {
                reusedState.showHat = false;
            }
            Item feet = entity.getItemBySlot(EquipmentSlot.FEET).getItem();
            Item legs = entity.getItemBySlot(EquipmentSlot.LEGS).getItem();
            if (GeckoLibUtils.isSkintight(feet) || GeckoLibUtils.isSkintight(legs)) {
                reusedState.showRightPants = false;
                reusedState.showLeftPants = false;
            }
        }
    }
}
