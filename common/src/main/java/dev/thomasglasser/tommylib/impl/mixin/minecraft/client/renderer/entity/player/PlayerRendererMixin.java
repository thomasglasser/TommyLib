package dev.thomasglasser.tommylib.impl.mixin.minecraft.client.renderer.entity.player;

import dev.thomasglasser.tommylib.impl.GeckoLibUtils;
import dev.thomasglasser.tommylib.impl.TommyLib;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(method = "setModelProperties", at = @At("TAIL"))
    private void handleSkintightBoneVisibility(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        if (TommyLib.Dependencies.GECKOLIB.isLoaded()) {
            PlayerModel<AbstractClientPlayer> playerModel = getModel();
            Item chest = clientPlayer.getItemBySlot(EquipmentSlot.CHEST).getItem();
            if (GeckoLibUtils.isSkintight(chest)) {
                playerModel.leftSleeve.visible = false;
                playerModel.rightSleeve.visible = false;
                playerModel.jacket.visible = false;
            }
            Item head = clientPlayer.getItemBySlot(EquipmentSlot.HEAD).getItem();
            if (GeckoLibUtils.isSkintight(head)) {
                playerModel.hat.visible = false;
            }
            Item feet = clientPlayer.getItemBySlot(EquipmentSlot.FEET).getItem();
            Item legs = clientPlayer.getItemBySlot(EquipmentSlot.LEGS).getItem();
            if (GeckoLibUtils.isSkintight(feet) || GeckoLibUtils.isSkintight(legs)) {
                playerModel.rightPants.visible = false;
                playerModel.leftPants.visible = false;
            }
        }
    }
}
