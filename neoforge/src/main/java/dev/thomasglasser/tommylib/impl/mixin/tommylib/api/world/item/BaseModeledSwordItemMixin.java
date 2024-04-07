package dev.thomasglasser.tommylib.impl.mixin.tommylib.api.world.item;

import dev.thomasglasser.tommylib.api.world.item.BaseModeledSwordItem;
import dev.thomasglasser.tommylib.api.world.item.ModeledItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Consumer;

@Mixin(BaseModeledSwordItem.class)
public abstract class BaseModeledSwordItemMixin extends Item implements ModeledItem
{
	private BaseModeledSwordItemMixin(Properties pProperties)
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
