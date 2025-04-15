package dev.thomasglasser.tommylib.api.world.item;

import dev.thomasglasser.tommylib.api.client.model.GeoBlockItemModel;
import dev.thomasglasser.tommylib.impl.client.renderer.item.GeoBlockItemRenderer;
import java.util.function.Consumer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * A {@link BlockItem} that can be rendered and animated using GeckoLib.
 */
public class GeoBlockItem extends BlockItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final GeoBlockItemModel model;

    public GeoBlockItem(Block block, Properties properties, GeoBlockItemModel model) {
        super(block, properties);
        this.model = model;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoBlockItemRenderer renderer;

            @Override
            public GeoItemRenderer<?> getGeoItemRenderer() {
                if (renderer == null)
                    renderer = new GeoBlockItemRenderer(model);
                return renderer;
            }
        });
    }
}
