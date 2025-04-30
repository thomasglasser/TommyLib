package dev.thomasglasser.tommylib.api.client.renderer.entity.layers.geo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.ItemArmorGeoLayer;

// TODO: Check
/**
 * Extension of {@link ItemArmorGeoLayer} that adds support for rendering elytra.
 *
 * @deprecated No longer needed in 1.21.5+
 *
 * @param <T> The type of entity that this layer will render for.
 */
@Deprecated(forRemoval = true, since = "31.0.0")
public abstract class ElytraAndItemArmorGeoLayer<T extends LivingEntity & GeoAnimatable> extends ItemArmorGeoLayer<T> {
    public ElytraAndItemArmorGeoLayer(GeoRenderer<T> geoRenderer) {
        super(geoRenderer);
    }

    @Override
    public void renderForBone(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        ItemStack armorStack = getArmorItemForBone(bone, animatable);

        if (armorStack == null)
            return;
        if (armorStack.getItem() instanceof ElytraItem) {
            ModelPart modelPart = Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.ELYTRA);
            ElytraModel<T> model = new ElytraModel<>(modelPart);
            ModelPart left = modelPart.getChild("left_wing");
            ModelPart right = modelPart.getChild("right_wing");

            model.setupAnim(animatable, 0, 0, 0, 0, 0);

            if (getEquipmentSlotForBone(bone, armorStack, animatable).equals(EquipmentSlot.CHEST)) {
                ResourceLocation texture = ResourceLocation.withDefaultNamespace("textures/entity/elytra.png");
                buffer = armorStack.hasFoil() ? bufferSource.getBuffer(RenderType.armorEntityGlint()) : bufferSource.getBuffer(RenderType.armorCutoutNoCull(texture));
                if (!left.cubes.isEmpty()) {
                    renderWing(poseStack, left, buffer, packedLight, packedOverlay);
                }
                if (!right.cubes.isEmpty()) {
                    renderWing(poseStack, right, buffer, packedLight, packedOverlay);
                }
            }
        } else {
            super.renderForBone(poseStack, animatable, bone, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
        }
    }

    private void renderWing(PoseStack poseStack, ModelPart modelPart, VertexConsumer buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.scale(-1, -1, 1);
        poseStack.translate(0, -1.5, 0.1);
        modelPart.render(poseStack, buffer, packedLight, packedOverlay);
        poseStack.popPose();
    }
}
