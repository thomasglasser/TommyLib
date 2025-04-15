package dev.thomasglasser.tommylib.impl.client;

import dev.thomasglasser.tommylib.TommyLib;
import dev.thomasglasser.tommylib.api.client.animation.AnimationUtils;
import net.fabricmc.api.ClientModInitializer;

public class TommyLibFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        if (TommyLib.Dependencies.PLAYERANIMATOR.isLoaded())
            AnimationUtils.registerPlayerForAnimation();
    }
}
