package dev.thomasglasser.tommylib.api.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.thomasglasser.tommylib.api.world.entity.projectile.ThrownSword;
import java.util.function.Function;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders a {@link ThrownSword} entity using a {@link ThrownTridentRenderState}.
 * 
 * @param <T> The type of {@link ThrownSword} entity to render.
 */
public class ThrownSwordRenderer<T extends ThrownSword> extends EntityRenderer<T, ThrownTridentRenderState> {
    public static final Function<ResourceLocation, ResourceLocation> TEXTURE = (loc) -> loc.withPrefix("textures/entity/item/").withSuffix(".png");

    private final Model model;
    private final ResourceLocation texture;

    public ThrownSwordRenderer(EntityRendererProvider.Context context, ResourceLocation itemLoc, Model model) {
        super(context);
        this.texture = TEXTURE.apply(itemLoc);
        this.model = model;
    }

    @Override
    public void render(ThrownTridentRenderState renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(renderState.yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(renderState.xRot + 90.0F));
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBuffer(bufferSource, this.model.renderType(texture), false, renderState.isFoil);
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(renderState, poseStack, bufferSource, packedLight);
    }

    @Override
    public ThrownTridentRenderState createRenderState() {
        return new ThrownTridentRenderState();
    }

    @Override
    public void extractRenderState(T entity, ThrownTridentRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.yRot = entity.getYRot(partialTick);
        reusedState.xRot = entity.getXRot(partialTick);
        reusedState.isFoil = entity.isFoil();
    }
}
