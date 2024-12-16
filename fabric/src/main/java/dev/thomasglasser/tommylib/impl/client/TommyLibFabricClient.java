package dev.thomasglasser.tommylib.impl.client;

import dev.thomasglasser.tommylib.api.client.animation.AnimationUtils;
import dev.thomasglasser.tommylib.api.client.renderer.BewlrProvider;
import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import dev.thomasglasser.tommylib.api.world.item.ModeledItem;
import java.util.concurrent.atomic.AtomicReference;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.registries.BuiltInRegistries;

public class TommyLibFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        if (TommyLibServices.PLATFORM.isModLoaded("playeranimator"))
            AnimationUtils.registerPlayerForAnimation();

        BuiltInRegistries.ITEM.stream().forEach(item -> {
            if (item instanceof ModeledItem modeledItem) {
                final AtomicReference<BewlrProvider> consumer = new AtomicReference<>(BewlrProvider.DEFAULT);

                modeledItem.createBewlrProvider(consumer::set);

                BlockEntityWithoutLevelRenderer bewlr = consumer.get().getBewlr();

                BuiltinItemRendererRegistry.INSTANCE.register(item, bewlr::renderByItem);
            }
        });

        ClientEntityEvents.ENTITY_LOAD.register(((trackedEntity, player) -> TommyLibClientEvents.onEntityJoinLevel(trackedEntity)));
    }
}
