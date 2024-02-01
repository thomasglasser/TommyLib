package dev.thomasglasser.tommylib.api.data.recipes;

import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.concurrent.CompletableFuture;

public abstract class ExtendedRecipeProvider extends RecipeProvider
{
	public ExtendedRecipeProvider(PackOutput p_248933_, CompletableFuture<HolderLookup.Provider> lookupProvider)
	{
		super(p_248933_, lookupProvider);
	}

	protected void woodSet(RecipeOutput writer, WoodSet set)
	{
		planksFromLogs(writer, set.planks().get(), set.logsItemTag().get(), 4);
		woodFromLogs(writer, set.wood().get(), set.log().get());
		woodFromLogs(writer, set.strippedWood().get(), set.strippedLog().get());
	}
}
