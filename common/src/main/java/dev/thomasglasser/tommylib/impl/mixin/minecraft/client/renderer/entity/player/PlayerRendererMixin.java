package dev.thomasglasser.tommylib.impl.mixin.minecraft.client.renderer.entity.player;

import dev.thomasglasser.tommylib.api.world.item.armor.GeoArmorItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>
{
	private PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> entityModel, float f)
	{
		super(context, entityModel, f);
	}

	@Inject(method = "setModelProperties", at = @At("TAIL"))
	private void tommylib_setModelProperties(AbstractClientPlayer clientPlayer, CallbackInfo ci)
	{
		PlayerModel<AbstractClientPlayer> playerModel = getModel();
		if (clientPlayer.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof GeoArmorItem geoArmorItem && geoArmorItem.isSkintight())
		{
			playerModel.leftSleeve.visible = false;
			playerModel.rightSleeve.visible = false;
			playerModel.jacket.visible = false;
			if (Minecraft.getInstance().screen instanceof EffectRenderingInventoryScreen<?>)
			{
				playerModel.leftArm.xScale = 0.90f;
				playerModel.leftArm.yScale = 0.90f;
				playerModel.leftArm.zScale = 0.90f;
				playerModel.rightArm.xScale = 0.90f;
				playerModel.rightArm.yScale = 0.90f;
				playerModel.rightArm.zScale = 0.90f;
				playerModel.body.xScale = 0.90f;
				playerModel.body.yScale = 0.90f;
				playerModel.body.zScale = 0.90f;
			}
			else
			{
				playerModel.leftArm.xScale = 1.0f;
				playerModel.leftArm.yScale = 1.0f;
				playerModel.leftArm.zScale = 1.0f;
				playerModel.rightArm.xScale = 1.0f;
				playerModel.rightArm.yScale = 1.0f;
				playerModel.rightArm.zScale = 1.0f;
				playerModel.body.xScale = 1.0f;
				playerModel.body.yScale = 1.0f;
				playerModel.body.zScale = 1.0f;
			}
		}
		else
		{
			tommyLib$reset(playerModel, EquipmentSlot.CHEST);
		}
		if (clientPlayer.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof GeoArmorItem geoArmorItem && geoArmorItem.isSkintight())
		{
			playerModel.hat.visible = false;
			if (Minecraft.getInstance().screen instanceof EffectRenderingInventoryScreen<?>)
			{
				playerModel.head.xScale = 0.98f;
				playerModel.head.yScale = 0.98f;
				playerModel.head.zScale = 0.98f;
			}
			else
			{
				playerModel.head.xScale = 1.0f;
				playerModel.head.yScale = 1.0f;
				playerModel.head.zScale = 1.0f;
			}
		}
		else
		{
			tommyLib$reset(playerModel, EquipmentSlot.HEAD);
		}
		if (clientPlayer.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof GeoArmorItem iGeoArmorBoots && iGeoArmorBoots.isSkintight() || clientPlayer.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof GeoArmorItem iGeoArmorLeggings && iGeoArmorLeggings.isSkintight())
		{
			playerModel.rightPants.visible = false;
			playerModel.leftPants.visible = false;
			if (Minecraft.getInstance().screen instanceof EffectRenderingInventoryScreen<?>)
			{
				playerModel.leftLeg.xScale = 0.90f;
				playerModel.leftLeg.yScale = 0.90f;
				playerModel.leftLeg.zScale = 0.90f;
				playerModel.rightLeg.xScale = 0.90f;
				playerModel.rightLeg.yScale = 0.90f;
				playerModel.rightLeg.zScale = 0.90f;
			}
			else
			{
				playerModel.leftLeg.xScale = 1.0f;
				playerModel.leftLeg.yScale = 1.0f;
				playerModel.leftLeg.zScale = 1.0f;
				playerModel.rightLeg.xScale = 1.0f;
				playerModel.rightLeg.yScale = 1.0f;
				playerModel.rightLeg.zScale = 1.0f;
			}
		}
		else
		{
			tommyLib$reset(playerModel, EquipmentSlot.LEGS);
		}
	}

	@Unique
	private void tommyLib$reset(PlayerModel<?> model, EquipmentSlot slot)
	{
		switch (slot)
		{
			case HEAD:
				model.hat.visible = true;
				model.head.xScale = 1.0f;
				model.head.yScale = 1.0f;
				model.head.zScale = 1.0f;
				break;
			case CHEST:
				model.leftSleeve.visible = true;
				model.rightSleeve.visible = true;
				model.jacket.visible = true;
				model.leftArm.xScale = 1.0f;
				model.leftArm.yScale = 1.0f;
				model.leftArm.zScale = 1.0f;
				model.rightArm.xScale = 1.0f;
				model.rightArm.yScale = 1.0f;
				model.rightArm.zScale = 1.0f;
				model.body.xScale = 1.0f;
				model.body.yScale = 1.0f;
				model.body.zScale = 1.0f;
				break;
			case LEGS:
				model.rightPants.visible = true;
				model.leftPants.visible = true;
				model.leftLeg.xScale = 1.0f;
				model.leftLeg.yScale = 1.0f;
				model.leftLeg.zScale = 1.0f;
				model.rightLeg.xScale = 1.0f;
				model.rightLeg.yScale = 1.0f;
				model.rightLeg.zScale = 1.0f;
				break;
			default:
				break;
		}
	}
}
