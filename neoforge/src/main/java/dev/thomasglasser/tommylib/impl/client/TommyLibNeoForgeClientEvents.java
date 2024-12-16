package dev.thomasglasser.tommylib.impl.client;

import dev.thomasglasser.tommylib.api.client.animation.AnimationUtils;
import dev.thomasglasser.tommylib.api.client.renderer.BewlrProvider;
import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import dev.thomasglasser.tommylib.api.world.item.ModeledItem;
import dev.thomasglasser.tommylib.impl.platform.NeoForgeClientHelper;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

public class TommyLibNeoForgeClientEvents {
    public static void onClientSetup(FMLClientSetupEvent event) {
        if (TommyLibServices.PLATFORM.isModLoaded("playeranimator"))
            AnimationUtils.registerPlayerForAnimation();
    }

    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        ((NeoForgeClientHelper) TommyLibServices.CLIENT).getKeyMappings().forEach(mapping -> event.register(mapping.get()));
    }

    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide) {
            TommyLibClientEvents.onEntityJoinLevel(event.getEntity());
        }
    }

    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        for (Item item : BuiltInRegistries.ITEM.stream().toList()) {
            if (item instanceof ModeledItem modeledItem) {
                event.registerItem(new IClientItemExtensions() {
                    private BlockEntityWithoutLevelRenderer bewlr;

                    @Override
                    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                        final AtomicReference<BewlrProvider> consumer = new AtomicReference<>(BewlrProvider.DEFAULT);

                        modeledItem.createBewlrProvider(consumer::set);

                        if (this.bewlr == null) this.bewlr = consumer.get().getBewlr();

                        return this.bewlr;
                    }
                }, item);
            }
        }
    }
}
