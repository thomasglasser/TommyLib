package dev.thomasglasser.tommylib.api.data.models;

import dev.thomasglasser.tommylib.api.registration.DeferredBlock;
import dev.thomasglasser.tommylib.api.registration.DeferredItem;
import dev.thomasglasser.tommylib.api.world.level.block.LeavesSet;
import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.client.model.generators.loaders.SeparateTransformsModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Extension of {@link ItemModelProvider} that provides functionality for mod holders.
 *
 * @deprecated Model generation is completely rewritten in 1.21.5+
 */
@Deprecated(forRemoval = true, since = "31.0.0")
public abstract class ExtendedItemModelProvider extends ItemModelProvider {
    protected ExtendedItemModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    /**
     * Generates a basic item model with the default texture location.
     * 
     * @param item The item to generate the model for
     * @return The {@link ItemModelBuilder} for the item
     */
    protected ItemModelBuilder basicItem(DeferredItem<?> item) {
        return basicItem(item.getId());
    }

    /**
     * Generates a basic item model appended with "_inventory" for the item texture.
     * 
     * @param item The item to generate the model for
     * @return The {@link ItemModelBuilder} for the item
     */
    protected ItemModelBuilder basicInventoryItem(ResourceLocation item) {
        return basicItem(ResourceLocation.fromNamespaceAndPath(item.getNamespace(), item.getPath() + "_inventory"), item.getPath());
    }

    protected ItemModelBuilder basicInventoryItem(DeferredItem<?> item) {
        return basicInventoryItem(item.getId());
    }

    /**
     * Generates a basic item model with the provided texture location.
     * 
     * @param item       The item to generate the model for
     * @param textureLoc The location of the texture for the item
     * @return The {@link ItemModelBuilder} for the item
     */
    protected ItemModelBuilder basicItem(ResourceLocation item, String textureLoc) {
        return getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "item/" + textureLoc));
    }

    protected ItemModelBuilder basicItem(DeferredItem<?> item, String textureLoc) {
        return basicItem(item.getId(), textureLoc);
    }

    /**
     * Generates a basic handheld item model with the default texture location.
     *
     * @param item The item to generate the model for
     * @return The {@link ItemModelBuilder} for the item
     */
    protected ItemModelBuilder handheldItem(DeferredItem<?> item) {
        return handheldItem(item.getId());
    }

    /**
     * Generates a basic handheld item model with the provided texture location.
     * 
     * @param item       The item to generate the model for
     * @param textureLoc The location of the texture for the item
     * @return The {@link ItemModelBuilder} for the item
     */
    protected ItemModelBuilder basicItemHandheld(ResourceLocation item, String textureLoc) {
        return singleTexture(item.getPath(), mcLoc("item/handheld"), "layer0", ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "item/" + textureLoc));
    }

    protected ItemModelBuilder basicItemHandheld(DeferredItem<?> item, String textureLoc) {
        return basicItemHandheld(item.getId(), textureLoc);
    }

    /**
     * Generates a spawn egg model
     *
     * @param egg The spawn egg
     * @return The {@link ItemModelBuilder} for the item
     */
    protected ItemModelBuilder spawnEggItem(DeferredItem<SpawnEggItem> egg) {
        return spawnEggItem(egg.getId());
    }

    /**
     * Generates item models for all blocks in a {@link WoodSet}.
     * 
     * @param set The set of blocks to generate models for
     */
    protected void woodSet(WoodSet set) {
        basicBlockItem(set.log());
        basicBlockItem(set.strippedLog());
        basicBlockItem(set.wood());
        basicBlockItem(set.strippedWood());
        basicBlockItem(set.planks());
        basicBlockItem(set.slab());
        basicBlockItem(set.stairs());
        basicBlockItem(set.pressurePlate());
        buttonInventory(set.button().getId().getPath(), blockLoc(set.planks()));
        fenceInventory(set.fence().getId().getPath(), blockLoc(set.planks()));
        basicBlockItem(set.fenceGate());
        basicItem(set.door().asItem());
        withExistingParent(set.trapdoor().getId().getPath(), blockLoc(set.trapdoor()).withSuffix("_bottom"));
        basicItem(set.sign().asItem());
        basicItem(set.hangingSign().asItem());
        basicItem(set.boatItem());
        basicItem(set.chestBoatItem());
    }

    /**
     * Generates item models for all blocks in a {@link LeavesSet}.
     * 
     * @param set The set of blocks to generate models for
     */
    protected void leavesSet(LeavesSet set) {
        basicBlockItem(set.leaves());
        withExistingParent(set.sapling().getId().getPath(), "item/generated").texture("layer0", blockLoc(set.sapling()));
    }

    /**
     * Generates an item model for a block.
     * 
     * @param block The block to generate the model for
     * @return The {@link ItemModelBuilder} for the item
     */
    protected ItemModelBuilder basicBlockItem(DeferredBlock<?> block) {
        return simpleBlockItem(block.getId());
    }

    /**
     * Generates an item model for an item that renders with a {@link BlockEntityWithoutLevelRenderer}.
     *
     * @param location The item to generate the model
     * @return The {@link ItemModelBuilder} for the item
     */
    protected ItemModelBuilder withEntityModel(ResourceLocation location) {
        return getBuilder(location.getPath())
                .parent(new ModelFile.UncheckedModelFile("builtin/entity"));
    }

    protected ItemModelBuilder withEntityModel(DeferredItem<?> item) {
        return withEntityModel(item.getId());
    }

    /**
     * Generates a {@link SeparateTransformsModelBuilder} for making a perspective-aware model
     * 
     * @param location The location of the model
     * @return The {@link SeparateTransformsModelBuilder} for the model
     */
    protected SeparateTransformsModelBuilder<ItemModelBuilder> withSeparateTransforms(ResourceLocation location) {
        return getBuilder(location.getPath()).guiLight(BlockModel.GuiLight.FRONT).customLoader(SeparateTransformsModelBuilder::begin);
    }

    protected SeparateTransformsModelBuilder<ItemModelBuilder> withSeparateTransforms(DeferredItem<?> item) {
        return withSeparateTransforms(item.getId());
    }

    /**
     * Generates an item model for an item that renders with a flat model for the inventory.
     * 
     * @param location The location of the model
     * @param base     The model for the item in the world
     * @return The {@link ItemModelBuilder} for the model
     */
    protected ItemModelBuilder withSeparateInventoryModel(ResourceLocation location, ItemModelBuilder base) {
        return withSeparateInventoryModel(location, base, basicInventoryItem(location));
    }

    protected ItemModelBuilder withSeparateInventoryModel(DeferredItem<?> item, ItemModelBuilder base) {
        return withSeparateInventoryModel(item.getId(), base);
    }

    /**
     * Generates an item model for an item that renders different models in the world and in the inventory.
     * 
     * @param location  The location of the model
     * @param base      The model for the item in hand
     * @param inventory The model for the item in the inventory
     * @return The {@link ItemModelBuilder} for the model
     */
    protected ItemModelBuilder withSeparateInventoryModel(ResourceLocation location, ItemModelBuilder base, ItemModelBuilder inventory) {
        generatedModels.remove(base.getLocation());
        generatedModels.remove(inventory.getLocation());
        return withSeparateTransforms(location)
                .base(base)
                .perspective(ItemDisplayContext.GUI, inventory)
                .perspective(ItemDisplayContext.FIXED, inventory)
                .perspective(ItemDisplayContext.GROUND, inventory)
                .end();
    }

    protected ItemModelBuilder withSeparateInventoryModel(DeferredItem<?> item, ItemModelBuilder base, ItemModelBuilder inventory) {
        return withSeparateInventoryModel(item.getId(), base, inventory);
    }

    /**
     * Generates a {@link ResourceLocation} for a mod item model.
     * 
     * @param item The item to generate the {@link ResourceLocation} for
     * @return The {@link ResourceLocation} for the item model
     */
    protected ResourceLocation itemLoc(DeferredItem<?> item) {
        return item.getId().withPrefix(ModelProvider.ITEM_FOLDER + "/");
    }

    /**
     * Generates a {@link ResourceLocation} for a mod block model.
     * 
     * @param block The block to generate the {@link ResourceLocation} for
     * @return The {@link ResourceLocation} for the block model
     */
    protected ResourceLocation blockLoc(DeferredBlock<?> block) {
        return block.getId().withPrefix(ModelProvider.BLOCK_FOLDER + "/");
    }

    /**
     * Generates a {@link ResourceLocation} for a Minecraft block model.
     * 
     * @param path The path of the block model
     * @return The {@link ResourceLocation} for the block model
     */
    protected ResourceLocation mcBlockLoc(String path) {
        return ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/" + path);
    }

    /**
     * Generates a {@link ResourceLocation} for a Minecraft item model.
     * 
     * @param path The path of the item model
     * @return The {@link ResourceLocation} for the item model
     */
    protected ResourceLocation mcItemLoc(String path) {
        return ResourceLocation.withDefaultNamespace(ModelProvider.ITEM_FOLDER + "/" + path);
    }

    /**
     * Generates a {@link ResourceLocation} for a mod block model.
     * 
     * @param path The path of the block model
     * @return The {@link ResourceLocation} for the block model
     */
    protected ResourceLocation modBlockLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(modid, ModelProvider.BLOCK_FOLDER + "/" + path);
    }

    /**
     * Generates a {@link ResourceLocation} for a mod item model.
     * 
     * @param path The path of the item model
     * @return The {@link ResourceLocation} for the item model
     */
    protected ResourceLocation modItemLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(modid, ModelProvider.ITEM_FOLDER + "/" + path);
    }
}
