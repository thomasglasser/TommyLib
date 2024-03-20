package dev.thomasglasser.tommylib.impl.mixin.tommylib.api.world.item.armor;

import dev.thomasglasser.tommylib.api.world.item.ModeledItem;
import dev.thomasglasser.tommylib.api.world.item.armor.BaseGeoArmorItem;
import dev.thomasglasser.tommylib.api.world.item.armor.GeoArmorItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.function.Consumer;

@Mixin(BaseGeoArmorItem.class)
public abstract class BaseGeoArmorItemMixin extends ArmorItem implements GeoArmorItem
{
	private BaseGeoArmorItemMixin(ArmorMaterial pMaterial, Type pType, Properties pProperties)
	{
		super(pMaterial, pType, pProperties);
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer)
	{
		if (this instanceof ModeledItem modeledItem)
		{
			consumer.accept(new IClientItemExtensions() {
				private BlockEntityWithoutLevelRenderer bewlr;

				@Override
				public BlockEntityWithoutLevelRenderer getCustomRenderer() {
					if (this.bewlr == null)
						this.bewlr = modeledItem.getBEWLR();

					return this.bewlr;
				}

				GeoArmorRenderer<?> renderer;

				@Override
				public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
					if (renderer == null) renderer = newRenderer();

					this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

					return renderer;
				}
			});
		}
		else
		{
			consumer.accept(new IClientItemExtensions() {
				GeoArmorRenderer<?> renderer;

				@Override
				public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
					if (renderer == null) renderer = newRenderer();

					this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

					return renderer;
				}
			});
		}
	}
}
