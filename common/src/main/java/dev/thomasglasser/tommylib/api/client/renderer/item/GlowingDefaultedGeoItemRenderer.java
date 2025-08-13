package dev.thomasglasser.tommylib.api.client.renderer.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

/**
 * A {@link DefaultedGeoItemRenderer} with a default glowmask.
 * 
 * @param <T> The item the renderer is for
 */
public class GlowingDefaultedGeoItemRenderer<T extends Item & GeoAnimatable> extends DefaultedGeoItemRenderer<T> {
    public GlowingDefaultedGeoItemRenderer(ResourceLocation id) {
        super(id);
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
