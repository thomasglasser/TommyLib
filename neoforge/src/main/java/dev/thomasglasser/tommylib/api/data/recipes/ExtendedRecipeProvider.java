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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.level.ItemLike;
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

    protected static String getHasName(TagKey<?> tag) {
        String[] split = tag.location().getPath().split("/");
        StringBuilder builder = new StringBuilder();
        builder.append("has_");
        for (int i = split.length - 1; i >= 0; i--) {
            builder.append(split[i]);
            if (i > 0)
                builder.append("_");
        }
        return builder.toString();
    }

    /**
     * Adds recipes for blocks in a {@link WoodSet}.
     * 
     * @param writer The {@link RecipeOutput} instance to write the recipes to
     * @param set    The {@link WoodSet} to add recipes for
     */
    protected static void woodSet(RecipeOutput writer, WoodSet set) {
        planksFromLogs(writer, set.planks(), set.logsItemTag(), 4);
        woodFromLogs(writer, set.wood(), set.log());
        woodFromLogs(writer, set.strippedWood(), set.strippedLog());
        woodenBoat(writer, set.boatItem(), set.planks());
        chestBoat(writer, set.chestBoatItem(), set.boatItem());
        hangingSign(writer, set.hangingSign(), set.strippedLog());
        generateRecipes(writer, set.toBlockFamily(), FeatureFlagSet.of());
    }

    protected static void trimWithCopy(RecipeOutput recipeOutput, ItemLike template, ItemLike copyMaterial) {
        ResourceLocation templateLoc = BuiltInRegistries.ITEM.getKey(template.asItem());
        trimSmithing(recipeOutput, template.asItem(), templateLoc.withSuffix("_smithing_trim"));
        copySmithingTemplate(recipeOutput, template, copyMaterial);
    }

    protected static void simpleSmeltingRecipe(RecipeOutput recipeOutput, ItemLike ingredient, ItemLike result, float experience) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ingredient), RecipeCategory.FOOD, result, experience, 200)
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(recipeOutput);
    }

    protected static <T extends AbstractCookingRecipe> void simpleCookingRecipe(
            RecipeOutput recipeOutput,
            String cookingMethod,
            RecipeSerializer<T> cookingSerializer,
            AbstractCookingRecipe.Factory<T> recipeFactory,
            int cookingTime,
            ItemLike material,
            ItemLike result,
            float experience) {
        SimpleCookingRecipeBuilder.generic(Ingredient.of(material), RecipeCategory.FOOD, result, experience, cookingTime, cookingSerializer, recipeFactory)
                .unlockedBy(getHasName(material), has(material))
                .save(recipeOutput, BuiltInRegistries.ITEM.getKey(result.asItem()).withSuffix("_from_" + cookingMethod));
    }

    protected static void simpleSmokingRecipe(RecipeOutput recipeOutput, ItemLike ingredient, ItemLike result, float experience) {
        simpleCookingRecipe(recipeOutput, "smoking", RecipeSerializer.SMOKING_RECIPE, SmokingRecipe::new, 100, ingredient, result, experience);
    }

    protected static void simpleCampfireCookingRecipe(RecipeOutput recipeOutput, ItemLike ingredient, ItemLike result, float experience) {
        simpleCookingRecipe(recipeOutput, "campfire_cooking", RecipeSerializer.CAMPFIRE_COOKING_RECIPE, CampfireCookingRecipe::new, 600, ingredient, result, experience);
    }

    protected static void simpleCookingRecipes(RecipeOutput recipeOutput, ItemLike input, ItemLike output, float experience) {
        simpleSmeltingRecipe(recipeOutput, input, output, experience);
        simpleSmokingRecipe(recipeOutput, input, output, experience);
        simpleCampfireCookingRecipe(recipeOutput, input, output, experience);
    }
}
