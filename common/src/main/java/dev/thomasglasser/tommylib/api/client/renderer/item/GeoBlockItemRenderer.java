package dev.thomasglasser.tommylib.api.client.renderer.item;

import dev.thomasglasser.tommylib.api.client.model.GeoBlockItemModel;
import dev.thomasglasser.tommylib.api.world.item.GeoBlockItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class GeoBlockItemRenderer extends GeoItemRenderer<GeoBlockItem> {
    public GeoBlockItemRenderer(GeoBlockItemModel model) {
        super(model);
    }
}
