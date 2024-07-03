package dev.thomasglasser.tommylib.api.data.recipes;

import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

/**
 * Extension of {@link RecipeProvider} that provides functionality for mod holders.
 */
public abstract class ExtendedRecipeProvider extends RecipeProvider {
    public ExtendedRecipeProvider(PackOutput p_248933_, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(p_248933_, lookupProvider);
    }

    /**
     * Adds recipes for blocks in a {@link WoodSet}.
     * 
     * @param writer The {@link RecipeOutput} instance to write the recipes to.
     * @param set    The {@link WoodSet} to add recipes for.
     */
    protected void woodSet(RecipeOutput writer, WoodSet set) {
        planksFromLogs(writer, set.planks().get(), set.logsItemTag(), 4);
        woodFromLogs(writer, set.wood().get(), set.log().get());
        woodFromLogs(writer, set.strippedWood().get(), set.strippedLog().get());
    }
}
