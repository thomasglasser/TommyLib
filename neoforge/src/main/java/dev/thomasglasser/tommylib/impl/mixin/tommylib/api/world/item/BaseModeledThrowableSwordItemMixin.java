package dev.thomasglasser.tommylib.impl.mixin.tommylib.api.world.item;

import dev.thomasglasser.tommylib.api.client.renderer.BewlrProvider;
import dev.thomasglasser.tommylib.api.world.entity.projectile.ThrownSword;
import dev.thomasglasser.tommylib.api.world.item.BaseModeledThrowableSwordItem;
import dev.thomasglasser.tommylib.api.world.item.ModeledItem;
import dev.thomasglasser.tommylib.api.world.item.ThrowableSwordItem;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Tier;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BaseModeledThrowableSwordItem.class)
public abstract class BaseModeledThrowableSwordItemMixin extends ThrowableSwordItem implements ModeledItem {
    private BaseModeledThrowableSwordItemMixin(Supplier<EntityType<? extends ThrownSword>> projectile, Holder<SoundEvent> throwSound, Holder<SoundEvent> hitGroundSound, Tier pTier, Properties pProperties) {
        super(projectile, throwSound, hitGroundSound, pTier, pProperties);
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
