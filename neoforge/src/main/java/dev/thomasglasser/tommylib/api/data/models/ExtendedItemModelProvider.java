package dev.thomasglasser.tommylib.api.data.models;

import dev.thomasglasser.tommylib.api.registration.RegistryObject;
import dev.thomasglasser.tommylib.api.world.level.block.LeavesSet;
import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public abstract class ExtendedItemModelProvider extends ItemModelProvider
{
	public ExtendedItemModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper)
	{
		super(output, modid, existingFileHelper);
	}

	protected void basicItemHandheld(ResourceLocation item)
	{
		singleTexture(item.getPath(), mcLoc("item/handheld"), "layer0", new ResourceLocation(item.getNamespace(), "item/" + item.getPath()));
	}

	protected void basicItemHandheld(RegistryObject<? extends Item> item)
	{
		basicItemHandheld(item.getId());
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
		//        withExistingParent(BuiltInRegistries.BLOCK.getKey(set.strippedLog().get()).getPath(), modBlockModel(set.strippedLog().get()));
		withExistingParent(set.wood().getId().getPath(), modBlockModel(set.wood().getId().getPath()));
		//        withExistingParent(BuiltInRegistries.BLOCK.getKey(set.strippedWood().get()).getPath(), modBlockModel(set.strippedWood().get()));
	}

	protected void leavesSet(LeavesSet set)
	{
		withExistingParent(set.leaves().getId().getPath(), modBlockModel(BuiltInRegistries.BLOCK.getKey(set.leaves().get()).getPath()));
		singleTexture(set.sapling().getId().getPath(), mcItemModel("generated"), "layer0", modBlockModel(set.sapling().getId().getPath()));
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
