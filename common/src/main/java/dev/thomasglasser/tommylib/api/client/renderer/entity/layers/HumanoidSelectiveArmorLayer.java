package dev.thomasglasser.tommylib.api.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

/**
 * An extension of {@link HumanoidArmorLayer} that allows for selective rendering of armor slots.
 * 
 * @param <T> The entity type
 * @param <M> The inner model type
 * @param <A> The outer model type
 */
public class HumanoidSelectiveArmorLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends HumanoidArmorLayer<T, M, A> {
    private boolean renderHead = true;
    private boolean renderChest = true;
    private boolean renderLegs = true;
    private boolean renderFeet = true;

    public HumanoidSelectiveArmorLayer(RenderLayerParent<T, M> renderLayerParent, A humanoidModel, A humanoidModel2, ModelManager modelManager) {
        super(renderLayerParent, humanoidModel, humanoidModel2, modelManager);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (renderChest)
            this.renderArmorPiece(poseStack, buffer, livingEntity, EquipmentSlot.CHEST, packedLight, this.getArmorModel(EquipmentSlot.CHEST));
        if (renderLegs)
            this.renderArmorPiece(poseStack, buffer, livingEntity, EquipmentSlot.LEGS, packedLight, this.getArmorModel(EquipmentSlot.LEGS));
        if (renderFeet)
            this.renderArmorPiece(poseStack, buffer, livingEntity, EquipmentSlot.FEET, packedLight, this.getArmorModel(EquipmentSlot.FEET));
        if (renderHead)
            this.renderArmorPiece(poseStack, buffer, livingEntity, EquipmentSlot.HEAD, packedLight, this.getArmorModel(EquipmentSlot.HEAD));
    }

    public HumanoidSelectiveArmorLayer<T, M, A> setRenderHead(boolean renderHead) {
        this.renderHead = renderHead;
        return this;
    }

    public HumanoidSelectiveArmorLayer<T, M, A> setRenderChest(boolean renderChest) {
        this.renderChest = renderChest;
        return this;
    }

    public HumanoidSelectiveArmorLayer<T, M, A> setRenderLegs(boolean renderLegs) {
        this.renderLegs = renderLegs;
        return this;
    }

    public HumanoidSelectiveArmorLayer<T, M, A> setRenderFeet(boolean renderFeet) {
        this.renderFeet = renderFeet;
        return this;
    }

    public HumanoidSelectiveArmorLayer<T, M, A> setRenderAll(boolean renderAll) {
        this.renderHead = renderAll;
        this.renderChest = renderAll;
        this.renderLegs = renderAll;
        this.renderFeet = renderAll;
        return this;
    }
}
