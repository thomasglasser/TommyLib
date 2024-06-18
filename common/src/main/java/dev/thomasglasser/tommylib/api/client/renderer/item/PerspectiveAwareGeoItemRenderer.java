package dev.thomasglasser.tommylib.api.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.thomasglasser.tommylib.api.client.ClientUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

/**
 * Extension of {@link GeoItemRenderer} that renders a separate model for 2D item rendering contexts.
 * @param <T> The type of item to render
 */
public class PerspectiveAwareGeoItemRenderer<T extends Item & GeoAnimatable> extends GeoItemRenderer<T>
{
	protected ResourceLocation inventoryAssetLoc;
	protected ResourceLocation worldAssetLoc;

	public PerspectiveAwareGeoItemRenderer(GeoModel<T> model, ResourceLocation inventoryAssetLoc)
	{
		super(model);
		this.inventoryAssetLoc = inventoryAssetLoc;
		this.worldAssetLoc = ResourceLocation.fromNamespaceAndPath(inventoryAssetLoc.getNamespace(), "textures/item/geo/" + inventoryAssetLoc.getPath() + ".png");
	}

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay)
	{
		if (displayContext == ItemDisplayContext.GUI || displayContext == ItemDisplayContext.FIXED || displayContext == ItemDisplayContext.GROUND)
		{
			if (!(displayContext == ItemDisplayContext.GROUND)) poseStack.translate(0.0D, 0.5D, 0.0D);
			poseStack.translate(0.5D, 0.5D, 0.5D);
			poseStack.mulPose(Axis.YN.rotationDegrees(90));
			poseStack.mulPose(Axis.ZN.rotationDegrees(0.1f));
			ClientUtils.renderItem(stack, displayContext, false, poseStack, bufferSource, packedLight, packedOverlay, inventoryAssetLoc.getNamespace(), inventoryAssetLoc.getPath() + "_inventory");
		}
		else
			super.renderByItem(stack, displayContext, poseStack, bufferSource, packedLight, packedOverlay);
	}

	@Override
	public ResourceLocation getTextureLocation(T animatable)
	{
		return worldAssetLoc;
	}
}
