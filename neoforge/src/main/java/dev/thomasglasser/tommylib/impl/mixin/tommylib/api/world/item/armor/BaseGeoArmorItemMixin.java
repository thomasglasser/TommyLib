package dev.thomasglasser.tommylib.impl.mixin.tommylib.api.world.item.armor;

import dev.thomasglasser.tommylib.api.world.item.ModeledItem;
import dev.thomasglasser.tommylib.api.world.item.armor.BaseGeoArmorItem;
import dev.thomasglasser.tommylib.api.world.item.armor.GeoArmorItem;
import java.util.function.Consumer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BaseGeoArmorItem.class)
public abstract class BaseGeoArmorItemMixin extends ArmorItem implements GeoArmorItem {
    private BaseGeoArmorItemMixin(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        if (this instanceof ModeledItem modeledItem) {
            consumer.accept(new IClientItemExtensions() {
                private BlockEntityWithoutLevelRenderer bewlr;

                @Override
                public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    if (this.bewlr == null) this.bewlr = modeledItem.getBEWLR();

                    return this.bewlr;
                }
            });
        }
    }
}
