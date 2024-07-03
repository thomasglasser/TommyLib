package dev.thomasglasser.tommylib.impl.mixin.tommylib.api.world.item;

import dev.thomasglasser.tommylib.api.world.item.BaseModeledThrowableSwordItem;
import dev.thomasglasser.tommylib.api.world.item.ModeledItem;
import dev.thomasglasser.tommylib.api.world.item.ThrowableSwordItem;
import java.util.function.Consumer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Tier;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BaseModeledThrowableSwordItem.class)
public abstract class BaseModeledThrowableSwordItemMixin extends ThrowableSwordItem implements ModeledItem {
    private BaseModeledThrowableSwordItemMixin(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private BlockEntityWithoutLevelRenderer bewlr;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.bewlr == null) this.bewlr = getBEWLR();

                return this.bewlr;
            }
        });
    }
}
