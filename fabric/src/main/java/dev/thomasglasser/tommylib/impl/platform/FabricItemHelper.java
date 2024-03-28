package dev.thomasglasser.tommylib.impl.platform;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.thomasglasser.tommylib.api.client.ClientUtils;
import dev.thomasglasser.tommylib.impl.platform.services.ItemHelper;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;

import java.util.function.Supplier;

public class FabricItemHelper implements ItemHelper
{

    @Override
    public Attribute getAttackRangeAttribute() {
        // TODO: Switch to vanilla attribute when it's added
        return ReachEntityAttributes.ATTACK_RANGE;
    }

    @Override
    public Supplier<SpawnEggItem> makeSpawnEgg(Supplier<EntityType<? extends Mob>> entityType, int bg, int fg, Item.Properties properties) {
        return () -> new SpawnEggItem(entityType.get(), bg, fg, properties);
    }

    @Override
    public void renderItem(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, String modid, String model) {
        ClientUtils.getMinecraft().getItemRenderer().render(itemStack, displayContext, false, poseStack, buffer, combinedLight, combinedOverlay, ClientUtils.getMinecraft().getModelManager().getModel(new ModelResourceLocation(new ResourceLocation(modid, model), "inventory")));
    }

    @Override
    public void renderItem(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, String modid, String model, String defaultModel)
    {
        BakedModel m = ClientUtils.getMinecraft().getModelManager().getModel(new ModelResourceLocation(new ResourceLocation(modid, model), "inventory"));
        if (m == ClientUtils.getMinecraft().getModelManager().getMissingModel())
            m = ClientUtils.getMinecraft().getModelManager().getModel(new ModelResourceLocation(new ResourceLocation(modid, defaultModel), "inventory"));
        ClientUtils.getMinecraft().getItemRenderer().render(itemStack, displayContext, false, poseStack, buffer, combinedLight, combinedOverlay, m);
    }

    @Override
    public final CreativeModeTab newTab(Component title, Supplier<ItemStack> icon, boolean search, CreativeModeTab.DisplayItemsGenerator displayItems) {
        CreativeModeTab.Builder builder = FabricItemGroup.builder().title(title).icon(icon).displayItems(displayItems);
        return builder.build();
    }
}
