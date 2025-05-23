package dev.thomasglasser.tommylib.impl.client;

import dev.thomasglasser.tommylib.api.client.ClientUtils;
import dev.thomasglasser.tommylib.api.client.ExtendedKeyMapping;

public class TommyLibClientEvents {
    public static void onClientTick() {
        for (ExtendedKeyMapping keyMapping : ClientUtils.getKeyMappings()) {
            if (keyMapping.isDown()) {
                while (keyMapping.consumeClick()) {
                    keyMapping.onClick();
                }
            } else {
                keyMapping.onNoClick();
            }
        }
    }
}
