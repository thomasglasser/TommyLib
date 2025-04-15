package dev.thomasglasser.tommylib.api.data.models;

import dev.thomasglasser.tommylib.api.world.level.block.LeavesSet;
import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.data.PackOutput;

/**
 * An extension of {@link ModelProvider} that adds helpers for mod features.
 */
public abstract class ExtendedModelProvider extends ModelProvider {
    public ExtendedModelProvider(PackOutput output, String modId) {
        super(output, modId);
    }

    @Override
    protected abstract void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels);

    /**
     * Adds block and item assets for a {@link WoodSet}
     * 
     * @param set         The {@link WoodSet} to generate assets for
     * @param blockModels The {@link BlockModelGenerators} to use for block asset generation
     * @param itemModels  The {@link ItemModelGenerators} to use for item asset generation
     */
    protected void woodSet(WoodSet set, BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        blockModels.family(set.toBlockFamily().getBaseBlock()).generateFor(set.toBlockFamily());
        blockModels.woodProvider(set.log().get()).logWithHorizontal(set.log().get()).wood(set.wood().get());
        blockModels.woodProvider(set.strippedLog().get()).logWithHorizontal(set.strippedLog().get()).wood(set.strippedWood().get());
        blockModels.createHangingSign(set.strippedLog().get(), set.hangingSign().get(), set.wallHangingSign().get());
        itemModels.generateFlatItem(set.boatItem().get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(set.chestBoatItem().get(), ModelTemplates.FLAT_ITEM);
    }

    /**
     * Adds block and item assets for a {@link LeavesSet}
     * 
     * @param set         The {@link LeavesSet} to generate assets for
     * @param tint        The tint of the leaves block
     * @param blockModels The {@link BlockModelGenerators} to use for block asset generation
     * @param itemModels  The {@link ItemModelGenerators} to use for item asset generation
     */
    protected void leavesSet(LeavesSet set, int tint, BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        blockModels.createPlantWithDefaultItem(set.sapling().get(), set.pottedSapling().get(), BlockModelGenerators.PlantType.NOT_TINTED);
        blockModels.createTintedLeaves(set.leaves().get(), TexturedModel.LEAVES, tint);
    }
}
