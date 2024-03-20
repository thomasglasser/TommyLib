package dev.thomasglasser.tommylib.impl.mixin.tommylib.api.world.item;

import dev.thomasglasser.tommylib.api.world.item.BaseModeledItem;
import dev.thomasglasser.tommylib.api.world.item.ModeledItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Consumer;

@Mixin(BaseModeledItem.class)
public abstract class BaseModeledItemMixin extends Item implements ModeledItem
{
	private BaseModeledItemMixin(Properties pProperties)
	{
		super(pProperties);
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer)
	{
		consumer.accept(new IClientItemExtensions()
		{
			private BlockEntityWithoutLevelRenderer bewlr;

			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer()
			{
				if (this.bewlr == null) this.bewlr = getBEWLR();

				return this.bewlr;
			}
		});
	}
}
