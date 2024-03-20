package dev.thomasglasser.tommylib.impl.mixin.tommylib.api.world.item;

import dev.thomasglasser.tommylib.api.world.item.BaseModeledDiggerItem;
import dev.thomasglasser.tommylib.api.world.item.ModeledItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Consumer;

@Mixin(BaseModeledDiggerItem.class)
public abstract class BaseModeledDiggerItemMixin extends DiggerItem implements ModeledItem
{
	private BaseModeledDiggerItemMixin(float pAttackDamageModifier, float pAttackSpeedModifier, Tier pTier, TagKey<Block> pBlocks, Properties pProperties)
	{
		super(pAttackDamageModifier, pAttackSpeedModifier, pTier, pBlocks, pProperties);
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
