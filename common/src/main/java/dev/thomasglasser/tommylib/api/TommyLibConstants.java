package dev.thomasglasser.tommylib.api;

import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TommyLibConstants {
    public static final String MOD_ID = "tommylib";
    public static final String MOD_NAME = "TommyLib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static Identifier modId(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
