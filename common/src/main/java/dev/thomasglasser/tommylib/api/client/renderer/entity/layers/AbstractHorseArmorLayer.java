package dev.thomasglasser.tommylib.api.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;

// TODO: Check
/**
 * A layer that renders horse armor on an abstract horse entity.
 *
 * @deprecated No longer needed in 1.21.5+
 *
 * @param <T> The type of horse entity.
 */
@Deprecated(forRemoval = true, since = "31.0.0")
public class AbstractHorseArmorLayer<T extends AbstractHorse> extends RenderLayer<T, HorseModel<T>> {
    private final HorseModel<T> model;

    public AbstractHorseArmorLayer(RenderLayerParent<T, HorseModel<T>> renderLayerParent, EntityModelSet entityModelSet) {
        super(renderLayerParent);
        this.model = new HorseModel<>(entityModelSet.bakeLayer(ModelLayers.HORSE_ARMOR));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack bodyStack = livingEntity.getBodyArmorItem();
        if (bodyStack.getItem() instanceof AnimalArmorItem animalArmorItem) {
            if (animalArmorItem.getBodyType() == AnimalArmorItem.BodyType.EQUESTRIAN) {
                this.getParentModel().copyPropertiesTo(this.model);
                this.model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTicks);
                this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                int i;
                if (bodyStack.is(ItemTags.DYEABLE)) {
                    i = FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(bodyStack, -6265536));
                } else {
                    i = -1;
                }
                VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(animalArmorItem.getTexture()));
                this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, i);
            }
        }
    }
}
