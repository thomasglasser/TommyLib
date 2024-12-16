package dev.thomasglasser.tommylib.api.client;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ClientUtils {
    /**
     * Gets the client player by their UUID.
     * 
     * @param uuid The UUID of the player.
     * @return The client player.
     */
    public static Player getPlayerByUUID(UUID uuid) {
        return Minecraft.getInstance().level.getPlayerByUUID(uuid);
    }

    /**
     * Sets the current screen to the provided screen.
     * 
     * @param screen The screen to set.
     */
    public static void setScreen(Screen screen) {
        Minecraft.getInstance().setScreen(screen);
    }

    /**
     * Gets the entity by their ID.
     * 
     * @param id The ID of the entity.
     * @return The entity.
     */
    public static Entity getEntityById(int id) {
        return Minecraft.getInstance().level == null ? null : Minecraft.getInstance().level.getEntity(id);
    }

    /**
     * Gets the main client player.
     * 
     * @return The main client player.
     */
    public static Player getMainClientPlayer() {
        return Minecraft.getInstance().player;
    }

    /**
     * Gets the current level.
     * 
     * @return The current level.
     */
    public static Level getLevel() {
        return Minecraft.getInstance().level;
    }

    /**
     * Gets the Minecraft instance.
     * 
     * @return The Minecraft instance.
     */
    public static Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }

    /**
     * Renders an item inventory model.
     * 
     * @param itemStack       The item stack to render.
     * @param displayContext  The display context of the item.
     * @param leftHand        Whether the item is in the left hand.
     * @param poseStack       The pose stack.
     * @param buffer          The buffer.
     * @param combinedLight   The combined light.
     * @param combinedOverlay The combined overlay.
     * @param modid           The mod ID of the item.
     * @param model           The model of the item.
     */
    public static void renderItem(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, String modid, String model) {
        ModelResourceLocation location = new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(modid, "item/" + model), "standalone");
        ClientUtils.getMinecraft().getItemRenderer().render(itemStack, displayContext, leftHand, poseStack, buffer, combinedLight, combinedOverlay, ClientUtils.getMinecraft().getModelManager().getModel(location));
    }

    /**
     * Renders an item inventory model with a fallback model.
     * 
     * @param itemStack       The item stack to render.
     * @param displayContext  The display context of the item.
     * @param leftHand        Whether the item is in the left hand.
     * @param poseStack       The pose stack.
     * @param buffer          The buffer.
     * @param combinedLight   The combined light.
     * @param combinedOverlay The combined overlay.
     * @param modid           The mod ID of the item.
     * @param model           The model of the item.
     * @param fallbackModel   The fallback model of the item.
     */
    public static void renderItem(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, String modid, String model, String fallbackModel) {
        ModelResourceLocation location = new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(modid, "item/" + model), "standalone");
        ModelResourceLocation fallbackLocation = new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(modid, "item/" + fallbackModel), "standalone");
        BakedModel m = ClientUtils.getMinecraft().getModelManager().getModel(location);
        if (m == ClientUtils.getMinecraft().getModelManager().getMissingModel())
            m = ClientUtils.getMinecraft().getModelManager().getModel(fallbackLocation);
        ClientUtils.getMinecraft().getItemRenderer().render(itemStack, displayContext, leftHand, poseStack, buffer, combinedLight, combinedOverlay, m);
    }
}
