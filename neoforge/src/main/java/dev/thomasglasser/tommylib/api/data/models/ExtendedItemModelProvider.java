package dev.thomasglasser.tommylib.api.data.models;

import dev.thomasglasser.tommylib.api.registration.DeferredItem;
import dev.thomasglasser.tommylib.api.world.level.block.LeavesSet;
import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Extension of {@link ItemModelProvider} that provides functionality for mod holders.
 */
public abstract class ExtendedItemModelProvider extends ItemModelProvider
{
	public ExtendedItemModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper)
	{
		super(output, modid, existingFileHelper);
	}

	/**
	 * Generates a basic item model appended with "_inventory" for the item texture.
	 * @param item The item to generate the model for.
	 * @return The item model builder for the item.
	 */
	public ItemModelBuilder basicInventoryItem(ResourceLocation item)
	{
		return basicItem(ResourceLocation.fromNamespaceAndPath(item.getNamespace(), item.getPath() + "_inventory"), item.getPath());
	}
	public ItemModelBuilder basicInventoryItem(DeferredItem<?> item)
	{
		return basicInventoryItem(item.getId());
	}

	/**
	 * Generates a basic item model with the provided texture location.
	 * @param item The item to generate the model for.
	 * @param textureLoc The location of the texture for the item.
	 * @return The item model builder for the item.
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
	 * @param item The item to generate the model for.
	 */
	protected void basicItemHandheld(ResourceLocation item)
	{
		singleTexture(item.getPath(), mcLoc("item/handheld"), "layer0", ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "item/" + item.getPath()));
	}
	protected void basicItemHandheld(DeferredItem<?> item)
	{
		basicItemHandheld(item.getId());
	}

	/**
	 * Generates a basic handheld item model with the provided texture location.
	 * @param item The item to generate the model for.
	 * @param textureLoc The location of the texture for the item.
	 */
	protected void basicItemHandheld(ResourceLocation item, String textureLoc)
	{
		singleTexture(item.getPath(), mcLoc("item/handheld"), "layer0", ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "item/" + textureLoc));
	}
	protected void basicItemHandheld(DeferredItem<?> item, String textureLoc)
	{
		basicItemHandheld(item.getId(), textureLoc);
	}

	/**
	 * Generates a spawn egg model
	 * @param path The path of the spawn egg
	 */
	protected void spawnEgg(String path)
	{
		withExistingParent(path, mcLoc("item/template_spawn_egg"));
	}
	protected void spawnEgg(DeferredItem<SpawnEggItem> egg)
	{
		withExistingParent(egg.getId().getPath(), mcLoc("item/template_spawn_egg"));
	}

	/**
	 * Generates item models for all blocks in a {@link WoodSet}.
	 * @param set The set of blocks to generate models for.
	 */
	protected void woodSet(WoodSet set)
	{
		withExistingParent(set.planks().getId().getPath(), modBlockModel(set.planks().getId().getPath()));
		withExistingParent(set.log().getId().getPath(), modBlockModel(set.log().getId().getPath()));
		withExistingParent(set.strippedLog().getId().getPath(), modBlockModel(set.strippedLog().getId().getPath()));
		withExistingParent(set.wood().getId().getPath(), modBlockModel(set.wood().getId().getPath()));
		withExistingParent(set.strippedWood().getId().getPath(), modBlockModel(set.strippedWood().getId().getPath()));
	}

	/**
	 * Generates item models for all blocks in a {@link LeavesSet}.
	 * @param set The set of blocks to generate models for.
	 */
	protected void leavesSet(LeavesSet set)
	{
		withExistingParent(set.leaves().getId().getPath(), modBlockModel(BuiltInRegistries.BLOCK.getKey(set.leaves().get()).getPath()));
		singleTexture(set.sapling().getId().getPath(), mcItemModel("generated"), "layer0", modBlockModel(set.sapling().getId().getPath()));
	}

	/**
	 * Generates an item model for a block.
	 * @param block The block to generate the model for.
	 */
	protected void basicBlockItem(Block block)
	{
		ResourceLocation rl = BuiltInRegistries.BLOCK.getKey(block);
		withExistingParent(rl.getPath(), ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), "block/" + rl.getPath()));
	}

	/**
	 * Generates a {@link ResourceLocation} for a mod item model.
	 * @param path The path of the item model.
	 * @return The {@link ResourceLocation} for the item model.
	 */
	public ResourceLocation modItemModel(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(modid, "item/" + path);
	}

	/**
	 * Generates a {@link ResourceLocation} for a mod block model.
	 * @param path The path of the block model.
	 * @return The {@link ResourceLocation} for the block model.
	 */
	public ResourceLocation modBlockModel(String path)
	{
		return modLoc("block/" + path);
	}

	/**
	 * Generates a {@link ResourceLocation} for a Minecraft item model.
	 * @param path The path of the item model.
	 * @return The {@link ResourceLocation} for the item model.
	 */
	public ResourceLocation mcItemModel(String path)
	{
		return ResourceLocation.withDefaultNamespace("item/" + path);
	}
}
