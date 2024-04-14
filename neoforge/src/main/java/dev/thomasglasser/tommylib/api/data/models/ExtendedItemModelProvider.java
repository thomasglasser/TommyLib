package dev.thomasglasser.tommylib.api.data.models;

import dev.thomasglasser.tommylib.api.registration.RegistryObject;
import dev.thomasglasser.tommylib.api.world.level.block.LeavesSet;
import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public abstract class ExtendedItemModelProvider extends ItemModelProvider
{
	public ExtendedItemModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper)
	{
		super(output, modid, existingFileHelper);
	}

	public ItemModelBuilder basicInventoryItem(ResourceLocation item)
	{
		return basicItem(new ResourceLocation(item.getNamespace(), item.getPath() + "_inventory"), item.getPath());
	}

	public ItemModelBuilder basicInventoryItem(RegistryObject<? extends Item> item)
	{
		return basicInventoryItem(item.getId());
	}

	public ItemModelBuilder basicItem(ResourceLocation item, String textureLoc) {
		return getBuilder(item.toString())
				.parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0", new ResourceLocation(item.getNamespace(), "item/" + textureLoc));
	}

	public ItemModelBuilder basicItem(RegistryObject<? extends Item> item, String textureLoc) {
		return basicItem(item.getId(), textureLoc);
	}

	protected void basicItemHandheld(ResourceLocation item)
	{
		singleTexture(item.getPath(), mcLoc("item/handheld"), "layer0", new ResourceLocation(item.getNamespace(), "item/" + item.getPath()));
	}

	protected void basicItemHandheld(RegistryObject<? extends Item> item)
	{
		basicItemHandheld(item.getId());
	}

	protected void basicItemHandheld(ResourceLocation item, String textureLoc)
	{
		singleTexture(item.getPath(), mcLoc("item/handheld"), "layer0", new ResourceLocation(item.getNamespace(), "item/" + textureLoc));
	}

	protected void basicItemHandheld(RegistryObject<? extends Item> item, String textureLoc)
	{
		basicItemHandheld(item.getId(), textureLoc);
	}

	protected void spawnEgg(String path)
	{
		withExistingParent(path, mcLoc("item/template_spawn_egg"));
	}

	protected void spawnEgg(RegistryObject<SpawnEggItem> egg)
	{
		withExistingParent(egg.getId().getPath(), mcLoc("item/template_spawn_egg"));
	}

	protected void woodSet(WoodSet set)
	{
		withExistingParent(set.planks().getId().getPath(), modBlockModel(set.planks().getId().getPath()));
		withExistingParent(set.log().getId().getPath(), modBlockModel(set.log().getId().getPath()));
		withExistingParent(set.strippedLog().getId().getPath(), modBlockModel(set.strippedLog().getId().getPath()));
		withExistingParent(set.wood().getId().getPath(), modBlockModel(set.wood().getId().getPath()));
		withExistingParent(set.strippedWood().getId().getPath(), modBlockModel(set.strippedWood().getId().getPath()));
	}

	protected void leavesSet(LeavesSet set)
	{
		withExistingParent(set.leaves().getId().getPath(), modBlockModel(BuiltInRegistries.BLOCK.getKey(set.leaves().get()).getPath()));
		singleTexture(set.sapling().getId().getPath(), mcItemModel("generated"), "layer0", modBlockModel(set.sapling().getId().getPath()));
	}

	protected void basicBlockItem(Block block)
	{
		ResourceLocation rl = BuiltInRegistries.BLOCK.getKey(block);
		withExistingParent(rl.getPath(), new ResourceLocation(rl.getNamespace(), "block/" + rl.getPath()));
	}

	public ResourceLocation modItemModel(String path)
	{
		return new ResourceLocation(modid, "item/" + path);
	}
	public ResourceLocation modBlockModel(String path)
	{
		return modLoc("block/" + path);
	}

	public ResourceLocation mcItemModel(String path)
	{
		return new ResourceLocation("item/" + path);
	}
}
