package dev.thomasglasser.tommylib.api.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.thomasglasser.tommylib.api.registration.DeferredItem;
import dev.thomasglasser.tommylib.api.world.entity.projectile.ThrownSword;
import java.util.function.Function;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * Renders a {@link ThrownSword}.
 * 
 * @param <T> The type of {@link ThrownSword} to render
 */
public class ThrownSwordRenderer<T extends ThrownSword> extends EntityRenderer<T> {
    /**
     * Provides the default texture location of a {@link ThrownSword} given its item.
     */
    public static final Function<ResourceLocation, ResourceLocation> TEXTURE = (loc) -> loc.withPrefix("textures/entity/item/").withSuffix(".png");

    private final ResourceLocation texture;
    private final Model model;

    /**
     * Constructs a new {@link ThrownSwordRenderer} instance.
     *
     * @param context The entity renderer provider context
     * @param texture The texture location
     * @param model   The model used for rendering the thrown sword
     */
    public ThrownSwordRenderer(EntityRendererProvider.Context context, ResourceLocation texture, Model model) {
        super(context);
        this.texture = texture;
        this.model = model;
    }

    /**
     * Constructs a new {@link ThrownSwordRenderer} instance with the default texture location given its item.
     *
     * @param context The entity renderer provider context
     * @param item    The item of the {@link ThrownSword} providing the texture
     * @param model   The model used for rendering the thrown sword
     */
    public ThrownSwordRenderer(EntityRendererProvider.Context context, DeferredItem<?> item, Model model) {
        this(context, TEXTURE.apply(item.getId()), model);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTick, entity.xRotO, entity.getXRot()) + 90.0F));
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(bufferSource, this.model.renderType(this.getTextureLocation(entity)), false, entity.isFoil());
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return texture;
    }
}
