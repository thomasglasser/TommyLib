package dev.thomasglasser.tommylib.impl.mixin.minecraft.world.item;

import dev.thomasglasser.tommylib.api.world.item.ModeledItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(Item.class)
public class ItemMixin
{
    private final Item INSTANCE = (Item) (Object) this;

    @Inject(method = "initializeClient", at = @At("TAIL"), remap = false)
    void initializeClient(Consumer<IClientItemExtensions> consumer, CallbackInfo ci)
    {
        if (this instanceof ModeledItem modeledItem)
        {
            consumer.accept(new IClientItemExtensions()
            {
                private BlockEntityWithoutLevelRenderer bewlr;

                @Override
                public BlockEntityWithoutLevelRenderer getCustomRenderer()
                {
                    if (this.bewlr == null) this.bewlr = modeledItem.getBEWLR();

                    return this.bewlr;
                }
            });
        }
    }
}
