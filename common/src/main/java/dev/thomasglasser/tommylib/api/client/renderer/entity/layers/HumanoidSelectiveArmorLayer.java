package dev.thomasglasser.tommylib.api.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.EquipmentSlot;

/**
 * An extension of {@link HumanoidArmorLayer} that allows for selective rendering of armor slots.
 * 
 * @param <S> The entity render state
 * @param <M> The inner model type
 * @param <A> The outer model type
 */
public class HumanoidSelectiveArmorLayer<S extends HumanoidRenderState, M extends HumanoidModel<S>, A extends HumanoidModel<S>> extends HumanoidArmorLayer<S, M, A> {
    public boolean renderHead = true;
    public boolean renderChest = true;
    public boolean renderLegs = true;
    public boolean renderFeet = true;

    public HumanoidSelectiveArmorLayer(RenderLayerParent<S, M> renderer, A innerModel, A outerModel, EquipmentLayerRenderer equipmentRenderer) {
        super(renderer, innerModel, outerModel, equipmentRenderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, S renderState, float yRot, float xRot) {
        if (renderChest)
            this.renderArmorPiece(poseStack, bufferSource, renderState.chestEquipment, EquipmentSlot.CHEST, packedLight, this.getArmorModel(renderState, EquipmentSlot.CHEST));
        if (renderLegs)
            this.renderArmorPiece(poseStack, bufferSource, renderState.legsEquipment, EquipmentSlot.LEGS, packedLight, this.getArmorModel(renderState, EquipmentSlot.LEGS));
        if (renderFeet)
            this.renderArmorPiece(poseStack, bufferSource, renderState.feetEquipment, EquipmentSlot.FEET, packedLight, this.getArmorModel(renderState, EquipmentSlot.FEET));
        if (renderHead)
            this.renderArmorPiece(poseStack, bufferSource, renderState.headEquipment, EquipmentSlot.HEAD, packedLight, this.getArmorModel(renderState, EquipmentSlot.HEAD));
    }
}
