package dev.thomasglasser.tommylib.api.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.thomasglasser.tommylib.TommyLib;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
import org.apache.commons.io.IOUtils;

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
     * Checks if the player is a VIP of the specified type in the specified gist.
     *
     * @param gist The ID of the gist.
     * @param uuid The UUID of the player.
     * @param type The VIP type.
     * @return Whether the player is a VIP of the specified type.
     */
    public static boolean isVip(String gist, UUID uuid, String type) {
        BufferedReader fileReader = null;

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://gist.github.com/" + gist + "/raw/").openConnection();

            connection.setConnectTimeout(1000);
            connection.connect();

            if (HttpURLConnection.HTTP_OK != connection.getResponseCode()) {
                TommyLib.LOGGER.error("Failed connection to cloud based special player list, response code " + connection.getResponseMessage());

                return false;
            }

            fileReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = fileReader.readLine()) != null) {
                if (!line.startsWith(" <!DOCTYPE")) {
                    String[] lineSplit = line.split("\\|");
                    UUID givenUUID;

                    if (lineSplit.length > 2) {
                        try {
                            givenUUID = UUID.fromString(lineSplit[1]);

                            if (givenUUID.equals(uuid) && lineSplit[2].contains(type)) {
                                return true;
                            }
                        } catch (IllegalArgumentException ex) {
                            TommyLib.LOGGER.error("Invalid UUID format from web: " + lineSplit[1]);
                        }
                    }
                }
            }

            connection.disconnect();
        } catch (Exception e) {
            TommyLib.LOGGER.error("Error while performing HTTP Tasks, dropping.", e);
        } finally {
            IOUtils.closeQuietly(fileReader);
        }

        return false;
    }

    /**
     * Checks if the player is a snapshot tester in the specified gist.
     *
     * @param gist The ID of the gist.
     * @param uuid The UUID of the player.
     * @return Whether the player is a snapshot tester.
     */
    public static boolean checkSnapshotTester(String gist, UUID uuid) {
        return isVip(gist, uuid, "snapshot");
    }

    /**
     * Checks if the player is a dev team member in the specified gist.
     *
     * @param gist The ID of the gist.
     * @param uuid The UUID of the player.
     * @return Whether the player is a dev team member.
     */
    public static boolean checkDevTeam(String gist, UUID uuid) {
        return isVip(gist, uuid, "dev");
    }

    /**
     * Checks if the player is a legacy dev team member in the specified gist.
     *
     * @param gist The ID of the gist.
     * @param uuid The UUID of the player.
     * @return Whether the player is a legacy dev team member.
     */
    public static boolean checkLegacyDevTeam(String gist, UUID uuid) {
        return isVip(gist, uuid, "legacy_dev");
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
