package dev.thomasglasser.tommylib.impl.mixin;

import net.neoforged.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class TommyLibMixinConfigPlugin implements IMixinConfigPlugin
{
	@Override
	public void onLoad(String mixinPackage)
	{

	}

	@Override
	public String getRefMapperConfig()
	{
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
	{
		boolean geckoLoaded = LoadingModList.get().getModFileById("geckolib") != null;
		if (mixinClassName.equals("dev.thomasglasser.tommylib.impl.mixin.minecraft.world.item.ItemGeckoLibMixin"))
		{
			return geckoLoaded;
		}
		else if (mixinClassName.equals("dev.thomasglasser.tommylib.impl.mixin.minecraft.world.item.ItemMixin"))
		{
			return !geckoLoaded;
		}
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets)
	{

	}

	@Override
	public List<String> getMixins()
	{
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo)
	{

	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo)
	{

	}
}
