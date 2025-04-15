package dev.thomasglasser.tommylib.api.data.recipes;

import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.flag.FeatureFlagSet;

/**
 * Extension of {@link RecipeProvider} that provides functionality for mod features.
 */
public abstract class ExtendedRecipeProvider extends RecipeProvider {
    public ExtendedRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    public abstract void buildRecipes();

    /**
     * Adds recipes for blocks in a {@link WoodSet}.
     * 
     * @param set The {@link WoodSet} to add recipes for.
     */
    protected void woodSet(WoodSet set) {
        planksFromLogs(set.planks(), set.logsItemTag(), 4);
        woodFromLogs(set.wood(), set.log());
        woodFromLogs(set.strippedWood(), set.strippedLog());
        woodenBoat(set.boatItem(), set.planks());
        chestBoat(set.chestBoatItem(), set.boatItem());
        hangingSign(set.hangingSign(), set.strippedLog());
        generateRecipes(set.toBlockFamily(), FeatureFlagSet.of());
    }
}
