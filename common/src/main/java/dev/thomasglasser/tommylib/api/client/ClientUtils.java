package dev.thomasglasser.tommylib.api.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.thomasglasser.tommylib.TommyLib;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;

/**
 * Side safe utils for working with the client.
 */
public class ClientUtils {
    /**
     * Contains all the {@link ExtendedKeyMapping}s to check on client tick.
     */
    private static final ReferenceOpenHashSet<ExtendedKeyMapping> KEY_MAPPINGS = new ReferenceOpenHashSet<>();

    /**
     * Adds the provided {@link ExtendedKeyMapping} to be checked
     * 
     * @param mapping the key mapping to check
     * @return the provided key mapping
     */
    public static ExtendedKeyMapping registerKeyMapping(ExtendedKeyMapping mapping) {
        KEY_MAPPINGS.add(mapping);
        return mapping;
    }

    /**
     * Adds a new {@link ExtendedKeyMapping} with the specified name, key,
     * and category that calls the provided {@link Runnable} on click.
     * 
     * @param id       The id of the key mapping
     * @param key      The key of the key mapping
     * @param category The category of the key mapping, see {@link net.minecraft.client.KeyMapping} for examples.
     * @param onClick  The {@link Runnable} to call on click.
     * @return The newly created {@link ExtendedKeyMapping}
     */
    public static ExtendedKeyMapping registerKeyMapping(ResourceLocation id, int key, String category, Runnable onClick) {
        return registerKeyMapping(new ExtendedKeyMapping(id.toLanguageKey("key"), key, category) {
            @Override
            public void onClick() {
                onClick.run();
            }
        });
    }

    public static Set<ExtendedKeyMapping> getKeyMappings() {
        return KEY_MAPPINGS;
    }

    /**
     * Gets a client player by their UUID.
     * 
     * @param uuid The UUID of the player
     * @return The client player
     */
    public static @Nullable Player getPlayerByUUID(UUID uuid) {
        return Minecraft.getInstance().level == null ? null : Minecraft.getInstance().level.getPlayerByUUID(uuid);
    }

    /**
     * Gets an entity by their ID.
     * 
     * @param id The ID of the entity
     * @return The entity
     */
    public static @Nullable Entity getEntityById(int id) {
        return Minecraft.getInstance().level == null ? null : Minecraft.getInstance().level.getEntity(id);
    }

    /**
     * Gets the local client player.
     * 
     * @return The local client player
     */
    public static @Nullable Player getLocalPlayer() {
        return Minecraft.getInstance().player;
    }

    /**
     * Gets the client level.
     * 
     * @return The client level
     */
    public static @Nullable Level getLevel() {
        return Minecraft.getInstance().level;
    }

    /**
     * Checks if the player is the specified type of special in the specified gist.
     *
     * @param gist The ID of the gist
     * @param uuid The UUID of the player
     * @param type The special type
     * @return Whether the player is the specified type of special
     */
    public static boolean isSpecial(String gist, UUID uuid, String type) {
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
     * @param gist The ID of the gist
     * @param uuid The UUID of the player
     * @return Whether the player is a snapshot tester
     */
    public static boolean checkSnapshot(String gist, UUID uuid) {
        return isSpecial(gist, uuid, "snapshot");
    }

    /**
     * Checks if the player is a dev team member in the specified gist.
     *
     * @param gist The ID of the gist
     * @param uuid The UUID of the player
     * @return Whether the player is a dev team member
     */
    public static boolean checkDev(String gist, UUID uuid) {
        return isSpecial(gist, uuid, "dev");
    }

    /**
     * Checks if the player is a legacy dev team member in the specified gist.
     *
     * @param gist The ID of the gist
     * @param uuid The UUID of the player
     * @return Whether the player is a legacy dev team member
     */
    public static boolean checkLegacyDev(String gist, UUID uuid) {
        return isSpecial(gist, uuid, "legacy_dev");
    }

    /**
     * Renders an item inventory model.
     * 
     * @deprecated Changed in 1.21.5
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
    @Deprecated(forRemoval = true, since = "31.0.0")
    public static void renderItem(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, String modid, String model) {
        ModelResourceLocation location = new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(modid, "item/" + model), "standalone");
        Minecraft.getInstance().getItemRenderer().render(itemStack, displayContext, leftHand, poseStack, buffer, combinedLight, combinedOverlay, Minecraft.getInstance().getModelManager().getModel(location));
    }

    /**
     * Renders an item inventory model with a fallback model.
     *
     * @deprecated Changed in 1.21.5
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
    @Deprecated(forRemoval = true, since = "31.0.0")
    public static void renderItem(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, String modid, String model, String fallbackModel) {
        ModelResourceLocation location = new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(modid, "item/" + model), "standalone");
        ModelResourceLocation fallbackLocation = new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(modid, "item/" + fallbackModel), "standalone");
        ModelManager modelManager = Minecraft.getInstance().getModelManager();
        BakedModel m = modelManager.getModel(location);
        if (m == modelManager.getMissingModel())
            m = modelManager.getModel(fallbackLocation);
        Minecraft.getInstance().getItemRenderer().render(itemStack, displayContext, leftHand, poseStack, buffer, combinedLight, combinedOverlay, m);
    }
}
