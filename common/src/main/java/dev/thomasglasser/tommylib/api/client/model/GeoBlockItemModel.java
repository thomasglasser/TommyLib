package dev.thomasglasser.tommylib.api.client.model;

import dev.thomasglasser.tommylib.api.world.item.GeoBlockItem;
import net.minecraft.resources.Identifier;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

/**
 * A GeoModel for a block item.
 */
public class GeoBlockItemModel extends DefaultedItemGeoModel<GeoBlockItem> {
    public GeoBlockItemModel(Identifier assetSubpath) {
        super(assetSubpath);
    }

    /**
     * Redirects the requested path to the block folder.
     */
    @Override
    protected String subtype() {
        return "block";
    }
}
