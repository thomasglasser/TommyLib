package dev.thomasglasser.tommylib.api.client.renderer.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

/**
 * A {@link GeoItemRenderer} using the default model and texture location based on the passed item {@link ResourceLocation}.
 * 
 * @param <T> The item the renderer is for
 */
public class DefaultedGeoItemRenderer<T extends Item & GeoAnimatable> extends GeoItemRenderer<T> {
    private final ResourceLocation texture;

    public DefaultedGeoItemRenderer(ResourceLocation id) {
        super(new DefaultedItemGeoModel<>(id));
        texture = makeTextureLocation(id);
    }

    public static ResourceLocation makeTextureLocation(ResourceLocation id) {
        return id.withPrefix("textures/item/geo/").withSuffix(".png");
    }

    public ResourceLocation getTextureLocation() {
        return texture;
    }

    @Override
    public ResourceLocation getTextureLocation(T animatable) {
        return getTextureLocation();
    }
}
