package dev.thomasglasser.tommylib.api.data.recipes;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.criterion.ImpossibleTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.WithConditions;
import org.jetbrains.annotations.Nullable;

/**
 * A version of {@link RecipeProvider.Runner} that takes in a {@link ExtendedRecipeProvider} and logs recipes and advancements
 */
public class ExtendedRecipeProviderRunner implements DataProvider {
    protected final PackOutput packOutput;
    protected final CompletableFuture<HolderLookup.Provider> registries;
    protected final String modId;
    protected final BiFunction<HolderLookup.Provider, RecipeOutput, ? extends ExtendedRecipeProvider> factory;

    protected ExtendedRecipeProviderRunner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries, String modId, BiFunction<HolderLookup.Provider, RecipeOutput, ? extends ExtendedRecipeProvider> factory) {
        this.packOutput = packOutput;
        this.registries = registries;
        this.modId = modId;
        this.factory = factory;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return this.registries
                .thenCompose(
                        provider -> {
                            final PackOutput.PathProvider recipePathProvider = this.packOutput.createRegistryElementsPathProvider(Registries.RECIPE);
                            final PackOutput.PathProvider advancementPathProvider = this.packOutput.createRegistryElementsPathProvider(Registries.ADVANCEMENT);
                            final Set<ResourceKey<Recipe<?>>> recipeKeys = Sets.newHashSet();
                            final Set<Identifier> advancementIds = Sets.newHashSet();
                            final List<CompletableFuture<?>> outputs = new ArrayList<>();
                            RecipeOutput recipeOutput = new RecipeOutput() {
                                @Override
                                public void accept(ResourceKey<Recipe<?>> key, Recipe<?> recipe, @Nullable AdvancementHolder holder, ICondition... conditions) {
                                    if (!recipeKeys.add(key)) {
                                        throw new IllegalStateException("Duplicate recipe " + key.identifier());
                                    } else {
                                        this.saveRecipe(key, recipe, conditions);
                                        if (holder != null) {
                                            advancementIds.add(holder.id());
                                            this.saveAdvancement(holder, conditions);
                                        }
                                    }
                                }

                                @Override
                                public Advancement.Builder advancement() {
                                    return Advancement.Builder.recipeAdvancement().parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
                                }

                                @Override
                                public void includeRootAdvancement() {
                                    AdvancementHolder advancementholder = Advancement.Builder.recipeAdvancement()
                                            .addCriterion("impossible", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
                                            .build(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
                                    this.saveAdvancement(advancementholder);
                                }

                                private void saveRecipe(ResourceKey<Recipe<?>> p_380099_, Recipe<?> p_364792_) {
                                    saveRecipe(p_380099_, p_364792_, new ICondition[0]);
                                }

                                private void saveRecipe(ResourceKey<Recipe<?>> p_380099_, Recipe<?> p_364792_, ICondition... conditions) {
                                    outputs.add(
                                            DataProvider.saveStable(output, provider, Recipe.CONDITIONAL_CODEC, Optional.of(new WithConditions<>(p_364792_, conditions)), recipePathProvider.json(p_380099_.identifier())));
                                }

                                private void saveAdvancement(AdvancementHolder p_363148_) {
                                    saveAdvancement(p_363148_, new ICondition[0]);
                                }

                                private void saveAdvancement(AdvancementHolder p_363148_, ICondition... conditions) {
                                    outputs.add(
                                            DataProvider.saveStable(
                                                    output, provider, Advancement.CONDITIONAL_CODEC, Optional.of(new WithConditions<>(p_363148_.value(), conditions)), advancementPathProvider.json(p_363148_.id())));
                                }
                            };
                            this.factory.apply(provider, recipeOutput).buildRecipes();
                            JsonArray jsonarray = new JsonArray();
                            recipeKeys.stream().sorted().forEach(id -> jsonarray.add(id.identifier().toString()));
                            outputs.add(DataProvider.saveStable(output, jsonarray, this.packOutput.getOutputFolder(PackOutput.Target.REPORTS).resolve("recipes.json")));
                            JsonArray advancementArray = new JsonArray();
                            advancementIds.stream().sorted().forEach(id -> advancementArray.add(id.toString()));
                            outputs.add(DataProvider.saveStable(output, advancementArray, this.packOutput.getOutputFolder(PackOutput.Target.REPORTS).resolve("recipe_advancements.json")));
                            return CompletableFuture.allOf(outputs.toArray(CompletableFuture[]::new));
                        });
    }

    @Override
    public String getName() {
        return modId + " Recipe Provider Runner";
    }
}
