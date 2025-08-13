package dev.thomasglasser.tommylib.api.data.recipes;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.WithConditions;
import org.jetbrains.annotations.Nullable;

/**
 * Extension of {@link RecipeProvider} that provides helpers
 * and dumps a list of all generated recipes.
 */
public abstract class ExtendedRecipeProvider extends RecipeProvider {
    protected final PackOutput output;

    protected ExtendedRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
        this.output = output;
    }

    @Override
    protected CompletableFuture<?> run(CachedOutput output, HolderLookup.Provider registries) {
        final Set<ResourceLocation> set = Sets.newHashSet();
        final Set<ResourceLocation> advancementSet = Sets.newHashSet();
        final List<CompletableFuture<?>> list = new ArrayList();
        this.buildRecipes(new RecipeOutput() {
            public void accept(ResourceLocation p_312039_, Recipe<?> p_312254_, @Nullable AdvancementHolder p_311794_, ICondition... conditions) {
                if (!set.add(p_312039_)) {
                    throw new IllegalStateException("Duplicate recipe " + p_312039_);
                } else {
                    list.add(DataProvider.saveStable(output, registries, Recipe.CONDITIONAL_CODEC, Optional.of(new WithConditions(p_312254_, conditions)), recipePathProvider.json(p_312039_)));
                    if (p_311794_ != null) {
                        advancementSet.add(p_311794_.id());
                        list.add(DataProvider.saveStable(output, registries, Advancement.CONDITIONAL_CODEC, Optional.of(new WithConditions(p_311794_.value(), conditions)), advancementPathProvider.json(p_311794_.id())));
                    }

                }
            }

            public Advancement.Builder advancement() {
                return Advancement.Builder.recipeAdvancement().parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
            }
        }, registries);
        JsonArray jsonarray = new JsonArray();
        set.stream().sorted().forEach(rl -> jsonarray.add(rl.toString()));
        list.add(DataProvider.saveStable(output, jsonarray, this.output.getOutputFolder(PackOutput.Target.REPORTS).resolve("recipes.json")));
        JsonArray advancementArray = new JsonArray();
        advancementSet.stream().sorted().forEach(rl -> advancementArray.add(rl.toString()));
        list.add(DataProvider.saveStable(output, advancementArray, this.output.getOutputFolder(PackOutput.Target.REPORTS).resolve("recipe_advancements.json")));
        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    /**
     * Adds recipes for blocks in a {@link WoodSet}.
     * 
     * @param writer The {@link RecipeOutput} instance to write the recipes to
     * @param set    The {@link WoodSet} to add recipes for
     */
    protected void woodSet(RecipeOutput writer, WoodSet set) {
        planksFromLogs(writer, set.planks(), set.logsItemTag(), 4);
        woodFromLogs(writer, set.wood(), set.log());
        woodFromLogs(writer, set.strippedWood(), set.strippedLog());
        woodenBoat(writer, set.boatItem(), set.planks());
        chestBoat(writer, set.chestBoatItem(), set.boatItem());
        hangingSign(writer, set.hangingSign(), set.strippedLog());
        generateRecipes(writer, set.toBlockFamily(), FeatureFlagSet.of());
    }
}
