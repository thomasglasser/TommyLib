package dev.thomasglasser.tommylib.api.world.entity.player;

import dev.thomasglasser.tommylib.impl.TommyLib;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import org.apache.commons.io.IOUtils;

public class SpecialPlayerUtils {
    /**
     * Common types of special player
     */
    public static final String BETA_TESTER_KEY = "beta";
    public static final String DEV_KEY = "dev";
    public static final String LEGACY_DEV_KEY = "legacy_dev";

    /**
     * Checks if the cosmetic layer should render considering the given slot.
     * 
     * @param player The player to check rendering for
     * @param slot   The slot to check rendering for
     * @return Whether the cosmetic layer should render
     */
    public static boolean renderCosmeticLayerInSlot(AbstractClientPlayer player, EquipmentSlot slot) {
        return slot == null || !player.hasItemInSlot(slot);
    }

    /**
     * Checks the types of special the player is listed as in the provided gist.
     *
     * @param gist The ID of the gist
     * @param uuid The UUID of the player
     * @return The types of special the player is listed as in the provided gist
     */
    public static Set<String> getSpecialTypes(String gist, UUID uuid) {
        BufferedReader fileReader = null;

        try {
            HttpURLConnection connection = (HttpURLConnection) new URI("https://gist.github.com/" + gist + "/raw/").toURL().openConnection();

            connection.setConnectTimeout(1000);
            connection.connect();

            if (HttpURLConnection.HTTP_OK != connection.getResponseCode()) {
                TommyLib.LOGGER.error("Failed connection to cloud based special player list, response code {}", connection.getResponseMessage());

                return ReferenceOpenHashSet.of();
            }

            fileReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = fileReader.readLine()) != null) {
                if (!line.startsWith(" <!DOCTYPE")) {
                    String[] lineSplit = line.split("\\|");
                    UUID givenUUID;

                    if (lineSplit.length > 2) {
                        String uuidString = lineSplit[1];
                        try {
                            givenUUID = UUID.fromString(uuidString);

                            if (givenUUID.equals(uuid)) {
                                String[] types = lineSplit[2].split(",");
                                TommyLib.LOGGER.debug("Found special player types for UUID {}: {}", uuid, types);
                                return ReferenceOpenHashSet.of(types);
                            }
                        } catch (IllegalArgumentException ex) {
                            TommyLib.LOGGER.error("Invalid UUID format from web: {}", uuidString);
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

        return ReferenceOpenHashSet.of();
    }
}
