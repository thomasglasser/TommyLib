package dev.thomasglasser.tommylib.impl.client;

import dev.thomasglasser.tommylib.impl.TommyLib;
import dev.thomasglasser.tommylib.api.client.ClientUtils;
import dev.thomasglasser.tommylib.api.client.animation.PlayerAnimationHandler;
import dev.thomasglasser.tommylib.api.client.renderer.BewlrProvider;
import dev.thomasglasser.tommylib.api.world.item.ModeledItem;
import java.util.concurrent.atomic.AtomicReference;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.registries.BuiltInRegistries;

public class TommyLibFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        if (TommyLib.Dependencies.PLAYERANIMATOR.isLoaded())
            PlayerAnimationHandler.init();

        ClientUtils.getKeyMappings().forEach(KeyBindingHelper::registerKeyBinding);

        BuiltInRegistries.ITEM.stream().forEach(item -> {
            if (item instanceof ModeledItem modeledItem) {
                final AtomicReference<BewlrProvider> consumer = new AtomicReference<>();

                modeledItem.createBewlrProvider(consumer::set);

                BlockEntityWithoutLevelRenderer bewlr = consumer.get().getBewlr();

                BuiltinItemRendererRegistry.INSTANCE.register(item, bewlr::renderByItem);
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register((minecraft -> TommyLibClientEvents.onClientTick()));
    }
}
