package dev.thomasglasser.tommylib.api.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.thomasglasser.tommylib.api.world.entity.projectile.ThrownSword;
import java.util.List;
import java.util.function.Function;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;

/**
 * Renders a {@link ThrownSword} entity using a {@link ThrownTridentRenderState}.
 * 
 * @param <T> The type of {@link ThrownSword} entity to render.
 */
public class ThrownSwordRenderer<T extends ThrownSword> extends EntityRenderer<T, ThrownTridentRenderState> {
    public static final Function<Identifier, Identifier> TEXTURE = (loc) -> loc.withPrefix("textures/entity/item/").withSuffix(".png");

    private final Model<Unit> model;
    private final Identifier texture;

    public ThrownSwordRenderer(EntityRendererProvider.Context context, Identifier itemId, Model<Unit> model) {
        super(context);
        this.texture = TEXTURE.apply(itemId);
        this.model = model;
    }

    @Override
    public void submit(ThrownTridentRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(renderState.yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(renderState.xRot + 90.0F));
        List<RenderType> list = ItemRenderer.getFoilRenderTypes(this.model.renderType(texture), false, renderState.isFoil);

        for (int i = 0; i < list.size(); i++) {
            nodeCollector.order(i)
                    .submitModel(
                            this.model, Unit.INSTANCE, poseStack, list.get(i), renderState.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, renderState.outlineColor, null);
        }

        poseStack.popPose();
        super.submit(renderState, poseStack, nodeCollector, cameraRenderState);
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
