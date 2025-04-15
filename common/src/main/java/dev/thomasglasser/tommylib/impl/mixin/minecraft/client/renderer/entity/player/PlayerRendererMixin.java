package dev.thomasglasser.tommylib.impl.mixin.minecraft.client.renderer.entity.player;

import dev.thomasglasser.tommylib.TommyLib;
import dev.thomasglasser.tommylib.impl.GeckoLibUtils;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerRenderState, PlayerModel> {
    private PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/client/renderer/entity/state/PlayerRenderState;F)V", at = @At("TAIL"))
    private void extractRenderState(AbstractClientPlayer player, PlayerRenderState renderState, float partialTick, CallbackInfo ci) {
        if (TommyLib.Dependencies.GECKOLIB.isLoaded()) {
            Item chest = player.getItemBySlot(EquipmentSlot.CHEST).getItem();
            if (GeckoLibUtils.isSkintight(chest)) {
                renderState.showLeftSleeve = false;
                renderState.showRightSleeve = false;
                renderState.showJacket = false;
            }
            Item head = player.getItemBySlot(EquipmentSlot.HEAD).getItem();
            if (GeckoLibUtils.isSkintight(head)) {
                renderState.showHat = false;
            }
            Item feet = player.getItemBySlot(EquipmentSlot.FEET).getItem();
            Item legs = player.getItemBySlot(EquipmentSlot.LEGS).getItem();
            if (GeckoLibUtils.isSkintight(feet) || GeckoLibUtils.isSkintight(legs)) {
                renderState.showRightPants = false;
                renderState.showLeftPants = false;
            }
        }
    }

    // TODO: Port this
//    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"), order = 1001)
//    private void render(AbstractClientPlayer entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
//        if (TommyLib.Dependencies.PLAYERANIMATOR.isLoaded()) {
//            PlayerModel<AbstractClientPlayer> playerModel = getModel();
//            Item chest = entity.getItemBySlot(EquipmentSlot.CHEST).getItem();
//            if (GeckoLibUtils.isSkintight(chest)) {
//                playerModel.leftSleeve.visible = false;
//                playerModel.rightSleeve.visible = false;
//            }
//        }
//    }
}
