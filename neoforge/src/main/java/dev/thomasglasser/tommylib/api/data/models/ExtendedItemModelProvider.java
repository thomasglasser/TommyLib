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
 */
public abstract class ExtendedItemModelProvider extends ItemModelProvider {
    public ExtendedItemModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    /**
     * Generates a basic item model with the default texture location.
     * 
     * @param item The item to generate the model for.
     * @return The {@link ItemModelBuilder} for the item.
     */
    protected ItemModelBuilder basicItem(DeferredItem<?> item) {
        return basicItem(item.getId(), item.getId().getPath());
    }

    /**
     * Generates a basic item model appended with "_inventory" for the item texture.
     * 
     * @param item The item to generate the model for.
     * @return The {@link ItemModelBuilder} for the item.
     */
    public ItemModelBuilder basicInventoryItem(ResourceLocation item) {
        return basicItem(ResourceLocation.fromNamespaceAndPath(item.getNamespace(), item.getPath() + "_inventory"), item.getPath());
    }

    public ItemModelBuilder basicInventoryItem(DeferredItem<?> item) {
        return basicInventoryItem(item.getId());
    }

    /**
     * Generates a basic item model with the provided texture location.
     * 
     * @param item       The item to generate the model for.
     * @param textureLoc The location of the texture for the item.
     * @return The {@link ItemModelBuilder} for the item.
     */
    public ItemModelBuilder basicItem(ResourceLocation item, String textureLoc) {
        return getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "item/" + textureLoc));
    }

    public ItemModelBuilder basicItem(DeferredItem<?> item, String textureLoc) {
        return basicItem(item.getId(), textureLoc);
    }

    /**
     * Generates a basic handheld item model with the default texture location.
     * 
     * @param item The item to generate the model for.
     * @return The {@link ItemModelBuilder} for the item.
     */
    protected ItemModelBuilder basicItemHandheld(ResourceLocation item) {
        return singleTexture(item.getPath(), mcLoc("item/handheld"), "layer0", ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "item/" + item.getPath()));
    }

    protected ItemModelBuilder basicItemHandheld(DeferredItem<?> item) {
        return basicItemHandheld(item.getId());
    }

    /**
     * Generates a basic handheld item model with the provided texture location.
     * 
     * @param item       The item to generate the model for.
     * @param textureLoc The location of the texture for the item.
     * @return The {@link ItemModelBuilder} for the item.
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
     * @param path The path of the spawn egg
     * @return The {@link ItemModelBuilder} for the item.
     */
    protected ItemModelBuilder spawnEgg(String path) {
        return withExistingParent(path, mcLoc("item/template_spawn_egg"));
    }

    protected ItemModelBuilder spawnEgg(DeferredItem<SpawnEggItem> egg) {
        return withExistingParent(egg.getId().getPath(), mcLoc("item/template_spawn_egg"));
    }

    /**
     * Generates item models for all blocks in a {@link WoodSet}.
     * 
     * @param set The set of blocks to generate models for.
     */
    protected void woodSet(WoodSet set) {
        withExistingParent(set.log().getId().getPath(), blockLoc(set.log()));
        withExistingParent(set.strippedLog().getId().getPath(), blockLoc(set.strippedLog()));
        withExistingParent(set.wood().getId().getPath(), blockLoc(set.wood()));
        withExistingParent(set.strippedWood().getId().getPath(), blockLoc(set.strippedWood()));
        withExistingParent(set.planks().getId().getPath(), blockLoc(set.planks()));
        withExistingParent(set.slab().getId().getPath(), blockLoc(set.slab()));
        withExistingParent(set.stairs().getId().getPath(), blockLoc(set.stairs()));
        withExistingParent(set.pressurePlate().getId().getPath(), blockLoc(set.pressurePlate()));
        buttonInventory(set.button().getId().getPath(), blockLoc(set.planks()));
        fenceInventory(set.fence().getId().getPath(), blockLoc(set.planks()));
        withExistingParent(set.fenceGate().getId().getPath(), blockLoc(set.fenceGate()));
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
     * @param set The set of blocks to generate models for.
     */
    protected void leavesSet(LeavesSet set) {
        withExistingParent(set.leaves().getId().getPath(), blockLoc(set.leaves()));
        singleTexture(set.sapling().getId().getPath(), mcItemLoc("generated"), "layer0", blockLoc(set.sapling()));
    }

    /**
     * Generates an item model for a block.
     * 
     * @param block The block to generate the model for.
     * @return The {@link ItemModelBuilder} for the item.
     */
    protected ItemModelBuilder basicBlockItem(DeferredBlock<?> block) {
        ResourceLocation id = block.getId();
        return withExistingParent(id.getPath(), ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "block/" + id.getPath()));
    }

    /**
     * Generates an item model for an item that renders with a {@link BlockEntityWithoutLevelRenderer}.
     *
     * @param item The item to generate the model for.
     * @return The {@link ItemModelBuilder} for the item.
     */
    protected ItemModelBuilder withEntityModel(DeferredItem<?> item) {
        return getBuilder(item.getId().getPath())
                .parent(new ModelFile.UncheckedModelFile("builtin/entity"));
    }

    protected ItemModelBuilder withEntityModel(ResourceLocation location) {
        return getBuilder(location.getPath())
                .parent(new ModelFile.UncheckedModelFile("builtin/entity"));
    }

    /**
     * Generates a {@link SeparateTransformsModelBuilder} for making a perspective aware model
     * 
     * @param location The location of the model
     * @return The {@link SeparateTransformsModelBuilder} for the model
     */
    protected SeparateTransformsModelBuilder<ItemModelBuilder> withSeparateTransforms(ResourceLocation location) {
        return getBuilder(location.getPath()).guiLight(BlockModel.GuiLight.FRONT).customLoader(SeparateTransformsModelBuilder::begin);
    }

    protected SeparateTransformsModelBuilder<ItemModelBuilder> withSeparateTransforms(DeferredItem<?> item) {
        return getBuilder(item.getId().getPath()).customLoader(SeparateTransformsModelBuilder::begin);
    }

    /**
     * Generates an item model for an item that renders in the world with a {@link BlockEntityWithoutLevelRenderer} and a flat model for the inventory.
     * 
     * @param location The location of the model
     * @return The {@link ItemModelBuilder} for the model
     */
    protected ItemModelBuilder withEntityModelInHand(ResourceLocation location) {
        return withEntityModelInHand(location, basicInventoryItem(location));
    }

    protected ItemModelBuilder withEntityModelInHand(DeferredItem<?> item) {
        return withEntityModelInHand(item.getId());
    }

    /**
     * Generates an item model for an item that renders in the world with a {@link BlockEntityWithoutLevelRenderer} and a separate model for the inventory.
     * 
     * @param location    The location of the model
     * @param inHandModel The model for the item in hand
     * @return The {@link ItemModelBuilder} for the model
     */
    protected ItemModelBuilder withEntityModelInHand(ResourceLocation location, ItemModelBuilder inHandModel) {
        return withEntityModelInHand(location, inHandModel, basicInventoryItem(location));
    }

    protected ItemModelBuilder withEntityModelInHand(DeferredItem<?> item, ItemModelBuilder inHandModel) {
        return withEntityModelInHand(item.getId(), inHandModel);
    }

    /**
     * Generates an item model for an item that renders different models in the world and in the inventory.
     * 
     * @param item           The item to generate the model for
     * @param inHandModel    The model for the item in hand
     * @param inventoryModel The model for the item in the inventory
     * @return The {@link ItemModelBuilder} for the model
     */
    protected ItemModelBuilder withEntityModelInHand(DeferredItem<?> item, ItemModelBuilder inHandModel, ItemModelBuilder inventoryModel) {
        return withEntityModelInHand(item.getId(), inHandModel, inventoryModel);
    }

    protected ItemModelBuilder withEntityModelInHand(ResourceLocation location, ItemModelBuilder inHandModel, ItemModelBuilder inventoryModel) {
        generatedModels.remove(inHandModel.getLocation());
        generatedModels.remove(inventoryModel.getLocation());
        return withSeparateTransforms(location)
                .base(inHandModel)
                .perspective(ItemDisplayContext.GUI, inventoryModel)
                .perspective(ItemDisplayContext.FIXED, inventoryModel)
                .perspective(ItemDisplayContext.GROUND, inventoryModel)
                .end();
    }

    /**
     * Generates a {@link ResourceLocation} for a mod item model.
     * 
     * @param item The item to generate the {@link ResourceLocation} for.
     * @return The {@link ResourceLocation} for the item model.
     */
    public ResourceLocation itemLoc(DeferredItem<?> item) {
        return item.getId().withPrefix(ModelProvider.ITEM_FOLDER + "/");
    }

    /**
     * Generates a {@link ResourceLocation} for a mod block model.
     * 
     * @param block The block to generate the {@link ResourceLocation} for.
     * @return The {@link ResourceLocation} for the block model.
     */
    public ResourceLocation blockLoc(DeferredBlock<?> block) {
        return block.getId().withPrefix(ModelProvider.BLOCK_FOLDER + "/");
    }

    /**
     * Generates a {@link ResourceLocation} for a Minecraft block model.
     * 
     * @param path The path of the block model.
     * @return The {@link ResourceLocation} for the block model.
     */
    public static ResourceLocation mcBlockLoc(String path) {
        return ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/" + path);
    }

    /**
     * Generates a {@link ResourceLocation} for a Minecraft item model.
     * 
     * @param path The path of the item model.
     * @return The {@link ResourceLocation} for the item model.
     */
    public static ResourceLocation mcItemLoc(String path) {
        return ResourceLocation.withDefaultNamespace(ModelProvider.ITEM_FOLDER + "/" + path);
    }

    /**
     * Generates a {@link ResourceLocation} for a mod block model.
     * 
     * @param path The path of the block model.
     * @return The {@link ResourceLocation} for the block model.
     */
    protected ResourceLocation modBlockLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(modid, ModelProvider.BLOCK_FOLDER + "/" + path);
    }

    /**
     * Generates a {@link ResourceLocation} for a mod item model.
     * 
     * @param path The path of the item model.
     * @return The {@link ResourceLocation} for the item model.
     */
    protected ResourceLocation modItemLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(modid, ModelProvider.ITEM_FOLDER + "/" + path);
    }
}
