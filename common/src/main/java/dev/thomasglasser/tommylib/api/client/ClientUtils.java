package dev.thomasglasser.tommylib.api.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.thomasglasser.tommylib.api.world.item.ItemUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ClientUtils
{
    /**
     * A list of all key mappings registered via {@link #registerKeyMapping(ResourceLocation, int, String)} to be registered by the mod.
     */
    private static final ArrayList<KeyMapping> KEY_MAPPINGS = new ArrayList<>();

    /**
     * Gets the client player by their UUID.
     * @param uuid The UUID of the player.
     * @return The client player.
     */
    public static AbstractClientPlayer getClientPlayerByUUID(UUID uuid) {
        return (AbstractClientPlayer) Minecraft.getInstance().level.getPlayerByUUID(uuid);
    }

    /**
     * Sets the current screen to the provided screen.
     * @param screen The screen to set.
     */
    public static void setScreen(Screen screen)
    {
        Minecraft.getInstance().setScreen(screen);
    }

    /**
     * Gets the entity by their ID.
     * @param id The ID of the entity.
     * @return The entity.
     */
    public static Entity getEntityById(int id)
    {
        return Minecraft.getInstance().level == null ? null : Minecraft.getInstance().level.getEntity(id);
    }

    /**
     * Gets the main client player.
     * @return The main client player.
     */
    public static Player getMainClientPlayer()
    {
        return Minecraft.getInstance().player;
    }

    /**
     * Gets the current level.
     * @return The current level.
     */
    public static Level getLevel()
    {
        return Minecraft.getInstance().level;
    }

    /**
     * Gets the Minecraft instance.
     * @return The Minecraft instance.
     */
    public static Minecraft getMinecraft()
    {
        return Minecraft.getInstance();
    }

    /**
     * Gets the items for the provided {@link CreativeModeTab}.
     * @param tab The tab to get the items for.
     * @return The items for the tab.
     */
    public static List<ItemStack> getItemsForTab(ResourceKey<CreativeModeTab> tab)
    {
        List<ItemStack> items = new ArrayList<>();

        ItemUtils.getItemTabs().forEach((itemTab, itemLikes) -> {
            if (tab == itemTab)
            {
                itemLikes.forEach((itemLike) -> items.add(Objects.requireNonNull(BuiltInRegistries.ITEM.get(itemLike)).getDefaultInstance()));
            }
        });

        return items;
    }

    /**
     * Gets the key mappings registered via {@link #registerKeyMapping(ResourceLocation, int, String)}.
     * @return The key mappings.
     */
    public static ArrayList<KeyMapping> getKeyMappings()
    {
        return KEY_MAPPINGS;
    }

    /**
     * Registers a key mapping to be registered by the mod.
     * @param name The name of the key mapping.
     * @param key The key code of the key mapping.
     * @param category The category of the key mapping.
     * @return The key mapping.
     */
    public static KeyMapping registerKeyMapping(ResourceLocation name, int key, String category)
    {
        KeyMapping mapping = new KeyMapping(name.toLanguageKey("key"), key, category);
        KEY_MAPPINGS.add(mapping);
        return mapping;
    }

    /**
     * Renders an item inventory model.
     * @param itemStack The item stack to render.
     * @param displayContext The display context of the item.
     * @param leftHand Whether the item is in the left hand.
     * @param poseStack The pose stack.
     * @param buffer The buffer.
     * @param combinedLight The combined light.
     * @param combinedOverlay The combined overlay.
     * @param modid The mod ID of the item.
     * @param model The model of the item.
     */
    public static void renderItem(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, String modid, String model) {
        ClientUtils.getMinecraft().getItemRenderer().render(itemStack, displayContext, leftHand, poseStack, buffer, combinedLight, combinedOverlay, ClientUtils.getMinecraft().getModelManager().getModel(ModelResourceLocation.inventory(ResourceLocation.fromNamespaceAndPath(modid, model))));
    }

    /**
     * Renders an item inventory model with a fallback model.
     * @param itemStack The item stack to render.
     * @param displayContext The display context of the item.
     * @param leftHand Whether the item is in the left hand.
     * @param poseStack The pose stack.
     * @param buffer The buffer.
     * @param combinedLight The combined light.
     * @param combinedOverlay The combined overlay.
     * @param modid The mod ID of the item.
     * @param model The model of the item.
     * @param fallbackModel The fallback model of the item.
     */
    public static void renderItem(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, String modid, String model, String fallbackModel)
    {
        BakedModel m = ClientUtils.getMinecraft().getModelManager().getModel(ModelResourceLocation.inventory(ResourceLocation.fromNamespaceAndPath(modid, model)));
        if (m == ClientUtils.getMinecraft().getModelManager().getMissingModel())
            m = ClientUtils.getMinecraft().getModelManager().getModel(ModelResourceLocation.inventory(ResourceLocation.fromNamespaceAndPath(modid, fallbackModel)));
        ClientUtils.getMinecraft().getItemRenderer().render(itemStack, displayContext, leftHand, poseStack, buffer, combinedLight, combinedOverlay, m);
    }
}
