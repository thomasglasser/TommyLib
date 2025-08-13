package dev.thomasglasser.tommylib.impl.client;

import dev.thomasglasser.tommylib.api.client.ClientUtils;
import dev.thomasglasser.tommylib.api.client.animation.PlayerAnimationHandler;
import dev.thomasglasser.tommylib.api.client.renderer.BewlrProvider;
import dev.thomasglasser.tommylib.api.world.item.ModeledItem;
import dev.thomasglasser.tommylib.impl.TommyLib;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

public class TommyLibNeoForgeClientEvents {
    // Init
    public static void onClientSetup(FMLClientSetupEvent event) {
        if (TommyLib.Dependencies.PLAYERANIMATOR.isLoaded())
            PlayerAnimationHandler.init();
    }

    // Keys
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        ClientUtils.getKeyMappings().forEach(event::register);
    }

    // Items
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        for (Item item : BuiltInRegistries.ITEM.stream().toList()) {
            if (item instanceof ModeledItem modeledItem) {
                event.registerItem(new IClientItemExtensions() {
                    private BlockEntityWithoutLevelRenderer bewlr;

                    @Override
                    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                        final AtomicReference<BewlrProvider> consumer = new AtomicReference<>();

                        modeledItem.createBewlrProvider(consumer::set);

                        if (this.bewlr == null) this.bewlr = consumer.get().getBewlr();

                        return this.bewlr;
                    }
                }, item);
            }
        }
    }
}
