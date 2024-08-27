package dev.thomasglasser.tommylib.impl.mixin.tommylib.api.world.item;

import dev.thomasglasser.tommylib.api.client.renderer.BewlrProvider;
import dev.thomasglasser.tommylib.api.world.item.BaseModeledSwordItem;
import dev.thomasglasser.tommylib.api.world.item.ModeledItem;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BaseModeledSwordItem.class)
public abstract class BaseModeledSwordItemMixin extends Item implements ModeledItem {
    private BaseModeledSwordItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private BlockEntityWithoutLevelRenderer bewlr;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                final AtomicReference<BewlrProvider> consumer = new AtomicReference<>(BewlrProvider.DEFAULT);

                createBewlrProvider(consumer::set);

                if (this.bewlr == null) this.bewlr = consumer.get().getBewlr();

                return this.bewlr;
            }
        });
    }
}
